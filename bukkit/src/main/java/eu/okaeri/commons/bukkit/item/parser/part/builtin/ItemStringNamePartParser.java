package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor
@AllArgsConstructor
public class ItemStringNamePartParser implements ItemStringPartParser<String> {

    private String spaceSupplement = "_";

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.NAME;
    }

    @Override
    public String parse(ItemStringPart part) {
        return ChatColor.translateAlternateColorCodes('&', part.getValue())
                .replace(this.spaceSupplement, " ");
    }
}
