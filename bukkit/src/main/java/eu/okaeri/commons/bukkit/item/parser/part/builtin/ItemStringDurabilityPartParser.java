package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.bukkit.item.parser.ItemStringException;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;

public class ItemStringDurabilityPartParser implements ItemStringPartParser<Short> {

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.DURABILITY;
    }

    @Override
    public Short parse(ItemStringPart part) {

        Short amount;
        try {
            amount = Short.valueOf(part.getValue());
        } catch (NumberFormatException exception) {
            throw new ItemStringException("item amount must be a valid non-negative number");
        }

        if (amount < 0) {
            throw new ItemStringException("item amount cannot be less than 0");
        }

        return amount;
    }
}
