package 力扣.位运算.A67;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-31 9:30
 */
public class 二进制求和 {

}

//先将 a 和 b 转化成十进制数，求和后再转化为二进制数（为了适用于长度较大的字符串计算，我们应该使用更加健壮的算法）
class Solution {
    public String addBinary(String a, String b) {
        return Integer.toBinaryString(
                Integer.parseInt(a, 2) + Integer.parseInt(b, 2)
        );
    }
}

//模拟
class Solution2 {
    public String addBinary(String a, String b) {
        StringBuffer ans = new StringBuffer();

        int n = Math.max(a.length(), b.length()), carry = 0;
        for (int i = 0; i < n; ++i) {
            carry += i < a.length() ? (a.charAt(a.length() - 1 - i) - '0') : 0;
            carry += i < b.length() ? (b.charAt(b.length() - 1 - i) - '0') : 0;
            ans.append((char) (carry % 2 + '0'));
            carry /= 2;
        }

        if (carry > 0) {
            ans.append('1');
        }
        ans.reverse();

        return ans.toString();
    }
}

//TODO
class Solution3 {
    public String addBinary(String a, String b) {
        int x = Integer.parseInt(a, 2), y = Integer.parseInt(b, 2);
        while (y != 0){
            int answer = x ^ y; //无进位相加，answer 的最后一位是 x 和 y 相加之后的结果
            int carry = (x & y) << 1; //x和y的进位
            x = answer;
            y = carry;

        }
        return Integer.toBinaryString(x);
    }
}