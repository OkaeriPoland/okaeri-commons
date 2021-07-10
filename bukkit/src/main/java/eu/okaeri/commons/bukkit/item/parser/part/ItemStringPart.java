package eu.okaeri.commons.bukkit.item.parser.part;

import lombok.Data;
import lombok.NonNull;

@Data
public class ItemStringPart {

    private final ItemStringPartType type;
    private final String value;
    private final String[] extended;

    public ItemStringPart(@NonNull ItemStringPartType type, @NonNull String value) {
        this.type = type;
        this.value = value;
        this.extended = new String[0];
    }

    public ItemStringPart(@NonNull ItemStringPartType type, @NonNull String value, @NonNull String... extended) {
        this.type = type;
        this.value = value;
        this.extended = extended;
    }
}
