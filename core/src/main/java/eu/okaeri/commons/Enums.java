package eu.okaeri.commons;

import lombok.NonNull;

import java.util.Optional;

public final class Enums {

    public static <E extends Enum<E>> Optional<E> matchIgnoreCase(@NonNull Class<E> type, @NonNull String name, E fallback) {
        // 1:1 match ONE=ONE
        try {
            return Optional.of(Enum.valueOf(type, name));
        }
        // match first case-insensitive
        catch (IllegalArgumentException ignored) {
            Enum[] enumValues = type.getEnumConstants();
            for (Enum value : enumValues) {
                if (!name.equalsIgnoreCase(value.name())) {
                    continue;
                }
                return Optional.of(type.cast(value));
            }
        }
        // match fail
        return Optional.ofNullable(fallback);
    }

    public static <E extends Enum<E>> Optional<E> matchIgnoreCase(@NonNull Class<E> type, @NonNull String name) {
        return matchIgnoreCase(type, name, null);
    }

    public static <E extends Enum<E>> Optional<E> match(@NonNull Class<E> type, @NonNull String name, E fallback) {
        try {
            return Optional.of(Enum.valueOf(type, name));
        } catch (IllegalArgumentException exception) {
            return Optional.ofNullable(fallback);
        }
    }

    public static <E extends Enum<E>> Optional<E> match(@NonNull Class<E> type, @NonNull String name) {
        return match(type, name, null);
    }

    public static <E extends Enum<E>> boolean isValid(@NonNull Class<E> type, @NonNull String name) {
        return match(type, name).isPresent();
    }
}
