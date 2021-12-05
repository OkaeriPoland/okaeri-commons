package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link ConcurrentHashMap}.
 *
 * @param <VT> the type of the set values (map value)
 * @param <KT> the type of the set index (map key)
 */
public class IndexedConcurrentHashSet<KT, VT> extends AbstractIndexedSet<VT, KT> {

    public IndexedConcurrentHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new ConcurrentHashMap<>());
    }

    public IndexedConcurrentHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new ConcurrentHashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedConcurrentHashSet(@NonNull AbstractIndexedSet<VT, KT> source) {
        super(source.keyFunction, new ConcurrentHashMap<>());
        this.addAll(source.data.values());
    }
}
