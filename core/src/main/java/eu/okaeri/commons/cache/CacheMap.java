package eu.okaeri.commons.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;


public class CacheMap<K, V> implements Map<K, V> {

    private final Map<K, V> data;

    public CacheMap() {
        this.data = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("CloneableClassWithoutClone")
    public CacheMap(int maxSize) {
        this.data = Collections.synchronizedMap(new LinkedHashMap<K, V>((maxSize * 10) / 7, 0.7f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return this.size() > maxSize;
            }
        });
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
    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.data.get(key);
    }

    @Override
    public V put(K key, V value) {
        return this.data.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.data.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.data.putAll(map);
    }

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.data.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.data.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.data.entrySet();
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return this.data.compute(key, remappingFunction);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return this.data.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return this.data.computeIfPresent(key, remappingFunction);
    }
}
