package eu.okaeri.commons.bukkit.easytype;

import eu.okaeri.commons.cache.CacheMap;
import eu.okaeri.commons.cache.Cached;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("FieldNamingConvention")
@EqualsAndHashCode(of = "name")
public class OEnchantment {

    private static final Map<String, OEnchantment> MATCH_CACHE = new CacheMap<>(1024);
    private static final Cached<List<OEnchantment>> ALL_ENCHANTMENTS = new Cached<List<OEnchantment>>() {
        @Override
        @SuppressWarnings("Convert2Lambda")
        public List<OEnchantment> resolve() {
            return Collections.unmodifiableList(Arrays.stream(this.getClass().getEnclosingClass().getDeclaredFields())
                    .filter(field -> {
                        int modifiers = field.getModifiers();
                        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
                    })
                    .map(new Function<Field, Object>() {
                        @Override
                        @SneakyThrows
                        public Object apply(Field field) {
                            return field.get(null);
                        }
                    })
                    .map(OEnchantment.class::cast)
                    .collect(Collectors.toList()));
        }
    };

    public static final OEnchantment ARROW_DAMAGE = new OEnchantment("ARROW_DAMAGE", "POWER", "ARROW_POWER", "AD");
    public static final OEnchantment ARROW_FIRE = new OEnchantment("ARROW_FIRE", "FLAME", "FLAME_ARROW", "FIRE_ARROW", "AF");
    public static final OEnchantment ARROW_INFINITE = new OEnchantment("ARROW_INFINITE", "INFINITY", "INF_ARROWS", "INFINITE_ARROWS", "INFINITE", "UNLIMITED", "UNLIMITED_ARROWS", "AI");
    public static final OEnchantment ARROW_KNOCKBACK = new OEnchantment("ARROW_KNOCKBACK", "PUNCH", "ARROW_KNOCKBACK", "ARROWKB", "ARROW_PUNCH", "AK");
    public static final OEnchantment BINDING_CURSE = new OEnchantment("BINDING_CURSE", "BINDING_CURSE", "BIND_CURSE", "BINDING", "BIND");
    public static final OEnchantment CHANNELING = new OEnchantment("CHANNELING", "CHANNELLING", "CHANNELLING", "CHANELLING", "CHANELING", "CHANNEL");
    public static final OEnchantment DAMAGE_ALL = new OEnchantment("DAMAGE_ALL", "SHARPNESS", "ALL_DAMAGE", "ALL_DMG", "DMG_ALL", "SHARP", "DAL");
    public static final OEnchantment DAMAGE_ARTHROPODS = new OEnchantment("DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS", "ARDMG", "BANE_OF_ARTHROPOD", "ARTHROPOD", "DAR");
    public static final OEnchantment DAMAGE_UNDEAD = new OEnchantment("DAMAGE_UNDEAD", "SMITE", "UNDEAD_DAMAGE", "DU");
    public static final OEnchantment DEPTH_STRIDER = new OEnchantment("DEPTH_STRIDER", "DEPTH_STRIDER", "DEPTH", "STRIDER");
    public static final OEnchantment DIG_SPEED = new OEnchantment("DIG_SPEED", "EFFICIENCY", "MINE_SPEED", "CUT_SPEED", "DS", "EFF");
    public static final OEnchantment DURABILITY = new OEnchantment("DURABILITY", "UNBREAKING", "DURA", "D");
    public static final OEnchantment FIRE_ASPECT = new OEnchantment("FIRE_ASPECT", "FIRE_ASPECT", "FIRE", "MELEE_FIRE", "MELEE_FLAME", "FA");
    public static final OEnchantment FROST_WALKER = new OEnchantment("FROST_WALKER", "FROST_WALKER", "FROST", "WALKER");
    public static final OEnchantment IMPALING = new OEnchantment("IMPALING", "IMPALING", "IMPALE", "OCEAN_DAMAGE", "OCEAN_DMG");
    public static final OEnchantment SOUL_SPEED = new OEnchantment("SOUL_SPEED", "SOUL_SPEED", "SPEED_SOUL", "SOUL_RUNNER");
    public static final OEnchantment KNOCKBACK = new OEnchantment("KNOCKBACK", "KNOCKBACK", "K_BACK", "KB", "K");
    public static final OEnchantment LOOT_BONUS_BLOCKS = new OEnchantment("LOOT_BONUS_BLOCKS", "FORTUNE", "BLOCKS_LOOT_BONUS", "FORT", "LBB");
    public static final OEnchantment LOOT_BONUS_MOBS = new OEnchantment("LOOT_BONUS_MOBS", "LOOTING", "MOB_LOOT", "MOBS_LOOT_BONUS", "LBM");
    public static final OEnchantment LOYALTY = new OEnchantment("LOYALTY", "LOYALTY", "LOYAL", "RETURN");
    public static final OEnchantment LUCK = new OEnchantment("LUCK", "LUCK_OF_THE_SEA", "LUCK_OF_SEA", "LUCK_OF_SEAS", "ROD_LUCK");
    public static final OEnchantment LURE = new OEnchantment("LURE", "LURE", "ROD_LURE");
    public static final OEnchantment MENDING = new OEnchantment("MENDING", "MENDING");
    public static final OEnchantment MULTISHOT = new OEnchantment("MULTISHOT", "MULTISHOT", "TRIPLE_SHOT");
    public static final OEnchantment OXYGEN = new OEnchantment("OXYGEN", "RESPIRATION", "BREATH", "BREATHING", "O2", "O");
    public static final OEnchantment PIERCING = new OEnchantment("PIERCING", "PIERCING");
    public static final OEnchantment PROTECTION_ENVIRONMENTAL = new OEnchantment("PROTECTION_ENVIRONMENTAL", "PROTECTION", "PROTECT", "PROT");
    public static final OEnchantment PROTECTION_EXPLOSIONS = new OEnchantment("PROTECTION_EXPLOSIONS", "BLAST_PROTECTION", "BLAST_PROTECT", "EXPLOSIONS_PROTECTION", "EXP_PROT", "BPROTECTION", "BPROTECT", "EXPLOSION_PROTECTION", "BLAST_PROTECTION", "PE");
    public static final OEnchantment PROTECTION_FALL = new OEnchantment("PROTECTION_FALL", "FEATHER_FALLING", "FALL_PROT", "FEATHER_FALL", "FALL_PROTECTION", "FEATHER_FALLING", "PFA");
    public static final OEnchantment PROTECTION_FIRE = new OEnchantment("PROTECTION_FIRE", "FIRE_PROTECTION", "FIRE_PROT", "FIRE_PROTECT", "FIRE_PROTECTION", "FLAME_PROTECTION", "FLAME_PROTECT", "FLAME_PROT", "PF");
    public static final OEnchantment PROTECTION_PROJECTILE = new OEnchantment("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION", "PROJECTILE_PROTECTION", "PROJ_PROT", "PP");
    public static final OEnchantment QUICK_CHARGE = new OEnchantment("QUICK_CHARGE", "QUICK_CHARGE", "QUICKCHARGE", "QUICK_DRAW", "FAST_CHARGE", "FAST_DRAW");
    public static final OEnchantment RIPTIDE = new OEnchantment("RIPTIDE", "RIPTIDE", "RIP", "TIDE", "LAUNCH");
    public static final OEnchantment SILK_TOUCH = new OEnchantment("SILK_TOUCH", "SILK_TOUCH", "SOFT_TOUCH", "ST");
    public static final OEnchantment SWEEPING_EDGE = new OEnchantment("SWEEPING_EDGE", "SWEEPING", "SWEEPING_EDGE", "SWEEP_EDGE");
    public static final OEnchantment THORNS = new OEnchantment("THORNS", "THORNS", "HIGHCRIT", "THORN", "HIGHERCRIT", "T");
    public static final OEnchantment VANISHING_CURSE = new OEnchantment("VANISHING_CURSE", "VANISHING_CURSE", "VANISHING_CURSE", "VANISH_CURSE", "VANISHING", "VANISH");
    public static final OEnchantment WATER_WORKER = new OEnchantment("WATER_WORKER", "AQUA_AFFINITY", "WATER_WORKER", "AQUA_AFFINITY", "WATER_MINE", "WW");

    private final String name;
    private final Set<String> aliases;
    private final Supplier<Enchantment> supplier;

    public OEnchantment(String name, String... aliases) {
        this(name, aliases, new Supplier<Enchantment>() {

            private Enchantment enchantment = Enchantment.getByName(aliases[0]);

            @Override
            public Enchantment get() {
                return this.enchantment;
            }
        });
    }

    public OEnchantment(String name, String[] aliases, Supplier<Enchantment> supplier) {
        this.name = name;
        this.aliases = Stream.concat(Stream.of(name), Arrays.stream(aliases))
                .map(alias -> alias.replace("_", ""))
                .map(alias -> alias.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        this.supplier = supplier;
    }

    public String name() {
        return this.name;
    }

    public Enchantment value() {
        return this.supplier.get();
    }

    public static List<OEnchantment> all() {
        return ALL_ENCHANTMENTS.get();
    }

    public static Optional<OEnchantment> match(String name) {

        String simplifiedName = name
                .replace("_", "")
                .toLowerCase(Locale.ROOT);

        return Optional.ofNullable(MATCH_CACHE.computeIfAbsent(simplifiedName, (lName) -> all().stream()
                .filter(oe -> oe.aliases.contains(lName))
                .findFirst()
                .orElse(null)));
    }
}
