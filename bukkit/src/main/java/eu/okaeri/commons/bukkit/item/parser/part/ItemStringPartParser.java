package eu.okaeri.commons.bukkit.item.parser.part;

public interface ItemStringPartParser<O> {
    ItemStringPartType getForType();
    O parse(ItemStringPart part);
}
