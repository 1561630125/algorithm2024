package 力扣.数组和字符串.A26;

/**
 * A80
 *
 * @author faming.yang@hand-china.com 2024-10-13 18:35
 */
public class 删除有序数组中的重复项2 {
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        if (n <= 2) {
            return n;
        }
        int slow = 2, fast = 2;
        while (fast < n) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }
}
