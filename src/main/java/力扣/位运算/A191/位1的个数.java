package 力扣.位运算.A191;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-30 16:44
 */
public class 位1的个数 {

}

//循环检查二进制位
class Solution {
    public int hammingWeight(int n) {
        int ret = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0) {
                ret++;
            }
        }
        return ret;
    }
}

//位运算优化
class Solution2 {
    public int hammingWeight(int n) {
        int ret = 0;
        while (n != 0) {
            n &= n - 1;
            ret++;
        }
        return ret;
    }
}
