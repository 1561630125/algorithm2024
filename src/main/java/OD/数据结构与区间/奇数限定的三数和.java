package OD.数据结构与区间;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 15:05
 */
public class 奇数限定的三数和 {

    /**
     *
     * 奇数限定的三数和
     * @param nums int[] — 候选整数
     * @param target int — 目标和
     * @return int[][]
     */
    static public int[][] threeSum(int[] nums, int target) {
        // write code here
        Arrays.sort(nums);

        List<Integer> temp = new LinkedList<>();

        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        dfs(result,nums, temp, target,used);

        int[][] arr = result.stream()
                .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                .toArray(int[][]::new);
        return arr;
    }

    static public void dfs(List<List<Integer>> result,int[] nums,List<Integer> temp,int target,boolean[] used) {
        if (temp.size() == 3) {
            if (target == 0) {
                int count = 0;
                for(int i = 0; i < temp.size(); i++) {
                    if (temp.get(i) % 2 != 0) count++;
                }
                if (count >= 2) result.add(new ArrayList<>(temp));
            }
            return;
        }

        for(int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            temp.add(nums[i]);
            used[i] = true;
            dfs(result,nums,temp,target - nums[i], used);
            used[i] = false;
            temp.remove(temp.size() - 1);
        }
    }


    static public int[][] threeSum3(int[] nums, int target) {
        Arrays.sort(nums);  // 排序，方便去重和剪枝

        List<List<Integer>> result = new ArrayList<>();
        dfs3(result, nums, new ArrayList<>(), target, 0);

        // List<List<Integer>> 转 int[][]
        return result.stream()
                .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                .toArray(int[][]::new);
    }

    static private void dfs3(List<List<Integer>> result, int[] nums,
                            List<Integer> temp, int remain, int start) {

        // 已经选了 3 个数
        if (temp.size() == 3) {
            if (remain == 0) {
                // 额外条件：至少两个奇数
                int oddCount = 0;
                for (int v : temp) {
                    if ((v & 1) == 1) oddCount++;
                }
                if (oddCount >= 2) {
                    result.add(new ArrayList<>(temp));
                }
            }
            return;
        }

        // 还要选 3 - temp.size() 个数，如果剩余不够，剪枝
        if (nums.length - start < 3 - temp.size()) return;

        for (int i = start; i < nums.length; i++) {

            // 同一层跳过重复元素，避免重复三元组
            if (i > start && nums[i] == nums[i - 1]) continue;

            // 剪枝：排序后，如果当前数已经让 remain 过小，后面更大，直接 break
            // 这里可选，视 target 正负情况，简单题可不加

            int need = 3 - temp.size();  // 还需要选几个
            // 剩余元素不够（包括当前），剪枝
            if (nums.length - i < need) break;

            // 剪枝1：当前数 + 后面最小的 need-1 个数 已经 > remain
            // 说明从 i 开始往后都凑不出 remain，直接 break
            long minSum = nums[i];
            for (int k = 1; k < need; k++) {
                minSum += nums[i + k];
            }
            if (minSum > remain) break;

            // 剪枝2：当前数 + 末尾最大的 need-1 个数 仍 < remain
            // 说明当前数太小，换下一个更大的数试试
            long maxSum = nums[i];
            for (int k = 1; k < need; k++) {
                maxSum += nums[nums.length - k];
            }
            if (maxSum < remain) continue;


            temp.add(nums[i]);
            dfs3(result, nums, temp, remain - nums[i], i + 1); // 从 i+1 开始，保证组合不重复
            temp.remove(temp.size() - 1);
        }
    }

    public int[][] threeSum2(int[] nums, int target) {
        // 复制一份数组，避免修改原数组
        int[] values = nums.clone();

        // 排序，方便用双指针 + 跳过重复元素
        java.util.Arrays.sort(values);

        // 用 List 收集答案，元素是长度为 3 的 int[]
        java.util.List<int[]> answer = new java.util.ArrayList<>();

        // 枚举三元组中最小的那个数，作为第一个数
        // index + 2 < length 保证后面至少还有两个数
        for (int index = 0; index + 2 < values.length; index++) {

            // 跳过重复的第一个数，避免产生重复三元组
            if (index > 0 && values[index] == values[index - 1])
                continue;

            // 双指针：在 index 右边找两个数，使三数之和等于 target
            int left = index + 1, right = values.length - 1;

            while (left < right) {
                // 用 long 计算和，防止三数相加溢出 int
                long total = (long) values[index] + values[left] + values[right];

                if (total < target) {
                    // 和太小，左指针右移，让和变大
                    left++;
                } else if (total > target) {
                    // 和太大，右指针左移，让和变小
                    right--;
                } else {
                    // 找到一个和等于 target 的三元组

                    // 额外筛选条件：
                    // 取三个数的二进制最低位（奇偶性）相加
                    // 只有当其中至少两个是奇数时，才把这个三元组加入答案
                    // 即：三个数中至少有两个奇数
                    if (
                            (values[index] & 1) +
                                    (values[left] & 1) +
                                    (values[right] & 1) >= 2
                    ) {
                        answer.add(new int[] {
                                values[index], values[left], values[right]
                        });
                    }

                    // 找到答案后，左右指针同时收缩，继续找下一组
                    left++;
                    right--;

                    // 跳过重复的左指针元素（避免重复三元组）
                    while (left < right && values[left] == values[left - 1])
                        left++;

                    // 跳过重复的右指针元素
                    while (left < right && values[right] == values[right + 1])
                        right--;
                }
            }
        }

        // List<int[]> 转成 int[][]
        return answer.toArray(new int[0][]);
    }


    public static void main(String[] args) {
        int[] nums = new int[]{-2,-1,0,1,2};
        int target = 0;

        System.out.println(Arrays.deepToString(threeSum(nums,target)));
    }

}
