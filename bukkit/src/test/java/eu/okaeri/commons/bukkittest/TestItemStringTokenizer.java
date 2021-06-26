package eu.okaeri.commons.bukkittest;

import eu.okaeri.commons.bukkit.easytype.OEnchantment;
import eu.okaeri.commons.bukkit.easytype.OEnchantmentPair;
import eu.okaeri.commons.bukkit.item.parser.ItemStringException;
import eu.okaeri.commons.bukkit.item.parser.ItemStringTokenizer;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPart;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartParserRegistry;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringPartType;
import eu.okaeri.commons.bukkit.item.parser.part.ItemStringParts;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestItemStringTokenizer {

    private ItemStringTokenizer tokenizer;

    @BeforeAll
    public void prepare() {
        this.tokenizer = new ItemStringTokenizer(new ItemStringPartParserRegistry());
    }

    @Test
    public void test_simple_1() {

        ItemStringParts tokens = this.tokenizer.tokenize("diamond_sword 1");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "diamond_sword"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "1"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIAMOND_SWORD, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(1, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
    }

    @Test
    public void test_type_amount() {

        ItemStringParts tokens = this.tokenizer.tokenize("diamond_sword 1");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "diamond_sword"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "1"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIAMOND_SWORD, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(1, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
    }

    @Test
    public void test_type_durability_amount_1() {

        ItemStringParts tokens = this.tokenizer.tokenize("diamond_sword:123 1");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "diamond_sword"),
                new ItemStringPart(ItemStringPartType.DURABILITY, "123"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "1"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIAMOND_SWORD, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(1, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
    }

    @Test
    public void test_type_durability_amount_invalid_1() {
        assertThrows(ItemStringException.class, () -> this.tokenizer.tokenize("diamond_sword:-123 1").firstOfAsOrThrow(ItemStringPartType.DURABILITY, Short.class));
        assertThrows(ItemStringException.class, () -> this.tokenizer.tokenize("diamond_sword:123132132 1").firstOfAsOrThrow(ItemStringPartType.DURABILITY, Short.class));
        assertThrows(ItemStringException.class, () -> this.tokenizer.tokenize("diamond_sword:0.3 1").firstOfAsOrThrow(ItemStringPartType.DURABILITY, Short.class));
    }

    @Test
    public void test_type_amount_name_1() {

        ItemStringParts tokens = this.tokenizer.tokenize("dirt 32 name:Ababab");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "dirt"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "32"),
                new ItemStringPart(ItemStringPartType.NAME, "Ababab"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIRT, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(32, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals("Ababab", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
    }

    @Test
    public void test_type_amount_name_2() {

        ItemStringParts tokens = this.tokenizer.tokenize("stone 2 name:Pretty_stone");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "stone"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "2"),
                new ItemStringPart(ItemStringPartType.NAME, "Pretty_stone"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.STONE, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(2, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals("Pretty stone", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
    }

    @Test
    public void test_type_amount_name_3() {

        ItemStringParts tokens = this.tokenizer.tokenize("stone 3 name:&cPretty_red_stone");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "stone"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "3"),
                new ItemStringPart(ItemStringPartType.NAME, "&cPretty_red_stone"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.STONE, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(3, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals("§cPretty red stone", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
    }

    @Test
    public void test_type_amount_name_lore_1() {

        ItemStringParts tokens = this.tokenizer.tokenize("stone 2 name:Pretty_stone lore:Yes,|very_pretty|in_fact");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "stone"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "2"),
                new ItemStringPart(ItemStringPartType.NAME, "Pretty_stone"),
                new ItemStringPart(ItemStringPartType.LORE, "Yes,|very_pretty|in_fact"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.STONE, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(2, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals("Pretty stone", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
        assertEquals("Yes,\nvery pretty\nin fact", tokens.firstOfAsOrThrow(ItemStringPartType.LORE, String.class));
    }

    @Test
    public void test_type_amount_name_lore_2() {

        ItemStringParts tokens = this.tokenizer.tokenize("stone 2 name:Pretty_stone lore:&eYes,|very_pretty|in_fact");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "stone"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "2"),
                new ItemStringPart(ItemStringPartType.NAME, "Pretty_stone"),
                new ItemStringPart(ItemStringPartType.LORE, "&eYes,|very_pretty|in_fact"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.STONE, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(2, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals("Pretty stone", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
        assertEquals("§eYes,\nvery pretty\nin fact", tokens.firstOfAsOrThrow(ItemStringPartType.LORE, String.class));
    }

    @Test
    public void test_type_amount_name_lore_enchantment_1() {

        ItemStringParts tokens = this.tokenizer.tokenize("diamond_sword 1 sharpness:5 name:Nice_sword lore:&eVery_nice|&ein_fact");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "diamond_sword"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "1"),
                new ItemStringPart(ItemStringPartType.ENCHANTMENT, OEnchantment.DAMAGE_ALL.name(), "5"),
                new ItemStringPart(ItemStringPartType.NAME, "Nice_sword"),
                new ItemStringPart(ItemStringPartType.LORE, "&eVery_nice|&ein_fact"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIAMOND_SWORD, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(1, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));
        assertEquals(new OEnchantmentPair(OEnchantment.DAMAGE_ALL, 5), tokens.firstOfAsOrThrow(ItemStringPartType.ENCHANTMENT, OEnchantmentPair.class));
        assertEquals("Nice sword", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
        assertEquals("§eVery nice\n§ein fact", tokens.firstOfAsOrThrow(ItemStringPartType.LORE, String.class));
    }

    @Test
    public void test_type_amount_name_lore_enchantment_2() {

        ItemStringParts tokens = this.tokenizer.tokenize("diamond_sword 1 sharpness:5 unbreaking:2 name:Nice_sword lore:&eVery_nice|&ein_fact");
        List<ItemStringPart> valid = Arrays.asList(
                new ItemStringPart(ItemStringPartType.TYPE, "diamond_sword"),
                new ItemStringPart(ItemStringPartType.AMOUNT, "1"),
                new ItemStringPart(ItemStringPartType.ENCHANTMENT, OEnchantment.DAMAGE_ALL.name(), "5"),
                new ItemStringPart(ItemStringPartType.ENCHANTMENT, OEnchantment.DURABILITY.name(), "2"),
                new ItemStringPart(ItemStringPartType.NAME, "Nice_sword"),
                new ItemStringPart(ItemStringPartType.LORE, "&eVery_nice|&ein_fact"));

        assertIterableEquals(valid, tokens);
        assertEquals(Material.DIAMOND_SWORD, tokens.firstOfAsOrThrow(ItemStringPartType.TYPE, Material.class));
        assertEquals(1, tokens.firstOfAsOrThrow(ItemStringPartType.AMOUNT, Integer.class));

        List<OEnchantmentPair> validEnchants = Arrays.asList(new OEnchantmentPair(OEnchantment.DAMAGE_ALL, 5), new OEnchantmentPair(OEnchantment.DURABILITY, 2));
        assertIterableEquals(validEnchants, tokens.listOfAsOrThrow(ItemStringPartType.ENCHANTMENT, OEnchantmentPair.class));

        assertEquals("Nice sword", tokens.firstOfAsOrThrow(ItemStringPartType.NAME, String.class));
        assertEquals("§eVery nice\n§ein fact", tokens.firstOfAsOrThrow(ItemStringPartType.LORE, String.class));
    }
}
