package eu.okaeri.commons.bukkit.teleport;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@RequiredArgsConstructor
public final class QueuedTeleports {

    private final Plugin plugin;
    private final Queue<TeleportAction> teleportQueue = new ConcurrentLinkedQueue<>();

    public CompletableFuture<Entity> teleport(Entity who, Location where) {
        CompletableFuture<Entity> future = new CompletableFuture<>();
        this.teleport(who, where, future::complete);
        return future;
    }

    public void teleport(@NonNull Collection<? extends Entity> who, @NonNull Location where, TeleportActionCallback callback) {
        new ArrayList<>(who).forEach(target -> this.teleport(target, where, callback));
    }

    public void teleport(@NonNull Entity who, @NonNull Location where, TeleportActionCallback callback) {
        this.teleportQueue.add(new TeleportAction(this.plugin, who, where, callback));
    }
}
