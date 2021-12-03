package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link LinkedHashMap}.
 *
 * @param <KT> the type of the set index (map key)
 * @param <VT> the type of the set values (map value)
 */
public class IndexedLinkedHashSet<KT, VT> extends AbstractIndexedSet<KT, VT> {

    public IndexedLinkedHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new LinkedHashMap<>());
    }

    public IndexedLinkedHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new LinkedHashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedLinkedHashSet(@NonNull AbstractIndexedSet<KT, VT> source) {
        super(source.keyFunction, new LinkedHashMap<>());
        this.addAll(source.data.values());
    }
}
