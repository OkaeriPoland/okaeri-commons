package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.Enums;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import lombok.NonNull;
import org.bukkit.Material;

public class ItemStringTypePartParser implements ItemStringPartParser<Material> {

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.TYPE;
    }

    @Override
    public Material parse(@NonNull ItemStringPart part) {
        return Enums.matchIgnoreCase(Material.class, part.getValue())
                .orElseThrow(() -> new IllegalArgumentException("unknown material " + part.getValue()));
    }
}
