package test;

import com.sait.oop3.exceptions.DuplicateKeyException;
import com.sait.oop3.implementations.Dictionary;
import com.sait.oop3.utilities.DictionaryADT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ArrayList-backed Dictionary implementation.
 * <p>
 * Tests all functionality specified in DictionaryADT contract:
 * - insert, remove, update, lookup operations
 * - containsKey, size, isEmpty, clear operations
 * - keys, values defensive copy operations
 * - All exception handling paths
 * - Constructor variants
 * - Edge cases and boundary conditions
 * <p>
 * Follows Test-Driven Development approach as specified in lab requirements.
 */
public class TestDictionary {

    private DictionaryADT<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new Dictionary<>();
    }

    // ========== Constructor Tests ==========

    @Test
    void defaultConstructor_startsEmpty() {
        assertEquals(0, dict.size());
        assertTrue(dict.isEmpty());
    }

    @Test
    void capacityConstructor_allowsZero_andStartsEmpty() {
        DictionaryADT<String, Integer> d0 = new Dictionary<>(0);
        assertEquals(0, d0.size());
        assertTrue(d0.isEmpty());
    }

    @Test
    void capacityConstructor_negative_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new Dictionary<String, Integer>(-1));
    }

    @Test
    void capacityConstructor_validCapacity_startsEmpty() {
        DictionaryADT<String, Integer> dict100 = new Dictionary<>(100);
        assertEquals(0, dict100.size());
        assertTrue(dict100.isEmpty());
    }

    // ========== Core Insert/Lookup/Contains/Size Tests ==========

    @Test
    void insert_singlePair_thenLookup_andContains_andSize() throws Exception {
        dict.insert("A", 1);

        assertEquals(1, dict.size());
        assertFalse(dict.isEmpty());
        assertTrue(dict.containsKey("A"));
        assertEquals(1, dict.lookup("A"));
    }

    @Test
    void insert_multiplePairs_thenLookupAll() throws Exception {
        dict.insert("A", 1);
        dict.insert("B", 2);
        dict.insert("C", null); // null value allowed per ADT contract

        assertEquals(3, dict.size());
        assertFalse(dict.isEmpty());

        assertTrue(dict.containsKey("A"));
        assertEquals(1, dict.lookup("A"));

        assertTrue(dict.containsKey("B"));
        assertEquals(2, dict.lookup("B"));

        assertTrue(dict.containsKey("C"));
        assertNull(dict.lookup("C"));
    }

    @Test
    void insert_nullKey_throwsIAE() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dict.insert(null, 0));
        assertTrue(ex.getMessage().contains("key must not be null"));
    }

    @Test
    void insert_duplicateKey_throwsDuplicateKeyException() throws Exception {
        dict.insert("A", 1);

        DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
                () -> dict.insert("A", 99));
        assertTrue(ex.getMessage().contains("Key already exists"));
    }

    @Test
    void insert_nullValue_isAllowed() throws Exception {
        dict.insert("nullValue", null);

        assertEquals(1, dict.size());
        assertTrue(dict.containsKey("nullValue"));
        assertNull(dict.lookup("nullValue"));
    }

    // ========== Lookup Tests ==========

    @Test
    void lookup_missingKey_throwsNoSuchElementException() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> dict.lookup("MISSING"));
        assertTrue(ex.getMessage().contains("Key not found"));
    }

    @Test
    void lookup_nullKey_throwsIAE() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dict.lookup(null));
        assertTrue(ex.getMessage().contains("key must not be null"));
    }

    @Test
    void lookup_emptyDictionary_throwsNoSuchElementException() {
        assertTrue(dict.isEmpty());
        assertThrows(NoSuchElementException.class, () -> dict.lookup("any"));
    }

    // ========== Update Tests ==========

    @Test
    void update_existingKey_returnsOldValue_setsNewValue_sizeUnchanged() throws Exception {
        dict.insert("K", 10);

        Integer oldValue = dict.update("K", 99);

        assertEquals(10, oldValue);
        assertEquals(99, dict.lookup("K"));
        assertEquals(1, dict.size()); // size unchanged per ADT contract
    }

    @Test
    void update_toNullValue_isAllowed() throws Exception {
        dict.insert("N", 7);

        Integer oldValue = dict.update("N", null);

        assertEquals(7, oldValue);
        assertNull(dict.lookup("N"));
        assertEquals(1, dict.size());
    }

    @Test
    void update_fromNullValue_isAllowed() throws Exception {
        dict.insert("N", null);

        Integer oldValue = dict.update("N", 42);

        assertNull(oldValue);
        assertEquals(42, dict.lookup("N"));
    }

    @Test
    void update_missingKey_throwsNoSuchElementException() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> dict.update("MISSING", 1));
        assertTrue(ex.getMessage().contains("Key not found"));
    }

    @Test
    void update_nullKey_throwsIAE() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dict.update(null, 1));
        assertTrue(ex.getMessage().contains("key must not be null"));
    }

    // ========== Remove Tests ==========

    @Test
    void remove_existingKey_returnsValue_removesEntry_decreasesSize() throws Exception {
        dict.insert("X", 7);
        dict.insert("Y", 8);

        Integer removedValue = dict.remove("X");

        assertEquals(7, removedValue);
        assertEquals(1, dict.size());
        assertFalse(dict.containsKey("X"));
        assertTrue(dict.containsKey("Y"));
    }

    @Test
    void remove_keyWithNullValue_returnsNull() throws Exception {
        dict.insert("nullKey", null);

        Integer removedValue = dict.remove("nullKey");

        assertNull(removedValue);
        assertEquals(0, dict.size());
        assertFalse(dict.containsKey("nullKey"));
    }

    @Test
    void remove_lastKey_makesDictionaryEmpty() throws Exception {
        dict.insert("onlyKey", 42);

        dict.remove("onlyKey");

        assertEquals(0, dict.size());
        assertTrue(dict.isEmpty());
    }

    @Test
    void remove_missingKey_throwsNoSuchElementException() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> dict.remove("MISSING"));
        assertTrue(ex.getMessage().contains("Key not found"));
    }

    @Test
    void remove_nullKey_throwsIAE() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dict.remove(null));
        assertTrue(ex.getMessage().contains("key must not be null"));
    }

    // ========== Clear/IsEmpty Tests ==========

    @Test
    void clear_emptyDictionary_remainsEmpty() {
        assertTrue(dict.isEmpty());
        dict.clear();
        assertTrue(dict.isEmpty());
        assertEquals(0, dict.size());
    }

    @Test
    void clear_nonEmptyDictionary_becomesEmpty_allKeysRemoved() throws Exception {
        dict.insert("A", 1);
        dict.insert("B", 2);
        dict.insert("C", null);

        dict.clear();

        assertEquals(0, dict.size());
        assertTrue(dict.isEmpty());
        assertFalse(dict.containsKey("A"));
        assertFalse(dict.containsKey("B"));
        assertFalse(dict.containsKey("C"));
    }

    // ========== ContainsKey Tests ==========

    @Test
    void containsKey_presentAndAbsent_andNullThrows() throws Exception {
        dict.insert("A", 1);

        assertTrue(dict.containsKey("A"));
        assertFalse(dict.containsKey("B"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dict.containsKey(null));
        assertTrue(ex.getMessage().contains("key must not be null"));
    }

    @Test
    void containsKey_emptyDictionary_returnsFalse() {
        assertFalse(dict.containsKey("any"));
    }

    // ========== Keys/Values Defensive Copy Tests ==========

    @Test
    void keys_values_areDefensiveAndParallel() throws Exception {
        dict.insert("A", 1);
        dict.insert("B", 2);
        dict.insert("C", 3);

        List<String> keys = dict.keys();
        List<Integer> values = dict.values();

        assertEquals(keys.size(), values.size());
        assertEquals(3, keys.size());

        for (int i = 0; i < keys.size(); i++) {
            assertEquals(dict.lookup(keys.get(i)), values.get(i));
        }

        keys.clear();
        values.clear();

        assertEquals(3, dict.size());
        assertEquals(1, dict.lookup("A"));
        assertEquals(2, dict.lookup("B"));
        assertEquals(3, dict.lookup("C"));
    }

    @Test
    void keys_values_emptyDictionary_returnsEmptyLists() {
        List<String> keys = dict.keys();
        List<Integer> values = dict.values();

        assertTrue(keys.isEmpty());
        assertTrue(values.isEmpty());
        assertEquals(0, keys.size());
        assertEquals(0, values.size());
    }

    @Test
    void keys_values_withNullValues_handledCorrectly() throws Exception {
        dict.insert("key1", null);
        dict.insert("key2", 42);

        List<String> keys = dict.keys();
        List<Integer> values = dict.values();

        assertEquals(2, keys.size());
        assertEquals(2, values.size());

        for (int i = 0; i < keys.size(); i++) {
            if (values.get(i) == null) {
                assertEquals("key1", keys.get(i));
            } else {
                assertEquals(42, values.get(i));
                assertEquals("key2", keys.get(i));
            }
        }
    }

    // ========== ToString Tests ==========

    @Test
    void toString_emptyDictionary() {
        String result = dict.toString();
        assertEquals("{}", result);
    }

    @Test
    void toString_singlePair() throws Exception {
        dict.insert("A", 1);
        String result = dict.toString();
        assertEquals("{A=1}", result);
    }

    @Test
    void toString_multiplePairs_containsAllPairs() throws Exception {
        dict.insert("A", 1);
        dict.insert("B", 2);

        String result = dict.toString();

        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("A=1"));
        assertTrue(result.contains("B=2"));
    }

    @Test
    void toString_withNullValue() throws Exception {
        dict.insert("nullKey", null);
        String result = dict.toString();
        assertTrue(result.contains("nullKey=null"));
    }

    // ========== Equals-based Key Matching Tests ==========

    @Test
    void usesEqualsForKeyMatching_notIdentity() throws Exception {
        // 替换 record TestKey 为 JDK 11 可用的普通类
        class TestKey {
            private final String id;

            public TestKey(String id) {
                this.id = id;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof TestKey)) return false;
                TestKey other = (TestKey) o;
                return id.equals(other.id);
            }

            @Override
            public int hashCode() {
                return id.hashCode();
            }

            @Override
            public String toString() {
                return id;
            }
        }

        DictionaryADT<TestKey, String> testDict = new Dictionary<>();
        TestKey key1 = new TestKey("id-1");
        TestKey key2 = new TestKey("id-1");

        testDict.insert(key1, "VALUE");

        assertEquals("VALUE", testDict.lookup(key2));
        assertTrue(testDict.containsKey(key2));
        assertEquals("VALUE", testDict.update(key2, "UPDATED"));
        assertEquals("UPDATED", testDict.remove(key2));
        assertEquals(0, testDict.size());
    }

    // ========== Capacity Growth Tests ==========

    @Test
    void bulkInsert_exceedsInitialCapacity_growsCorrectly() throws Exception {
        DictionaryADT<String, Integer> smallDict = new Dictionary<>(5);

        for (int i = 0; i < 20; i++) {
            smallDict.insert("K" + i, i);
        }

        assertEquals(20, smallDict.size());

        assertEquals(0, smallDict.lookup("K0"));
        assertEquals(10, smallDict.lookup("K10"));
        assertEquals(19, smallDict.lookup("K19"));
    }

    @Test
    void bulkInsert_100Elements_allOperationsWork() throws Exception {
        for (int i = 0; i < 100; i++) {
            dict.insert("K" + i, i);
        }

        assertEquals(100, dict.size());
        assertFalse(dict.isEmpty());

        assertEquals(0, dict.lookup("K0"));
        assertEquals(50, dict.lookup("K50"));
        assertEquals(99, dict.lookup("K99"));

        assertTrue(dict.containsKey("K0"));
        assertTrue(dict.containsKey("K50"));
        assertTrue(dict.containsKey("K99"));
        assertFalse(dict.containsKey("K100"));

        List<String> keys = dict.keys();
        List<Integer> values = dict.values();
        assertEquals(100, keys.size());
        assertEquals(100, values.size());
    }

    // ========== Edge Case Tests ==========

    @Test
    void insertUpdateRemove_sameKey_sequentially() throws Exception {
        dict.insert("lifecycle", 1);
        assertEquals(1, dict.lookup("lifecycle"));

        Integer oldValue = dict.update("lifecycle", 2);
        assertEquals(1, oldValue);
        assertEquals(2, dict.lookup("lifecycle"));

        Integer removedValue = dict.remove("lifecycle");
        assertEquals(2, removedValue);
        assertFalse(dict.containsKey("lifecycle"));
        assertEquals(0, dict.size());
    }

    @Test
    void operations_afterClear_workCorrectly() throws Exception {
        dict.insert("A", 1);
        dict.insert("B", 2);

        dict.clear();
        assertTrue(dict.isEmpty());

        dict.insert("C", 3);
        assertEquals(1, dict.size());
        assertEquals(3, dict.lookup("C"));
    }
}
