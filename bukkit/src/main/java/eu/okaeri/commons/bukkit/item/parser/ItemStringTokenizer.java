package eu.okaeri.commons.bukkit.item.parser;

import eu.okaeri.commons.Enums;
import eu.okaeri.commons.bukkit.easytype.OEnchantment;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParserRegistry;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringParts;
import eu.okaeri.commons.cache.CacheMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ItemStringTokenizer {

    private final Map<String, ItemStringParts> tokenizerCache = new CacheMap<>(1024);
    private final ItemStringPartParserRegistry partParserRegistry;

    public ItemStringParts tokenize(@NonNull String itemString) {
        return this.tokenizerCache.computeIfAbsent(itemString, this::tokenizeUncached);
    }

    private ItemStringParts tokenizeUncached(@NonNull String itemString) {

        String[] strParts = itemString.split(" ");
        List<ItemStringPart> parts = new ArrayList<>();

        for (int index = 0; index < strParts.length; index++) {

            String strPart = strParts[index];

            if (index == 0) {
                if (strPart.contains(":")) {
                    String[] typeAndDurability = strPart.split(":", 2);
                    String type = typeAndDurability[0];
                    String durability = typeAndDurability[1];
                    parts.add(new ItemStringPart(ItemStringPartType.TYPE, type));
                    parts.add(new ItemStringPart(ItemStringPartType.DURABILITY, durability));
                } else {
                    parts.add(new ItemStringPart(ItemStringPartType.TYPE, strPart));
                }
                continue;
            }

            if (index == 1) {
                parts.add(new ItemStringPart(ItemStringPartType.AMOUNT, strPart));
                continue;
            }

            String[] keyAndValue = strPart.split(":", 2);
            if (keyAndValue.length != 2) {
                throw new IllegalArgumentException("cannot resolve part: " + strPart);
            }

            String keyStr = keyAndValue[0];
            String valueStr = keyAndValue[1];
            Optional<ItemStringPartType> partTypeOptional = Enums.matchIgnoreCase(ItemStringPartType.class, keyStr);

            if (!partTypeOptional.isPresent()) {
                Optional<OEnchantment> enchantmentOptional = OEnchantment.match(keyStr);
                if (enchantmentOptional.isPresent()) {
                    OEnchantment enchantment = enchantmentOptional.get();
                    parts.add(new ItemStringPart(ItemStringPartType.ENCHANTMENT, enchantment.name(), valueStr));
                }
                continue;
            }

            parts.add(new ItemStringPart(partTypeOptional.get(), valueStr));
        }

        return new ItemStringParts(parts, this.partParserRegistry);
    }
}
