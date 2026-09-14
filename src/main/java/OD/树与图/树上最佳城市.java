package OD.树与图;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 14:26
 */
public class 树上最佳城市 {


    static class Solution {
        /**
         * 并查集查找（带路径压缩，含「路径减半」优化）。
         * 找到 value 所在集合的代表元（根），同时压缩路径。
         */
        int batch66Find(int value, int[] parent) {
            // 只要当前节点不是自己的父节点，就继续往上找
            while (parent[value] != value) {
                // 路径减半：把当前节点直接连到「祖父」节点，降低树高
                parent[value] = parent[parent[value]];
                value = parent[value];
            }
            return value;
        }

        /**
         * 对每个城市尝试「移除」，使剩下的图分裂成若干连通块；
         * 目标：让「最大连通块的大小」尽可能小。
         * 返回所有能达到最小最大连通块大小的「被移除城市编号」，升序。
         *
         * 思路：暴力枚举每个要移除的城市 removed，用并查集求剩余图的连通块，
         *       记录最大连通块大小 maximum，取全局最小；记录所有达到最小的 removed。
         */
        long[] minimumPolymerizationCities(int cityCount, long[][] roads) {
            // result：所有能达到最小值的 removed 城市编号
            java.util.List<Long> result = new java.util.ArrayList<>();

            // minimum：全局最小的「最大连通块大小」，初始为无穷大
            int minimum = Integer.MAX_VALUE;

            // 1. 枚举每一个可能被移除的城市（编号 1 ~ cityCount）
            for (int removed = 1; removed <= cityCount; removed++) {

                // 2. 为本次枚举初始化并查集：每个城市初始自成一个集合
                int[] parent = new int[cityCount + 1];
                for (int city = 0; city <= cityCount; city++)
                    parent[city] = city;

                // 3. 遍历所有道路，把两端城市合并（跳过被移除的城市和非法数据）
                for (long[] road : roads) {
                    if (road.length < 2)          // 非法道路数据，跳过
                        continue;

                    long leftValue  = road[0];
                    long rightValue = road[1];

                    // 跳过与被移除城市相关的边，以及越界 / 负数 / 非法城市编号
                    if (leftValue == removed || rightValue == removed
                            || leftValue < 0 || rightValue < 0
                            || leftValue > cityCount || rightValue > cityCount)
                        continue;

                    // 找到两端城市所在集合的根
                    int left  = batch66Find((int) leftValue, parent);
                    int right = batch66Find((int) rightValue, parent);

                    // 按「较小根作为新根」合并两个集合（保证合并方向一致，仅用于简化）
                    int root = Math.min(left, right);
                    parent[left]  = root;
                    parent[right] = root;
                }

                // 4. 统计去掉 removed 后，各连通块的大小，求最大值
                int[] counts = new int[cityCount + 1];   // counts[root] = 该连通块节点数
                int maximum = 0;                         // 当前最大连通块大小
                for (int city = 1; city <= cityCount; city++) {
                    if (city == removed)                 // 跳过被移除的城市
                        continue;
                    int root = batch66Find(city, parent);
                    // 累加该连通块节点数，并更新最大值
                    maximum = Math.max(maximum, ++counts[root]);
                }

                // 5. 更新全局最优解
                if (maximum < minimum) {
                    // 发现更小的「最大连通块」→ 重置结果集
                    minimum = maximum;
                    result.clear();
                    result.add((long) removed);
                } else if (maximum == minimum) {
                    // 与当前最优持平 → 追加该城市
                    result.add((long) removed);
                }
            }

            // 6. 把 List<Long> 转成 long[] 返回
            long[] output = new long[result.size()];
            for (int i = 0; i < output.length; i++)
                output[i] = result.get(i);
            return output;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int cityCount = 5;
        long[][] roads = new long[][]{{1,2},{2,3},{3,4}};

        System.out.println(Arrays.toString(solution.minimumPolymerizationCities(cityCount, roads)));
    }

}
