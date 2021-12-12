package eu.okaeri.commons.indexedset;

import lombok.NonNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * {@link IndexedSet} backend by {@link LinkedHashMap}.
 *
 * @param <VT> the type of the set values (map value)
 * @param <KT> the type of the set index (map key)
 */
public class IndexedLinkedHashSet<VT, KT> extends AbstractIndexedSet<VT, KT> {

    public IndexedLinkedHashSet(@NonNull Function<VT, KT> keyFunction) {
        super(keyFunction, new LinkedHashMap<>());
    }

    public IndexedLinkedHashSet(@NonNull Function<VT, KT> keyFunction, @NonNull VT[] elements) {
        super(keyFunction, new LinkedHashMap<>());
        this.addAll(Arrays.asList(elements));
    }

    public IndexedLinkedHashSet(@NonNull AbstractIndexedSet<VT, KT> source) {
        super(source.keyFunction, new LinkedHashMap<>());
        this.addAll(source.data.values());
    }
}
