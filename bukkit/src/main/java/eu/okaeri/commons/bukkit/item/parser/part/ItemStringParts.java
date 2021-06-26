package eu.okaeri.commons.bukkit.item.parser.part;

import eu.okaeri.commons.bukkit.item.parser.ItemStringException;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
public class ItemStringParts implements Iterable<ItemStringPart> {

    private final List<ItemStringPart> parts;
    private final ItemStringPartParserRegistry partParserRegistry;

    public ItemStringParts(List<ItemStringPart> parts, ItemStringPartParserRegistry partParserRegistry) {
        this.parts = Collections.unmodifiableList(parts);
        this.partParserRegistry = partParserRegistry;
    }

    public <T> T firstOfAsOrThrow(ItemStringPartType type, Class<T> target) {

        ItemStringPartParser parser = this.partParserRegistry.get(type);
        ItemStringPart part = this.firstOf(type).orElseThrow(() -> new ItemStringException("no part of type " + type));

        return (parser == null) ? target.cast(part.getValue()) : target.cast(parser.parse(part));
    }

    public Optional<ItemStringPart> firstOf(ItemStringPartType type) {
        return this.parts.stream()
                .filter(part -> part.getType() == type)
                .findFirst();
    }

    public <T> Stream<T> allOfAsOrThrow(ItemStringPartType type, Class<T> target) {

        ItemStringPartParser parser = this.partParserRegistry.get(type);
        if (parser == null) {
            return this.allOf(type).map(target::cast);
        }

        return this.allOf(type)
                .map((Function<ItemStringPart, Object>) parser::parse)
                .map(target::cast);
    }

    public Stream<ItemStringPart> allOf(ItemStringPartType type) {
        return this.parts.stream()
                .filter(part -> part.getType() == type);
    }

    public <T> List<T> listOfAsOrThrow(ItemStringPartType type, Class<T> target) {
        return this.allOfAsOrThrow(type, target)
                .collect(Collectors.toList());
    }

    public List<ItemStringPart> listOf(ItemStringPartType type) {
        return this.allOf(type)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<ItemStringPart> iterator() {
        return this.parts.iterator();
    }

    @Override
    public void forEach(Consumer<? super ItemStringPart> action) {
        this.parts.forEach(action);
    }

    @Override
    public Spliterator<ItemStringPart> spliterator() {
        return this.parts.spliterator();
    }
}
