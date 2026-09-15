package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:51
 */
public class 事件类别排名 {

    public class Solution {

        /**
         * 统计数组中每个数字的出现次数，按规则排序后返回数字序列。
         *
         * 排序规则（优先级从高到低）：
         *   1. 出现次数多的排前面（降序）
         *   2. 次数相同时，首次出现位置靠前的排前面（升序）
         *
         * @param nums 输入数组
         * @return     按规则排序后的"不同数字"序列
         */
        public int[] statisticsProduct(int[] nums) {

            // counts: 数字 -> 出现次数
            java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();

            // first:  数字 -> 首次出现的下标
            java.util.Map<Integer, Integer> first = new java.util.HashMap<>();

            // ---- 1. 一遍扫描，同时统计次数和首次位置 ----
            for (int index = 0; index < nums.length; index++) {
                // merge：key 不存在则放 1；存在则用 Integer::sum 累加
                counts.merge(nums[index], 1, Integer::sum);

                // putIfAbsent：只在 key 不存在时放入 index，保证记录的是"首次"
                first.putIfAbsent(nums[index], index);
            }

            // ---- 2. 取出所有不同数字，准备排序 ----
            java.util.List<Integer> values = new java.util.ArrayList<>(counts.keySet());

            // ---- 3. 按"次数降序 → 首次位置升序"排序 ----
            values.sort((left, right) -> {
                // 规则1：次数降序（次数多的在前）
                int byCount = Integer.compare(
                        counts.get(right),   // 注意参数顺序：right 在前 → 降序
                        counts.get(left)
                );
                // 规则2：次数相同时，按首次出现位置升序（位置小的在前）
                return byCount != 0
                        ? byCount
                        : Integer.compare(first.get(left), first.get(right));
            });

            // ---- 4. List<Integer> 转 int[] ----
            return values.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    public class Solution2 {
        public int[] statisticsProduct(int[] nums) {

            // statistics: 数字 -> [出现次数, 首次出现下标]
            java.util.Map<Integer, int[]> statistics = new java.util.HashMap<>();

            for (int index = 0; index < nums.length; index++) {
                int[] value = statistics.get(nums[index]);
                if (value == null) {
                    // 首次出现：初始化 [次数=1, 首次下标=index]
                    statistics.put(nums[index], new int[] {1, index});
                } else {
                    // 已存在：只累加次数，首次下标不变
                    value[0]++;
                }
            }

            // 取出所有不同数字
            java.util.List<Integer> values = new java.util.ArrayList<>(statistics.keySet());

            // 按"次数降序 → 首次位置升序"排序
            values.sort((left, right) -> {
                int[] x = statistics.get(left);
                int[] y = statistics.get(right);

                // 规则1：次数降序
                int byCount = Integer.compare(y[0], x[0]);
                if (byCount != 0)
                    return byCount;

                // 规则2：首次位置升序
                return Integer.compare(x[1], y[1]);
            });

            // List<Integer> → int[]
            int[] result = new int[values.size()];
            for (int i = 0; i < values.size(); i++)
                result[i] = values.get(i);
            return result;
        }
    }

}
