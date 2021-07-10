package eu.okaeri.commons.bukkit.item.parser;

import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParserRegistry;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringParts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor
@AllArgsConstructor
public class ItemStackParser {

    private ItemStringPartParserRegistry partParserRegistry = new ItemStringPartParserRegistry();
    private ItemStringTokenizer tokenizer = new ItemStringTokenizer(this.partParserRegistry);

    public ItemStack toItem(@NonNull String itemString) {

        ItemStringParts parts = this.tokenizer.tokenize(itemString);
        String name = parts.firstOfAsOrThrow(ItemStringPartType.NAME, String.class);
        String lore = parts.firstOfAsOrThrow(ItemStringPartType.LORE, String.class);

        return null; // TODO
    }
}
