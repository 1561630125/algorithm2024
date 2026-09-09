package OD.基础与模拟.线段树;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:56
 */
public class SegmentTree {

    /**
     * 线段树 - 区间求和，单点更新
     */
    static class SegmentTreeSum {
        private int[] tree;  // 线段树数组
        private int n;       // 原始数组长度

        /**
         * 构造函数：构建线段树
         */
        public SegmentTreeSum(int[] arr) {
            this.n = arr.length;
            tree = new int[4 * n];  // 4倍空间足够
            build(arr, 0, 0, n - 1);
        }

        /**
         * 递归构建线段树
         * @param arr 原始数组
         * @param node 当前节点索引
         * @param left 当前区间左边界
         * @param right 当前区间右边界
         */
        private void build(int[] arr, int node, int left, int right) {
            if (left == right) {
                // 叶子节点：存储单个元素
                tree[node] = arr[left];
                return;
            }

            int mid = left + (right - left) / 2;
            int leftChild = node * 2 + 1;
            int rightChild = node * 2 + 2;

            // 递归构建左右子树
            build(arr, leftChild, left, mid);
            build(arr, rightChild, mid + 1, right);

            // 当前节点的值 = 左右子节点之和
            tree[node] = tree[leftChild] + tree[rightChild];
        }

        /**
         * 单点更新：将 index 位置的值修改为 value
         */
        public void update(int index, int value) {
            update(0, 0, n - 1, index, value);
        }

        private void update(int node, int left, int right, int index, int value) {
            if (left == right) {
                // 找到叶子节点，直接更新
                tree[node] = value;
                return;
            }

            int mid = left + (right - left) / 2;
            int leftChild = node * 2 + 1;
            int rightChild = node * 2 + 2;

            // 根据 index 决定去左子树还是右子树
            if (index <= mid) {
                update(leftChild, left, mid, index, value);
            } else {
                update(rightChild, mid + 1, right, index, value);
            }

            // 更新父节点
            tree[node] = tree[leftChild] + tree[rightChild];
        }

        /**
         * 区间查询：查询 [queryLeft, queryRight] 的和
         */
        public int query(int queryLeft, int queryRight) {
            return query(0, 0, n - 1, queryLeft, queryRight);
        }

        private int query(int node, int left, int right, int queryLeft, int queryRight) {
            // 当前区间完全在查询范围内
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }

            // 当前区间与查询范围无交集
            if (right < queryLeft || left > queryRight) {
                return 0;
            }

            // 部分重叠：递归查询左右子树
            int mid = left + (right - left) / 2;
            int leftChild = node * 2 + 1;
            int rightChild = node * 2 + 2;

            int leftSum = query(leftChild, left, mid, queryLeft, queryRight);
            int rightSum = query(rightChild, mid + 1, right, queryLeft, queryRight);

            return leftSum + rightSum;
        }
    }


    /**
     * 线段树 - 区间求和，区间更新（使用懒标记）
     */
    static class SegmentTreeLazy {
        private long[] tree;    // 线段树数组
        private long[] lazy;    // 懒标记数组
        private int n;

        public SegmentTreeLazy(int[] arr) {
            this.n = arr.length;
            tree = new long[4 * n];
            lazy = new long[4 * n];
            build(arr, 0, 0, n - 1);
        }

        private void build(int[] arr, int node, int left, int right) {
            if (left == right) {
                tree[node] = arr[left];
                return;
            }

            int mid = left + (right - left) / 2;
            build(arr, node * 2 + 1, left, mid);
            build(arr, node * 2 + 2, mid + 1, right);
            tree[node] = tree[node * 2 + 1] + tree[node * 2 + 2];
        }

        /**
         * 下推懒标记
         */
        private void pushDown(int node, int left, int right) {
            if (lazy[node] != 0) {
                int mid = left + (right - left) / 2;
                int leftChild = node * 2 + 1;
                int rightChild = node * 2 + 2;

                // 将懒标记传递给子节点
                lazy[leftChild] += lazy[node];
                lazy[rightChild] += lazy[node];

                // 更新子节点的值
                tree[leftChild] += lazy[node] * (mid - left + 1);
                tree[rightChild] += lazy[node] * (right - mid);

                // 清除当前节点的懒标记
                lazy[node] = 0;
            }
        }

        /**
         * 区间更新：将 [updateLeft, updateRight] 范围内的所有元素增加 delta
         */
        public void updateRange(int updateLeft, int updateRight, int delta) {
            updateRange(0, 0, n - 1, updateLeft, updateRight, delta);
        }

        private void updateRange(int node, int left, int right,
                                 int updateLeft, int updateRight, int delta) {
            // 当前区间完全在更新范围内
            if (updateLeft <= left && right <= updateRight) {
                tree[node] += (long) delta * (right - left + 1);
                lazy[node] += delta;  // 添加懒标记
                return;
            }

            // 无交集
            if (right < updateLeft || left > updateRight) {
                return;
            }

            // 部分重叠：需要下推懒标记
            pushDown(node, left, right);

            int mid = left + (right - left) / 2;
            updateRange(node * 2 + 1, left, mid, updateLeft, updateRight, delta);
            updateRange(node * 2 + 2, mid + 1, right, updateLeft, updateRight, delta);

            tree[node] = tree[node * 2 + 1] + tree[node * 2 + 2];
        }

        /**
         * 区间查询
         */
        public long queryRange(int queryLeft, int queryRight) {
            return queryRange(0, 0, n - 1, queryLeft, queryRight);
        }

        private long queryRange(int node, int left, int right,
                                int queryLeft, int queryRight) {
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }

            if (right < queryLeft || left > queryRight) {
                return 0;
            }

            // 查询前下推懒标记
            pushDown(node, left, right);

            int mid = left + (right - left) / 2;
            long leftSum = queryRange(node * 2 + 1, left, mid, queryLeft, queryRight);
            long rightSum = queryRange(node * 2 + 2, mid + 1, right, queryLeft, queryRight);

            return leftSum + rightSum;
        }
    }


    /**
     * 通用线段树 - 支持自定义合并操作
     */
    static class SegmentTreeGeneric<T> {
        private T[] tree;
        private T[] arr;
        private java.util.function.BinaryOperator<T> merger;
        private int n;

        @SuppressWarnings("unchecked")
        public SegmentTreeGeneric(T[] arr, java.util.function.BinaryOperator<T> merger) {
            this.n = arr.length;
            this.arr = arr;
            this.merger = merger;
            tree = (T[]) new Object[4 * n];
            build(0, 0, n - 1);
        }

        private void build(int node, int left, int right) {
            if (left == right) {
                tree[node] = arr[left];
                return;
            }

            int mid = left + (right - left) / 2;
            build(node * 2 + 1, left, mid);
            build(node * 2 + 2, mid + 1, right);

            // 使用自定义合并操作
            tree[node] = merger.apply(tree[node * 2 + 1], tree[node * 2 + 2]);
        }

        public void update(int index, T value) {
            update(0, 0, n - 1, index, value);
        }

        private void update(int node, int left, int right, int index, T value) {
            if (left == right) {
                tree[node] = value;
                return;
            }

            int mid = left + (right - left) / 2;
            if (index <= mid) {
                update(node * 2 + 1, left, mid, index, value);
            } else {
                update(node * 2 + 2, mid + 1, right, index, value);
            }

            tree[node] = merger.apply(tree[node * 2 + 1], tree[node * 2 + 2]);
        }

        public T query(int queryLeft, int queryRight) {
            return query(0, 0, n - 1, queryLeft, queryRight);
        }

        private T query(int node, int left, int right, int queryLeft, int queryRight) {
            if (queryLeft <= left && right <= queryRight) {
                return tree[node];
            }

            if (right < queryLeft || left > queryRight) {
                return null;
            }

            int mid = left + (right - left) / 2;
            T leftResult = query(node * 2 + 1, left, mid, queryLeft, queryRight);
            T rightResult = query(node * 2 + 2, mid + 1, right, queryLeft, queryRight);

            if (leftResult == null) return rightResult;
            if (rightResult == null) return leftResult;

            return merger.apply(leftResult, rightResult);
        }
    }


    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};

        // ========== 1. 基础版：区间求和 ==========
        SegmentTreeSum st = new SegmentTreeSum(arr);
        System.out.println("区间 [1, 4] 的和: " + st.query(1, 4));  // 3+5+7+9 = 24

        // 单点更新
        st.update(2, 10);  // 将索引2的值改为10
        System.out.println("更新后 [1, 4] 的和: " + st.query(1, 4));  // 3+10+7+9 = 29

        // ========== 2. 升级版：区间更新 ==========
        SegmentTreeLazy stLazy = new SegmentTreeLazy(arr);
        System.out.println("原始 [2, 6] 的和: " + stLazy.queryRange(2, 6));  // 5+7+9+11+13 = 45

        // 区间更新：将 [2, 6] 所有元素 +5
        stLazy.updateRange(2, 6, 5);
        System.out.println("更新后 [2, 6] 的和: " + stLazy.queryRange(2, 6));  // (5+5)+(7+5)+... = 70

        // ========== 3. 通用版：自定义操作 ==========
        Integer[] nums = {3, 1, 4, 1, 5, 9, 2, 6};

        // 求最大值
        SegmentTreeGeneric<Integer> maxTree = new SegmentTreeGeneric<>(
                nums,
                (a, b) -> Math.max(a, b)
        );
        System.out.println("区间 [2, 5] 最大值: " + maxTree.query(2, 5));  // max(4,1,5,9) = 9

        // 求最大公约数
        SegmentTreeGeneric<Integer> gcdTree = new SegmentTreeGeneric<>(
                nums,
                (a, b) -> gcd(a, b)
        );
        System.out.println("区间 [0, 6] 的 GCD: " + gcdTree.query(0, 6));
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }



    class Demo {

      /*  // 自底向上（需要子节点结果）
        // 场景1：计算树的高度
        int getHeight(node) {
            if (node == null) return 0;
            int leftHeight = getHeight(node.left);   // 先递归
            int rightHeight = getHeight(node.right); // 先递归
            return 1 + Math.max(leftHeight, rightHeight); // 后处理
        }

        // 场景2：线段树构建
        build(node) {
            build(left);   // 先递归
            build(right);  // 先递归
            tree[node] = tree[left] + tree[right];  // 后处理
        }

        // 场景3：后序遍历
        postorder(node) {
            postorder(left);   // 先递归
            postorder(right);  // 先递归
            visit(node);       // 后处理
        }

        // 场景4：归并排序
        mergeSort(arr) {
            mergeSort(left);   // 先递归
            mergeSort(right);  // 先递归
            merge(left, right); // 后处理（合并）
        }*/






        /*// 自顶向下（需要父节点信息）
        // 场景1：查找节点
        find(node, target) {
            if (node.value == target) return node;
            if (target < node.value) {
                return find(node.left, target);   // 后递归
            } else {
                return find(node.right, target);  // 后递归
            }
        }

        // 场景2：前序遍历
        preorder(node) {
            visit(node);         // 先处理
            preorder(left);      // 后递归
            preorder(right);     // 后递归
        }

        // 场景3：深度标记
        markDepth(node, depth) {
            node.depth = depth;              // 先处理（父传子）
            markDepth(node.left, depth + 1); // 后递归
            markDepth(node.right, depth + 1);// 后递归
        }*/
    }
}
