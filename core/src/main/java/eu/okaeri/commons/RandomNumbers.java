package eu.okaeri.commons;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomNumbers {

    public static boolean chance(double chance) {
        return chance(ThreadLocalRandom.current(), chance);
    }

    public static boolean chance(@NonNull Random random, double chance) {
        return (chance > 0.0) && ((chance >= 100.0) || (chance >= nextDouble(random, 0.0, 100.0)));
    }

    public static <T> Optional<T> choice(@NonNull List<T> things) {
        return choice(ThreadLocalRandom.current(), things);
    }

    public static <T> Optional<T> choice(@NonNull Random random, @NonNull List<T> things) {
        return things.isEmpty() ? Optional.empty() : Optional.of(things.get(random.nextInt(things.size())));
    }

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static int nextInt(@NonNull Random random, int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static long nextLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static long nextLong(@NonNull Random random, long min, long max) {
        return (long) ((random.nextDouble() * (max - min)) + min);
    }

    public static double nextDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double nextDouble(@NonNull Random random, double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

    public static float nextFloat(float min, float max) {
        return nextFloat(ThreadLocalRandom.current(), min, max);
    }

    public static float nextFloat(@NonNull Random random, float min, float max) {
        return (random.nextFloat() * (max - min)) + min;
    }
}
