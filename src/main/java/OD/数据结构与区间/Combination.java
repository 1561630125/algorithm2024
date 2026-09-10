package OD.数据结构与区间;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 14:20
 */
public class Combination {
    class Combination2 {

        /**
         * 从n个数中选择k个数的所有组合
         */
        public List<List<Integer>> combine(int n, int k) {
            List<List<Integer>> result = new ArrayList<>();
            backtrack(result, new ArrayList<>(), 1, n, k);
            return result;
        }

        private void backtrack(List<List<Integer>> result,
                               List<Integer> temp,
                               int start, int n, int k) {
            // 找到k个数的组合
            if (temp.size() == k) {
                result.add(new ArrayList<>(temp));
                return;
            }

            // 剪枝：剩余元素不足以凑齐k个
            for (int i = start; i <= n - (k - temp.size()) + 1; i++) {
                temp.add(i);
                backtrack(result, temp, i + 1, n, k);
                temp.remove(temp.size() - 1);
            }
        }

        public void main(String[] args) {
            List<List<Integer>> result = combine(5, 3);
            System.out.println("C(5,3) = " + result.size());
            result.forEach(System.out::println);
        }
    }


    public class CombinationCount {

        /**
         * 计算组合数 C(n, k)
         */
        public long combination(int n, int k) {
            if (k > n - k) k = n - k;  // C(n,k) = C(n,n-k)

            long result = 1;
            for (int i = 0; i < k; i++) {
                result = result * (n - i) / (i + 1);
            }
            return result;
        }

        public void main(String[] args) {
            System.out.println("C(10,3) = " + combination(10, 3));  // 120
            System.out.println("C(20,10) = " + combination(20, 10)); // 184756
        }
    }


    public class Permutation {

        /**
         * 数组的全排列（包含重复元素处理）
         */
        public  List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            boolean[] used = new boolean[nums.length];
            Arrays.sort(nums); // 排序用于去重
            backtrack(result, new ArrayList<>(), nums, used);
            return result;
        }

        private void backtrack(List<List<Integer>> result,
                               List<Integer> temp,
                               int[] nums,
                               boolean[] used) {
            if (temp.size() == nums.length) {
                result.add(new ArrayList<>(temp));
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (used[i]) continue;

                // 去重：跳过重复元素
                if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;

                used[i] = true;
                temp.add(nums[i]);
                backtrack(result, temp, nums, used);
                temp.remove(temp.size() - 1);
                used[i] = false;
            }
        }

        public  void main(String[] args) {
            int[] nums = {1, 2, 3};
            List<List<Integer>> result = permute(nums);
            System.out.println("全排列数量: " + result.size());
            result.forEach(System.out::println);
        }
    }


    public class PermutationK {

        /**
         * 从n个数中选k个数的排列 A(n, k)
         */
        public  List<List<Integer>> permuteK(int n, int k) {
            List<List<Integer>> result = new ArrayList<>();
            boolean[] used = new boolean[n + 1];
            backtrack(result, new ArrayList<>(), 1, n, k, used);
            return result;
        }

        private  void backtrack(List<List<Integer>> result,
                                List<Integer> temp,
                                int start, int n, int k,
                                boolean[] used) {
            if (temp.size() == k) {
                result.add(new ArrayList<>(temp));
                return;
            }

            for (int i = 1; i <= n; i++) {
                if (used[i]) continue;
                used[i] = true;
                temp.add(i);
                backtrack(result, temp, i, n, k, used);
                temp.remove(temp.size() - 1);
                used[i] = false;
            }
        }

        public void main(String[] args) {
            List<List<Integer>> result = permuteK(4, 2);
            System.out.println("A(4,2) = " + result.size()); // 12
            result.forEach(System.out::println);
        }
    }


    public class NextPermutation {

        /**
         * 获取下一个字典序排列
         */
        public void nextPermutation(int[] nums) {
            int i = nums.length - 2;

            // 1. 从右向左找第一个下降点
            while (i >= 0 && nums[i] >= nums[i + 1]) {
                i--;
            }

            if (i >= 0) {
                // 2. 从右向左找第一个大于nums[i]的数
                int j = nums.length - 1;
                while (nums[j] <= nums[i]) {
                    j--;
                }
                swap(nums, i, j);
            }

            // 3. 反转i+1到末尾
            reverse(nums, i + 1, nums.length - 1);
        }

        private void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        private void reverse(int[] nums, int start, int end) {
            while (start < end) {
                swap(nums, start++, end--);
            }
        }

        public void main(String[] args) {
            int[] nums = {1, 2, 3};
            do {
                System.out.println(Arrays.toString(nums));
                nextPermutation(nums);
            } while (!Arrays.equals(nums, new int[]{1, 2, 3}));
        }
    }
}
