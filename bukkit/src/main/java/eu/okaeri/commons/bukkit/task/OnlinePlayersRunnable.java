package eu.okaeri.commons.bukkit.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class OnlinePlayersRunnable implements Runnable {

    public static Runnable consuming(Consumer<Player> consumer) {
        return new OnlinePlayersRunnable() {
            @Override
            public void runFor(Player player) {
                consumer.accept(player);
            }
        };
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(this::runFor);
    }

    public abstract void runFor(Player player);
}
