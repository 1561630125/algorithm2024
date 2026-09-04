package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 14:07
 */
public class 四位数字谜底 {

    /**
     * 数字猜谜游戏 - 根据线索推导唯一数字
     *
     * 游戏规则（类似 Bulls and Cows / 1A2B 游戏）：
     * 1. 玩家猜测一个4位数字（0000-9999），可以包含前导零
     * 2. 每条线索格式："guess score"，如 "1234 1A2B"
     * 3. A表示位置和数字都正确（exact match）
     * 4. B表示数字正确但位置错误（misplaced match）
     * 5. 根据所有线索推导出唯一正确的数字
     *
     * 线索格式：
     * - 猜测部分：4位数字字符串
     * - 评分部分："{exact}A{misplaced}B"，如 "1A2B"
     * - 评分可以为负数（表示无效线索），此时应返回 "NA"
     *
     * @param clues 线索数组，每个元素格式为 "guess score"
     * @return 唯一符合条件的数字（4位字符串），如果有0个或多个则返回 "NA"
     */
    class Solution {
        String deduceUniqueNumber(String[] clues) {
            // 存储所有猜测的数字
            java.util.ArrayList<String> guesses = new java.util.ArrayList<>();
            // 存储每个猜测对应的分数 [exact, misplaced]
            java.util.ArrayList<int[]> scores = new java.util.ArrayList<>();

            // 解析并验证所有线索
            for (String clue : clues) {
                // 按空白字符分割（支持多个空格）
                String[] parts = clue.trim().split("\\s+");

                // 验证格式：
                // 1. 必须包含2个部分（猜测和分数）
                // 2. 猜测必须是4位数字
                // 3. 分数必须符合 "数字A数字B" 格式，如 "1A2B"
                if (parts.length != 2 || !parts[0].matches("[0-9]{4}")
                        || !parts[1].matches("-?[0-9]+A-?[0-9]+B")) {
                    return "NA";  // 格式错误
                }

                // 解析分数：找到 A 的位置
                int aIndex = parts[1].indexOf('A');
                int exact, misplaced;

                try {
                    // 提取 A 前面的数字（精确匹配数）
                    exact = Integer.parseInt(parts[1].substring(0, aIndex));
                    // 提取 A 和 B 之间的数字（位置错误但数字正确的数量）
                    // 注意：substring 不包含最后一个字符 'B'
                    misplaced = Integer.parseInt(parts[1].substring(aIndex + 1, parts[1].length() - 1));
                } catch (NumberFormatException error) {
                    return "NA";  // 数字解析失败
                }

                // 验证分数的合理性
                // 1. 精确数和错位数不能为负数
                // 2. 精确数 + 错位数不能超过4（总共只有4位）
                if (exact < 0 || misplaced < 0 || exact + misplaced > 4) {
                    return "NA";
                }

                guesses.add(parts[0]);      // 存储猜测
                scores.add(new int[] {exact, misplaced});  // 存储分数
            }

            // 尝试所有可能的4位数字（0000-9999）
            String candidate = null;

            for (int number = 0; number < 10000; number++) {
                // 格式化为4位数字字符串（补前导零）
                String secret = String.format(java.util.Locale.ROOT, "%04d", number);

                // 检查当前数字是否符合所有线索
                boolean valid = true;
                for (int index = 0; index < guesses.size(); index++) {
                    // 计算当前数字与某个猜测的反馈分数
                    int[] feedback = feedback(secret, guesses.get(index));
                    int[] expected = scores.get(index);

                    // 如果分数不匹配，当前数字无效
                    if (!java.util.Arrays.equals(feedback, expected)) {
                        valid = false;
                        break;
                    }
                }

                // 如果当前数字符合所有线索
                if (valid) {
                    // 如果已经找到了一个候选数字，说明不唯一
                    if (candidate != null) {
                        return "NA";  // 多个解
                    }
                    candidate = secret;  // 记录唯一候选
                }
            }

            // 返回结果：找到候选数字则返回，否则返回 "NA"
            return candidate == null ? "NA" : candidate;
        }

        /**
         * 计算反馈分数（类似 Bulls and Cows）
         *
         * @param secret 秘密数字（4位字符串）
         * @param guess 猜测的数字（4位字符串）
         * @return [exact, misplaced] 精确匹配数和位置错配数
         *
         * 算法说明：
         * 1. 先统计精确匹配（位置和数字都相同）
         * 2. 对于非精确匹配的位置，统计各数字出现的次数
         * 3. 错配数 = 各数字在secret和guess中出现次数的较小值之和
         */
        private int[] feedback(String secret, String guess) {
            int exact = 0;
            // 统计非精确匹配位置上的数字出现次数
            // first[digit] = secret中digit出现的次数
            // second[digit] = guess中digit出现的次数
            int[] first = new int[10];
            int[] second = new int[10];

            // 遍历4个位置
            for (int index = 0; index < 4; index++) {
                char secretChar = secret.charAt(index);
                char guessChar = guess.charAt(index);

                if (secretChar == guessChar) {
                    // 位置和数字都正确 → 精确匹配
                    exact++;
                } else {
                    // 记录非精确匹配位置的数字出现次数
                    first[secretChar - '0']++;
                    second[guessChar - '0']++;
                }
            }

            // 计算位置错配数（数字正确但位置错误）
            int misplaced = 0;
            for (int digit = 0; digit < 10; digit++) {
                // 对于每个数字，取secret和guess中出现次数的最小值
                // 这表示该数字在两个字符串中都出现但不在同一位置的数量
                misplaced += Math.min(first[digit], second[digit]);
            }

            return new int[] {exact, misplaced};
        }
    }
}
