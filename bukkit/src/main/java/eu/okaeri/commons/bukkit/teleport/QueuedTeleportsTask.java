package eu.okaeri.commons.bukkit.teleport;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.util.Queue;

public class QueuedTeleportsTask implements Runnable {

    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("okaeri.platform.debug", "false"));

    private final Queue<TeleportAction> queue;
    private final Plugin plugin;
    private final int teleportsPerRun;

    public QueuedTeleportsTask(@NonNull QueuedTeleports queuedTeleports, @NonNull Plugin plugin) {
        this(queuedTeleports, plugin, 1);
    }

    public QueuedTeleportsTask(@NonNull QueuedTeleports queuedTeleports, @NonNull Plugin plugin, int teleportsPerRun) {
        this.queue = queuedTeleports.getTeleportQueue();
        this.plugin = plugin;
        this.teleportsPerRun = teleportsPerRun;
    }

    @Override
    public void run() {

        long start = System.nanoTime();
        int actionsPerformed = 0;

        for (int i = 0; i < this.teleportsPerRun; i++) {
            TeleportAction action = this.queue.poll();
            if (action == null) {
                continue;
            }
            action.perform();
            actionsPerformed++;
        }

        if ((actionsPerformed == 0) || !DEBUG) {
            return;
        }

        long took = System.nanoTime() - start;
        long tookMs = took / 1000L / 1000L;

        this.plugin.getLogger().info("Teleport Queue [-" + actionsPerformed + "]: " + this.queue.size() + " actions left (" + took + " ns, " + tookMs + " ms)");
    }
}
