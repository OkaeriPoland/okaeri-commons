package eu.okaeri.commons.bukkit.task;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.function.Consumer;

public abstract class AllWorldsRunnable implements Runnable {

    public static Runnable consuming(Consumer<World> consumer) {
        return new AllWorldsRunnable() {
            @Override
            public void runFor(World world) {
                consumer.accept(world);
            }
        };
    }

    @Override
    public void run() {
        Bukkit.getWorlds().forEach(this::runFor);
    }

    public abstract void runFor(World world);
}
