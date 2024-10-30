package 力扣.二分查找.A35;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-29 15:54
 */
public class 搜索插入位置 {

}


class Solution {
    public int searchInsert(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (target <= nums[mid]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
}
