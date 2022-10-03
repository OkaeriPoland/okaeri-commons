package eu.okaeri.commons.bukkit.holographicdisplays;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Holo {

    protected final Plugin plugin;
    protected Location location;
    protected Hologram hologram;
    protected List<HologramLine> lines = new ArrayList<>();
    protected List<HoloLine> rawLines = new ArrayList<>();

    public static Holo create(@NonNull Plugin plugin, @NonNull Location location) {
        return new Holo(plugin).location(location);
    }

    public static Holo create(@NonNull Plugin plugin) {
        return new Holo(plugin);
    }

    public Holo location(@NonNull Location location) {
        this.location = location;
        return this;
    }

    // single append
    public Holo appendItem(@NonNull ItemStack item) {
        return this.appendLine(HoloLine.of(item));
    }

    public Holo appendText(@NonNull String content) {
        return this.appendLine(HoloLine.of(content));
    }

    public Holo appendLine(@NonNull HoloLine content) {
        this.rawLines.add(content);
        return this;
    }

    // single put
    public Holo putItem(int index, @NonNull ItemStack item) {
        return this.putLine(index, HoloLine.of(item));
    }

    public Holo putText(int index, @NonNull String content) {
        return this.putLine(index, HoloLine.of(content));
    }

    public Holo putLine(int index, @NonNull HoloLine content) {
        this.rawLines.set(index, content);
        return this;
    }

    // single insert
    public Holo insertItem(int index, @NonNull ItemStack item) {
        return this.insertLine(index, HoloLine.of(item));
    }

    public Holo insertText(int index, @NonNull String content) {
        return this.insertLine(index, HoloLine.of(content));
    }

    public Holo insertLine(int index, @NonNull HoloLine content) {
        this.rawLines.add(index, content);
        return this;
    }

    // append multiple
    public Holo appendItems(@NonNull List<ItemStack> items) {
        return this.appendLines(items.stream()
            .map(HoloLine::of)
            .collect(Collectors.toList()));
    }

    public Holo appendTexts(@NonNull List<String> contents) {
        return this.appendLines(contents.stream()
            .map(HoloLine::of)
            .collect(Collectors.toList()));
    }

    public Holo appendLines(@NonNull List<HoloLine> contents) {
        contents.forEach(this::appendLine);
        return this;
    }

    // replace
    public Holo replaceLines(@NonNull List<HoloLine> contents) {
        return this.clear().appendLines(contents);
    }

    // visibility
    public Holo visibilityDefault(boolean visible) {

        if (this.hologram == null) {
            throw new IllegalStateException("Visibility change before #update() or race condition (try using #update with callback argument)");
        }

        this.hologram.getVisibilityManager().setVisibleByDefault(visible);
        return this;
    }

    public Holo visibilityUpdate(@NonNull Function<Player, Boolean> function) {

        if (this.hologram == null) {
            throw new IllegalStateException("Visibility change before #update() or race condition (try using #update with callback argument)");
        }

        VisibilityManager visibilityManager = this.hologram.getVisibilityManager();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (function.apply(player)) {
                visibilityManager.showTo(player);
            } else {
                visibilityManager.hideTo(player);
            }
        }

        return this;
    }

    public Holo visibilityBy(@NonNull Player owner, boolean hideToOwner) {
        UUID uniqueId = owner.getUniqueId();
        return this.visibilityUpdate(player -> {
            // owner self visibility
            if (player.getUniqueId().equals(uniqueId)) {
                return !hideToOwner;
            }
            // other player visibility
            return player.canSee(owner);
        });
    }

    public Holo visibilityBy(@NonNull Player owner) {
        return this.visibilityBy(owner, false);
    }

    public Holo visibilityHideTo(@NonNull Player player) {
        if (this.hologram == null) {
            throw new IllegalStateException("Visibility change before #update() or race condition (try using #update with callback argument)");
        }
        this.hologram.getVisibilityManager().hideTo(player);
        return this;
    }

    public Holo visibilityShowTo(@NonNull Player player) {
        if (this.hologram == null) {
            throw new IllegalStateException("Visibility change before #update() or race condition (try using #update with callback argument)");
        }
        this.hologram.getVisibilityManager().showTo(player);
        return this;
    }

    // other
    public Holo clear() {
        this.rawLines.clear();
        return this;
    }

    public Holo update(@NonNull Consumer<Holo> callback) {

        if (Bukkit.isPrimaryThread()) {
            this.updateUnsafe();
            callback.accept(this);
            return this;
        }

        Bukkit.getScheduler().runTask(this.plugin, () -> this.update(callback));
        return this;
    }

    public Holo update() {
        return this.update(Function.identity()::apply);
    }

    public Holo update(@NonNull Runnable callback) {
        return this.update(holo -> callback.run());
    }

    protected Holo updateUnsafe() {

        // no location, no hologram
        if (this.location == null) {
            throw new RuntimeException("Location cannot be null");
        }

        // create hologram if null
        if (this.hologram == null) {
            Hologram hologram = HologramsAPI.createHologram(this.plugin, this.location);
            if (hologram == null) {
                throw new RuntimeException("Failed to create hologram! HologramsAPI#createHologram returned null");
            }
            this.hologram = hologram;
        }

        // teleport hologram if location changed
        if (!this.location.equals(this.hologram.getLocation())) {
            this.hologram.teleport(this.location);
        }

        // lines is longer than rawLines
        if (this.lines.size() > this.rawLines.size()) {
            for (int i = this.rawLines.size(); i < this.lines.size(); i++) {
                this.lines.remove(i);
                this.hologram.removeLine(i);
            }
        }

        // update lines
        for (int i = 0; i < this.rawLines.size(); i++) {

            // line not present
            if (this.lines.size() <= i) {
                this.lines.add(null);
            }

            // update line
            HologramLine hologramLine = this.lines.get(i);
            HoloLine rawLine = this.rawLines.get(i);

            // match - text
            if ((hologramLine instanceof TextLine) && rawLine.isText()) {
                ((TextLine) hologramLine).setText(rawLine.getText());
                continue;
            }

            // match - item
            if ((hologramLine instanceof ItemLine) && rawLine.isItem()) {
                ((ItemLine) hologramLine).setItemStack(rawLine.getItem());
                continue;
            }

            // type mismatch
            if (hologramLine != null) {
                this.hologram.removeLine(i);
            }

            // add new
            this.lines.set(i, rawLine.isText()
                ? this.hologram.insertTextLine(i, rawLine.getText())
                : this.hologram.insertItemLine(i, rawLine.getItem()));
        }

        return this;
    }

    public Holo delete() {

        if (Bukkit.isPrimaryThread()) {
            this.hologram.delete();
        } else {
            Hologram targetHologram = this.hologram;
            Bukkit.getScheduler().runTask(this.plugin, targetHologram::delete);
        }

        this.hologram = null;
        return this;
    }
}
