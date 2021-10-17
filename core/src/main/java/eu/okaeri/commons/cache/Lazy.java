package eu.okaeri.commons.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Lazy<T> implements Supplier<T> {

    public static <A> Lazy<A> of(@NonNull Supplier<A> supplier) {
        return new Lazy<>(supplier);
    }

    @NonNull protected Supplier<T> supplier;
    @Getter protected Instant lastUpdated;
    @Getter protected T value;

    @Override
    public T get() {
        if (this.lastUpdated == null) {
            this.value = this.supplier.get();
            this.lastUpdated = Instant.now();
        }
        return this.value;
    }
}
