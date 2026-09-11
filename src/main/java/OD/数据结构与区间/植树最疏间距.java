package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 23:14
 */
public class 植树最疏间距 {

    int bestPlantingDistance(int[] positions, int trees) {
        return 0;
    }


    void dfs(int trees,int[] positions) {

    }


    class Solution {

        /**
         * 在 positions 里选 trees 个位置种树，
         * 使任意两棵树之间的最小距离尽可能大
         *
         * @param positions 可种树的位置
         * @param trees     需要种几棵树
         * @return 最大的「最小间距」
         */
        int bestPlantingDistance(int[] positions, int trees) {

            if (positions.length == 0) return 0;

            // 1. 复制并排序，方便贪心放置
            int[] ordered = java.util.Arrays.copyOf(positions, positions.length);
            java.util.Arrays.sort(ordered);

            // 2. 二分答案：最小间距的范围是 [1, 最远两点距离]
            int left = 1;
            int right = ordered[ordered.length - 1] - ordered[0];

            while (left < right) {
                // 向上取整的中点，避免死循环
                int middle = (left + right + 1) / 2;

                // 贪心检查：间距至少为 middle 时，最多能种几棵
                int planted = 1;             // 第一棵种在 ordered[0]
                int last = ordered[0];       // 上一棵种的位置

                for (int index = 1; index < ordered.length; index++) {
                    // 当前位置和上一棵的距离 >= middle，就可以种
                    if (ordered[index] - last >= middle) {
                        planted++;
                        last = ordered[index];
                    }
                }

                // 能种够 trees 棵，说明 middle 可行，尝试更大的间距
                if (planted >= trees) left = middle;
                else right = middle - 1;  // 种不够，间距要缩小
            }

            return left;
        }
    }



    class Solution2 {

        private int[] ordered;   // 排序后的位置
        private int gap;         // 当前二分的间距
        private int[][] memo;    // memo[index][lastIndex]：从 index 开始、上一棵种在 lastIndex 时，最多能种几棵

        int bestPlantingDistance(int[] positions, int trees) {
            if (positions.length == 0) return 0;

            ordered = java.util.Arrays.copyOf(positions, positions.length);
            java.util.Arrays.sort(ordered);

            int left = 1;
            int right = ordered[ordered.length - 1] - ordered[0];

            while (left < right) {
                int middle = (left + right + 1) / 2;

                // 每次二分重新建 memo
                gap = middle;
                int n = ordered.length;
                memo = new int[n][n];
                for (int[] row : memo)
                    java.util.Arrays.fill(row, -1);

                // 第一棵种在 ordered[0]，从 index=1 开始递归
                int planted = 1 + maxPlant(1, 0);

                if (planted >= trees) left = middle;
                else right = middle - 1;
            }

            return left;
        }

        /**
         * 从第 index 个位置开始，上一棵种在 lastIndex，
         * 间距至少为 gap 时，最多还能种几棵
         *
         * @param index     当前考虑的位置下标
         * @param lastIndex 上一棵种的位置下标
         * @return 最多还能种的棵数
         */
        private int maxPlant(int index, int lastIndex) {
            if (index == ordered.length) return 0;

            if (memo[index][lastIndex] != -1)
                return memo[index][lastIndex];

            // 不种当前位置
            int skip = maxPlant(index + 1, lastIndex);

            // 种当前位置（距离够的话）
            int plant = 0;
            if (ordered[index] - ordered[lastIndex] >= gap) {
                plant = 1 + maxPlant(index + 1, index);
            }

            return memo[index][lastIndex] = Math.max(skip, plant);
        }
    }
}
