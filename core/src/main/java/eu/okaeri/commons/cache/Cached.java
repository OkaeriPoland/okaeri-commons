package eu.okaeri.commons.cache;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

@ToString
@EqualsAndHashCode(callSuper = true)
public class Cached<T> extends Lazy<T> {

    private @Getter @Setter Duration ttl;

    protected Cached(Duration ttl, @NonNull Supplier<T> supplier) {
        super(supplier);
        this.ttl = ttl;
    }

    public static <A> Cached<A> of(@NonNull Supplier<A> supplier) {
        return of(null, supplier);
    }

    public static <A> Cached<A> of(Duration ttl, @NonNull Supplier<A> supplier) {
        return new Cached<>(ttl, supplier);
    }

    protected boolean isEmptyOrExpired() {
        return (this.getLastUpdated() == null) || ((this.getTtl() != null) && (Duration.between(this.getLastUpdated(), Instant.now()).compareTo(this.getTtl()) > 0));
    }

    /**
     * Gets the current cache value. In case there is no value present or the ttl expired,
     * the provided {@link #supplier} is used to update the current cache value before returning.
     *
     * @return Current cached value
     */
    @Override
    public T get() {
        return this.isEmptyOrExpired() ? this.update() : this.getValue();
    }

    /**
     * Force updates the cached value.
     *
     * @return Updated cached value
     */
    public T update() {
        this.value = this.resolve();
        this.lastUpdated = Instant.now();
        return this.getValue();
    }

    /**
     * Gets the supplier value without updating the cached value.
     *
     * @return Supplier returned value
     */
    public T resolve() {
        return this.supplier.get();
    }

    /**
     * Void-returning {@link #update()} for use as a scheduled update task.
     */
    @Override
    public void run() {
        this.update();
    }
}
