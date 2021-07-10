package eu.okaeri.commons.bukkit.item;

import eu.okaeri.commons.bukkit.UnsafeBukkitCommons;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemStackBuilder {

    private ItemStack itemStack;

    private ItemStackBuilder() {
    }

    private ItemStackBuilder(@NonNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public static ItemStackBuilder of(@NonNull Material material) {
        return new ItemStackBuilder(material, 1);
    }

    public static ItemStackBuilder of(@NonNull Material material, int amount) {
        return new ItemStackBuilder(material, amount);
    }

    public static ItemStackBuilder of(@NonNull ItemStack item) {
        return ItemStackBuilder.of(item.getType(), item.getAmount())
                .withDurability(item.getDurability())
                .withOwnItemMeta(item.getItemMeta());
    }

    public static ItemStackBuilder ofCopy(@NonNull ItemStack item) {
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder();
        itemStackBuilder.itemStack = item.clone();
        return itemStackBuilder;
    }

    public ItemStackBuilder withName(@NonNull String displayName) {
        return this.withNameRaw(ChatColor.translateAlternateColorCodes('&', displayName));
    }

    public ItemStackBuilder withNameRaw(@NonNull String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withLore(@NonNull String lore) {
        return this.withLore(Collections.singletonList(lore));
    }

    public ItemStackBuilder withLore(@NonNull List<String> lore) {
        return this.withLoreRaw(lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList()));
    }

    public ItemStackBuilder withLoreRaw(@NonNull String lore) {
        return this.withLoreRaw(Collections.singletonList(lore));
    }

    public ItemStackBuilder withLoreRaw(@NonNull List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder appendLoreRaw(@NonNull List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(lore);
        } else {
            List<String> newLore = itemMeta.getLore();
            newLore.addAll(lore);
            itemMeta.setLore(newLore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder appendLore(@NonNull List<String> lore) {
        return this.appendLoreRaw(lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList()));
    }

    public ItemStackBuilder appendLoreRaw(@NonNull String line) {
        return this.appendLoreRaw(Collections.singletonList(line));
    }

    public ItemStackBuilder appendLore(@NonNull String line) {
        return this.appendLore(Collections.singletonList(line));
    }

    public ItemStackBuilder withDurability(@NonNull int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemStackBuilder withFlag(@NonNull ItemFlag itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withEnchantment(@NonNull Enchantment enchantment, int level) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withEnchantments(@NonNull Map<Enchantment, Integer> enchantments) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withOwnItemMeta(@NonNull ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder makeUnbreakable() {
        return this.setUnbreakable(true);
    }

    public ItemStackBuilder setUnbreakable(boolean state) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        UnsafeBukkitCommons.setItemMetaUnbreakable(itemMeta, state);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder manipulate(@NonNull ItemStackManipulator manipulator) {
        this.itemStack = manipulator.manipulate(this.itemStack);
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }
}