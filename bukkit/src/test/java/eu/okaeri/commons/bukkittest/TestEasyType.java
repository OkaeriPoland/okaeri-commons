package eu.okaeri.commons.bukkittest;

import eu.okaeri.commons.bukkit.easytype.OEnchantment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEasyType {

    @Test
    public void test_enchantment_valid() {
        assertEquals(OEnchantment.DAMAGE_ALL, OEnchantment.match("sharpness").orElse(null));
        assertEquals(OEnchantment.DAMAGE_ALL, OEnchantment.match("dmgall").orElse(null));
        assertEquals(OEnchantment.MENDING, OEnchantment.match("mending").orElse(null));
        assertEquals(OEnchantment.MENDING, OEnchantment.match("Mending").orElse(null));
        assertEquals(OEnchantment.MENDING, OEnchantment.match("MENDING").orElse(null));
    }

    @Test
    public void test_enchantment_invalid() {
        assertEquals(null, OEnchantment.match("sharpnes").orElse(null));
        assertEquals(null, OEnchantment.match("ostrosc").orElse(null));
    }
}
