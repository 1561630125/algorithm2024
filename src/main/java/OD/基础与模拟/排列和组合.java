package OD.基础与模拟;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 22:18
 */
public class 排列和组合 {

     class Permutation {
        // 全排列 - 无重复元素
        public  List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            boolean[] used = new boolean[nums.length];
            backtrack(nums, new ArrayList<>(), used, result);
            return result;
        }

        private void backtrack(int[] nums, List<Integer> path,
                                      boolean[] used, List<List<Integer>> result) {
            if (path.size() == nums.length) {
                result.add(new ArrayList<>(path));
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (used[i]) continue;
                used[i] = true;
                path.add(nums[i]);
                backtrack(nums, path, used, result);
                path.remove(path.size() - 1);
                used[i] = false;
            }
        }

        // 全排列 - 有重复元素
        public List<List<Integer>> permuteUnique(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(nums); // 排序方便去重
            boolean[] used = new boolean[nums.length];
            backtrackUnique(nums, new ArrayList<>(), used, result);
            return result;
        }

        private void backtrackUnique(int[] nums, List<Integer> path,
                                            boolean[] used, List<List<Integer>> result) {
            if (path.size() == nums.length) {
                result.add(new ArrayList<>(path));
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (used[i]) continue;
                // 去重：相同元素且前一个未使用，跳过
                if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;

                used[i] = true;
                path.add(nums[i]);
                backtrackUnique(nums, path, used, result);
                path.remove(path.size() - 1);
                used[i] = false;
            }
        }
    }

    class PermutationSwap {
        public void permute(int[] nums, int start) {
            if (start == nums.length) {
                System.out.println(Arrays.toString(nums));
                return;
            }

            for (int i = start; i < nums.length; i++) {
                swap(nums, start, i);
                permute(nums, start + 1);
                swap(nums, start, i); // 回溯
            }
        }

        private void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }


    public class Combination {
        // 从 n 个元素中选 k 个
        public  List<List<Integer>> combine(int n, int k) {
            List<List<Integer>> result = new ArrayList<>();
            backtrack(1, n, k, new ArrayList<>(), result);
            return result;
        }

        private void backtrack(int start, int n, int k,
                                      List<Integer> path, List<List<Integer>> result) {
            if (path.size() == k) {
                result.add(new ArrayList<>(path));
                return;
            }

            // 剪枝优化
            for (int i = start; i <= n - (k - path.size()) + 1; i++) {
                path.add(i);
                backtrack(i + 1, n, k, path, result);
                path.remove(path.size() - 1);
            }
        }
    }

    class CombinationSum {
        // 从候选数组中选择和为 target 的组合（可重复）
        public  List<List<Integer>> combinationSum(int[] candidates, int target) {
            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(candidates);
            backtrack(candidates, target, 0, new ArrayList<>(), result);
            return result;
        }

        private  void backtrack(int[] candidates, int target, int start,
                                      List<Integer> path, List<List<Integer>> result) {
            if (target == 0) {
                result.add(new ArrayList<>(path));
                return;
            }

            for (int i = start; i < candidates.length && candidates[i] <= target; i++) {
                path.add(candidates[i]);
                backtrack(candidates, target - candidates[i], i, path, result);
                path.remove(path.size() - 1);
            }
        }

        // 组合总和 II（每个元素只能使用一次）
        public List<List<Integer>> combinationSum2(int[] candidates, int target) {
            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(candidates);
            backtrack2(candidates, target, 0, new ArrayList<>(), result);
            return result;
        }

        private void backtrack2(int[] candidates, int target, int start,
                                       List<Integer> path, List<List<Integer>> result) {
            if (target == 0) {
                result.add(new ArrayList<>(path));
                return;
            }

            for (int i = start; i < candidates.length && candidates[i] <= target; i++) {
                // 去重
                if (i > start && candidates[i] == candidates[i-1]) continue;

                path.add(candidates[i]);
                backtrack2(candidates, target - candidates[i], i + 1, path, result);
                path.remove(path.size() - 1);
            }
        }
    }

}
