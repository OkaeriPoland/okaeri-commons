package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.bukkit.easytype.OEnchantment;
import eu.okaeri.commons.bukkit.easytype.OEnchantmentPair;
import eu.okaeri.commons.bukkit.item.parser.ItemStringException;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;

import java.util.Optional;

public class ItemStringEnchantmentPartParser implements ItemStringPartParser<OEnchantmentPair> {

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.ENCHANTMENT;
    }

    @Override
    public OEnchantmentPair parse(ItemStringPart part) {

        Optional<OEnchantment> enchantmentOptional = OEnchantment.match(part.getValue());
        if (!enchantmentOptional.isPresent()) {
            throw new ItemStringException("unknown enchantment " + part.getValue());
        }

        if (part.getExtended().length < 1) {
            throw new ItemStringException("enchantment extended part must contain level");
        }

        Integer level;
        try {
            level = Integer.valueOf(part.getExtended()[0]);
        } catch (NumberFormatException exception) {
            throw new ItemStringException("enchantment level must be a valid positive number");
        }

        if (level < 1) {
            throw new ItemStringException("enchantment level cannot be less than 1");
        }

        return new OEnchantmentPair(enchantmentOptional.get(), level);
    }
}
