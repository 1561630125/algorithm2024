package 力扣.二分查找.A33;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-29 16:27
 */
public class 搜索旋转排序数组 {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = new int[]{6,7,0,1,2,3,4,5};


        System.out.println(solution.search(nums,7));
    }
}


class Solution {
    public int search(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return -1;
        }
        if (n == 1) {
            return nums[0] == target ? 0 : -1;
        }
        int l = 0, r = n - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[0] <= nums[mid]) {
                if (nums[0] <= target && target < nums[mid]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[n - 1]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return -1;
    }
}