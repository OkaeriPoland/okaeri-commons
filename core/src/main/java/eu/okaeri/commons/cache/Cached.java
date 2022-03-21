package eu.okaeri.commons.cache;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

@ToString
@EqualsAndHashCode(callSuper = false)
public class Cached<T> extends Lazy<T> {

    @Getter @Setter private Duration ttl;

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

    @Override
    public T get() {

        if (this.getLastUpdated() == null) {
            return super.get();
        }

        if (this.getTtl() == null) {
            return this.getValue();
        }

        Duration timeLived = Duration.between(this.getLastUpdated(), Instant.now());
        if (timeLived.compareTo(this.getTtl()) > 0) {
            return this.update();
        }

        return this.getValue();
    }

    public T update() {
        this.value = this.resolve();
        this.lastUpdated = Instant.now();
        return this.getValue();
    }

    public T resolve() {
        return this.supplier.get();
    }

    @Override
    public void run() {
        this.update();
    }
}
