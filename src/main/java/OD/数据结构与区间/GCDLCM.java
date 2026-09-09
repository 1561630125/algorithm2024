package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 17:13
 */
public class GCDLCM {

    // 求最大公约数 (递归)
    public static int gcd(int a, int b) {
        // 确保非负，如果有负数取绝对值
        a = Math.abs(a);
        b = Math.abs(b);
        // 欧几里得算法核心：gcd(a, b) = gcd(b, a % b)
        return b == 0 ? a : gcd(b, a % b);
    }

    // 求最小公倍数
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0; // 数学上通常定义0和任何数的LCM为0，或根据业务处理
        }
        // 先除以GCD再乘，防止溢出 (a * b 可能会超出int范围)
        return a / gcd(a, b) * b;
    }

    /**
     * 辗转相除法（迭代 - 防止递归栈溢出）
     *
     * @param a
     * @param b
     * @return int
     */
    public static int gcdIterative(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    /**
     * 适合大数减法，但效率低于取模运算
     *
     * @param a
     * @param b
     * @return int
     */
    public static int gcdBySubtraction(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (a != b) {
            if (a > b) a -= b;
            else b -= a;
        }
        return a;
    }

    // 求数组最大公约数
    public static int gcdArray(int[] arr) {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = gcd(result, arr[i]);
            if (result == 1) break; // 剪枝
        }
        return result;
    }

    // 求数组最小公倍数
    public static int lcmArray(int[] arr) {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = lcm(result, arr[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        int a = 12, b = 18;
        System.out.println("GCD: " + gcd(a, b)); // 输出 6
        System.out.println("LCM: " + lcm(a, b)); // 输出 36
    }
}
