package eu.okaeri.commons.bukkit.command;

public interface CommandFieldReplacer<T> {
    String replace(T target);
}
