package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:35
 */
public class 粒度分配 {

    class Solution {

        /**
         * 内存分配模拟。
         *
         * pool:      每行是 [内存块大小, 该大小的数量]，表示"有多少个指定大小的内存块"
         * requests:  每次请求的内存大小
         *
         * 分配规则（从代码行为推断）：
         *   对每个请求大小 r，在 available 中找"键 >= r"的最小键 k
         *   （因为 TreeMap 的 tailMap(r, true) 是从 r 开始升序）
         *   如果 k 对应的块数 > 0，就分配一个，结果记为 true
         *   否则该请求失败，记为 false
         *
         * @param pool     内存池：[大小, 数量] 列表
         * @param requests 请求序列
         * @return         每个请求是否成功
         */
        boolean[] allocateMemory(long[][] pool, long[] requests) {

            // ---- 1. 用 TreeMap 汇总内存池：key=块大小，value=该大小的数量 ----
            // TreeMap 保证 key 有序，便于做"找 >= r 的最小 key"这种查询
            java.util.TreeMap<Long, Long> available = new java.util.TreeMap<>();

            for (long[] item : pool)
                if (item.length >= 2)
                    // merge：key=item[0]，把 item[1] 累加到已有值上
                    // 相同大小的块，数量累加
                    available.merge(item[0], item[1], Long::sum);

            // ---- 2. 逐个处理请求 ----
            boolean[] result = new boolean[requests.length];

            for (int i = 0; i < requests.length; i++)

                // tailMap(requests[i], true)：返回所有 key >= requests[i] 的子 Map
                // 因为是 TreeMap，遍历顺序是 key 升序
                // 所以第一个 value > 0 的 entry，就是"能满足请求的最小块"
                for (java.util.Map.Entry<Long, Long> entry : available.tailMap(requests[i], true).entrySet())
                    if (entry.getValue() > 0) {
                        // 分配一个：数量 -1
                        entry.setValue(entry.getValue() - 1);
                        result[i] = true;
                        break;   // 找到就停止，处理下一个请求
                    }

            return result;
        }
    }

}
