package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 15:18
 */
public class 山谷蓄水 {


    String maximumReservoir(long[] heights) {
        int[] leftStack = new int[heights.length];
        int[] rightStack = new int[heights.length];

        for(int i = 0; i < heights.length; i++) {
            int top = 0;
            if (heights[i] < leftStack[top--]) {

            }
        }

        return "0";
    }


    /**
     * 寻找最大蓄水量的区间
     *
     * 题目背景：
     * 给定一个地形数组 h，每个位置的高度为 h[i]。
     * 蓄水规则：选择一段区间，在区间两端筑坝，计算区间内的蓄水量。
     *
     * 需要找到：
     * 1. 蓄水量最大的区间
     * 2. 如果多个区间蓄水量相同，选择区间跨度最小的（坝间距最小）
     * 3. 如果跨度也相同，选择左端点最靠左的
     *
     * 蓄水量计算 = Σ(水位高度 - h[i])，其中水位高度 = min(左坝高度, 右坝高度)
     *
     * 例如：
     * h = [3, 0, 2, 0, 4]
     * 区间 [1, 3]（索引1到3）：左坝高3，右坝高4，水位=3
     *   蓄水量 = (3-0) + (3-2) + (3-0) = 3+1+3 = 7
     *
     * @param h 地形高度数组
     * @return 格式：起始索引 结束索引:蓄水量，如果没有蓄水返回 "0"
     */
   static String maximumReservoir2(long[] h) {
        int n = h.length;

        // ========== 1. 预处理左右最大高度 ==========
        long[] l = new long[n];  // l[i] = h[0..i-1] 的最大值（左侧最高）
        long[] r = new long[n];  // r[i] = h[i+1..n-1] 的最大值（右侧最高）
        long[] w = new long[n];  // w[i] = 位置i被淹没时需要的蓄水水位

        // 计算左侧最大高度
        for (int i = 1; i < n; i++) {
            l[i] = Math.max(l[i - 1], h[i - 1]);
        }

        // 计算右侧最大高度
        for (int i = n - 2; i >= 0; i--) {
            r[i] = Math.max(r[i + 1], h[i + 1]);
        }

        // ========== 2. 计算每个位置的水位和蓄水高度 ==========
        // levels：存储所有可能的水位值
        java.util.TreeSet<Long> levels = new java.util.TreeSet<>();

        for (int i = 1; i < n - 1; i++) {
            // 当前位置能蓄水的条件：
            // min(左最高, 右最高) > h[i]
            long waterLevel = Math.min(l[i], r[i]);  // 水位（能蓄到的最高高度）
            long x = waterLevel - h[i];  // 蓄水高度
            if (x > 0) {
                w[i] = waterLevel;  // 水位
                levels.add(waterLevel);  // 记录可能的水位
            }
        }

        // ========== 3. 枚举所有可能的水位 ==========
        long[] res = {0, 0, 0};  // {左边界, 右边界, 蓄水量}

        for (long level : levels) {
            // ====== 3.1 找到左右边界 ======
            // 左边界：第一个 w[i] >= level 且 h[i] < level 的位置
            int a = 0, b = n - 1;
            while (w[a] < level || h[a] >= level) {
                a++;
            }

            // 右边界：最后一个 w[i] >= level 且 h[i] < level 的位置
            while (w[b] < level || h[b] >= level) {
                b--;
            }

            // ====== 3.2 计算蓄水量 ======
            long total = 0;
            for (int i = a; i <= b; i++) {
                total += Math.max(0, level - h[i]);
            }

            // ====== 3.3 根据规则选择最优解 ======
            long candidateDistance = b - a + 2;  // 坝间距 = b - a + 2（两端筑坝）
            long currentDistance = res[1] - res[0];

            // 优先规则：
            // 1. 蓄水量更大的优先
            // 2. 蓄水量相同，坝间距更小的优先
            // 3. 蓄水量和坝间距都相同，左端点更靠左的优先
            if (total > res[2]
                    || (total == res[2]
                    && (candidateDistance < currentDistance
                    || (candidateDistance == currentDistance
                    && a - 1 < res[0])))) {
                res = new long[]{a - 1, b + 1, total};
            }
        }

        // ========== 4. 返回结果 ==========
        return res[2] == 0 ? "0" : res[0] + " " + res[1] + ":" + res[2];
    }

    public static void main(String[] args) {
        System.out.println(maximumReservoir2(new long[]{1,0,2,0,3,0,4,4,4,4,0,3,0,2,0,1}));


        System.out.println(maximumReservoir2(new long[]{1,0,0,0,0,1,2,3,4,5,5,5,5,0,5}));
    }

}
