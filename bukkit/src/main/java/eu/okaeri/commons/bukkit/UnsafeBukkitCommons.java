package eu.okaeri.commons.bukkit;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class UnsafeBukkitCommons {

    //
    // NMS / REFLECTIONS
    //
    @Getter private static String nmsVersion;
    @Getter private static String cbPackage;
    private static boolean legacy18o17;
    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    static {
        cbPackage = Bukkit.getServer().getClass().getPackage().getName();
        legacy18o17 = (cbPackage.endsWith("v1_8_R1") || cbPackage.contains("v1_7_"));

        nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);
        if (!nmsVersion.contains("_")) nmsVersion = null; // non-versioned package
    }

    public static Class<?> getClass(@NonNull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static Class<?> getCbClass(@NonNull String suffix) {
        return getClass(getCbPackage() + "." + suffix);
    }

    public static Class<?> getNmsClass(@NonNull String suffix) {
        Class<?> clazz;
        clazz = getClass("net.minecraft.server." + suffix);
        if (clazz == null) {
            clazz = getClass("net.minecraft.server." + getNmsVersion() + "." + suffix);
        }
        return clazz;
    }

    public static MethodHandle getMHFrom(Class<?> clazz, @NonNull String name, @NonNull Class<?>... params) {

        if (clazz == null) {
            return null;
        }

        try {
            Method method = clazz.getDeclaredMethod(name, params);
            method.setAccessible(true);
            return lookup.unreflect(method);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static MethodHandle getMHGFrom(Class<?> clazz, @NonNull String name) {

        if (clazz == null) {
            return null;
        }

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return lookup.unreflectGetter(field);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static MethodHandle getMHSFrom(Class<?> clazz, @NonNull String name) {

        if (clazz == null) {
            return null;
        }

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return lookup.unreflectSetter(field);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Class<?> CraftPlayer = getCbClass("entity.CraftPlayer");
    private static Class<?> EntityPlayer = getNmsClass("EntityPlayer");
    private static Class<?> PlayerConnection = getNmsClass("PlayerConnection");
    private static Class<?> PacketPlayOutChat = getNmsClass("PacketPlayOutChat");
    private static Class<?> Packet = getNmsClass("Packet");
    private static Class<?> IChatBaseComponent = getNmsClass("IChatBaseComponent");
    private static Class<?> ChatComponentText = getNmsClass("ChatComponentText");
    private static Class<?> ChatSerializer = getNmsClass("ChatSerializer");
    private static Class<?> BCTextComponent = getClass("net.md_5.bungee.api.chat.TextComponent");
    private static Class<?> BCChatMessageType = getClass("net.md_5.bungee.api.ChatMessageType");
    private static Class<?> BCBaseComponentArray = getClass("[Lnet.md_5.bungee.api.chat.BaseComponent;");
    private static Class<?> BCBaseComponent = getClass("net.md_5.bungee.api.chat.BaseComponent");
    private static Class<?> PlayerSpigot = getClass("org.bukkit.entity.Player$Spigot");
    private static Class<?> CommandSenderSpigot = getClass("org.bukkit.command.CommandSender$Spigot");
    private static Class<?> MinecraftServer = getNmsClass("MinecraftServer");
    private static Class<?> CraftServer = getCbClass("CraftServer");

    private static MethodHandle CraftPlayerGetHandle = getMHFrom(CraftPlayer, "getHandle");
    private static MethodHandle ItemMetaSetUnbreakable = getMHFrom(ItemMeta.class, "setUnbreakable", boolean.class);
    private static MethodHandle EntityPlayerPlayerConnection = getMHGFrom(EntityPlayer, "playerConnection");
    private static MethodHandle EntityPlayerLocale = getMHGFrom(EntityPlayer, "locale");
    private static MethodHandle PlayerConnectionSendPacket = getMHFrom(PlayerConnection, "sendPacket", Packet);
    private static MethodHandle BCTextComponentFromLegacyText = getMHFrom(BCTextComponent, "fromLegacyText", String.class);
    private static MethodHandle BCChatMessageTypeValueOf = getMHFrom(BCChatMessageType, "valueOf", String.class);
    private static MethodHandle PlayerGetSpigot = getMHFrom(Player.class, "spigot");
    private static MethodHandle PlayerGetLocale = getMHFrom(Player.class, "getLocale");
    private static MethodHandle PlayerSendTitleLegacy = getMHFrom(Player.class, "sendTitle", String.class, String.class);
    private static MethodHandle PlayerSendTitleNew = getMHFrom(Player.class, "sendTitle", String.class, String.class, int.class, int.class, int.class);
    private static MethodHandle PlayerSpigotSendMessageBA = getMHFrom(PlayerSpigot, "sendMessage", BCBaseComponentArray);
    private static MethodHandle PlayerSpigotSendMessageTBA = getMHFrom(PlayerSpigot, "sendMessage", BCChatMessageType, BCBaseComponentArray);
    private static MethodHandle CommandSenderGetSpigot = getMHFrom(CommandSender.class, "spigot");
    private static MethodHandle CommandSenderSpigotSendMessageBA = getMHFrom(CommandSenderSpigot, "sendMessage", BCBaseComponentArray);
    private static MethodHandle CraftServerGetMinecraftServer = getMHFrom(CraftServer, "console");
    private static MethodHandle MinecraftServerRecentTps = getMHFrom(MinecraftServer, "recentTps");

    //
    // ITEMMETA: UNBREAKABLE
    //
    @SneakyThrows
    public static void setItemMetaUnbreakable(@NonNull ItemMeta itemMeta, boolean state) {

        if (ItemMetaSetUnbreakable != null) {
            ItemMetaSetUnbreakable.bindTo(itemMeta).invoke(state);
            return;
        }

        itemMeta.spigot().setUnbreakable(state);
    }

    //
    // LOCALE HELPERS
    //
    @SneakyThrows
    public static String getLocaleString(@NonNull Player player) {

        if (PlayerGetLocale != null) {
            return (String) PlayerGetLocale.bindTo(player).invoke();
        }

        Object handle = CraftPlayerGetHandle.bindTo(player).invoke();
        return (String) EntityPlayerLocale.bindTo(handle).invoke();
    }

    //
    // TEXT COMPONENTS / MESSAGES
    //
    @SneakyThrows
    public static void sendTitle(@NonNull Player player, @NonNull String title, @NonNull String subtitle, int fadeIn, int stay, int fadeOut) {

        if (PlayerSendTitleNew != null) {
            PlayerSendTitleNew.bindTo(player).invoke(title, subtitle, fadeIn, stay, fadeOut);
            return;
        }

        if (PlayerSendTitleLegacy != null) {
            PlayerSendTitleLegacy.bindTo(player).invoke(title, subtitle);
            return;
        }

        throw new RuntimeException("Cannot find viable method to send title message");
    }

    @SneakyThrows
    public static Object toBaseComponentArray(@NonNull String message) {
        return BCTextComponentFromLegacyText.invoke(message);
    }

    @SneakyThrows
    public static void sendComponent(@NonNull CommandSender sender, Object message) {

        if (message == null) {
            return;
        }

        if (BCBaseComponent.isInstance(message)) {
            Object[] arr = (Object[]) Array.newInstance(BCBaseComponent, 1);
            arr[0] = message;
            message = arr;
        }

        if (!BCBaseComponentArray.isInstance(message)) {
            throw new IllegalArgumentException("Message not instance of " + BCBaseComponentArray + ": " + message);
        }

        if (CommandSenderSpigotSendMessageBA != null) {
            CommandSenderSpigotSendMessageBA.bindTo(CommandSenderGetSpigot.invoke(sender)).invoke(message);
            return;
        }

        throw new RuntimeException("Cannot send component (" + sender.getName() + "): " + message);
    }

    @SneakyThrows
    public static void sendComponent(@NonNull Player player, Object message, @NonNull ChatTarget target) {

        if ((message == null) || !player.isOnline()) {
            return;
        }

        if (BCBaseComponent.isInstance(message)) {
            Object[] arr = (Object[]) Array.newInstance(BCBaseComponent, 1);
            arr[0] = message;
            message = arr;
        }

        if (!BCBaseComponentArray.isInstance(message)) {
            throw new IllegalArgumentException("Message not instance of " + BCBaseComponentArray + ": " + message);
        }

        if (((target == ChatTarget.CHAT) || (target == ChatTarget.SYSTEM)) && (PlayerSpigotSendMessageBA != null)) {
            PlayerSpigotSendMessageBA.bindTo(PlayerGetSpigot.invoke(player)).invoke(message);
            return;
        }

        if (PlayerSpigotSendMessageTBA != null) {
            PlayerSpigotSendMessageTBA.bindTo(PlayerGetSpigot.invoke(player)).invoke(BCChatMessageTypeValueOf.invoke(target.name()), message);
            return;
        }

        throw new RuntimeException("Cannot send component (" + player.getName() + ", " + target.name() + "): " + message);
    }

    @SneakyThrows
    public static void sendMessage(@NonNull Player player, String message, @NonNull ChatTarget target) {

        if ((message == null) || !player.isOnline()) {
            return;
        }

        if (((target == ChatTarget.CHAT) || (target == ChatTarget.SYSTEM)) && (PlayerSpigotSendMessageBA != null)) {
            PlayerSpigotSendMessageBA.bindTo(PlayerGetSpigot.invoke(player)).invoke(toBaseComponentArray(message));
            return;
        }

        if (PlayerSpigotSendMessageTBA != null) {
            PlayerSpigotSendMessageTBA.bindTo(PlayerGetSpigot.invoke(player)).invoke(BCChatMessageTypeValueOf.invoke(target.name()), toBaseComponentArray(message));
            return;
        }

        // partial support for 1.8/1.7.10
        Object component = createPacketComponent(message);
        Object packet = PacketPlayOutChat.getConstructor(new Class<?>[]{IChatBaseComponent, byte.class}).newInstance(component, target.position);
        Object handle = CraftPlayerGetHandle.invoke(CraftPlayer.cast(player));
        Object connection = EntityPlayerPlayerConnection.invoke(handle);
        PlayerConnectionSendPacket.invoke(connection, packet);
    }

    @SneakyThrows
    private static Object createPacketComponent(@NonNull String message) {

        if (legacy18o17) {
            return IChatBaseComponent.cast(ChatSerializer.getDeclaredMethod("a", String.class).invoke(ChatSerializer, "{\"text\": \"" + message + "\"}"));
        }

        return ChatComponentText.getConstructor(new Class<?>[]{String.class}).newInstance(message);
    }

    @RequiredArgsConstructor
    public enum ChatTarget {

        CHAT((byte) 0),
        SYSTEM((byte) 1),
        ACTION_BAR((byte) 2);

        final byte position;
    }

    //
    // TPS
    //
    @SneakyThrows
    public static double[] getServerTps(@NonNull Server server) {
        Object minecraftServer = CraftServerGetMinecraftServer.bindTo(server).invoke();
        return (double[]) MinecraftServerRecentTps.bindTo(minecraftServer).invoke();
    }
}
