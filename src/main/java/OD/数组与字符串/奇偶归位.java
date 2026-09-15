package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:54
 */
public class 奇偶归位 {

    public class Solution {

        /**
         * 把数组 nums 重新排列成：偶数升序 + 奇数升序 交错排列。
         * <p>
         * 排列规则：
         * - 所有偶数按升序排在前面的偶数位置（下标 0, 2, 4, ...）
         * - 所有奇数按升序排在后面的奇数位置（下标 1, 3, 5, ...）
         * <p>
         * 例：nums = [4, 1, 3, 2]
         * 偶数升序: [2, 4]
         * 奇数升序: [1, 3]
         * 交错结果: [2, 1, 4, 3]
         *
         * @param nums 输入数组
         * @return 偶奇交错排列后的数组
         */
        public int[] sortArr(int[] nums) {

            // ---- 1. 把 nums 拆成"偶数列表"和"奇数列表" ----
            java.util.List<Integer> even = new java.util.ArrayList<>();
            java.util.List<Integer> odd = new java.util.ArrayList<>();

            for (int value : nums) {
                // 三元运算符返回列表引用，直接在对应列表上 add
                // value % 2 == 0 → 偶数
                (value % 2 == 0 ? even : odd).add(value);
            }

            // ---- 2. 两个列表各自升序排序 ----
            java.util.Collections.sort(even);
            java.util.Collections.sort(odd);

            // ---- 3. 交错填充结果数组 ----
            // 偶数放 0, 2, 4, ...
            // 奇数放 1, 3, 5, ...
            int[] answer = new int[nums.length];
            for (int index = 0; index < even.size(); index++) {
                answer[index * 2] = even.get(index);   // 偶数位置
                answer[index * 2 + 1] = odd.get(index);    // 奇数位置
            }
            return answer;
        }
    }


    public class Solution2 {
        public int[] sortArr(int[] nums) {

            // ---- 1. 先对整个数组升序排序 ----
            int[] sorted = nums.clone();        // 不改原数组
            java.util.Arrays.sort(sorted);

            // ---- 2. 从已排序的数组里拆出偶数和奇数 ----
            // 因为整体已升序，所以 even 和 odd 也各自升序
            java.util.List<Integer> even = new java.util.ArrayList<>();
            java.util.List<Integer> odd = new java.util.ArrayList<>();

            for (int value : sorted)
                (value % 2 == 0 ? even : odd).add(value);

            // ---- 3. 交错填充 ----
            int[] answer = new int[nums.length];
            for (int index = 0; index < even.size(); index++) {
                answer[index * 2] = even.get(index);
                answer[index * 2 + 1] = odd.get(index);
            }
            return answer;
        }
    }


    public class Solution3 {
        public int[] sortArr(int[] nums) {

            // ---- 1. 排序 ----
            int[] sorted = nums.clone();
            java.util.Arrays.sort(sorted);

            // ---- 2. 双指针：ei 找下一个偶数，oi 找下一个奇数 ----
            int[] answer = new int[sorted.length];
            int ei = 0, oi = 0, pos = 0;

            while (pos < answer.length) {
                // 找下一个偶数
                while (ei < sorted.length && sorted[ei] % 2 != 0) ei++;
                // 找下一个奇数
                while (oi < sorted.length && sorted[oi] % 2 == 0) oi++;

                // 偶数位放偶数
                if (pos % 2 == 0 && ei < sorted.length) {
                    answer[pos++] = sorted[ei++];
                }
                // 奇数位放奇数
                else if (pos % 2 == 1 && oi < sorted.length) {
                    answer[pos++] = sorted[oi++];
                }
                // 对应类型没了，就放另一种（兜底）
                else if (ei < sorted.length) {
                    answer[pos++] = sorted[ei++];
                } else if (oi < sorted.length) {
                    answer[pos++] = sorted[oi++];
                } else {
                    break;
                }
            }
            return answer;
        }
    }


    public class Solution4 {
        public int[] sortArr(int[] nums) {

            // ---- 1. 排序 ----
            int[] sorted = nums.clone();
            java.util.Arrays.sort(sorted);

            // ---- 2. 分别收集偶数和奇数（用数组，避免装箱）----
            int[] even = new int[sorted.length];
            int[] odd = new int[sorted.length];
            int ec = 0, oc = 0;
            for (int v : sorted) {
                if (v % 2 == 0) even[ec++] = v;
                else odd[oc++] = v;
            }

            // ---- 3. 交错填充 ----
            int[] answer = new int[sorted.length];
            for (int i = 0; i < ec; i++) {
                answer[i * 2] = even[i];
                answer[i * 2 + 1] = odd[i];
            }
            return answer;
        }
    }


    public class Solution5 {
        public int[] sortArr(int[] nums) {

            // ---- 1. 先排序 ----
            int[] sorted = nums.clone();
            java.util.Arrays.sort(sorted);

            // ---- 2. 用两个列表暂存（也可用双指针，见方案三）----
            int[] answer = new int[nums.length];

            // 找出偶数和奇数的数量
            int evenCount = 0;
            for (int v : sorted) if (v % 2 == 0) evenCount++;
            int oddCount = sorted.length - evenCount;

            // 偶数从 sorted 头部开始（排序后偶数在前？不一定！见下方说明）
            // 用两个指针从 sorted 里按顺序取
            int ei = 0, oi = 0;
            // 分别找出偶数和奇数的起始位置
            java.util.List<Integer> even = new java.util.ArrayList<>();
            java.util.List<Integer> odd = new java.util.ArrayList<>();
            for (int v : sorted)
                (v % 2 == 0 ? even : odd).add(v);

            for (int index = 0; index < even.size(); index++) {
                answer[index * 2] = even.get(index);
                answer[index * 2 + 1] = odd.get(index);
            }
            return answer;
        }
    }

}
