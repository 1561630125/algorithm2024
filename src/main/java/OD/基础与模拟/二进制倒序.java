package OD.基础与模拟;

import java.math.BigInteger;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 0:19
 */
public class 二进制倒序 {

    public String reverseBinaryDigits(String num) {
        // 1. 将输入的十进制字符串转换为 BigInteger 对象
        //    BigInteger 可以处理任意大的整数，避免 int/long 溢出
        java.math.BigInteger value = new java.math.BigInteger(num);

        // 2. 如果数值为 0，直接返回 "0"
        //    因为 0 的二进制是 "0"，反转后仍是 "0"，特殊处理提高效率并避免空字符串问题
        if (value.signum() == 0)
            return "0";

        // 3. 将数值转换为二进制字符串（不带前导零）
        //    例如：value=6 → bits="110"
        String bits = value.toString(2);

        // 4. 反转二进制字符串
        //    例如：bits="110" → 反转后为 "011"
        //    使用 StringBuilder 高效反转
        String reversedBits = new StringBuilder(bits).reverse().toString();

        // 5. 将反转后的二进制字符串解析为 BigInteger（指定基数为2）
        //    然后调用 toString() 将其转换为十进制字符串并返回
        //    例如：反转后的 "011" 解析为二进制数，值为 3，输出 "3"
        return new java.math.BigInteger(reversedBits, 2).toString();
    }

    public static String toBinaryString(BigInteger value) {
        if (value.signum() == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        BigInteger two = BigInteger.valueOf(2);
        BigInteger temp = value.abs(); // 取绝对值处理

        // 除2取余，直到商为0
        while (temp.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] result = temp.divideAndRemainder(two);
            sb.append(result[1]); // 余数（0或1）
            temp = result[0];     // 商
        }

        // 反转得到正确的二进制顺序
        return sb.reverse().toString();
    }


    public static String toBinaryString(int num) {
        if (num == 0) return "0";

        StringBuilder sb = new StringBuilder();
        int n = num;

        // 从最高位开始检查（假设32位int）
        boolean started = false;
        for (int i = 31; i >= 0; i--) {
            int bit = (n >> i) & 1;
            if (bit == 1) started = true;
            if (started) {
                sb.append(bit);
            }
        }
        return sb.toString();
    }

    public static String toBinaryString2(BigInteger value) {
        if (value.signum() == 0) {
            return "0";
        }

        BigInteger two = BigInteger.valueOf(2);
        BigInteger[] result = value.divideAndRemainder(two);

        // 递归：商继续转换，然后拼接余数
        if (result[0].signum() == 0) {
            return result[1].toString(); // 最后一位
        } else {
            return toBinaryString2(result[0]) + result[1].toString();
        }
    }


    public static void main(String[] args) {
        System.out.println(toBinaryString2(BigInteger.valueOf(5)));
    }

}
