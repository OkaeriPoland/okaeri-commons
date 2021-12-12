package eu.okaeri.commons.indexedset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexedSetBuilder<VT, KT> {

    private Class<? extends AbstractIndexedSet> type;
    private AbstractIndexedSet<VT, KT> data;

    /**
     * Updates the type of the target {@link IndexedSet}.
     *
     * @param type the type of the target set
     * @return self
     */
    public IndexedSetBuilder<VT, KT> type(@NonNull Class<? extends AbstractIndexedSet> type) {
        this.type = type;
        return this;
    }

    /**
     * Updates the key function by replacing current set with the new one.
     *
     * @param keyFunction the key function to be used for the set
     * @return self
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public IndexedSetBuilder<VT, KT> keyFunction(@NonNull Function<VT, KT> keyFunction) {
        if (this.type == null) {
            throw new IllegalArgumentException("Specify the type before keyFunction!");
        }
        // resolve built-in
        if (this.type == IndexedLinkedHashSet.class) {
            this.data = new IndexedLinkedHashSet<>(keyFunction);
            return this;
        }
        if (this.type == IndexedConcurrentHashSet.class) {
            this.data = new IndexedConcurrentHashSet<>(keyFunction);
            return this;
        }
        if (this.type == IndexedHashSet.class) {
            this.data = new IndexedHashSet<>(keyFunction);
            return this;
        }
        // try resolving custom
        Constructor<? extends AbstractIndexedSet> constructor;
        try {
            constructor = this.type.getConstructor(Function.class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalArgumentException("No keyFunction constructor available in " + this.type);
        }
        this.data = constructor.newInstance(keyFunction);
        return this;
    }

    /**
     * Adds new element to the set if not present already.
     *
     * @param value the elements to be added
     * @return self
     * @throws IllegalArgumentException when element with the same key was already present, or keyFunction was not set
     */
    public IndexedSetBuilder<VT, KT> add(VT value) {
        if (this.data == null) {
            throw new IllegalArgumentException("Specify the keyFunction before adding the data!");
        }
        this.data.add(value);
        return this;
    }

    /**
     * Adds the elements to the set if not present already.
     *
     * @param values the elements to be added
     * @return self
     * @throws IllegalArgumentException when element with the same key was already present, or keyFunction was not set
     */
    public final IndexedSetBuilder<VT, KT> addAll(Collection<? extends VT> values) {
        if (this.data == null) {
            throw new IllegalArgumentException("Specify the keyFunction before adding the data!");
        }
        this.data.addAll(values);
        return this;
    }

    /**
     * Closes the builder flow by returning the set.
     *
     * @return resulting set
     */
    public IndexedSet<VT, KT> build() {
        return this.data;
    }
}
