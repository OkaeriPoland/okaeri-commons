package eu.okaeri.commons.indexedset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
public class AbstractIndexedSet<VT, KT> implements IndexedSet<VT, KT> {

    @NonNull protected final Function<VT, KT> keyFunction;
    @NonNull protected final Map<KT, VT> data;

    @Override
    public VT get(KT key) {
        return this.data.get(key);
    }

    @Override
    public boolean containsKey(KT key) {
        return this.data.containsKey(key);
    }

    @Override
    public VT removeKey(KT key) {
        return this.data.remove(key);
    }

    @Override
    public Set<KT> keySet() {
        return this.data.keySet();
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set
     * @return {@code true} if this set did not already contain the specified element
     * @throws IllegalArgumentException if element with the same key (derived from the {@code keyFunction}) exists.
     */
    @Override
    public boolean add(@NonNull VT value) {

        KT key = this.keyFunction.apply(value);
        if (this.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate element for key '" + key + "'. Use IndexedSet#put to allow overrides.");
        }

        return this.data.put(key, value) == null;
    }

    /**
     * Adds all of the elements in the specified collection to this set if they're not already present.
     *
     * @param values collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws IllegalArgumentException if element with the same key (derived from the {@code keyFunction}) exists.
     */
    @Override
    public boolean addAll(@NonNull Collection<@NonNull ? extends VT> values) {
        boolean changed = false;
        for (VT value : values) {
            if (this.add(value)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public VT put(VT value) {
        KT key = this.keyFunction.apply(value);
        return this.data.put(key, value);
    }

    @Override
    public void putAll(Collection<? extends VT> values) {
        values.forEach(this::put);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> values) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean contains(Object value) {
        return this.data.containsValue(value);
    }

    @Override
    public Iterator<VT> iterator() {
        return this.data.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.data.values().toArray();
    }

    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T[] toArray(T[] a) {
        return this.data.values().toArray(a);
    }

    @Override
    public boolean remove(Object value) {
        return this.data.values().remove(value);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> values) {
        return this.data.values().containsAll(values);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> values) {
        return this.data.values().removeAll(values);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public String toString() {
        return this.data.toString();
    }
}
