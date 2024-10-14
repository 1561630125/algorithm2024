package 力扣.数组和字符串.A55;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-14 10:40
 */
public class 跳跃游戏 {

    //贪心，我们依次遍历数组中的每一个位置，并实时维护 最远可以到达的位置
    public boolean canJump(int[] nums) {
        int n = nums.length;
        int rightmost = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= rightmost) {
                rightmost = Math.max(rightmost, i + nums[i]);
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
