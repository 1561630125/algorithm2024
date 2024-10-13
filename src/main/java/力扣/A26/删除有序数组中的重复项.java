package 力扣.A26;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-13 11:36
 */
public class 删除有序数组中的重复项 {
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int fast = 1, slow = 1;
        while (fast < n) {
            if (nums[fast] != nums[fast - 1]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }
}
