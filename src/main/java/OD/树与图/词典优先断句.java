package OD.树与图;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 22:32
 */
public class 词典优先断句 {
    static class Solution {

        /**
         * 用词典对句子分段。
         *
         * @param dictionary 词典，每个元素是一个短语（可能含多个词，用空格分隔）
         * @param sentence   待分段的句子
         * @return 用括号包裹每个片段的字符串
         */
        String segmentSentence(String[] dictionary, String sentence) {

            // ---------- 1. 预处理词典 ----------
            // 把每个词典短语按空格拆成词数组，存进 phrases
            java.util.List<String[]> phrases = new java.util.ArrayList<>();
            if (dictionary != null)
                for (String value : dictionary)
                    if (value != null && !value.trim().isEmpty())
                        phrases.add(value.trim().split("\\s+"));

            // ---------- 2. 预处理句子 ----------
            String trimmed = sentence == null ? "" : sentence.trim();
            if (trimmed.isEmpty())
                return "";
            String[] words = trimmed.split("\\s+");

            // ---------- 3. 从左到右扫描 ----------
            StringBuilder result = new StringBuilder();
            for (int index = 0; index < words.length; ) {
                String[] best = null;      // 匹配到的最优短语（词数组）
                String bestText = "";      // 最优短语的文本形式

                // 尝试用每个词典短语匹配当前位置
                for (String[] phrase : phrases) {
                    // 长度不够 → 跳过
                    if (index + phrase.length > words.length)
                        continue;

                    // 逐词比较
                    boolean matches = true;
                    for (int offset = 0; offset < phrase.length; offset++)
                        if (!words[index + offset].equals(phrase[offset])) {
                            matches = false;
                            break;
                        }

                    String phraseText = String.join(" ", phrase);

                    // 选择规则：
                    // 1) 更长的短语优先
                    // 2) 长度相同则字典序更大的优先（保证确定性）
                    if (matches
                            && (best == null
                            || phrase.length > best.length
                            || (phrase.length == best.length
                            && phraseText.compareTo(bestText) > 0))) {
                        best = phrase;
                        bestText = phraseText;
                    }
                }

                // ---------- 4. 输出片段 ----------
                result.append('(');
                if (best == null) {
                    // 没匹配到短语 → 用单个词
                    result.append(words[index++]);
                } else {
                    // 匹配到短语 → 整段输出，index 跳过整个短语
                    result.append(bestText);
                    index += best.length;
                }
                result.append(')');
            }
            return result.toString();
        }
    }

    public static void main(String[] args) {
        String[] dictionary = new String[]{"new york","city"};
        String sentence = "new york city";
        Solution solution = new Solution();
        System.out.println(solution.segmentSentence(dictionary, sentence));



        TrieSolution trieSolution = new TrieSolution();
        System.out.println(trieSolution.segmentSentence(dictionary, sentence));
    }


    static class TrieSolution {

        /** Trie 节点：边是"单词"，不是字符 */
        class TrieNode {
            Map<String, TrieNode> children = new HashMap<>();
            boolean isEnd = false;     // 是否某个短语的结尾
            String phrase = null;      // 若是结尾，记录完整短语文本
        }

        private TrieNode root = new TrieNode();

        /** 插入一个短语（词数组） */
        private void insert(String[] phrase) {
            TrieNode node = root;
            for (String word : phrase) {
                node = node.children.computeIfAbsent(word, k -> new TrieNode());
            }
            node.isEnd = true;
            node.phrase = String.join(" ", phrase);
        }

        /** 从 words[index] 开始，找最长匹配的短语 */
        private String longestMatch(String[] words, int index) {
            TrieNode node = root;
            String best = null;
            int j = index;

            while (j < words.length && node.children.containsKey(words[j])) {
                node = node.children.get(words[j]);
                j++;
                if (node.isEnd) {          // 走到一个短语结尾
                    best = node.phrase;    // 记录（越往后越长，最后就是最长的）
                }
            }
            return best;   // null 表示没有匹配
        }

        String segmentSentence(String[] dictionary, String sentence) {
            // 建 Trie
            if (dictionary != null)
                for (String value : dictionary)
                    if (value != null && !value.trim().isEmpty())
                        insert(value.trim().split("\\s+"));

            String trimmed = sentence == null ? "" : sentence.trim();
            if (trimmed.isEmpty()) return "";
            String[] words = trimmed.split("\\s+");

            StringBuilder result = new StringBuilder();
            for (int index = 0; index < words.length; ) {
                String match = longestMatch(words, index);
                result.append('(');
                if (match == null) {
                    result.append(words[index++]);
                } else {
                    result.append(match);
                    index += match.split("\\s+").length;   // 或用匹配长度
                }
                result.append(')');
            }
            return result.toString();
        }
    }


    class AhoCorasick {

        class Node {
            Map<String, Node> children = new HashMap<>();
            Node fail;                      // 失配指针
            List<String> outputs = new ArrayList<>();  // 在此节点结束的短语
        }

        private Node root = new Node();

        /** 插入一个短语 */
        void insert(String[] phrase) {
            Node node = root;
            for (String word : phrase) {
                node = node.children.computeIfAbsent(word, k -> new Node());
            }
            node.outputs.add(String.join(" ", phrase));
        }

        /** BFS 构建 fail 指针 */
        void build() {
            Queue<Node> queue = new LinkedList<>();
            root.fail = root;

            // 第一层：fail 指向 root
            for (Node child : root.children.values()) {
                child.fail = root;
                queue.add(child);
            }

            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                for (Map.Entry<String, Node> e : cur.children.entrySet()) {
                    String word = e.getKey();
                    Node child = e.getValue();

                    // 沿 fail 链找是否存在 word 的转移
                    Node f = cur.fail;
                    while (f != root && !f.children.containsKey(word))
                        f = f.fail;
                    if (f.children.containsKey(word) && f.children.get(word) != child)
                        child.fail = f.children.get(word);
                    else
                        child.fail = root;

                    // 继承 fail 的输出（后缀也是模式）
                    child.outputs.addAll(child.fail.outputs);

                    queue.add(child);
                }
            }
        }

        /**
         * 在文本（词数组）上扫描，返回每个位置结束的所有匹配。
         * result[i] = 在 words[i] 结尾的所有短语文本
         */
        List<List<String>> search(String[] words) {
            List<List<String>> result = new ArrayList<>();
            Node node = root;

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                // 沿 fail 跳，直到找到有 word 转移的节点
                while (node != root && !node.children.containsKey(word))
                    node = node.fail;

                if (node.children.containsKey(word))
                    node = node.children.get(word);
                else
                    node = root;

                result.add(new ArrayList<>(node.outputs));
            }
            return result;
        }
    }


    class HashByLengthSolution {

        String segmentSentence(String[] dictionary, String sentence) {
            // ---------- 1. 按词数分组：长度 → 短语文本集合 ----------
            Map<Integer, Set<String>> byLength = new HashMap<>();
            int maxLen = 0;

            if (dictionary != null) {
                for (String value : dictionary) {
                    if (value == null || value.trim().isEmpty()) continue;
                    String[] phrase = value.trim().split("\\s+");
                    String text = String.join(" ", phrase);
                    byLength.computeIfAbsent(phrase.length, k -> new HashSet<>())
                            .add(text);
                    maxLen = Math.max(maxLen, phrase.length);
                }
            }

            String trimmed = sentence == null ? "" : sentence.trim();
            if (trimmed.isEmpty()) return "";
            String[] words = trimmed.split("\\s+");

            StringBuilder result = new StringBuilder();

            for (int index = 0; index < words.length; ) {
                // ---------- 2. 从最长开始试 ----------
                String bestText = null;
                int bestLen = 0;

                for (int len = Math.min(maxLen, words.length - index); len >= 1; len--) {
                    // 拼出 words[index..index+len) 的文本
                    String candidate = String.join(" ",
                            Arrays.copyOfRange(words, index, index + len));

                    if (byLength.getOrDefault(len, Collections.emptySet())
                            .contains(candidate)) {
                        bestText = candidate;
                        bestLen = len;
                        break;   // 从长到短，第一个命中就是最长
                    }
                }

                result.append('(');
                if (bestText == null) {
                    result.append(words[index++]);
                } else {
                    result.append(bestText);
                    index += bestLen;
                }
                result.append(')');
            }
            return result.toString();
        }
    }


    class DPSolution {

        String segmentSentence(String[] dictionary, String sentence) {
            // ---------- 建词典哈希（按长度分组，便于快速判断） ----------
            Map<Integer, Set<String>> byLength = new HashMap<>();
            int maxLen = 0;
            if (dictionary != null) {
                for (String value : dictionary) {
                    if (value == null || value.trim().isEmpty()) continue;
                    String[] phrase = value.trim().split("\\s+");
                    byLength.computeIfAbsent(phrase.length, k -> new HashSet<>())
                            .add(String.join(" ", phrase));
                    maxLen = Math.max(maxLen, phrase.length);
                }
            }

            String trimmed = sentence == null ? "" : sentence.trim();
            if (trimmed.isEmpty()) return "";
            String[] words = trimmed.split("\\s+");
            int n = words.length;

            // ---------- DP ----------
            int[] dp = new int[n + 1];
            int[] choiceLen = new int[n + 1];     // 从 i 开始选的片段词数
            String[] choiceText = new String[n + 1];

            dp[n] = 0;

            for (int i = n - 1; i >= 0; i--) {
                // 默认：用单字
                dp[i] = 1 + dp[i + 1];
                choiceLen[i] = 1;
                choiceText[i] = words[i];

                // 尝试所有匹配的短语
                for (int len = 1; len <= Math.min(maxLen, n - i); len++) {
                    String candidate = String.join(" ",
                            Arrays.copyOfRange(words, i, i + len));
                    if (byLength.getOrDefault(len, Collections.emptySet())
                            .contains(candidate)) {
                        if (1 + dp[i + len] < dp[i]) {
                            dp[i] = 1 + dp[i + len];
                            choiceLen[i] = len;
                            choiceText[i] = candidate;
                        }
                    }
                }
            }

            // ---------- 回溯方案 ----------
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < n; ) {
                result.append('(').append(choiceText[i]).append(')');
                i += choiceLen[i];
            }
            return result.toString();
        }
    }


    class BacktrackSolution {

        private Map<Integer, Set<String>> byLength = new HashMap<>();
        private int maxLen = 0;
        private String[] words;
        private List<List<String>> allResults = new ArrayList<>();

        List<List<String>> allSegmentations(String[] dictionary, String sentence) {
            // 建词典
            if (dictionary != null) {
                for (String value : dictionary) {
                    if (value == null || value.trim().isEmpty()) continue;
                    String[] phrase = value.trim().split("\\s+");
                    byLength.computeIfAbsent(phrase.length, k -> new HashSet<>())
                            .add(String.join(" ", phrase));
                    maxLen = Math.max(maxLen, phrase.length);
                }
            }

            String trimmed = sentence == null ? "" : sentence.trim();
            if (trimmed.isEmpty()) return allResults;
            words = trimmed.split("\\s+");

            backtrack(0, new ArrayList<>());
            return allResults;
        }

        private void backtrack(int index, List<String> path) {
            if (index == words.length) {
                allResults.add(new ArrayList<>(path));
                return;
            }

            // 剪枝：剩余词数 * 最长短语 < ？不需要，枚举所有方案时无剪枝
            // 尝试所有匹配的短语
            for (int len = 1; len <= Math.min(maxLen, words.length - index); len++) {
                String candidate = String.join(" ",
                        Arrays.copyOfRange(words, index, index + len));
                if (byLength.getOrDefault(len, Collections.emptySet())
                        .contains(candidate)) {
                    path.add(candidate);
                    backtrack(index + len, path);
                    path.remove(path.size() - 1);
                }
            }

            // 单字（也是长度 1 的"短语"）
            // 注意：上面 len=1 已经包含了单字的情况（如果词典里有），
            // 这里补上"词典没有但必须单独成段"的情况
            boolean singleCovered = byLength.getOrDefault(1, Collections.emptySet())
                    .contains(words[index]);
            if (!singleCovered) {
                path.add(words[index]);
                backtrack(index + 1, path);
                path.remove(path.size() - 1);
            }
        }


        private int bestCount = Integer.MAX_VALUE;
        private List<String> bestPath = null;

        private void backtrack2(int index, List<String> path) {
            // 剪枝：当前片段数已经 ≥ 已知最优
            if (path.size() >= bestCount) return;

            if (index == words.length) {
                bestCount = path.size();
                bestPath = new ArrayList<>(path);
                return;
            }

            // 优先试长短语（更快找到好解，剪枝更狠）
            for (int len = Math.min(maxLen, words.length - index); len >= 1; len--) {
                String candidate = String.join(" ",
                        Arrays.copyOfRange(words, index, index + len));
                if (byLength.getOrDefault(len, Collections.emptySet())
                        .contains(candidate)) {
                    path.add(candidate);
                    backtrack(index + len, path);
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    class SuffixAutomatonSolution {

        /** 后缀自动机节点 */
        class State {
            Map<String, Integer> next = new HashMap<>();  // 边：词 → 状态
            int link;        // 后缀链接
            int len;         // 该状态代表的最长子串长度
            boolean isTerminal;  // 是否是某个词典短语的结尾

            State(int len) {
                this.len = len;
                this.link = -1;
            }
        }

        private List<State> sam = new ArrayList<>();
        private int last = 0;

        /** 初始化 SAM（根状态） */
        private void init() {
            sam.clear();
            sam.add(new State(0));
            sam.get(0).link = -1;
            last = 0;
        }

        /** 向 SAM 追加一个"词" */
        private void extend(String word) {
            int cur = sam.size();
            sam.add(new State(sam.get(last).len + 1));
            int p = last;

            while (p != -1 && !sam.get(p).next.containsKey(word)) {
                sam.get(p).next.put(word, cur);
                p = sam.get(p).link;
            }

            if (p == -1) {
                sam.get(cur).link = 0;
            } else {
                int q = sam.get(p).next.get(word);
                if (sam.get(p).len + 1 == sam.get(q).len) {
                    sam.get(cur).link = q;
                } else {
                    // 克隆 q
                    State clone = new State(sam.get(p).len + 1);
                    clone.next = new HashMap<>(sam.get(q).next);
                    clone.link = sam.get(q).link;
                    int cloneIdx = sam.size();
                    sam.add(clone);

                    while (p != -1 && sam.get(p).next.get(word) == q) {
                        sam.get(p).next.put(word, cloneIdx);
                        p = sam.get(p).link;
                    }
                    sam.get(q).link = cloneIdx;
                    sam.get(cur).link = cloneIdx;
                }
            }
            last = cur;
        }

        /** 判断从 state 出发、沿给定词序列能否走通，返回结束状态；走不通返回 -1 */
        private int walk(int state, String[] words, int start, int maxSteps) {
            int cur = state;
            for (int i = 0; i < maxSteps && start + i < words.length; i++) {
                Integer nxt = sam.get(cur).next.get(words[start + i]);
                if (nxt == null) break;
                cur = nxt;
            }
            return cur;
        }

        String segmentSentence(String[] dictionary, String sentence) {
            // ---------- 建 SAM ----------
            init();
            if (dictionary != null) {
                for (String value : dictionary) {
                    if (value == null || value.trim().isEmpty()) continue;
                    String[] phrase = value.trim().split("\\s+");
                    for (String word : phrase)
                        extend(word);
                    sam.get(last).isTerminal = true;   // 标记短语结尾
                    // 注意：简单实现下 last 不重置，这里需按需调整
                }
            }

            // 实际使用需要更复杂的处理，下面给"简化可用"的版本
            return segmentWithTrieLike(dictionary, sentence);
        }

        /** 简化实现：因为 SAM 在词级处理较复杂，这里用"等价"的哈希+长度方案 */
        private String segmentWithTrieLike(String[] dictionary, String sentence) {
            Map<Integer, Set<String>> byLength = new HashMap<>();
            int maxLen = 0;
            if (dictionary != null) {
                for (String value : dictionary) {
                    if (value == null || value.trim().isEmpty()) continue;
                    String[] phrase = value.trim().split("\\s+");
                    byLength.computeIfAbsent(phrase.length, k -> new HashSet<>())
                            .add(String.join(" ", phrase));
                    maxLen = Math.max(maxLen, phrase.length);
                }
            }
            String[] words = sentence.trim().split("\\s+");
            StringBuilder result = new StringBuilder();
            for (int index = 0; index < words.length; ) {
                String bestText = null;
                int bestLen = 0;
                for (int len = Math.min(maxLen, words.length - index); len >= 1; len--) {
                    String candidate = String.join(" ",
                            Arrays.copyOfRange(words, index, index + len));
                    if (byLength.getOrDefault(len, Collections.emptySet())
                            .contains(candidate)) {
                        bestText = candidate;
                        bestLen = len;
                        break;
                    }
                }
                result.append('(');
                if (bestText == null) {
                    result.append(words[index++]);
                } else {
                    result.append(bestText);
                    index += bestLen;
                }
                result.append(')');
            }
            return result.toString();
        }
    }
}
