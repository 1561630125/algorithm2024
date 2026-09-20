package OD.搜索与枚举;

/**
 * 考点：全排列
 *
 * @author faming.yang@hand-china.com 2026-09-13 15:54
 */
public class 纯净单词排列 {

    public class Solution {

        public String[] magicSpellPermutations(String[] words) {
            // 用于保存“每个单词内部没有重复字符”的合法单词
            java.util.List<String> clean = new java.util.ArrayList<>();

            // 遍历每个单词，过滤掉含有重复字符的单词
            for (String word : words) {
                // 用 Set 记录当前单词中出现过的字符
                java.util.Set<Character> seen = new java.util.HashSet<>();
                boolean valid = true;

                // 遍历当前单词的每个字符
                for (char value : word.toCharArray()) {
                    // 如果该字符已经出现过，说明单词内部有重复字符
                    if (!seen.add(value)) {
                        valid = false;
                        break; // 当前单词不合法，直接跳出
                    }
                }

                // 只有没有重复字符的单词才加入 clean
                if (valid) {
                    clean.add(word);
                }
            }

            // 对合法单词排序，保证全排列结果按字典序稳定输出
            java.util.Collections.sort(clean);

            // 用于保存最终所有排列结果
            java.util.List<String> answer = new java.util.ArrayList<>();

            // 开始递归生成全排列
            buildPlans(
                    clean,                          // 候选单词列表
                    new boolean[clean.size()],      // used 数组，标记某个单词是否已经被使用
                    new java.util.ArrayList<>(),    // path，当前正在构造的排列
                    answer                          // 最终结果集合
            );

            // 将 List<String> 转换为 String[] 返回
            return answer.toArray(new String[0]);
        }

        /**
         * 递归生成全排列
         *
         * @param words  候选单词列表
         * @param used   标记每个位置的单词是否已经被使用
         * @param path   当前已经选择的单词序列
         * @param answer 保存所有完整排列的结果
         */
        private void buildPlans(
                java.util.List<String> words,
                boolean[] used,
                java.util.List<String> path,
                java.util.List<String> answer
        ) {
            // 递归终止条件：当前排列长度等于单词总数，说明已经形成一个完整排列
            if (path.size() == words.size()) {
                // 如果 path 非空，则把当前排列用空格连接成一个字符串，加入结果
                if (!path.isEmpty()) {
                    answer.add(String.join(" ", path));
                }
                return;
            }

            // 尝试把每一个尚未使用的单词放到当前位置
            for (int index = 0; index < words.size(); index++) {
                if (!used[index]) {
                    // 标记该单词已使用
                    used[index] = true;

                    // 把该单词加入当前排列
                    path.add(words.get(index));

                    // 递归处理下一个位置
                    buildPlans(words, used, path, answer);

                    // 回溯：移除最后加入的单词
                    path.remove(path.size() - 1);

                    // 回溯：取消该单词的使用标记
                    used[index] = false;
                }
            }
        }
    }


}
