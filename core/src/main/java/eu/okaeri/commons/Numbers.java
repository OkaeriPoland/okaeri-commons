package eu.okaeri.commons;

import org.jetbrains.annotations.Nullable;

/**
 * Hacky helper for number validation/parsing. Eat it.
 */
public final class Numbers {

    @Deprecated
    public static boolean isInteger(@Nullable String text) {
        return isInt(text);
    }

    public static boolean isInt(@Nullable String text) {
        return asInt(text) != null;
    }

    public static boolean isLong(@Nullable String text) {
        return asLong(text) != null;
    }

    public static boolean isDouble(@Nullable String text) {
        return asDouble(text) != null;
    }

    public static boolean isFloat(@Nullable String text) {
        return asFloat(text) != null;
    }

    @Nullable
    public static Integer asInt(@Nullable String text) {
        if (text == null) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static int asInt(@Nullable String text, int def) {
        Integer i = asInt(text);
        return (i == null) ? def : i;
    }

    @Nullable
    public static Long asLong(@Nullable String text) {
        if (text == null) {
            return null;
        }
        try {
            return Long.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static long asLong(@Nullable String text, long def) {
        Long value = asLong(text);
        return (value == null) ? def : value;
    }

    @Nullable
    public static Double asDouble(@Nullable String text) {
        if (text == null) {
            return null;
        }
        try {
            return Double.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static double asDouble(@Nullable String text, double def) {
        Double value = asDouble(text);
        return (value == null) ? def : value;
    }

    @Nullable
    public static Float asFloat(@Nullable String text) {
        if (text == null) {
            return null;
        }
        try {
            return Float.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static float asFloat(@Nullable String text, float def) {
        Float value = asFloat(text);
        return (value == null) ? def : value;
    }
}
