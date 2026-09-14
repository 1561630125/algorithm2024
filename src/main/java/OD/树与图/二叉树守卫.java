package OD.树与图;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 16:20
 */
public class 二叉树守卫 {

    class TreeNode {
        int id;
        long val;
        TreeNode left, right;
        TreeNode parent;

        TreeNode(int id, long val) {
            this.id = id;
            this.val = val;
        }
    }

    public class Solution {

        public int minimumFogCost(TreeNode root) {
            // visit(root) 返回一个长度为 3 的 long 数组 state
            // state[0] / state[1] / state[2] 分别表示当前子树在三种状态下的最小代价
            long[] state = visit(root);

            // 根节点没有父节点约束，所以最终答案取 state[1] 和 state[2] 的较小值
            // 注意：一般不会取 state[0]，因为 state[0] 表示“当前节点不选，且需要父节点选”的情况
            return (int) Math.min(state[1], state[2]);
        }

        /**
         * 返回当前子树在三种状态下的最小代价：
         *
         * state[0]：当前节点 node 不选，并且 node 的父节点必须选。
         *           也就是说，node 自己不选，但它的子节点们可以自由选择，
         *           因为 node 不选不会影响子节点是否必须被父节点覆盖。
         *
         * state[1]：当前节点 node 选，并且 node 已经被自己覆盖。
         *           此时 node 的子节点可以被 node 覆盖，因此子节点可以选择不选。
         *
         * state[2]：当前节点 node 不选，但它必须被某个子节点覆盖。
         *           也就是说，node 自己不选，所以必须至少有一个子节点选，
         *           由这个子节点来覆盖 node。
         */
        private long[] visit(TreeNode node) {
            // 用一个足够大的数表示“无穷大”，避免加法溢出
            long infinity = Long.MAX_VALUE / 8;

            // 空节点：
            // state[0] = 0：空节点不选，不需要代价
            // state[1] = 0：空节点“选”没有意义，但为了转移方便，设为 0
            // state[2] = infinity：空节点不可能被某个子节点覆盖，因为根本没有子节点
            if (node == null)
                return new long[] { 0, 0, infinity };

            // 递归处理左右子树
            long[] left = visit(node.left), right = visit(node.right);

            // 当前节点的三种状态转移
            return new long[] {
                    // state[0]：当前节点 node 不选，且父节点必须选。
                    // 因为 node 不选，所以 node 不需要被子节点覆盖；
                    // 左右子节点都只需要考虑“父节点已经选了”的情况，
                    // 也就是子节点可以处于 state[1] 或 state[2]？
                    //
                    // 这里代码写的是 left[1] + right[1]。
                    // 含义是：node 不选，但 node 的父节点会选；
                    // 此时 node 的子节点不能指望 node 来覆盖它们，
                    // 所以子节点必须自己选，或者由它们的子节点覆盖。
                    //
                    // 但代码只取了 left[1] + right[1]，
                    // 说明这个 DP 的 state[0] 定义可能是：
                    // “当前节点不选，并且当前节点已经被父节点覆盖”，
                    // 因此子节点不能再依赖当前节点，必须自己解决覆盖问题。
                    //
                    // 如果子节点也不选，它就需要靠它的子节点覆盖，
                    // 对应子节点的 state[2]。
                    // 所以严格来说，这里可能需要写成：
                    // Math.min(left[1], left[2]) + Math.min(right[1], right[2])
                    // 下面先按原代码注释，并指出这个点。
                    left[1] + right[1],

                    // state[1]：当前节点 node 选。
                    // node 选了之后，可以覆盖它的左右子节点。
                    // 对于左子节点：
                    //   - 如果左子节点不选，它可以被 node 覆盖，对应 left[1]？还是 left[0]？
                    //   - 如果左子节点选，对应 left[1] 或 left[2]？
                    //
                    // 原代码：
                    // left[2] + Math.min(right[1], right[2])
                    // 或
                    // right[2] + Math.min(left[1], left[2])
                    //
                    // 这个式子的含义是：
                    // node 选，但 node 不一定要覆盖所有子节点；
                    // 需要保证 node 的左右子节点中，至少有一个子节点处于 state[2]，
                    // 也就是“不选但被子节点覆盖”。
                    //
                    // 为什么？因为 state[1] 可能表示：
                    // “当前节点选，但当前节点必须被它的某个子节点覆盖”？
                    // 这和前面 state[1] 的定义有冲突。
                    //
                    // 更合理的解释：
                    // state[1]：当前节点选，并且当前节点被自己覆盖。
                    // 此时子节点可以被当前节点覆盖，所以子节点可以处于 state[0]。
                    //
                    // 但原代码没有用 state[0]，说明状态定义可能不同。
                    //
                    // 先按代码本身解释：
                    // 当前节点选，并且要求它的某个子节点处于 state[2]，
                    // 即某个子节点不选但被其子节点覆盖。
                    Math.min(
                            left[2] + Math.min(right[1], right[2]),
                            right[2] + Math.min(left[1], left[2])
                    ),

                    // state[2]：当前节点 node 不选，但必须被某个子节点覆盖。
                    // 因此 node 的左右子节点至少要有一个选。
                    // 这里代码写的是：
                    // node.val + min(left[0..2]) + min(right[0..2])
                    //
                    // 这说明 state[2] 其实是“当前节点选”的状态？
                    // 因为加上了 node.val。
                    //
                    // 所以这里状态命名和实际含义可能反了。
                    //
                    // 按代码看：
                    // 这个状态表示当前节点被选中，代价加上 node.val；
                    // 左右子节点可以任选状态，取最小值。
                    node.val
                            + Math.min(left[0], Math.min(left[1], left[2]))
                            + Math.min(right[0], Math.min(right[1], right[2])),
            };
        }
    }



    public class Solution5 {

        // 三种状态的含义（数组下标）：
        // state[0] = 当前节点【靠父节点】罩住时的最小代价（自己不选，父节点选）
        // state[1] = 当前节点【靠某个孩子】罩住时的最小代价（自己不选，某个孩子选）
        // state[2] = 当前节点【靠自己】罩住时的最小代价（自己选）
        private static final int BY_PARENT  = 0;
        private static final int BY_CHILD   = 1;
        private static final int BY_SELF    = 2;

        private static final long INF = Long.MAX_VALUE / 8;  // 表示"不可能"

        public int minimumFogCost(TreeNode root) {
            long[] result = solve(root);
            // 根节点没有父节点，所以只能靠"孩子"或"自己"
            return (int) Math.min(result[BY_CHILD], result[BY_SELF]);
        }

        /**
         * 返回一个长度为 3 的数组，表示以 node 为根的子树在三种状态下的最小代价：
         *   [0] = 靠父节点罩住
         *   [1] = 靠某个孩子罩住
         *   [2] = 靠自己罩住
         */
        private long[] solve(TreeNode node) {
            // 空节点：不需要任何代价，但"靠自己"是不可能的
            if (node == null) {
                return new long[] { 0, 0, INF };
            }

            // 先递归处理左右子树（后序遍历）
            long[] left  = solve(node.left);
            long[] right = solve(node.right);

            // ---------- 状态 0：靠父节点罩住 ----------
            // 自己不选，所以孩子不能靠"我"来罩，只能各自解决自己
            // 孩子要么"靠自己"，要么"靠它的孩子"，取较小值
            long costByParent = Math.min(left[BY_CHILD],  left[BY_SELF])
                    + Math.min(right[BY_CHILD], right[BY_SELF]);

            // ---------- 状态 1：靠某个孩子罩住 ----------
            // 自己不选，但要求"至少一个孩子选自己"来罩住我
            // 情况 A：左孩子选自己（BY_SELF），右孩子随意
            long caseA = left[BY_SELF] + Math.min(right[BY_CHILD], right[BY_SELF]);
            // 情况 B：右孩子选自己（BY_SELF），左孩子随意
            long caseB = right[BY_SELF] + Math.min(left[BY_CHILD],  left[BY_SELF]);
            long costByChild = Math.min(caseA, caseB);

            // ---------- 状态 2：靠自己罩住 ----------
            // 自己选，付出 node.val，孩子三种状态随便取最小
            long costBySelf = node.val
                    + Math.min(left[BY_PARENT], Math.min(left[BY_CHILD],  left[BY_SELF]))
                    + Math.min(right[BY_PARENT], Math.min(right[BY_CHILD], right[BY_SELF]));

            return new long[] { costByParent, costByChild, costBySelf };
        }
    }



    public class SolutionBruteForce {

        public long minimumFogCost(TreeNode root) {
            List<TreeNode> nodes = new ArrayList<>();
            collect(root, nodes);

            int n = nodes.size();
            long ans = Long.MAX_VALUE;

            // 枚举所有选点方案
            for (int mask = 0; mask < (1 << n); mask++) {
                boolean[] selected = new boolean[n];
                long cost = 0;

                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        selected[i] = true;
                        cost += nodes.get(i).val;
                    }
                }

                if (isAllCovered(nodes, selected)) {
                    ans = Math.min(ans, cost);
                }
            }

            return ans;
        }

        private void collect(TreeNode node, List<TreeNode> nodes) {
            if (node == null) return;
            node.id = nodes.size();
            nodes.add(node);
            collect(node.left, nodes);
            collect(node.right, nodes);
        }

        private boolean isAllCovered(List<TreeNode> nodes, boolean[] selected) {
            int n = nodes.size();

            for (int i = 0; i < n; i++) {
                TreeNode node = nodes.get(i);

                boolean covered = selected[i];

                if (!covered && node.parent != null) {
                    covered = selected[node.parent.id];
                }

                if (!covered && node.left != null) {
                    covered = selected[node.left.id];
                }

                if (!covered && node.right != null) {
                    covered = selected[node.right.id];
                }

                if (!covered) {
                    return false;
                }
            }

            return true;
        }
    }

    public class SolutionGreedy {

        public long minimumFogCost(TreeNode root) {
            if (root == null) return 0;

            // 1. 建图，并记录父节点
            Map<TreeNode, List<TreeNode>> graph = new HashMap<>();
            Map<TreeNode, TreeNode> parent = new HashMap<>();
            List<TreeNode> nodes = new ArrayList<>();

            buildGraph(root, null, graph, parent, nodes);

            // 2. 按深度从大到小排序
            Map<TreeNode, Integer> depth = new HashMap<>();
            computeDepth(root, 0, depth);

            nodes.sort((a, b) -> depth.get(b) - depth.get(a));

            // 3. 贪心覆盖
            Set<TreeNode> covered = new HashSet<>();
            long ans = 0;

            for (TreeNode u : nodes) {
                if (covered.contains(u)) continue;

                TreeNode p = parent.get(u);
                if (p == null) {
                    p = u; // 根节点没父节点，只能选自己
                }

                ans += p.val;

                // 选 p，覆盖 p 自己、父节点、所有子节点
                covered.add(p);

                TreeNode grandParent = parent.get(p);
                if (grandParent != null) {
                    covered.add(grandParent);
                }

                for (TreeNode neighbor : graph.getOrDefault(p, new ArrayList<>())) {
                    covered.add(neighbor);
                }
            }

            return ans;
        }

        private void buildGraph(TreeNode node,
                                TreeNode p,
                                Map<TreeNode, List<TreeNode>> graph,
                                Map<TreeNode, TreeNode> parent,
                                List<TreeNode> nodes) {
            if (node == null) return;

            parent.put(node, p);
            nodes.add(node);
            graph.putIfAbsent(node, new ArrayList<>());

            if (p != null) {
                graph.get(node).add(p);
                graph.putIfAbsent(p, new ArrayList<>());
                graph.get(p).add(node);
            }

            if (node.left != null) {
                graph.get(node).add(node.left);
                graph.putIfAbsent(node.left, new ArrayList<>());
                graph.get(node.left).add(node);
            }

            if (node.right != null) {
                graph.get(node).add(node.right);
                graph.putIfAbsent(node.right, new ArrayList<>());
                graph.get(node.right).add(node);
            }

            buildGraph(node.left, node, graph, parent, nodes);
            buildGraph(node.right, node, graph, parent, nodes);
        }

        private void computeDepth(TreeNode node, int d, Map<TreeNode, Integer> depth) {
            if (node == null) return;
            depth.put(node, d);
            computeDepth(node.left, d + 1, depth);
            computeDepth(node.right, d + 1, depth);
        }
    }

}
