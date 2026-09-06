package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:22
 */
public class 工号数字位 {
    int minimumEmployeeNumberDigits(long people, int letterLength) {
        // 如果人数 <= 0，至少需要1位数字（编号从0或1开始）
        if (people <= 0)
            return 1;

        // target: 需要容纳的人数
        // capacity: 当前编号方案能容纳的最大人数
        java.math.BigInteger target = java.math.BigInteger.valueOf(people),
                capacity = java.math.BigInteger.TEN;

        // letterLength > 0: 固定长度的字母前缀
        // letterLength = 0: 无字母前缀
        // letterLength < 0: 字母后缀（罕见情况，人数需要乘以26^|letterLength|）
        if (letterLength >= 0) {
            // 有字母前缀：总容量 = 26^letterLength × 10^digits
            // 例如：2个字母前缀 + 3位数字 => 26^2 × 10^3 = 676 × 1000 = 676,000
            capacity = java.math.BigInteger.valueOf(26).pow(letterLength)
                    .multiply(capacity);
        } else {
            // letterLength为负数：表示字母在数字后面
            // 此时等同于人数需求翻倍（因为字母后缀增加了组合数）
            // 例如：3位数字 + 1个字母后缀 => 总组合 = 10^3 × 26
            // 所以 target 需要乘以 26^|letterLength| 来等价比较
            target = target.multiply(
                    java.math.BigInteger.valueOf(26).pow(-letterLength)
            );
        }

        // 从1位数字开始，逐步增加数字位数，直到容量 >= 目标人数
        int digits = 1;
        while (capacity.compareTo(target) < 0) {
            capacity = capacity.multiply(java.math.BigInteger.TEN);  // 数字位+1，容量×10
            digits++;
        }

        return digits;  // 返回所需的最少数字位数
    }


    public static void main(String[] args) {

    }

}
