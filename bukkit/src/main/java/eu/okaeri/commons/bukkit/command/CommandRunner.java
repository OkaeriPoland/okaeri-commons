package eu.okaeri.commons.bukkit.command;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * CommandRunner runner = CommandRunner.of(Bukkit.getOnlinePlayers())
 *   .field("{name}", (target) -&gt; target.getName())
 *   .execute("say {name}");
 */
public class CommandRunner<T> {

    private Collection<? extends T> targets;
    private boolean forceMainThread = true;
    private CommandSender dispatcher = null;
    private Map<String, String> fields = new LinkedHashMap<>();
    private Map<String, CommandFieldReplacer<T>> dynamicFields = new LinkedHashMap<>();
    private Plugin plugin;

    private CommandRunner(@NonNull Plugin plugin, @NonNull Collection<? extends T> targets) {
        this.plugin = plugin;
        this.targets = targets;
    }

    public static CommandRunner<?> of(@NonNull Plugin plugin) {
        return of(plugin, Bukkit.getConsoleSender());
    }

    public static <T> CommandRunner<T> of(@NonNull Plugin plugin, @NonNull T target) {
        return new CommandRunner<T>(plugin, Collections.singletonList(target));
    }

    public static <T> CommandRunner<T> of(@NonNull Plugin plugin, @NonNull Collection<? extends T> targets) {
        return new CommandRunner<T>(plugin, new ArrayList<>(targets));
    }

    public CommandRunner<T> field(@NonNull String name, @NonNull String content) {
        this.fields.put(name, content);
        return this;
    }

    public CommandRunner<T> field(@NonNull String name, @NonNull CommandFieldReplacer<T> fieldReplacer) {
        this.dynamicFields.put(name, fieldReplacer);
        return this;
    }

    public CommandRunner<T> forceMainThread(boolean state) {
        this.forceMainThread = state;
        return this;
    }

    public CommandRunner<T> dispatcher(@NonNull CommandSender sender) {
        this.dispatcher = sender;
        return this;
    }

    public CommandRunner<T> execute(@NonNull String command) {
        return this.execute(Collections.singletonList(command));
    }

    public CommandRunner<T> execute(@NonNull List<String> commands) {

        if (commands.isEmpty()) {
            return this;
        }

        for (T target : this.targets) {
            commands.stream()
                .map(command -> {
                    for (Map.Entry<String, String> entry : this.fields.entrySet()) {
                        String fieldName = entry.getKey();
                        String fieldValue = entry.getValue();
                        command = command.replace("{" + fieldName + "}", fieldValue);
                    }
                    return command;
                })
                .map(command -> {
                    for (Map.Entry<String, CommandFieldReplacer<T>> replacerEntry : this.dynamicFields.entrySet()) {
                        String fieldName = replacerEntry.getKey();
                        CommandFieldReplacer<T> replacer = replacerEntry.getValue();
                        command = command.replace("{" + fieldName + "}", replacer.replace(target));
                    }
                    return command;
                })
                .forEachOrdered(command -> {
                    CommandSender whoDispatches = (this.dispatcher == null) ? Bukkit.getConsoleSender() : this.dispatcher;
                    if (this.forceMainThread) {
                        if (Bukkit.isPrimaryThread()) {
                            Bukkit.dispatchCommand(whoDispatches, command);
                        } else {
                            Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(whoDispatches, command));
                        }
                    } else {
                        Bukkit.dispatchCommand(whoDispatches, command);
                    }
                });
        }

        return this;
    }
}
