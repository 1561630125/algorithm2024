package OD.数组与字符串;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 22:43
 */
public class LRU {

    public static class LRUCache<K, V> {

        // 双向链表节点
        class Node {
            K key;
            V value;
            Node prev;
            Node next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private final Map<K, Node> cache;
        private final Node head;  // 伪头节点
        private final Node tail;  // 伪尾节点

        public LRUCache(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new Node(null, null);
            this.tail = new Node(null, null);
            head.next = tail;
            tail.prev = head;
        }

        // 添加到头部（最近使用）
        private void addToHead(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }

        // 从链表中移除节点
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        // 移动到头部
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }

        // 移除尾部节点（最久未使用）
        private Node removeTail() {
            Node lastNode = tail.prev;
            removeNode(lastNode);
            return lastNode;
        }

        // 获取
        public V get(K key) {
            Node node = cache.get(key);
            if (node == null) {
                return null;
            }
            // 移动到头部表示最近使用
            moveToHead(node);
            return node.value;
        }

        // 插入/更新
        public void put(K key, V value) {
            Node node = cache.get(key);

            if (node != null) {
                // 更新已存在的节点
                node.value = value;
                moveToHead(node);
            } else {
                // 创建新节点
                Node newNode = new Node(key, value);

                // 检查容量
                if (cache.size() >= capacity) {
                    // 移除最久未使用的节点
                    Node removed = removeTail();
                    cache.remove(removed.key);
                }

                // 添加新节点
                cache.put(key, newNode);
                addToHead(newNode);
            }
        }

        public int size() {
            return cache.size();
        }

        public boolean containsKey(K key) {
            return cache.containsKey(key);
        }

        // 测试
        public static void main(String[] args) {
            LRUCache<Integer, Integer> cache = new LRUCache<>(2);

            cache.put(1, 1);
            cache.put(2, 2);
            System.out.println(cache.get(1));    // 1
            cache.put(3, 3);                     // 淘汰 key=2
            System.out.println(cache.get(2));    // null
            cache.put(4, 4);                     // 淘汰 key=1
            System.out.println(cache.get(1));    // null
            System.out.println(cache.get(3));    // 3
            System.out.println(cache.get(4));    // 4
        }
    }


    public static class LRUCacheLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

        private final int capacity;

        public LRUCacheLinkedHashMap(int capacity) {
            // initialCapacity, loadFactor, accessOrder=true 表示按访问顺序排序
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            // 超过容量时移除最久未使用的
            return size() > capacity;
        }

        public static void main(String[] args) {
            LRUCacheLinkedHashMap<Integer, Integer> cache = new LRUCacheLinkedHashMap<>(2);

            cache.put(1, 1);
            cache.put(2, 2);
            System.out.println(cache.get(1));  // 1
            cache.put(3, 3);                   // 淘汰 key=2
            System.out.println(cache.get(2));  // null
            cache.put(4, 4);                   // 淘汰 key=1
            System.out.println(cache.get(1));  // null
            System.out.println(cache.get(3));  // 3
            System.out.println(cache.get(4));  // 4
        }
    }


    public class ThreadSafeLRUCache<K, V> {

        private class Node {
            K key;
            V value;
            Node prev, next;
            Node(K key, V value) { this.key = key; this.value = value; }
        }

        private final int capacity;
        private final Map<K, Node> cache;
        private final Node head, tail;
        private final ReentrantLock lock = new ReentrantLock();

        public ThreadSafeLRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new Node(null, null);
            this.tail = new Node(null, null);
            head.next = tail;
            tail.prev = head;
        }

        public V get(K key) {
            lock.lock();
            try {
                Node node = cache.get(key);
                if (node == null) return null;
                moveToHead(node);
                return node.value;
            } finally {
                lock.unlock();
            }
        }

        public void put(K key, V value) {
            lock.lock();
            try {
                Node node = cache.get(key);
                if (node != null) {
                    node.value = value;
                    moveToHead(node);
                } else {
                    Node newNode = new Node(key, value);
                    if (cache.size() >= capacity) {
                        Node removed = removeTail();
                        cache.remove(removed.key);
                    }
                    cache.put(key, newNode);
                    addToHead(newNode);
                }
            } finally {
                lock.unlock();
            }
        }

        private void addToHead(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }

        private Node removeTail() {
            Node last = tail.prev;
            removeNode(last);
            return last;
        }
    }


    public static class SegmentedLRUCache<K, V> {

        private final Segment<K, V>[] segments;
        private final int segmentCount;
        private final int segmentMask;

        @SuppressWarnings("unchecked")
        public SegmentedLRUCache(int totalCapacity, int concurrencyLevel) {
            // 找到 >= concurrencyLevel 的 2 的幂
            int s = 1;
            while (s < concurrencyLevel) s <<= 1;
            this.segmentCount = s;
            this.segmentMask = s - 1;
            this.segments = new Segment[s];

            int perSegmentCapacity = Math.max(1, totalCapacity / s);
            for (int i = 0; i < s; i++) {
                segments[i] = new Segment<>(perSegmentCapacity);
            }
        }

        private Segment<K, V> segmentFor(K key) {
            int hash = spread(key.hashCode());
            return segments[hash & segmentMask];
        }

        private static int spread(int h) {
            return h ^ (h >>> 16);
        }

        public V get(K key) {
            return segmentFor(key).get(key);
        }

        public void put(K key, V value) {
            segmentFor(key).put(key, value);
        }

        // 每个分段相当于一个独立的 LRU 缓存
        private static class Segment<K, V> {
            private static class Node<K, V> {
                K key;
                V value;
                Node<K, V> prev, next;
                Node(K key, V value) { this.key = key; this.value = value; }
            }

            private final int capacity;
            private final java.util.Map<K, Node<K, V>> map = new java.util.HashMap<>();
            private final Node<K, V> head = new Node<>(null, null);
            private final Node<K, V> tail = new Node<>(null, null);
            private final ReentrantLock lock = new ReentrantLock();

            Segment(int capacity) {
                this.capacity = capacity;
                head.next = tail;
                tail.prev = head;
            }

            V get(K key) {
                lock.lock();
                try {
                    Node<K, V> node = map.get(key);
                    if (node == null) return null;
                    moveToHead(node);
                    return node.value;
                } finally {
                    lock.unlock();
                }
            }

            void put(K key, V value) {
                lock.lock();
                try {
                    Node<K, V> node = map.get(key);
                    if (node != null) {
                        node.value = value;
                        moveToHead(node);
                    } else {
                        Node<K, V> newNode = new Node<>(key, value);
                        if (map.size() >= capacity) {
                            Node<K, V> removed = tail.prev;
                            removeNode(removed);
                            map.remove(removed.key);
                        }
                        map.put(key, newNode);
                        addToHead(newNode);
                    }
                } finally {
                    lock.unlock();
                }
            }

            private void addToHead(Node<K, V> node) {
                node.prev = head;
                node.next = head.next;
                head.next.prev = node;
                head.next = node;
            }

            private void removeNode(Node<K, V> node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            private void moveToHead(Node<K, V> node) {
                removeNode(node);
                addToHead(node);
            }
        }
    }
}
