package eu.okaeri.commons.bukkit.item.parser.part;

import lombok.Data;

@Data
public class ItemStringPart {

    private final ItemStringPartType type;
    private final String value;
    private final String[] extended;

    public ItemStringPart(ItemStringPartType type, String value) {
        this.type = type;
        this.value = value;
        this.extended = new String[0];
    }

    public ItemStringPart(ItemStringPartType type, String value, String... extended) {
        this.type = type;
        this.value = value;
        this.extended = extended;
    }
}
