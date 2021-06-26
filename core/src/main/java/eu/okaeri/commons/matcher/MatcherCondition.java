package eu.okaeri.commons.matcher;

public interface MatcherCondition<T> {
    boolean matches(T element);
}
