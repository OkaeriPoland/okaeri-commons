package eu.okaeri.commons.indexedset;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * {@link IndexedSet} is a type of {@link Set} backend by {@link java.util.Map} or similar structure,
 * where the element uniqueness is determined by the value's key.
 * <p>
 * The key should be derived from the value itself.
 * <p>
 * The value is best as immutable type to prevent inconsistency.
 * <p>
 * Available implementations:
 * - {@link IndexedLinkedHashSet}: {@link java.util.LinkedHashMap} backend with keyFunction mapper
 * - {@link IndexedHashSet}: {@link java.util.HashMap} backend with keyFunction mapper
 * - {@link IndexedConcurrentHashSet}: {@link java.util.concurrent.ConcurrentHashMap} backend with keyFunction mapper
 * <p>
 * Note:
 * - {@link #add(Object)}, {@link #addAll(Collection)} methods are intended for duplicate-checked adding
 * - {@link #put(Object)}, {@link #putAll(Collection)} methods are intended to allow overrides
 *
 * @param <VT> the type of the set values (map value)
 * @param <KT> the type of the set index (map key)
 */
public interface IndexedSet<VT, KT> extends Set<VT> {

    /**
     * Returns a mutable {@link IndexedLinkedHashSet} containing the specified elements.
     *
     * @param keyFunction the mapping function for the keys
     * @param elements    the elements to be contained in set
     * @param <VT>        the type of the set values (map value)
     * @param <KT>        the type of the set index (map key)
     * @return a {@link IndexedSet} containing the specified elements
     */
    @SafeVarargs
    static <VT, KT> IndexedSet<VT, KT> of(Function<VT, KT> keyFunction, VT... elements) {
        return new IndexedLinkedHashSet<>(keyFunction, elements);
    }

    /**
     * Returns a builder for the {@link IndexedLinkedHashSet}.
     *
     * @param valueType the class matching the VT
     * @param keyType   the class matching the KT
     * @param <VT>      the type of the set values (map value)
     * @param <KT>      the type of the set index (map key)
     * @return an uninitialized {@link IndexedSetBuilder}
     */
    static <VT, KT> IndexedSetBuilder<VT, KT> builder(Class<? super VT> valueType, Class<? super KT> keyType) {
        return new IndexedSetBuilder<VT, KT>().type(IndexedLinkedHashSet.class);
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this
     * set contains no mapping for the key.
     * <p>
     * More formally, if this map contains a mapping from a key {@code k} to a value {@code v}
     * such that {@code Objects.equals(key, k)}, then this method returns {@code v}; otherwise
     * it returns {@code null}. (There can be at most one such mapping.)
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped
     **/
    VT get(KT key);

    /**
     * Returns {@code true} if this set contains a mapping for the specified key.
     * <p>
     * More formally, returns {@code true} if and only if this map contains a mapping
     * for a key {@code k} such that {@code Objects.equals(key, k)}. (There can be at
     * most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     */
    boolean containsKey(KT key);

    /**
     * Removes the mapping for a key from this set if it is present (optional operation).
     * <p>
     * More formally, if this map contains a mapping from key {@code k} to value {@code v} such
     * that {@code Objects.equals(key, k)}, that mapping is removed. (The map can contain at
     * most one such mapping.)
     * <p>
     * Returns the value to which this set previously associated the key, or {@code null}
     * if the map contained no mapping for the key.
     * <p>
     * The map will not contain a mapping for the specified key once the call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or {@code null} if there was no
     * mapping for {@code key}.
     */
    VT removeKey(KT key);

    /**
     * Returns a {@link Set} view of the keys contained in this set.
     * <p>
     * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
     * <p>
     * If the map is modified while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of the iteration are undefined.
     * The set supports element removal, which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove}, {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations. It does not support the {@code add} or {@code addAll} operations.
     *
     * @return a set view of the keys contained in this set
     */
    Set<KT> keySet();

    /**
     * Associates the specified value with its key in this set (optional operation).
     * <p>
     * If the set previously contained a mapping for the key, the old value is replaced
     * by the specified value. (A map {@code m} is said to contain a mapping for a key
     * {@code k} if and only if {@link #containsKey(Object) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param value value to be associated with its key
     * @return the previous value associated with the same key, or {@code null} if there was no mapping for such key.
     */
    VT put(VT value);

    /**
     * Associates all the specified values with theirs keys in this set (optional operation).
     * <p>
     * If the set previously contained a mapping for the key, the old value is replaced
     * by the specified value. (A map {@code m} is said to contain a mapping for a key
     * {@code k} if and only if {@link #containsKey(Object) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param values values to be associated to theirs keys
     */
    void putAll(Collection<? extends VT> values);
}
