package eu.okaeri.commons.cache;

import lombok.*;

import java.time.Instant;
import java.util.function.Supplier;

/**
 * Trivial and thread-safe Lazy wrapper.
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Lazy<T> implements Supplier<T>, Runnable {

    protected @NonNull Supplier<T> supplier;
    protected @Getter Instant lastUpdated;
    protected @Getter T value;

    public static <A> Lazy<A> of(@NonNull Supplier<A> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Gets the lazy value. In case there is no value present, the provided
     * {@link #supplier} is used to update the lazy value before returning.
     *
     * @return Current cached value
     */
    @Override
    @Synchronized
    public T get() {
        if (this.lastUpdated == null) {
            this.value = this.supplier.get();
            this.lastUpdated = Instant.now();
        }
        return this.value;
    }

    /**
     * Void-returning {@link #get()} for use as a scheduled task.
     */
    @Override
    public void run() {
        this.get();
    }
}
