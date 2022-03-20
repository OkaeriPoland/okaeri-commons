package eu.okaeri.commons.bukkit.holographicdisplays;

import lombok.*;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor
class HoloLine {

    public static HoloLine of(@NonNull String text) {
        return new HoloLine(text, null);
    }

    public static HoloLine of(@NonNull ItemStack item) {
        return new HoloLine(null, item);
    }

    private final String text;
    private final ItemStack item;

    public boolean isText() {
        return this.text != null;
    }

    public boolean isItem() {
        return this.item != null;
    }
}
