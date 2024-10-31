package 力扣.位运算.A201;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-31 10:40
 */
public class 数字范围按位与 {

}


class Solution {
    public int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        // 找到公共前缀
        while (m < n) {
            m >>= 1;
            n >>= 1;
            ++shift;
        }
        return m << shift;
    }
}
//Brian Kernighan 算法
class Solution2 {
    public int rangeBitwiseAnd(int m, int n) {
        while (m < n) {
            // 抹去最右边的 1
            n = n & (n - 1);
        }
        return n;
    }
}
