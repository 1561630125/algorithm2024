package 力扣.数组和字符串.A189.A1979;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-14 9:44
 */
public class 找出数组的最大公约数 {
    public int findGCD(int[] nums) {
        int minNum = Integer.MAX_VALUE, maxNum = Integer.MIN_VALUE;
        for (int num : nums) {
            minNum = Math.min(minNum, num);
            maxNum = Math.max(maxNum, num);
        }
        return gcd(minNum, maxNum);
    }

    public int gcd(int num1, int num2) {
        while (num2 != 0) {
            int temp = num1;
            num1 = num2;
            num2 = temp % num2;
        }
        return num1;
    }
}
