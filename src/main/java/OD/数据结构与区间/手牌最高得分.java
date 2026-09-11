package OD.数据结构与区间;

/**
 * 记忆化搜索 + 回溯
 *
 * @author faming.yang@hand-china.com 2026-09-11 21:32
 */
public class 手牌最高得分 {

    class Solution {
        // counts[rank]：每个点数当前还剩几张
        // 下标 1..13 分别对应 A(1)、2..9、10、J(11)、Q(12)、K(13)
        private final int[] counts = new int[14];

        // memo：记忆化，key 是各点数剩余数量的状态串，value 是该状态能获得的最大分数
        private final java.util.Map<String, Long> memo = new java.util.HashMap<>();

        long maximumCardScore(String cards) {
            // 重置状态
            java.util.Arrays.fill(counts, 0);
            memo.clear();

            // 1. 统计每种点数出现的次数
            for (int index = 0; index < cards.length(); index++) {
                char character = cards.charAt(index);

                // 把字符映射成点数 1..13
                int rank = character == '0' ? 10
                        : character == 'J' ? 11
                        : character == 'Q' ? 12
                        : character == 'K' ? 13
                        : character >= '1' && character <= '9' ? character - '0'
                        : 0;

                // 只统计合法点数（rank > 0）
                if (rank > 0)
                    counts[rank]++;
            }

            return solve();
        }

        /**
         * 计算某个点数 count 张牌能贡献的分数
         * <p>
         * 规则：
         * 每 4 张算一个「炸弹」，每个炸弹得 rank * 12
         * 剩下的余数张数按不同倍率算：
         * 余 1 张：rank * 1
         * 余 2 张：rank * 4
         * 余 3 张：rank * 6
         * 余 0 张：0
         *
         * @param rank  点数
         * @param count 该点数的张数
         * @return 该点数贡献的分数
         */
        private long groupedScore(int rank, int count) {
            int bombs = count / 4;        // 完整的 4 张组数
            int remainder = count % 4;    // 剩余张数

            // 余数对应的倍率
            int multiplier = remainder == 1 ? 1
                    : remainder == 2 ? 4
                    : remainder == 3 ? 6
                    : 0;

            return (long) rank * (bombs * 12 + multiplier);
        }

        /**
         * 记忆化搜索：在当前剩余牌的基础上，求能获得的最大分数
         * <p>
         * 策略：
         * 1. 先算「不组成顺子」时，各点数分组得分之和
         * 2. 再尝试用任意一个 5 张连续顺子（1-5, 2-6, ..., 9-13），
         * 取走后递归求剩余牌的最大分数，取所有方案的最大值
         */
        private long solve() {
            // 1. 构造当前状态 key，并计算分组得分
            StringBuilder keyBuilder = new StringBuilder();
            long result = 0L;

            for (int rank = 1; rank <= 13; rank++) {
                keyBuilder.append(counts[rank]).append(',');
                result += groupedScore(rank, counts[rank]);  // 不组顺子的基础分
            }

            String key = keyBuilder.toString();

            // 2. 记忆化：算过直接返回
            Long cached = memo.get(key);
            if (cached != null)
                return cached;

            // 3. 尝试所有可能的 5 张顺子（起点 1..9）
            for (int start = 1; start <= 9; start++) {

                // 检查 start..start+4 这 5 个点数是否都还有牌
                boolean available = true;
                for (int rank = start; rank < start + 5; rank++)
                    if (counts[rank] == 0)
                        available = false;
                if (!available)
                    continue;

                // 取走顺子，累加顺子得分
                long straight = 0;
                for (int rank = start; rank < start + 5; rank++) {
                    counts[rank]--;
                    straight += rank * 2L;   // 顺子里每张牌得 rank * 2
                }

                // 递归求剩余牌的最大分数
                result = Math.max(result, straight + solve());

                // 回溯：把顺子放回去
                for (int rank = start; rank < start + 5; rank++)
                    counts[rank]++;
            }

            // 4. 记忆化并返回
            memo.put(key, result);
            return result;
        }
    }


    class Sol {
        long maximumCardScore(String cards) {

            int[] count = new int[14];
            for (int i = 0; i < cards.length(); i++) {
                char charAt = cards.charAt(i);
                if ('0' == charAt) {
                    count[10]++;
                }
                if ('J' == charAt) {
                    count[11]++;
                }
                if ('Q' == charAt) {
                    count[12]++;
                }
                if ('K' == charAt) {
                    count[13]++;
                }
                count[charAt - '0']++;
            }

            solve2(count);

            return 0L;
        }

        long solve2(int[] count) {
            long res = 0;
            int sum = 0;
            for (int i = 1; i < count.length; i++) {
                int boom = count[i] / 4;
                int div = count[i] % 4;
                int sum1;
                if (div == 1) {
                    sum1 = i;
                } else if (div == 2) {
                    sum1 = 4 * i;
                } else {
                    sum1 = 6 * i;
                }
                sum = i * 12 * boom + sum1;
            }

            for (int i = 1; i <= 9; i++) {

                boolean flag = false;
                int sum1 = 0;
                for (int j = i; j < i + 5; j++) {
                    if (count[i] == 0) {
                        flag = true;
                    }
                    sum1 += 2 * i;
                }

                if (flag) {
                    continue;
                }
                for (int j = i; j < i + 5; j++) {
                    count[i]--;
                }

                res = Math.max(sum, sum1 + solve2(count));

                for (int j = i; j < i + 5; j++) {
                    count[i]++;
                }

            }
            return res;
        }

    }


}
