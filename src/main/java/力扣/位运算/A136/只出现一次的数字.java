package 力扣.位运算.A136;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-31 9:55
 */
public class 只出现一次的数字 {

}


class Solution {
    public int singleNumber(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }
}