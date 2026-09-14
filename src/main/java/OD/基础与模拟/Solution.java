package OD.基础与模拟;

class Solution {

    /**
     * 单词接龙：从 startIndex 指定的单词出发，反复取"首字母 = 当前单词尾字母"的单词拼在末尾。
     *
     * @param startIndex 起始单词在 words 中的下标
     * @param words      单词数组
     * @return 拼接后的长字符串；下标非法时返回空串
     */
    String buildWordChain(int startIndex, String[] words) {

        // ---- 0. 边界检查：起始下标越界，直接返回空串 ----
        if (startIndex < 0 || startIndex >= words.length)
            return "";

        // ---- 1. 按"首字母"分桶 ----
        // buckets: 首字母 -> 该字母开头的所有单词
        java.util.Map<Character, java.util.List<String>> buckets = new java.util.HashMap<>();
        for (int index = 0; index < words.length; index++)
            // 排除起始单词本身（不能自己接自己），空串不参与分桶
            if (index != startIndex && !words[index].isEmpty())
                buckets
                    .computeIfAbsent(words[index].charAt(0), key -> new java.util.ArrayList<>())
                    .add(words[index]);

        // ---- 2. 每个桶内排序 ----
        // 规则：长度长的优先；长度相同时，字典序小的优先。
        // 目的：每次 pick 时，先接"更长"的词，让结果尽可能长。
        for (java.util.List<String> bucket : buckets.values())
            bucket.sort((a, b)
                            -> a.length() != b.length()
                               ? Integer.compare(b.length(), a.length())   // 长 → 短
                               : a.compareTo(b));                          // 字典序升序

        // ---- 3. 从起始词开始，不断往后接 ----
        String current = words[startIndex];
        StringBuilder output = new StringBuilder(current);

        while (!current.isEmpty()) {
            // 取当前词的最后一个字符，去对应桶里找"可以接上"的词
            java.util.List<String> bucket = buckets.get(current.charAt(current.length() - 1));

            // 桶不存在或已经空了 → 接不下去，结束
            if (bucket == null || bucket.isEmpty())
                break;

            // 取桶里排在最前的词（最长 / 字典序最小），并从桶里移除（每个词只能用一次）
            current = bucket.remove(0);
            output.append(current);
        }

        return output.toString();
    }
}