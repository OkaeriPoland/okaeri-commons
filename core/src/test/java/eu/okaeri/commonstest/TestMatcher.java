package eu.okaeri.commonstest;

import eu.okaeri.commons.matcher.Matcher;
import org.junit.jupiter.api.Test;

import static eu.okaeri.commons.matcher.Matcher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMatcher {

    @Test
    public void test_matcher_1() {

        Integer result = Matcher.of(2)
            .when(eq(1), re(1))
            .when(eq(2), re(20))
            .any(re(666))
            .result();

        assertEquals(20, result);
    }

    @Test
    public void test_matcher_any_1() {

        Integer result = Matcher.of(222)
            .when(eq(1), re(1))
            .when(eq(2), re(20))
            .any(re(666))
            .result();

        assertEquals(666, result);
    }

    @Test
    public void test_matcher_when_multiple_1() {

        Integer result = Matcher.of(3)
            .when(eqa(1, 2, 3, 4), re(20))
            .any(re(666))
            .result();

        assertEquals(20, result);
    }
}
