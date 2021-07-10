package eu.okaeri.commons.bukkit.teleport;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public final class QueuedTeleports {

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
        this.teleportQueue.add(new TeleportAction(who, where, callback));
    }
}
