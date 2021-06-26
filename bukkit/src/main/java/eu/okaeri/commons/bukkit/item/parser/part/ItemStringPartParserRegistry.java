package eu.okaeri.commons.bukkit.item.parser.part;

import eu.okaeri.commons.bukkit.item.parser.part.builtin.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class ItemStringPartParserRegistry {

    private Map<ItemStringPartType, ItemStringPartParser> parsers = new HashMap<>();
    {
        this.register(new ItemStringTypePartParser());
        this.register(new ItemStringDurabilityPartParser());
        this.register(new ItemStringAmountPartParser());
        this.register(new ItemStringNamePartParser());
        this.register(new ItemStringLorePartParser());
        this.register(new ItemStringEnchantmentPartParser());
    }

    public void register(ItemStringPartParser parser) {
        this.parsers.put(parser.getForType(), parser);
    }

    public ItemStringPartParser get(ItemStringPartType type) {
        return this.parsers.get(type);
    }
}
