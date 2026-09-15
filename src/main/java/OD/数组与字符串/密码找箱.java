package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:20
 */
public class 密码找箱 {

    static class Solution {

        /**
         * 在 boxes 数组中查找"字母组成与 key 完全相同"的箱子。
         *
         * "组成相同"指：忽略大小写、忽略非字母字符后，各字母出现的次数完全一致。
         * 例如 key = "Abc" 与 box = "a1Bc2" 视为相同（都是 a:1, b:1, c:1）。
         *
         * @param key    目标字符串（作为比对标准）
         * @param boxes  候选箱子数组
         * @return       第一个匹配箱子的 1-based 下标；找不到返回 -1
         */
        long findPasswordBox(String key, String[] boxes) {

            // ---- 1. 计算 key 的字母频次向量（26 维，对应 a~z）----
            int[] wanted = batch70Counts(key);

            // ---- 2. 逐个箱子比对 ----
            for (int i = 0; i < boxes.length; i++) {
                // 用 Arrays.equals 比较两个 int[26] 是否完全相同
                if (java.util.Arrays.equals(wanted, batch70Counts(boxes[i]))) {
                    return i + 1;   // 题目要求返回 1-based 下标
                }
            }

            // ---- 3. 没有任何箱子匹配 ----
            return -1;
        }

        /**
         * 统计字符串的字母频次。
         * 规则：
         *   - 忽略大小写（统一转小写）
         *   - 只统计 'a'~'z'，其他字符忽略
         *
         * @param value 输入字符串
         * @return      int[26]，下标 0 对应 'a'，下标 25 对应 'z'
         */
        int[] batch70Counts(String value) {

            // ---- 创建 26 维频次数组，初始全 0 ----
            int[] result = new int[26];

            // ---- 转小写后逐字符扫描 ----
            // toLowerCase(Locale.ROOT) 避免土耳其语等特殊 Locale 下 'I' 转换异常
            for (char raw : value.toLowerCase(java.util.Locale.ROOT).toCharArray()) {

                // 只统计字母，非字母（数字、符号、空格）直接跳过
                if (raw >= 'a' && raw <= 'z')
                    result[raw - 'a']++;
            }

            return result;
        }
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.findPasswordBox("abc", new String[]{"AAbbcc","bca"}));
    }

}
