package OD.树与图;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 23:34
 */
public class AC自动机 {

    public class AhoCorasickDemo {

        class Node {
            Map<Character, Node> children = new HashMap<>();
            Node fail;                           // 失配指针
            List<String> outputs = new ArrayList<>();  // 在此结束的模式串
        }

        private Node root = new Node();

        /** 插入一个模式串 */
        public void insert(String pattern) {
            Node node = root;
            for (char c : pattern.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new Node());
            }
            node.outputs.add(pattern);
        }

        /** BFS 构建 fail 指针 */
        public void build() {
            Queue<Node> queue = new LinkedList<>();
            root.fail = root;

            // 第一层：fail 指向 root
            for (Node child : root.children.values()) {
                child.fail = root;
                queue.add(child);
            }

            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                for (Map.Entry<Character, Node> e : cur.children.entrySet()) {
                    char c = e.getKey();
                    Node child = e.getValue();

                    // 沿 fail 链找有 c 出边的节点
                    Node f = cur.fail;
                    while (f != root && !f.children.containsKey(c))
                        f = f.fail;
                    if (f.children.containsKey(c) && f.children.get(c) != child)
                        child.fail = f.children.get(c);
                    else
                        child.fail = root;

                    // 继承 fail 的 outputs（后缀也是模式）
                    child.outputs.addAll(child.fail.outputs);

                    queue.add(child);
                }
            }
        }

        /**
         * 在 text 中扫描，返回所有匹配。
         * 结果格式：{起始位置, 模式串}
         */
        public List<int[]> search(String text) {
            List<int[]> result = new ArrayList<>();
            Node node = root;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                // 沿 fail 跳，直到找到有 c 出边的节点
                while (node != root && !node.children.containsKey(c))
                    node = node.fail;

                if (node.children.containsKey(c))
                    node = node.children.get(c);
                else
                    node = root;

                // 输出该节点所有匹配的模式串
                for (String pattern : node.outputs) {
                    int start = i - pattern.length() + 1;
                    result.add(new int[]{start, pattern.length()});
                    // 也可以用 List<Object[]> 存 {start, pattern}
                }
            }
            return result;
        }

        // ---------- 测试 ----------
        public  void main(String[] args) {
            AhoCorasickDemo ac = new AhoCorasickDemo();

            String[] patterns = {"he", "she", "his", "hers"};
            for (String p : patterns) ac.insert(p);
            ac.build();

            String text = "ushers";
            List<int[]> matches = ac.search(text);

            for (int[] m : matches) {
                int start = m[0], len = m[1];
                String pattern = text.substring(start, start + len);
                System.out.println("位置 " + start + ": " + pattern);
            }
        }
    }

}
