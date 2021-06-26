package eu.okaeri.commons.bukkit.teleport;

import org.bukkit.entity.Entity;

public interface TeleportActionCallback {
    void teleported(Entity who);
}
