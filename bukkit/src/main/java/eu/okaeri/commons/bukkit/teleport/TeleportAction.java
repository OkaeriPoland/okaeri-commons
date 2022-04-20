package eu.okaeri.commons.bukkit.teleport;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TeleportAction {

    private static MethodHandle paperTeleportAsync;
    private static MethodHandle entityTeleportAsync;
    private static String activeMethod = "";
    private static boolean announced = false;

    static {
        try {
            Class<?> paperLib = Class.forName("io.papermc.lib.PaperLib");
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            MethodType handleType = MethodType.methodType(Entity.class, Location.class);
            paperTeleportAsync = lookup.findStatic(paperLib, "teleportAsync", handleType);
            activeMethod = "PaperLib#teleportAsync";
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException ignored) {
            // try fallback 1.13+ method
            try {
                MethodHandles.Lookup lookup = MethodHandles.publicLookup();
                Method teleportAsyncEntity = Entity.class.getMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
                entityTeleportAsync = lookup.unreflect(teleportAsyncEntity);
                activeMethod = "Entity#teleportAsync";
            } catch (NoSuchMethodException | IllegalAccessException ignored1) {
                activeMethod = "Entity#teleport";
            }
        }
    }

    @NonNull private final Plugin plugin;
    @NonNull private final Entity who;
    @NonNull private final Location where;
    private final TeleportActionCallback callback;

    @SuppressWarnings("unchecked")
    public void perform() {

        if ((this.who instanceof Player) && !((Player) this.who).isOnline()) {
            return;
        }

        if (!announced) {
            this.plugin.getLogger().info("Teleport Queue: Using " + activeMethod);
            announced = true;
        }

        Consumer<Boolean> consumer = success -> {

            if (!success) {
                this.plugin.getLogger().severe("Failed to teleport the player " + this.who + " to " + this.where);
                return;
            }

            if (this.callback == null) {
                return;
            }

            this.callback.teleported(this.who);
        };

        if (paperTeleportAsync != null) {
            try {
                ((CompletableFuture<Boolean>) paperTeleportAsync
                    .invoke(this.who, this.where))
                    .thenAccept(consumer);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (entityTeleportAsync != null) {
            try {
                ((CompletableFuture<Boolean>) entityTeleportAsync.bindTo(this.who)
                    .invoke(this.where, PlayerTeleportEvent.TeleportCause.PLUGIN))
                    .thenAccept(consumer);
                return;
            } catch (Throwable ignored) {
            }
        }

        this.who.teleport(this.where);
        consumer.accept(true);
    }
}
