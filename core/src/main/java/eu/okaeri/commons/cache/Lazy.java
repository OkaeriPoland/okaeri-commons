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
    protected volatile @Getter Instant lastUpdated;
    protected volatile @Getter T value;

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
    public T get() {
        return (this.getLastUpdated() == null) ? this.get0() : this.getValue();
    }

    @Synchronized
    protected T get0() {
        if (this.getLastUpdated() == null) {
            this.value = this.supplier.get();
            this.lastUpdated = Instant.now();
        }
        return this.getValue();
    }

    /**
     * Void-returning {@link #get()} for use as a scheduled task.
     */
    @Override
    public void run() {
        this.get();
    }
}
