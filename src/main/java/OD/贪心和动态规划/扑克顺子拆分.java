package OD.贪心和动态规划;

/**
 * 考点：排序、贪心
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:20
 */
public class 扑克顺子拆分 {


    class Solution {
        /**
         * 从若干张牌（如 "3"、"10"、"J"、"A"、"2"）中找出所有"顺子"。
         * 顺子定义：连续 5 张或以上、点数递增、不含 2（2 不能参与顺子）。
         * 返回每个顺子用空格拼接成的字符串数组。
         */
        String[] findStraights(String[] cards) {
            // 克隆一份，避免修改原数组
            String[] sorted = cards.clone();
            // 按牌面点数从小到大排序（比较器用 rank 方法取点数序号）
            java.util.Arrays.sort(sorted, java.util.Comparator.comparingInt(this::rank));

            // sequences 保存当前正在延长的各个"候选顺子"（每个是一条 List）
            java.util.List<java.util.List<String>> sequences = new java.util.ArrayList<>();

            for (String card : sorted) {
                boolean matched = false;
                // 尝试把当前牌接到某条已有序列的末尾
                for (java.util.List<String> sequence : sequences) {
                    // 接牌条件：
                    //   1. 当前牌不是 "2"（2 不能进顺子）
                    //   2. 当前牌点数 = 该序列最后一张点数 + 1（连续）
                    if (
                            !card.equals("2") && rank(card)
                                    == rank(sequence.get(sequence.size() - 1)) + 1
                    ) {
                        sequence.add(card);
                        matched = true;
                        break;
                    }
                }
                // 接不上任何已有序列 → 以当前牌开一条新序列
                if (!matched) {
                    java.util.List<String> sequence = new java.util.ArrayList<>();
                    sequence.add(card);
                    sequences.add(sequence);
                }
            }

            // 收集长度 >= 5 的序列作为结果
            java.util.List<String> result = new java.util.ArrayList<>();
            for (java.util.List<String> sequence : sequences) {
                if (sequence.size() >= 5)
                    result.add(String.join(" ", sequence));
            }
            return result.toArray(new String[0]);
        }

        /**
         * 返回牌面在顺子中的序号：3=0, 4=1, ..., K=10, A=11, 2=12
         * 用于排序和判断相邻。
         */
        private int rank(String card) {
            String[] order = {
                    "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"
            };
            for (
                    int index = 0; index < order.length; index++
            )
                if (order[index].equals(card))
                    return index;
            return order.length; // 未识别的牌返回一个哨兵值（=13）
        }
    }

    private int rank(String card) {
        String[] order = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        for (int index = 0; index < order.length; index++) if (order[index].equals(card)) return index;
        return order.length;
    }

}
