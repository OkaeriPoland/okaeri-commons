package eu.okaeri.commons.bukkit.item.parser.part.builtin;

import eu.okaeri.commons.bukkit.item.parser.ItemStringException;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParser;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import lombok.NonNull;

public class ItemStringAmountPartParser implements ItemStringPartParser<Integer> {

    @Override
    public ItemStringPartType getForType() {
        return ItemStringPartType.AMOUNT;
    }

    @Override
    public Integer parse(@NonNull ItemStringPart part) {

        Integer amount;
        try {
            amount = Integer.valueOf(part.getValue());
        } catch (NumberFormatException exception) {
            throw new ItemStringException("item amount must be a valid positive number");
        }

        if (amount < 1) {
            throw new ItemStringException("item amount cannot be less than 1");
        }

        return amount;
    }
}
