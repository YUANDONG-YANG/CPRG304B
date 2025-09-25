package com.sait.oop3.utilities;

import java.util.List;
import java.util.NoSuchElementException;

import com.sait.oop3.exceptions.DuplicateKeyException;

/**
 * DictionaryADT
 *
 * <p>A generic Dictionary Abstract Data Type that stores unique (key, value) pairs.
 * Implementations MUST preserve the contract specified here.</p>
 *
 * <h2>General Rules</h2>
 * <ul>
 *   <li>Keys MUST be unique.</li>
 *   <li>Keys MUST NOT be {@code null}. Values MAY be {@code null}.</li>
 *   <li>All operations are in finite time using in-memory arrays/lists only.</li>
 * </ul>
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface DictionaryADT<K, V> {

    /**
     * Inserts a new (key, value) pair.
     *
     * <p><b>Preconditions:</b> {@code key != null} AND {@code key} not present.</p>
     * <p><b>Postconditions:</b>
     * <ul>
     *   <li>{@code size()} increases by 1</li>
     *   <li>{@code containsKey(key)} is {@code true}</li>
     *   <li>{@code lookup(key)} equals the inserted value (may be {@code null})</li>
     * </ul></p>
     *
     * @param key   non-null unique key
     * @param value value associated with {@code key}; may be {@code null}
     * @throws IllegalArgumentException if {@code key == null}
     * @throws DuplicateKeyException    if {@code key} already exists
     */
    void insert(K key, V value) throws DuplicateKeyException;

    /**
     * Removes the pair associated with {@code key} and returns its value.
     *
     * <p><b>Preconditions:</b> {@code key != null} AND {@code key} exists.</p>
     * <p><b>Postconditions:</b>
     * <ul>
     *   <li>{@code size()} decreases by 1</li>
     *   <li>{@code containsKey(key)} is {@code false}</li>
     * </ul></p>
     *
     * @param key non-null existing key
     * @return the value previously associated with {@code key} (may be {@code null})
     * @throws IllegalArgumentException if {@code key == null}
     * @throws NoSuchElementException   if {@code key} not found
     */
    V remove(K key);

    /**
     * Updates the value for an existing {@code key}, returning the old value.
     *
     * <p><b>Preconditions:</b> {@code key != null} AND {@code key} exists.</p>
     * <p><b>Postconditions:</b>
     * <ul>
     *   <li>{@code lookup(key)} equals {@code newValue}</li>
     *   <li>{@code size()} unchanged</li>
     * </ul></p>
     *
     * @param key      non-null existing key
     * @param newValue new value to associate (may be {@code null})
     * @return the previous value for {@code key} (may be {@code null})
     * @throws IllegalArgumentException if {@code key == null}
     * @throws NoSuchElementException   if {@code key} not found
     */
    V update(K key, V newValue);

    /**
     * Looks up and returns the value associated with {@code key}.
     *
     * <p><b>Preconditions:</b> {@code key != null} AND {@code key} exists.</p>
     * <p><b>Postconditions:</b> no change to the dictionary state.</p>
     *
     * @param key non-null existing key
     * @return the value for {@code key} (may be {@code null})
     * @throws IllegalArgumentException if {@code key == null}
     * @throws NoSuchElementException   if {@code key} not found
     */
    V lookup(K key);

    /**
     * Checks whether {@code key} exists in the dictionary.
     *
     * <p><b>Preconditions:</b> {@code key != null}.</p>
     * <p><b>Postconditions:</b> no change to the dictionary state.</p>
     *
     * @param key non-null key to test
     * @return {@code true} iff {@code key} exists, else {@code false}
     * @throws IllegalArgumentException if {@code key == null}
     */
    boolean containsKey(K key);

    /**
     * Returns the number of stored (key, value) pairs.
     *
     * @return current size
     */
    int size();

    /**
     * Returns whether the dictionary is empty.
     *
     * @return {@code true} iff {@code size() == 0}
     */
    boolean isEmpty();

    /**
     * Removes all entries from the dictionary.
     *
     * <p><b>Postconditions:</b> {@code size() == 0} and {@code isEmpty() == true}.</p>
     */
    void clear();

    /**
     * Returns a <b>snapshot</b> list of all keys in unspecified order.
     * Mutating the returned list MUST NOT affect the internal state.
     *
     * @return a defensive copy of keys
     */
    List<K> keys();

    /**
     * Returns a <b>snapshot</b> list of all values in unspecified order.
     * The order is parallel to {@link #keys()}.
     * Mutating the returned list MUST NOT affect the internal state.
     *
     * @return a defensive copy of values
     */
    List<V> values();
}
