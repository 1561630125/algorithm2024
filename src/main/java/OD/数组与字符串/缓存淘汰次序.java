package OD.数组与字符串;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 22:30
 */
public class 缓存淘汰次序 {

    class Solution {

        /**
         * 缓存项：保存每个文件在缓存中的元信息
         */
        class Item {
            long size;       // 文件大小（占用的缓存容量）
            long frequency;  // 访问频率（被 get 的次数）
            long recent;     // 最近访问时间戳（越大表示越新）

            Item(long s, long f, long r) {
                size = s;
                frequency = f;
                recent = r;
            }
        }

        /**
         * 堆节点：用于优先队列中进行淘汰排序的快照
         * 之所以用单独的 Node 而不是直接放 Item，是因为堆里可能残留旧版本的快照
         */
        class Node {
            String key;        // 对应的文件名
            long frequency;    // 快照时刻的访问频率
            long recent;       // 快照时刻的最近访问时间戳

            Node(String k, long f, long r) {
                key = k;
                frequency = f;
                recent = r;
            }
        }

        /**
         * 模拟一个带容量限制的文件缓存系统，最终返回缓存中所有文件名（按字典序、逗号分隔）。
         *
         * 淘汰策略（类似 LFU + LRU 组合）：
         *   优先淘汰 frequency 最小的；
         *   若 frequency 相同，则淘汰 recent 最小的（即最久未使用的）。
         *
         * @param capacity   缓存总容量
         * @param operations 操作序列，每个字符串形如：
         *                     "get <key>"          —— 读取文件（命中则增加频率并刷新时间）
         *                     "put <key> <size>"   —— 写入文件（不存在时才插入）
         * @return 缓存中文件名按字典序排序后用 "," 拼接；
         *         若缓存为空，则返回 "NONE"
         */
        String cachedFileNames(long capacity, String[] operations) {

            // 缓存主体：文件名 -> 缓存项
            java.util.Map<String, Item> cache = new java.util.HashMap<>();

            // 小顶堆：按 (frequency, recent) 排序，堆顶即"最该被淘汰"的候选
            // 注意：由于 Item 会被原地更新，堆中可能存在过期的 Node 快照，
            //       因此后续取出时需要校验其是否与当前 Item 一致
            java.util.PriorityQueue<Node> heap = new java.util.PriorityQueue<>(
                    (a, b) -> a.frequency != b.frequency
                            ? Long.compare(a.frequency, b.frequency)   // 频率小的优先
                            : Long.compare(a.recent, b.recent));       // 频率相同则时间早的优先

            long used = 0;   // 当前已使用的缓存容量
            long clock = 0;  // 逻辑时钟，每访问/写入一次自增，用于记录 recent

            // 逐个处理操作
            for (String operation : operations) {

                // 按空白字符拆分操作串
                String[] parts = operation.trim().split("\\s+");

                // 空操作跳过
                if (parts.length == 0)
                    continue;

                // ---------- get 操作 ----------
                if (parts[0].equals("get") && parts.length > 1 && cache.containsKey(parts[1])) {
                    Item item = cache.get(parts[1]);
                    item.frequency++;          // 命中一次，频率 +1
                    item.recent = ++clock;     // 刷新最近访问时间
                    // 向堆中推入新快照（旧的快照会成为"过期节点"，稍后会被跳过）
                    heap.add(new Node(parts[1], item.frequency, item.recent));
                }

                // ---------- put 操作 ----------
                // 仅当该 key 尚未在缓存中时才写入（已存在则忽略）
                else if (parts[0].equals("put") && parts.length > 2 && !cache.containsKey(parts[1])) {
                    String key = parts[1];
                    long size = Long.parseLong(parts[2]);

                    // 若容量不足，持续淘汰堆顶元素，直到能放下或缓存清空
                    while (used + size > capacity && used != 0 && !cache.isEmpty()) {
                        Node node;
                        Item item;

                        // 弹出堆顶，并跳过所有与当前 Item 状态不一致的"过期快照"
                        do {
                            node = heap.remove();
                            item = cache.get(node.key);
                        } while (item == null
                                || item.frequency != node.frequency
                                || item.recent != node.recent);

                        // 真正淘汰该文件，释放容量
                        cache.remove(node.key);
                        used -= item.size;
                    }

                    // 若腾出空间后能放下，则插入新文件
                    if (used + size <= capacity) {
                        Item item = new Item(size, 1, ++clock); // 新文件初始频率为 1
                        cache.put(key, item);
                        heap.add(new Node(key, 1, clock));
                        used += size;
                    }
                }
            }

            // ---------- 汇总结果 ----------
            java.util.List<String> names = new java.util.ArrayList<>(cache.keySet());
            java.util.Collections.sort(names);   // 按字典序排序
            return names.isEmpty() ? "NONE" : String.join(",", names);
        }
    }



    static class Solution2 {

        /** 缓存项：大小、访问频率、最近访问时间 */
        static class Item {
            long size, frequency, recent;
            Item(long size, long frequency, long recent) {
                this.size = size;
                this.frequency = frequency;
                this.recent = recent;
            }
        }

        String cachedFileNames(long capacity, String[] operations) {

            Map<String, Item> cache = new HashMap<>();
            long used = 0;   // 已用容量
            long clock = 0;  // 逻辑时钟

            for (String operation : operations) {
                String[] parts = operation.trim().split("\\s+");
                if (parts.length == 0) continue;

                String cmd = parts[0];

                // ---------- get：命中则频率 +1、刷新时间 ----------
                if (cmd.equals("get") && parts.length > 1) {
                    Item item = cache.get(parts[1]);
                    if (item != null) {
                        item.frequency++;
                        item.recent = ++clock;
                    }
                }

                // ---------- put：不存在时才写入，容量不足则先淘汰 ----------
                else if (cmd.equals("put") && parts.length > 2 && !cache.containsKey(parts[1])) {
                    String key = parts[1];
                    long size = Long.parseLong(parts[2]);

                    // 腾空间：反复淘汰，直到放得下
                    while (used + size > capacity && !cache.isEmpty()) {
                        String victim = findVictim(cache);
                        used -= cache.get(victim).size;
                        cache.remove(victim);
                    }

                    // 放得下才写入
                    if (used + size <= capacity) {
                        cache.put(key, new Item(size, 1, ++clock));
                        used += size;
                    }
                }
            }

            // ---------- 返回结果 ----------
            if (cache.isEmpty()) return "NONE";
            List<String> names = new ArrayList<>(cache.keySet());
            Collections.sort(names);
            return String.join(",", names);
        }

        /** 找出最该淘汰的 key：频率最小；频率相同则最久未使用 */
        private String findVictim(Map<String, Item> cache) {
            String victim = null;
            for (Map.Entry<String, Item> e : cache.entrySet()) {
                Item cur = e.getValue();
                if (victim == null) {
                    victim = e.getKey();
                    continue;
                }
                Item best = cache.get(victim);
                if (cur.frequency < best.frequency
                        || (cur.frequency == best.frequency && cur.recent < best.recent)) {
                    victim = e.getKey();
                }
            }
            return victim;
        }
    }

}
