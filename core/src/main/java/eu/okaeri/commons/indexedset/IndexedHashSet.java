package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link HashMap}.
 *
 * @param <VT> the type of the set values (map value)
 * @param <KT> the type of the set index (map key)
 */
public class IndexedHashSet<VT, KT> extends AbstractIndexedSet<VT, KT> {

    public IndexedHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new HashMap<>());
    }

    public IndexedHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new HashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedHashSet(@NonNull AbstractIndexedSet<VT, KT> source) {
        super(source.keyFunction, new HashMap<>());
        this.addAll(source.data.values());
    }
}
