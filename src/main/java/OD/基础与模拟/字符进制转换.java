package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 21:28
 */
public class 字符进制转换 {
    /**
     * 字符进制转换（使用大数除法直接转换）
     *
     * 算法核心：除基取余法（Short Division）
     * 将源进制表示的数字，不断除以目标进制基数，记录余数
     * 适用于超大数字，避免 BigInteger 溢出问题
     *
     * 时间复杂度：O(n * m)，n为数字长度，m为转换轮次
     * 空间复杂度：O(n)
     *
     * @param num 使用源数字表表示的非负整数
     * @param sourceDigits 从零开始排列的源进制数字表
     * @param targetDigits 从零开始排列的目标进制数字表
     * @return 使用目标数字表表示的转换结果
     */
    static public String digitConvert(String num, String sourceDigits, String targetDigits) {

        // ==================== 步骤1: 解析输入 ====================
        // 将源数字字符串转换为数值数组（每个元素代表一位数字的值）
        // 例如：sourceDigits="0123456789", num="255" → [2,5,5]
        java.util.List<Integer> digits = new java.util.ArrayList<>();
        for (int index = 0; index < num.length(); index++) {
            // sourceDigits.indexOf() 获取字符在源进制表中的位置，即该位的数值
            digits.add(sourceDigits.indexOf(num.charAt(index)));
        }

        // ==================== 步骤2: 去除前导零 ====================
        // 例如：[0,0,1,2] → [1,2]
        while (digits.size() > 1 && digits.get(0) == 0) {
            digits.remove(0);
        }

        // ==================== 步骤3: 检查是否为零 ====================
        // 判断所有位是否都是 0
        boolean zero = true;
        for (int digit : digits) {
            if (digit != 0) {
                zero = false;
                break;
            }
        }
        if (zero) {
            // 如果为零，直接返回目标进制表的第一个字符（代表 0）
            return targetDigits.substring(0, 1);
        }

        // ==================== 步骤4: 除基取余转换 ====================
        // 核心算法：不断将 digits 除以 targetDigits.length()，记录余数
        StringBuilder answer = new StringBuilder();

        while (!digits.isEmpty()) {
            // 存储本次除法的商
            java.util.List<Integer> quotient = new java.util.ArrayList<>();
            int remainder = 0;  // 上一位的余数

            // 对被除数（digits）逐位进行长除法
            for (int digit : digits) {
                // 当前被除数 = 上一位余数 × 源进制基数 + 当前位
                int current = remainder * sourceDigits.length() + digit;

                // 计算当前位的商
                int value = current / targetDigits.length();

                // 去除前导零：只有在商不为零或已经有过非零商时才添加
                if (!quotient.isEmpty() || value > 0) {
                    quotient.add(value);
                }

                // 更新余数
                remainder = current % targetDigits.length();
            }

            // 当前轮次的余数就是目标进制的一位（从低位到高位）
            answer.append(targetDigits.charAt(remainder));

            // 用商作为下一次的被除数
            digits = quotient;
        }

        // ==================== 步骤5: 反转结果 ====================
        // 因为余数是从低位到高位收集的，需要反转得到正确顺序
        return answer.reverse().toString();
    }


    public static void main(String[] args) {

        System.out.println(digitConvert("zz","0123456789abcdefghijklmnopqrstuvwxyz","0123456789"));

    }

}
