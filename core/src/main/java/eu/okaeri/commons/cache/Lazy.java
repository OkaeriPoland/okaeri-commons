package eu.okaeri.commons.cache;

import lombok.*;

import java.time.Instant;
import java.util.function.Supplier;

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

    @Override
    public T get() {
        if (this.lastUpdated == null) {
            this.value = this.supplier.get();
            this.lastUpdated = Instant.now();
        }
        return this.value;
    }

    @Override
    public void run() {
        this.get();
    }
}
