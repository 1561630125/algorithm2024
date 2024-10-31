package 力扣.数学.A172;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-31 14:41
 */
public class 阶乘后的0 {

}

class Solution {
    //转换成求 n! 中质因子 2 的个数和质因子 5 的个数的较小值;质因子 5 的个数不会大于质因子 2 的个数
    public int trailingZeroes(int n) {
        int ans = 0;
        for (int i = 5; i <= n; i += 5) {
            for (int x = i; x % 5 == 0; x /= 5) {
                ++ans;
            }
        }
        return ans;
    }
}



class Solution2 {
    public int trailingZeroes(int n) {
        int ans = 0;
        while (n != 0) {
            n /= 5;
            ans += n;
        }
        return ans;
    }
}
