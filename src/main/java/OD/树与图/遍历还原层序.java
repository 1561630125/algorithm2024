package OD.树与图;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 13:37
 */
public class 遍历还原层序 {
    class Solution {
        /**
         * 根据后序遍历（postorder）和中序遍历（inorder）重建二叉树，
         * 并以「广度优先遍历」（层序遍历）的顺序返回节点字符。
         * <p>
         * 若输入不合法（如 null、长度不一致、字符集不匹配、无法构成合法二叉树），
         * 则返回空字符串 ""。
         */
        String breadthFirstTraversal(String postorder, String inorder) {
            // 1. 基本合法性检查：null 或长度不一致直接返回空串
            if (postorder == null || inorder == null || postorder.length() != inorder.length())
                return "";

            int n = postorder.length();

            // 2. 用计数数组校验两个字符串的字符多重集是否完全相同
            //    这里假定字符为 16 位 char（0~65535），用 counts 同时统计：
            //    postorder 中的字符 +1，inorder 中的字符 -1，
            //    最终所有计数都应为 0。
            //    若某个字符在 postorder 中出现超过一次，直接判定非法（因为二叉树中节点应唯一）。
            int[] counts = new int[65536];
            for (int index = 0; index < n; index++) {
                char post = postorder.charAt(index), inside = inorder.charAt(index);
                if (++counts[post] > 1)
                    return "";
                counts[inside]--;
            }
            for (int value : counts)
                if (value != 0)
                    return "";

            // 3. 用数组模拟队列进行广度优先遍历（层序遍历）。
            //    每个队列元素存储当前子树在 postorder 和 inorder 中的起始下标及长度：
            //    queue[i] = {postStart, inStart, length}
            int[][] queue = new int[n][3];
            int head = 0, tail = 0;

            // 整棵树入队（若 n == 0，则队列为空，直接返回空串）
            if (n > 0)
                queue[tail++] = new int[]{0, 0, n};

            StringBuilder result = new StringBuilder();

            // 4. 逐层处理队列中的子树
            while (head < tail) {
                int postStart = queue[head][0];
                int inStart = queue[head][1];
                int length = queue[head++][2];

                // 后序遍历的最后一个字符即为当前子树的根
                char root = postorder.charAt(postStart + length - 1);

                // 在中序遍历区间 [inStart, inStart + length) 中查找根的位置
                int split = inorder.indexOf(root, inStart);

                // 若根不在合法区间内，说明输入无法构成合法二叉树
                if (split < inStart || split >= inStart + length)
                    return "";

                // 将根加入层序结果
                result.append(root);

                // 计算左右子树长度
                int leftLength = split - inStart;
                int rightLength = length - leftLength - 1;

                // 左子树入队（后序起点不变，中序起点不变）
                if (leftLength > 0)
                    queue[tail++] = new int[]{postStart, inStart, leftLength};

                // 右子树入队
                // 后序起点 = postStart + leftLength（跳过左子树部分）
                // 中序起点 = split + 1（跳过根）
                if (rightLength > 0)
                    queue[tail++] = new int[]{postStart + leftLength, split + 1, rightLength};
            }

            // 5. 返回层序遍历结果
            return result.toString();
        }
    }


    class Solution2 {
        /**
         * 根据后序遍历（postorder）和中序遍历（inorder）重建二叉树，
         * 并以「广度优先遍历」（层序遍历）的顺序返回节点字符。
         * <p>
         * 若输入不合法（如 null、长度不一致、字符集不匹配、无法构成合法二叉树），
         * 则返回空字符串 ""。
         */
        String breadthFirstTraversal(String postorder, String inorder) {
            // 1. 基本合法性检查：null 或长度不一致直接返回空串
            if (postorder == null || inorder == null || postorder.length() != inorder.length())
                return "";

            int n = postorder.length();

            // 2. 用计数数组校验两个字符串的字符多重集是否完全相同
            int[] counts = new int[65536];
            for (int index = 0; index < n; index++) {
                char post = postorder.charAt(index), inside = inorder.charAt(index);
                if (++counts[post] > 1)
                    return "";
                counts[inside]--;
            }
            for (int value : counts)
                if (value != 0)
                    return "";

            // 3. 使用 Java 标准库的 Queue 进行广度优先遍历。
            //    每个队列元素存储当前子树在 postorder 和 inorder 中的起始下标及长度：
            //    {postStart, inStart, length}
            Queue<int[]> queue = new LinkedList<>();

            // 整棵树入队（若 n == 0，则队列为空，直接返回空串）
            if (n > 0)
                queue.offer(new int[]{0, 0, n});

            StringBuilder result = new StringBuilder();

            // 4. 逐层处理队列中的子树
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int postStart = cur[0];
                int inStart = cur[1];
                int length = cur[2];

                // 后序遍历的最后一个字符即为当前子树的根
                char root = postorder.charAt(postStart + length - 1);

                // 在中序遍历区间 [inStart, inStart + length) 中查找根的位置
                int split = inorder.indexOf(root, inStart);

                // 若根不在合法区间内，说明输入无法构成合法二叉树
                if (split < inStart || split >= inStart + length)
                    return "";

                // 将根加入层序结果
                result.append(root);

                // 计算左右子树长度
                int leftLength = split - inStart;
                int rightLength = length - leftLength - 1;

                // 左子树入队（后序起点不变，中序起点不变）
                if (leftLength > 0)
                    queue.offer(new int[]{postStart, inStart, leftLength});

                // 右子树入队
                // 后序起点 = postStart + leftLength（跳过左子树部分）
                // 中序起点 = split + 1（跳过根）
                if (rightLength > 0)
                    queue.offer(new int[]{postStart + leftLength, split + 1, rightLength});
            }

            // 5. 返回层序遍历结果
            return result.toString();
        }
    }


    class Solution3 {
        /**
         * 根据后序遍历（postorder）和中序遍历（inorder）重建二叉树，
         * 并以「广度优先遍历」（层序遍历）的顺序返回节点字符。
         */
        String breadthFirstTraversal(String postorder, String inorder) {
            // 1. 基本合法性检查
            if (postorder == null || inorder == null || postorder.length() != inorder.length())
                return "";

            int n = postorder.length();

            // 2. 字符多重集校验
            int[] counts = new int[65536];
            for (int index = 0; index < n; index++) {
                char post = postorder.charAt(index), inside = inorder.charAt(index);
                if (++counts[post] > 1)
                    return "";
                counts[inside]--;
            }
            for (int value : counts)
                if (value != 0)
                    return "";

            // 3. 用 List<List<Character>> 按层收集字符
            List<List<Character>> levels = new ArrayList<>();

            // 4. 递归处理整棵树
            if (n > 0)
                build(postorder, inorder, 0, 0, n, 0, levels);

            // 5. 按层拼接结果
            StringBuilder result = new StringBuilder();
            for (List<Character> level : levels)
                for (char c : level)
                    result.append(c);

            return result.toString();
        }

        /**
         * 递归处理一棵子树。
         *
         * @param postorder 后序字符串
         * @param inorder   中序字符串
         * @param postStart 当前子树在后序中的起始下标
         * @param inStart   当前子树在中序中的起始下标
         * @param length    当前子树节点数
         * @param depth     当前子树根所在的层（从 0 开始）
         * @param levels    按层收集字符的结果容器
         * @return 是否合法；false 表示无法构成合法二叉树
         */
        private boolean build(String postorder, String inorder,
                              int postStart, int inStart, int length,
                              int depth, List<List<Character>> levels) {
            // 空子树，合法
            if (length <= 0)
                return true;

            // 后序最后一个字符 = 当前子树的根
            char root = postorder.charAt(postStart + length - 1);

            // 在中序区间中查找根的位置
            int split = inorder.indexOf(root, inStart);
            if (split < inStart || split >= inStart + length)
                return false;

            // 把根加入对应层（若该层还没创建，先创建）
            while (levels.size() <= depth)
                levels.add(new ArrayList<>());
            levels.get(depth).add(root);

            // 左右子树长度
            int leftLength = split - inStart;
            int rightLength = length - leftLength - 1;

            // 递归左子树（深度 +1）
            if (!build(postorder, inorder,
                    postStart, inStart, leftLength,
                    depth + 1, levels))
                return false;

            // 递归右子树（深度 +1）
            // 后序起点 = postStart + leftLength
            // 中序起点 = split + 1
            if (!build(postorder, inorder,
                    postStart + leftLength, split + 1, rightLength,
                    depth + 1, levels))
                return false;

            return true;
        }
    }

}
