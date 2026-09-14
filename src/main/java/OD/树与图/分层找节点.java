package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 16:12
 */
public class 分层找节点 {
    public class Solution {

        /**
         * 返回一棵树中第 k 层所有节点值去重并升序排序后的结果。
         *
         * @param n      节点总数，节点编号为 0 ~ n-1
         * @param values values[i] 表示节点 i 的值
         * @param edges  edges[j] = {parent, child}，表示一条从 parent 指向 child 的有向边
         * @param k      目标层数，根节点（0）视为第 0 层
         * @return 第 k 层所有节点值去重、升序后的数组
         */
        public int[] kthLevelValues(int n, int[] values, int[][] edges, int k) {

            // 1. 建图：把树转成邻接表 children，children.get(u) 存放 u 的所有直接子节点
            java.util.List<java.util.List<Integer>> children = new java.util.ArrayList<>();
            for (int index = 0; index < n; index++)
                children.add(new java.util.ArrayList<>());

            // edges 中每条边是 {parent, child}，直接加入 parent 的子节点列表
            for (int[] edge : edges)
                children.get(edge[0]).add(edge[1]);

            // 2. 逐层下降：level 保存当前层的所有节点编号
            //    初始为第 0 层，只有根节点 0
            java.util.List<Integer> level = new java.util.ArrayList<>();
            level.add(0);

            // 循环 k 次，每次把 level 替换成它所有子节点组成的下一层
            for (int depth = 0; depth < k; depth++) {
                java.util.List<Integer> next = new java.util.ArrayList<>();
                for (int parent : level)
                    // 把当前层每个节点的所有子节点加入下一层
                    next.addAll(children.get(parent));
                level = next;
            }

            // 此时 level 中就是第 k 层的所有节点编号（可能有重复值，但节点编号不会重复）

            // 3. 收集第 k 层节点的值，并用 TreeSet 去重 + 升序排序
            java.util.SortedSet<Integer> unique = new java.util.TreeSet<>();
            for (int index : level)
                unique.add(values[index]);

            // 4. 把有序集合转成 int[] 返回
            int[] answer = new int[unique.size()];
            int write = 0;
            for (int value : unique)
                answer[write++] = value;
            return answer;
        }
    }

}
