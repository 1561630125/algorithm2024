package OD.基础与模拟;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 15:01
 */
public class 括号等价优化 {
    // 并查集数组，只处理52个英文字母
    private static int[] parent = new int[52];

    // 寻找根节点，带路径压缩
    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // 合并两个节点所在的集合
    private static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootY] = rootX; // 将rootY的父节点设为rootX
        }
    }

    // 将字母转换为索引 (0-25: a-z, 26-51: A-Z)
    private static int charToIndex(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        } else { // 'A' - 'Z'
            return 26 + (c - 'A');
        }
    }

    // 将索引转换回字母（此处以原字符的大小写为准，或统一返回小写最小）
    private static char indexToChar(int idx) {
        if (idx < 26) {
            return (char) ('a' + idx);
        } else {
            return (char) ('A' + (idx - 26));
        }
    }


    public static String simplifyEquivalentString(String input) {
        // 1. 初始化并查集
        for (int i = 0; i < 52; i++) {
            parent[i] = i;
        }

        // 用于存放括号外的字符
        StringBuilder outsideSb = new StringBuilder();
        // 标记是否在括号内
        boolean inBracket = false;
        // 临时存储当前括号内的字符索引，用于建立连接
        List<Integer> currentBracketChars = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '(') {
                inBracket = true;
                currentBracketChars.clear();
            } else if (c == ')') {
                inBracket = false;
                // 将当前括号内的所有字母建立等效关系
                if (currentBracketChars.size() > 1) {
                    // 注意：大小写等效，合并时需要将大写转为小写再合并？
                    // 更稳妥的方式：把字母转成小写后找集合，或者直接连接原始字符。
                    // 但题目要求大小写等效，所以我们连接时，统一用小写字母作为代表。
                    // 先把当前集合的所有字符转成小写索引进行合并
                    int first = -1;
                    for (int idx : currentBracketChars) {
                        // 为了建立大小写的联系，我们统一将字符转为小写的索引来合并
                        // 例如 'A' 和 'a' 都映射到索引0
                        int lowerIdx = idx % 26;
                        if (first == -1) {
                            first = lowerIdx;
                        } else {
                            union(first, lowerIdx);
                        }
                    }
                }
                currentBracketChars.clear();
            } else {
                // 是英文字母
                int idx = charToIndex(c);
                if (inBracket) {
                    // 在括号内，记录该字符
                    currentBracketChars.add(idx);
                } else {
                    // 在括号外，保留该字符
                    outsideSb.append(c);
                }
            }
        }

        // 2. 确定每个集合的最小字典序字符
        // 字典序比较：a < b < ... < z < A < B < ... < Z?
        // 通常字典序是 A < B < ... < Z < a < b < ... < z，因为ASCII码大写在前。
        // 但题目示例中 "a" 和 "A" 等效，取最小通常指字母序，一般我们认为 'a' < 'A'。
        // 但为了稳妥，我们按字符的自然顺序（ASCII）排序，因为题目明确说小写和大写等效。
        // 我们最终替换时，从集合中取出ASCII码最小的那个字符。
        Map<Integer, List<Character>> groupMap = new HashMap<>();
        for (int i = 0; i < 52; i++) {
            char originalChar = indexToChar(i);
            int root = find(i % 26); // 我们只用小写索引作为根
            groupMap.computeIfAbsent(root, k -> new ArrayList<>()).add(originalChar);
        }

        // 对每个组排序，找出最小的字符
        Map<Integer, Character> minCharMap = new HashMap<>();
        for (Map.Entry<Integer, List<Character>> entry : groupMap.entrySet()) {
            List<Character> chars = entry.getValue();
            Collections.sort(chars);
            // 取最小字符，这里排序后取第一个即为ASCII最小
            minCharMap.put(entry.getKey(), chars.get(0));
        }

        // 3. 构建结果字符串
        StringBuilder result = new StringBuilder();
        for (char c : outsideSb.toString().toCharArray()) {
            int idx = charToIndex(c);
            int lowerIdx = idx % 26; // 用小写索引去查找组
            int root = find(lowerIdx);

            // 检查该字符是否在某个等效集合中，且集合内有多个元素（即存在等效替换）
            // 更准确：看看这个字符的根是否被映射到某个最小字符，且该最小字符不等于它本身
            Character minChar = minCharMap.get(root);
            if (minChar != null) {
                // 需要判断当前字符是否真的需要替换：如果它不在任何括号里，但属于某个等效组
                // 例如，括号内只有 'a'，括号外有 'a'，此时不需要替换？
                // 题目要求：将所有字符替换为“在小括号对里包含的且字典序最小的等效字符”。
                // 只要有等效关系，并且该字符在等效集合中，就要替换为最小的。
                // 但如果该字符本身就是最小的，替换了也一样。
                result.append(minChar);
            } else {
                result.append(c);
            }
        }

        String ans = result.toString();
        return ans.isEmpty() ? "0" : ans;
    }



    /**
     * 简化等效字符串（将括号内视为一组等价字符，把外部字符替换为组内最小字母）
     *
     * @param value 输入字符串，只能包含字母和括号 '('、')'，括号可嵌套但不允许重复
     * @return 简化后的字符串，若格式非法或外部无字符则返回 "0"
     */
    String simplifyEquivalentString2(String value) {
        // 并查集 parent 数组，用于记录 26 个小写字母的等价关系
        int[] parent = new int[26];
        for (int index = 0; index < parent.length; index++) {
            parent[index] = index; // 初始每个字母的根是自己
        }

        // present 数组标记哪些 ASCII 字符出现在括号内部（包括大小写）
        boolean[] present = new boolean[128];

        // outside 用于收集所有括号外的字母
        StringBuilder outside = new StringBuilder();

        // inside 标记当前是否在括号内
        boolean inside = false;

        // firstInGroup 记录当前括号内遇到的第一个字母索引（用于并查集合并）
        int firstInGroup = -1;

        // 遍历输入字符串的每个字符
        for (int offset = 0; offset < value.length(); offset++) {
            char character = value.charAt(offset);

            if (character == '(') {
                // 如果已经在括号内，说明出现嵌套括号，非法
                if (inside) return "0";
                inside = true;
                firstInGroup = -1; // 重置组内第一个字母
            }
            else if (character == ')') {
                // 如果不在括号内却遇到右括号，非法
                if (!inside) return "0";
                inside = false;
            }
            else if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                if (inside) {
                    // 括号内的字母：标记为 present，并合并到同一组
                    present[character] = true;
                    int index = Character.toLowerCase(character) - 'a'; // 转小写索引
                    if (firstInGroup < 0) {
                        firstInGroup = index; // 第一个字母作为组的根
                    } else {
                        union(parent, firstInGroup, index); // 后续字母与第一个合并
                    }
                } else {
                    // 括号外的字母直接追加到 outside
                    outside.append(character);
                }
            }
            else {
                // 非法字符（非字母、非括号）
                return "0";
            }
        }

        // 如果括号未闭合，非法
        if (inside) return "0";

        // minimumByRoot[root] 存储每个等价类中 ASCII 码最小的字符（保留大小写）
        char[] minimumByRoot = new char[26];
        java.util.Arrays.fill(minimumByRoot, Character.MAX_VALUE);

        // 遍历所有可能的 ASCII 字符，找出每个等价类的最小字母
        for (int character = 0; character < present.length; character++) {
            if (!present[character]) continue; // 只处理括号内出现过的字符
            int root = find(parent, Character.toLowerCase((char) character) - 'a');
            if (character < minimumByRoot[root]) {
                minimumByRoot[root] = (char) character; // 保存最小字符（保留原始大小写）
            }
        }

        // 替换 outside 中所有在括号内出现过的字母为对应等价类的最小字母
        for (int offset = 0; offset < outside.length(); offset++) {
            char character = outside.charAt(offset);
            if (present[character]) { // 如果该字符曾在括号内出现
                int root = find(parent, Character.toLowerCase(character) - 'a');
                outside.setCharAt(offset, minimumByRoot[root]); // 替换为最小字母
            }
        }

        // 如果外部没有字符，返回 "0"，否则返回简化后的字符串
        return outside.length() == 0 ? "0" : outside.toString();
    }

    /**
     * 并查集查找操作（带路径压缩）
     *
     * @param parent 并查集父数组
     * @param index  要查找的节点索引
     * @return 该节点所在集合的根节点索引
     */
    private int find(int[] parent, int index) {
        while (parent[index] != index) {
            parent[index] = parent[parent[index]]; // 路径压缩
            index = parent[index];
        }
        return index;
    }

    /**
     * 并查集合并操作（将 right 合并到 left 所在集合）
     *
     * @param parent 并查集父数组
     * @param left   第一个元素索引
     * @param right  第二个元素索引
     */
    private void union(int[] parent, int left, int right) {
        left = find(parent, left);
        right = find(parent, right);
        if (left != right) {
            parent[right] = left; // 将 right 的根指向 left 的根
        }
    }


    public static void main(String[] args) {
        System.out.println(simplifyEquivalentString("d()ab"));
    }

}
