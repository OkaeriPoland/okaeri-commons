package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.ChatColor;

@NoArgsConstructor
@AllArgsConstructor
public class ItemStringLorePartParser implements ItemStringPartParser<String> {

    private String lineSeparator = "|";
    private String spaceSupplement = "_";

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.LORE;
    }

    @Override
    public String parse(@NonNull ItemStringPart part) {
        return ChatColor.translateAlternateColorCodes('&', part.getValue())
                .replace(this.lineSeparator, "\n")
                .replace(this.spaceSupplement, " ");
    }
}
