package hashmap;

import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Ge Shuai
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    static final int initialCapacity = 16;
    int capacity;
    static final double initLoadFactor = 0.75;
    double loadFactor;

    int loading;
    // You should probably define some more!

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = new Collection[initialCapacity];
        capacity = initialCapacity;
        loadFactor = initLoadFactor;
        for (int i = 0; i < initialCapacity; i += 1) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialCapacity) {
        capacity = initialCapacity;
        loadFactor = initLoadFactor;
        buckets = new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i += 1) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = initialCapacity;
        buckets = new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i += 1) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * Note that that this is referring to the hash table bucket itself,
     * not the hash map itself.
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }
    // Your code won't compile until you do so!

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     */
    @Override
    public void put(K key, V value) {
        int hashValue = reduce(key.hashCode());
        Collection<Node> curBucket = buckets[hashValue];
        for (Node n : curBucket) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        curBucket.add(new Node(key, value));
        loading += 1;
        if ((double) loading / capacity > loadFactor) {
            resize(2 * capacity);
        }
    }

    private void resize(int volumn) {
        capacity = volumn;
        loading = 0;
        Collection<Node>[] oldBuckets = buckets;
        buckets = new Collection[capacity];
        for (int i = 0; i < capacity; i += 1) {
            buckets[i] = createBucket();
        }
        for (Collection<Node> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node n : bucket) {
                    put(n.key, n.value);
                }
            }
        }
    }

    @Override
    /** Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. */
    public V get(K key) {
        int hashValue = reduce(key.hashCode());
        Collection<Node> curBucket = buckets[hashValue];
        for (Node n : curBucket) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }
    @Override
    /** Returns whether this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int hashValue = reduce(key.hashCode());
        Collection<Node> curBucket = buckets[hashValue];
        for (Node n : curBucket) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;

    }
    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return loading;
    }
    @Override
    /** Removes every mapping from this map. */
    public void clear() {
        loading = 0;
        capacity = initialCapacity;
        buckets = new Collection[capacity];
        for (int i = 0; i < capacity; i += 1) {
            buckets[i] = createBucket();
        }
    }

    @Override
    /** Returns a Set view of the keys contained in this map. Not required for this lab.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<>();
        for (K key : this) {
            keys.add(key);
        }
        return keys;
    }
    @Override
    /** Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        int hashValue = reduce(key.hashCode());
        Collection<Node> bucket  = buckets[hashValue];
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                V value = n.value;
                bucket.remove(n);
                loading -= 1;
                return value;
            }
        }
        return null;
    }

    private int reduce(int hc) {
        return Math.floorMod(hc, capacity);
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {

        int bucketIndex;
        Iterator<Node> bucketIterator;

        public HashMapIterator() {
            bucketIndex = 0;
            bucketIterator = buckets[bucketIndex].iterator();
            autoSwitch();
        }


        private void autoSwitch() {
            while (bucketIndex < capacity - 1 && !bucketIterator.hasNext()) {
                bucketIndex += 1;
                bucketIterator = buckets[bucketIndex].iterator();
            }
        }


        @Override
        public boolean hasNext() {
            return bucketIndex < capacity && bucketIterator.hasNext();
        }

        @Override
        public K next() {
            if (hasNext()) {
                K key = bucketIterator.next().key;
                autoSwitch();
                return key;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
