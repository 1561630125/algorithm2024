package OD.贪心和动态规划;

/**
 * 数位DP
 *
 * @author faming.yang@hand-china.com 2026-09-04 10:50
 */
public class 避开101的数 {
    class Solution {

        // 统计区间 [left, right] 中，二进制表示不含 "101" 的数字个数
        long countWithout101(long left, long right) {
            // 前缀和思想：validUpTo(right) - validUpTo(left - 1)
            return validUpTo(right) - validUpTo(left - 1);
        }

        // 统计 [0, limit] 中，二进制表示不含 "101" 的数字个数
        private long validUpTo(long limit) {
            if (limit < 0)
                return 0; // 处理 left = 0 时 left - 1 = -1 的情况

            // 把 limit 转成二进制字符串，方便逐位枚举
            String bits = Long.toBinaryString(limit);

            // memo[position][previousTwo][tight]
            // position: 当前处理到二进制的第几位
            // previousTwo: 最近两位的状态（用 0~3 表示）
            // tight: 当前是否受到 limit 上界的限制（1 表示受限，0 表示不受限）
            long[][][] memo = new long[bits.length()][4][2];

            // 初始化 memo 为 -1，表示尚未计算
            for (long[][] layer : memo)
                for (long[] row : layer)
                    java.util.Arrays.fill(row, -1);

            // 从第 0 位开始搜索
            return search(bits, 0, 0, 1, memo);
        }

        /**
         * 数位 DP 搜索
         *
         * @param bits        limit 的二进制字符串
         * @param position    当前处理到第几位
         * @param previousTwo 最近两位的状态，用 0~3 表示：
         *                    二进制位含义：最低位是上一位，次低位是上上一位
         *                    例如：previousTwo = 0b10 表示最近两位是 "10"
         * @param tight       是否贴着 limit 的上界
         *                    1 表示当前位最多只能取 bits[position]
         *                    0 表示当前位可以取 0 或 1
         * @param memo        记忆化数组
         * @return 从当前位置往后，能构造出的合法数字个数
         */
        private long search(
                String bits,
                int position,
                int previousTwo,
                int tight,
                long[][][] memo
        ) {
            // 已经处理完所有二进制位，说明构造出了一个合法数字
            if (position == bits.length())
                return 1;

            // 记忆化：如果已经算过，直接返回
            if (memo[position][previousTwo][tight] >= 0)
                return memo[position][previousTwo][tight];

            // 当前位能取的最大值
            // 如果 tight == 1，最多只能取 limit 当前位的值
            // 否则可以取 1
            int maximum = tight == 1 ? bits.charAt(position) - '0' : 1;

            long result = 0;

            // 枚举当前位可以放的 bit（0 或 1，但受 maximum 限制）
            for (int bit = 0; bit <= maximum; bit++) {

                // 判断是否形成了 "101"
                // previousTwo == 2 表示最近两位是二进制 "10"
                // 此时如果当前 bit == 1，就会拼成 "101"，不合法，跳过
                if (previousTwo == 2 && bit == 1)
                    continue;

                // 更新最近两位状态：
                // previousTwo 左移一位，再或上当前 bit，然后 & 3 只保留低两位
                // 例如：previousTwo = 0b10，bit = 0
                //      (0b10 << 1) | 0 = 0b100，& 3 = 0b00
                //      表示最近两位变成了 "00"
                int nextPreviousTwo = ((previousTwo << 1) | bit) & 3;

                // 更新 tight：
                // 如果当前 tight == 1 且 bit == maximum，说明下一位仍然受上界限制
                // 否则下一位就不受限制了
                int nextTight = (tight == 1 && bit == maximum) ? 1 : 0;

                // 递归处理下一位，并累加结果
                result += search(
                        bits,
                        position + 1,
                        nextPreviousTwo,
                        nextTight,
                        memo
                );
            }

            // 记忆化并返回
            return memo[position][previousTwo][tight] = result;
        }
    }

}
