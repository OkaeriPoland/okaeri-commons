package eu.okaeri.commons;

/**
 * Hacky helper for number validation/parsing. Eat it.
 */
public final class Numbers {

    @Deprecated
    public static boolean isInteger(String text) {
        return isInt(text);
    }

    public static boolean isInt(String text) {
        return asInt(text) != null;
    }

    public static boolean isLong(String text) {
        return asLong(text) != null;
    }

    public static boolean isDouble(String text) {
        return asDouble(text) != null;
    }

    public static boolean isFloat(String text) {
        return asFloat(text) != null;
    }

    public static Integer asInt(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static int asInt(String text, int def) {
        Integer i = asInt(text);
        return (i == null) ? def : i;
    }

    public static Long asLong(String text) {
        try {
            return Long.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static long asLong(String text, long def) {
        Long value = asLong(text);
        return (value == null) ? def : value;
    }

    public static Double asDouble(String text) {
        try {
            return Double.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static double asDouble(String text, double def) {
        Double value = asDouble(text);
        return (value == null) ? def : value;
    }

    public static Float asFloat(String text) {
        try {
            return Float.valueOf(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static float asFloat(String text, float def) {
        Float value = asFloat(text);
        return (value == null) ? def : value;
    }
}
