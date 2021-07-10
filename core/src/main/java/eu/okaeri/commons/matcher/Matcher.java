package eu.okaeri.commons.matcher;

import lombok.NonNull;

public class Matcher<T, R> {

    public static <T> Matcher<T, Object> of(@NonNull T object) {
        return new Matcher<T, Object>(object);
    }

    public static <T, R> Matcher<T, R> of(@NonNull T object, @NonNull Class<R> result) {
        return new Matcher<T, R>(object);
    }

    private T value;
    private R result;
    private boolean matched;

    private Matcher(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public <A> Matcher<T, A> when(@NonNull MatcherCondition<T> condition, @NonNull MatcherResult<A> result) {
        if (!this.matched && condition.matches(this.value)) {
            this.matched = true;
            this.result = (R) result.fetch();
        }
        return (Matcher<T, A>) this;
    }

    @SuppressWarnings("unchecked")
    public <A> Matcher<T, A> any(@NonNull MatcherResult<A> result) {
        return (Matcher<T, A>) this.when(Matcher::any, (MatcherResult<R>) result);
    }

    public R result() {
        return this.result;
    }

    public static boolean any(Object o) {
        return true;
    }

    public static <A> MatcherCondition<A> eq(A a) {
        return e -> e == a;
    }

    public static <A> MatcherResult<A> re(A a) {
        return () -> a;
    }
}
