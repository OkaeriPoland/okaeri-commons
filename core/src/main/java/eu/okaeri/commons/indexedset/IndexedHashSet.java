package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link HashMap}.
 *
 * @param <KT> the type of the set index (map key)
 * @param <VT> the type of the set values (map value)
 */
public class IndexedHashSet<KT, VT> extends AbstractIndexedSet<KT, VT> {

    public IndexedHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new HashMap<>());
    }

    public IndexedHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new HashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedHashSet(@NonNull AbstractIndexedSet<KT, VT> source) {
        super(source.keyFunction, new HashMap<>());
        this.addAll(source.data.values());
    }
}
