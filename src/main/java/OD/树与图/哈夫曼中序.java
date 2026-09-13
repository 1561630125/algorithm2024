package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 17:52
 */
public class 哈夫曼中序 {

    class Solution {

        /** 霍夫曼树的节点 */
        class Node {
            long value;    // 该子树所有叶子权重之和
            int count;     // 该子树内的节点计数（用于打破平局）
            Node left, right;

            Node(long v, int c, Node l, Node r) {
                value = v;
                count = c;
                left = l;
                right = r;
            }
        }

        /**
         * 构造霍夫曼树，返回中序遍历的节点值序列。
         *
         * @param weights 各叶子的权重
         * @return 霍夫曼树中序遍历的节点值数组
         */
        long[] huffmanInorder(long[] weights) {

            // ---------- 1. 优先队列：按 value 升序，value 相同按 count 升序 ----------
            java.util.PriorityQueue<Node> q = new java.util.PriorityQueue<>((a, b) ->
                    a.value != b.value
                            ? Long.compare(a.value, b.value)
                            : Integer.compare(a.count, b.count));

            // ---------- 2. 每个权重作为一个叶子节点入队 ----------
            for (long v : weights)
                q.add(new Node(v, 1, null, null));

            // ---------- 3. 反复取两个最小节点，合并成一个新节点 ----------
            while (q.size() > 1) {
                Node l = q.remove();   // 最小
                Node r = q.remove();   // 次小
                // 新节点：value = 两者之和，count = 两者 count 之和 + 1（自身）
                q.add(new Node(l.value + r.value, l.count + r.count + 1, l, r));
            }

            // ---------- 4. 中序遍历霍夫曼树 ----------
            java.util.List<Long> out = new java.util.ArrayList<>();
            if (!q.isEmpty())
                visit(q.remove(), out);

            // ---------- 5. List<Long> 转 long[] ----------
            long[] result = new long[out.size()];
            for (int i = 0; i < result.length; i++)
                result[i] = out.get(i);
            return result;
        }

        /** 中序遍历：左 → 根 → 右 */
        void visit(Node n, java.util.List<Long> out) {
            if (n == null)
                return;
            visit(n.left, out);       // 左子树
            out.add(n.value);         // 根
            visit(n.right, out);      // 右子树
        }
    }


}
