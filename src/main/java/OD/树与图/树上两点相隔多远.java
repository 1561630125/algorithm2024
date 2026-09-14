package OD.树与图;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 15:28
 */
public class 树上两点相隔多远 {

    static public class Solution {
        /**
         * 在「层序遍历字符串」表示的二叉树中，求从 fromValue 到 toValue 的最短跳跃次数
         * （即两个节点之间经过的边数，等同于树上的最短路径长度）。
         *
         * treeLevelOrder 形如 "1,2,3,null,4,..."，按层序给出，null / 空串表示空节点。
         * 若树非法、节点不存在、或出现重复值，返回 -1。
         *
         * @param treeLevelOrder 层序遍历字符串（逗号分隔）
         * @param fromValue      起点节点值
         * @param toValue        终点节点值
         * @return 两节点间最短跳跃次数；不合法返回 -1
         */
        public int getMinJump(String treeLevelOrder, String fromValue, String toValue) {
            // 1. 按逗号切分；-1 保留末尾空串
            String[] tokens = treeLevelOrder.split(",", -1);

            // 空输入 / 根为空 → 非法
            if (tokens.length == 0 || tokens[0].isEmpty() || tokens[0].equals("null"))
                return -1;

            // parents[i]：节点 i 的父节点下标（根为 -1）
            java.util.List<Integer> parents = new java.util.ArrayList<>();
            // depths[i]：节点 i 的深度（根为 0）
            java.util.List<Integer> depths  = new java.util.ArrayList<>();
            // queue：BFS 用的节点下标队列（用 List 模拟，head 指向队首）
            java.util.List<Integer> queue   = new java.util.ArrayList<>();
            // indexes：节点值 → 内部下标
            java.util.Map<String, Integer> indexes = new java.util.HashMap<>();

            // 2. 初始化根节点（下标 0）
            parents.add(-1);
            depths.add(0);
            queue.add(0);
            indexes.put(tokens[0], 0);

            int tokenIndex = 1;   // 从第 2 个 token 开始（第 1 个已作为根）

            // 3. 按层序逐个读取子节点，构建父子关系和深度
            //    head 遍历 queue，每处理一个节点，就从 tokens 里取它的左右孩子
            for (int head = 0; head < queue.size() && tokenIndex < tokens.length; head++) {
                int current = queue.get(head);

                // 每个节点最多两个孩子：left、right
                for (int side = 0; side < 2 && tokenIndex < tokens.length; side++) {
                    String child = tokens[tokenIndex++];

                    // 空节点跳过（但仍消耗一个 token）
                    if (child.isEmpty() || child.equals("null"))
                        continue;

                    // 出现重复值 → 非法（节点值应唯一）
                    if (indexes.containsKey(child))
                        return -1;

                    int index = parents.size();          // 新节点的内部下标
                    indexes.put(child, index);
                    parents.add(current);                // 记录父节点
                    depths.add(depths.get(current) + 1); // 记录深度
                    queue.add(index);                    // 入队，等待处理其孩子
                }
            }

            // 4. 起点或终点不存在 → 非法
            if (!indexes.containsKey(fromValue) || !indexes.containsKey(toValue))
                return -1;

            // 5. 求两节点最短路径（跳跃次数）
            int left  = indexes.get(fromValue);
            int right = indexes.get(toValue);
            int jumps = 0;

            // 5.1 把较深的节点先上移到与另一节点同深度
            while (depths.get(left) > depths.get(right)) {
                left = parents.get(left);
                jumps++;
            }
            while (depths.get(right) > depths.get(left)) {
                right = parents.get(right);
                jumps++;
            }

            // 5.2 两个节点同时向上，直到相遇（LCA）
            while (left != right) {
                left  = parents.get(left);
                right = parents.get(right);
                jumps += 2;   // 两边各上移一步
            }

            return jumps;
        }
    }


    static public class Solution2 {
        /**
         * 在「层序遍历字符串」表示的二叉树中，求从 fromValue 到 toValue 的最短跳跃次数。
         * 使用标准库 Queue 进行 BFS 建树。
         */
        public int getMinJump(String treeLevelOrder, String fromValue, String toValue) {
            // 1. 按逗号切分；-1 保留末尾空串
            String[] tokens = treeLevelOrder.split(",", -1);

            // 空输入 / 根为空 → 非法
            if (tokens.length == 0 || tokens[0].isEmpty() || tokens[0].equals("null"))
                return -1;

            // parents[i]：节点 i 的父节点下标（根为 -1）
            List<Integer> parents = new ArrayList<>();
            // depths[i]：节点 i 的深度（根为 0）
            List<Integer> depths  = new ArrayList<>();
            // indexes：节点值 → 内部下标
            Map<String, Integer> indexes = new HashMap<>();

            // 2. 使用标准库 Queue 进行 BFS
            Queue<Integer> queue = new LinkedList<>();

            // 初始化根节点（下标 0）
            parents.add(-1);
            depths.add(0);
            indexes.put(tokens[0], 0);
            queue.offer(0);

            int tokenIndex = 1;   // 从第 2 个 token 开始

            // 3. 按层序逐个读取子节点，构建父子关系和深度
            while (!queue.isEmpty() && tokenIndex < tokens.length) {
                int current = queue.poll();

                // 每个节点最多两个孩子：left、right
                for (int side = 0; side < 2 && tokenIndex < tokens.length; side++) {
                    String child = tokens[tokenIndex++];

                    // 空节点跳过（但仍消耗一个 token）
                    if (child.isEmpty() || child.equals("null"))
                        continue;

                    // 出现重复值 → 非法
                    if (indexes.containsKey(child))
                        return -1;

                    int index = parents.size();          // 新节点的内部下标
                    indexes.put(child, index);
                    parents.add(current);                // 记录父节点
                    depths.add(depths.get(current) + 1); // 记录深度
                    queue.offer(index);                  // 入队，等待处理其孩子
                }
            }

            // 4. 起点或终点不存在 → 非法
            if (!indexes.containsKey(fromValue) || !indexes.containsKey(toValue))
                return -1;

            // 5. 求两节点最短路径（跳跃次数）
            int left  = indexes.get(fromValue);
            int right = indexes.get(toValue);
            int jumps = 0;

            // 5.1 深度对齐
            while (depths.get(left) > depths.get(right)) {
                left = parents.get(left);
                jumps++;
            }
            while (depths.get(right) > depths.get(left)) {
                right = parents.get(right);
                jumps++;
            }

            // 5.2 同时上移到 LCA
            while (left != right) {
                left  = parents.get(left);
                right = parents.get(right);
                jumps += 2;
            }

            return jumps;
        }
    }

    public static void main(String[] args) {
        Solution2 solution = new Solution2();
        String treeLevelOrder = "a,b,c";
        String fromValue = "b";
        String toValue = "c";

        System.out.println(solution.getMinJump(treeLevelOrder,fromValue,toValue));
    }

}
