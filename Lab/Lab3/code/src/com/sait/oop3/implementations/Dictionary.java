package com.sait.oop3.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.sait.oop3.exceptions.DuplicateKeyException;
import com.sait.oop3.utilities.DictionaryADT;

/**
 * ArrayList-backed Dictionary implementation with parallel key/value lists.
 *
 * <p><b>Invariant:</b> {@code keys.size() == values.size()} and for any index i,
 * {@code keys.get(i)} is paired with {@code values.get(i)}.</p>
 *
 * <p>All operations use linear search on {@code keys} to locate the index.</p>
 *
 * @param <K> key type
 * @param <V> value type
 */
public class Dictionary<K, V> implements DictionaryADT<K, V> {

    /**
     * Default initial capacity for the underlying lists.
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * Keys and values are stored in parallel lists (same indices pair together).
     */
    private final ArrayList<K> keys;
    private final ArrayList<V> values;

    /**
     * Constructs an empty dictionary with default capacity.
     */
    public Dictionary() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructs an empty dictionary with a caller-specified initial capacity.
     *
     * @param initialCapacity initial capacity for internal lists (must be &gt;= 0)
     * @throws IllegalArgumentException if {@code initialCapacity < 0}
     */
    public Dictionary(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0");
        }
        this.keys = new ArrayList<>(initialCapacity);
        this.values = new ArrayList<>(initialCapacity);
    }

    @Override
    public void insert(K key, V value) throws DuplicateKeyException {
        requireNonNullKey(key);
        int idx = indexOfKey(key);
        if (idx >= 0) {
            throw new DuplicateKeyException("Key already exists: " + key);
        }
        keys.add(key);
        values.add(value);
    }

    @Override
    public V remove(K key) {
        requireNonNullKey(key);
        int idx = indexOfKeyOrThrow(key);
        keys.remove(idx);      // maintain pairing: remove at the same index
        return values.remove(idx);
    }

    @Override
    public V update(K key, V newValue) {
        requireNonNullKey(key);
        int idx = indexOfKeyOrThrow(key);
        return values.set(idx, newValue);
    }

    @Override
    public V lookup(K key) {
        requireNonNullKey(key);
        int idx = indexOfKeyOrThrow(key);
        return values.get(idx);
    }

    @Override
    public boolean containsKey(K key) {
        requireNonNullKey(key);
        return indexOfKey(key) >= 0;
    }

    @Override
    public int size() {
        return keys.size(); // invariant guarantees equality with values.size()
    }

    @Override
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    @Override
    public void clear() {
        keys.clear();
        values.clear();
    }

    @Override
    public List<K> keys() {
        // defensive copy: external mutation must not affect internal state
        return new ArrayList<>(keys);
    }

    @Override
    public List<V> values() {
        // defensive copy parallel to keys() order
        return new ArrayList<>(values);
    }

    // ------------------- helpers -------------------

    private void requireNonNullKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    /**
     * Linear scan using {@code equals}; returns index or -1 if not found.
     */
    private int indexOfKey(K key) {
        for (int i = 0, n = keys.size(); i < n; i++) {
            if (key.equals(keys.get(i))) { // key is non-null by contract
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of {@code key} or throws {@link NoSuchElementException} if absent.
     */
    private int indexOfKeyOrThrow(K key) {
        int idx = indexOfKey(key);
        if (idx < 0) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        return idx;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0, n = keys.size(); i < n; i++) {
            if (i > 0) sb.append(", ");
            sb.append(keys.get(i)).append("=").append(values.get(i));
        }
        return sb.append("}").toString();
    }
}
