package eu.okaeri.commonstest;

import eu.okaeri.commons.cache.Cached;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestCached {

    @Test
    void test_cached_no_ttl() {
        Cached<Double> cached = Cached.of(() -> ThreadLocalRandom.current().nextDouble());
        double v1 = cached.get();
        double v2 = cached.get();
        assertEquals(v1, v2);
    }

    @Test
    void test_cached_ttl() throws InterruptedException {
        Cached<Double> cached = Cached.of(Duration.ofMillis(50), () -> ThreadLocalRandom.current().nextDouble());
        double v1 = cached.get();
        Thread.sleep(100);
        double v2 = cached.get();
        assertNotEquals(v1, v2);
    }
}
