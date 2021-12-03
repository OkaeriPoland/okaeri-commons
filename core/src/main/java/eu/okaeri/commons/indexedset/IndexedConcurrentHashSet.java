package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link ConcurrentHashMap}.
 *
 * @param <KT> the type of the set index (map key)
 * @param <VT> the type of the set values (map value)
 */
public class IndexedConcurrentHashSet<KT, VT> extends AbstractIndexedSet<KT, VT> {

    public IndexedConcurrentHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new ConcurrentHashMap<>());
    }

    public IndexedConcurrentHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new ConcurrentHashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedConcurrentHashSet(@NonNull AbstractIndexedSet<KT, VT> source) {
        super(source.keyFunction, new ConcurrentHashMap<>());
        this.addAll(source.data.values());
    }
}
