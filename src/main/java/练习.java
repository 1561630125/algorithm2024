import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-17 9:52
 */
public class 练习 {

    // 归并排序
    class so1 {
        void merge(int[] arr, int left, int mid, int right, int[] temp) {
            int k = 0;
            while (left <= mid && mid <= right) {
                if (arr[left] <= arr[right]) {
                    temp[k++] = arr[left++];
                } else {
                    temp[k++] = arr[right++];
                }
            }
            while (left <= mid) {
                temp[k++] = arr[left++];
            }
        }
    }

    // 最大公约数
    class so2 {
        int gcd(int a, int b) {
            while (b != 0) {
                int t = a % b;
                a = b;
                b = t;
            }

            return a == 0 ? 1 : a;
        }
    }

    // 排序
    class so3 {
        void sort() {
            Integer[] arr = new Integer[10];
            Arrays.sort(arr, (a, b) -> {
                return Integer.compare(a, b);
            });
            Arrays.sort(arr, Integer::compare);

            int[][] brr = new int[10][10];
            Arrays.sort(brr, (a, b) -> {
                return a[0] - b[0];
            });
            Arrays.sort(brr, Comparator.comparingInt(a -> a[0]));

            int[] crr = new int[10];
            Arrays.sort(brr, (a, b) -> {
                return a[0] - b[0];
            });
            Arrays.sort(brr, Comparator.comparingInt(a -> a[0]));

            int[][] copy = new int[brr.length][];
            for (int i = 0; i < brr.length; i++) {
                copy[i] = Arrays.copyOf(brr[i], brr[i].length);
            }
            int[][] copy1 = Arrays.stream(brr).map(int[]::clone).toArray(int[][]::new);
            int[][] copy2 = Arrays.stream(brr).map(row -> Arrays.copyOf(row, row.length)).toArray(int[][]::new);

            // 区间重叠
            int[] left = new int[2];
            int[] right = new int[2];
            if (Math.max(left[0], right[0]) < Math.min(left[1], right[1])) {
                /*不重叠: e1 <= s2 || e2 <= s1
                重叠:   e1 >  s2 && e2 >  s1*/
                System.out.println(true);
            }

        }
    }

    // 双端队列
    class so4 {
        void stack() {
            ArrayDeque<Integer> stack = new ArrayDeque<>();

            stack.offer(1);
            stack.pop();

            stack.addFirst(2);
            stack.offerFirst(3);

            stack.addLast(2);
            stack.addLast(3);

            stack.pollFirst();
            stack.pollLast();

            Integer peekFirst = stack.peekFirst();
            Integer peekLast = stack.peekLast();

        }
    }

    // 向量旋转
    class so7 {
        int x1;
        int y1;
        int x2;
        int y2;

        int dirX = x1 - x2;
        int dirY = y1 - y2;

        int x3 = x1 + dirY;
        int y3 = y1 - dirX;
        int x4 = x1 - dirY;
        int y4 = x2 + dirX;
    }


    // 进制转换，短除法
    class so8 {

        void div() {
            List<Integer> dig = new ArrayList<>();
            dig.add(2);
            dig.add(5);
            dig.add(5);

            String sourceDigits = "0123456789";
            String targetDigits = "0123456789ABCDEF";
            StringBuilder answer = new StringBuilder();
            while (!dig.isEmpty()) {
                List<Integer> qie = new ArrayList<>();
                int remind = 0;
                for (int i = 0; i < dig.size(); i++) {
                    int value = remind * sourceDigits.length() + dig.get(i);
                    int div = value / targetDigits.length();

                    if (!qie.isEmpty() || div > 0) {
                        qie.add(div);
                    }

                    remind = value % targetDigits.length();
                }
                answer.append(targetDigits.charAt(remind));
                dig = qie;
            }
        }

        /**
         * 将时间字符串 "HH:MM" 转换为当天的分钟数（从00:00开始计算）。
         *
         * @param time 时间字符串，格式为 "HH:MM"，例如 "08:30"
         * @return 对应的分钟数（0-1439）
         */
        int mintue(String time) {
            String[] split = time.split(":");
            return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
        }
    }

    // 线段树
    class so9 {
        private int[] tree;  // 线段树数组
        private int n;       // 原始数组长度

        void build(int left, int right, int[] arr, int node) {
            if (left == right) {
                tree[node] = arr[left];
            }

            int mid = left + (right - left) / 2;
            int leftnode = 2 * node + 1;
            int rightnode = 2 * node + 2;
            build(left, mid, arr, leftnode);
            build(mid + 1, right, arr, rightnode);
            tree[node] = tree[leftnode] + tree[rightnode];
        }

        void update(int val, int node, int left, int right, int index) {
            if (left == right) {
                tree[node] = val;
            }
            int mid = left + (right - left) / 2;
            int leftnode = 2 * node + 1;
            int rightnode = 2 * node + 2;

            if (index <= mid) {
                update(val, leftnode, left, mid, index);
            } else {
                update(val, rightnode, mid + 1, right, index);
            }
            tree[node] = tree[leftnode] + tree[rightnode];
        }

        int query(int node, int l, int r, int L, int R) {
            if (L <= l && r <= R) {
                return tree[node];
            }
            if (l > r || r < L) {
                return 0;
            }

            int mid = l + (l - r) / 2;
            int leftnode = 2 * node + 1;
            int rightnode = 2 * node + 2;

            int leftval = query(leftnode, l, mid, L, R);
            int rightval = query(rightnode, mid + 1, r, L, R);
            return leftval + rightval;
        }
    }

    // 单调栈
    class so10 {
        // TODO 补充更多应用场景
        // 下一个更小元素
        int[] nextSmaller(int[] nums) {
            int n = nums.length;
            int[] res = new int[n];
            Arrays.fill(res, -1);
            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i < n; i++) {

                while (!stack.isEmpty() && nums[i] < nums[stack.peek()]) {
                    Integer pop = stack.pop();
                    res[pop] = nums[i];
                }

                stack.push(i);
            }
            return res;
        }

        //前序验树
        boolean isValidBSTPreorder(int[] preorder) {
            Set<Integer> seen = new HashSet<>();
            Deque<Integer> stack = new ArrayDeque<>();
            long lower = Long.MIN_VALUE;

            for (int value : preorder) {
                if (!seen.add(value) || value <= lower) return false;
                while (!stack.isEmpty() && value > stack.peek())
                    lower = stack.pop();
                stack.push(value);
            }
            return true;
        }

        // 滑动窗口最大值
        public int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            int[] res = new int[n - k + 1];
            Deque<Integer> deque = new ArrayDeque<>();   // 单调递减

            for (int i = 0; i < n; i++) {
                while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i])
                    deque.pollLast();
                deque.offerLast(i);
                if (deque.peekFirst() <= i - k) deque.pollFirst();
                if (i >= k - 1) res[i - k + 1] = nums[deque.peekFirst()];
            }
            return res;
        }
    }

    // 链表是否有环
    class so11 {
        class ListNode {
            int val;
            ListNode next;

            ListNode(int x) {
                val = x;
                next = null;
            }
        }

        public ListNode cycle(ListNode head) {
            ListNode fast = head;
            ListNode slow = head;

            while (fast != null && slow != null) {
                slow = slow.next.next;
                fast = fast.next;

                if (slow == fast) {
                    slow = head;
                    while (fast != head) {
                        slow = slow.next;
                        fast = fast.next;
                    }
                    return slow;
                }
            }
            return null;
        }
    }

    // dfs
    class so12 {
        class TreeNode {
            int val;
            TreeNode left, right;

            TreeNode(int val) {
                this.val = val;
            }
        }

        // 树的前序遍历 -- 迭代
        List<Integer> preOrder(TreeNode root) {
            ArrayDeque<TreeNode> stack = new ArrayDeque<>();
            List<Integer> res = new ArrayList<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode pop = stack.pop();
                res.add(pop.val);

                if (root.right != null) stack.push(root.right);
                if (root.left != null) stack.push(root.left);
            }

            return res;
        }

        // 树的前序遍历 -- 递归
        void preOrder(TreeNode root, List<Integer> res) {
            if (root == null) return;
            res.add(root.val);
            preOrder(root.left, res);
            preOrder(root.right, res);
        }

        // 全排列
        public  List<List<Integer>> permute(int[] nums) {
            boolean[] used = new boolean[nums.length];
            List<List<Integer>> result = new ArrayList<>();
            backtrack(nums, new ArrayList<>(),used,result);
            return result;
        }

        private void backtrack(int[] nums, List<Integer> path,
                               boolean[] used, List<List<Integer>> result) {
            if (path.size() == nums.length) {
                result.add(new ArrayList<>(path));
            }

            for(int i = 0; i < nums.length; i++) {
                if (used[i]) continue;
                used[i] = true;
                path.add(nums[i]);
                backtrack(nums,path,used,result);
                used[i] = false;
                path.remove(path.size() - 1);
            }
        }

        // 组合
        public  List<List<Integer>> combine(int n, int k) {
            List<List<Integer>> result = new ArrayList<>();
            combine(1, n, k, new ArrayList<>(), result);
            return result;
        }

        private void combine(int start, int n, int k,
                               List<Integer> path, List<List<Integer>> result) {
            if (path.size() == k) result.add(new ArrayList<>(path));

            for(int i = start; i <= n - (k - path.size()) + 1; i++) {
                path.add(i);
                combine(i + 1, n ,k,path,result);
                path.remove(path.size() - 1);
            }
        }
    }

    // bfs + dir
    class so13{

    }

    // 并查集
    class so14{

    }

    //背包问题
    class so15{

    }

    // 滑动窗口
    class so16{

    }

    // 二分查找
    class so17{

    }

    // 位运算 & 掩码
    class so18{

    }

    //前缀和 & 后缀和
    class so19{

    }

}
