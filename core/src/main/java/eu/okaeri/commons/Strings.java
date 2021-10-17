package eu.okaeri.commons;

import lombok.NonNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class Strings {

    public static String random(@NonNull Random random, int length, int start, int end) {
        return random.ints(start, end + 1)
                .filter(i -> ((i <= 57) || (i >= 65)) && ((i <= 90) || (i >= 97)))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String randomAlphanumeric(@NonNull Random random, int length) {
        return random(random, length, 48, 122); // 48=0, 122=z
    }

    public static String randomAlphanumeric(int length) {
        return randomAlphanumeric(ThreadLocalRandom.current(), length);
    }

    public static String randomAlphabetic(@NonNull Random random, int length) {
        return random(random, length, 65, 122); // 65=A, 122=z
    }

    public static String randomAlphabetic(int length) {
        return randomAlphabetic(ThreadLocalRandom.current(), length);
    }
}
