package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 22:40
 */
public class 压缩串误码 {
    String compressedErrorRate(String standard, String transmitted) {
        int length1 = standard.length();
        int length2 = transmitted.length();
        int maxlength = Math.max(length1, length2);

        int total = 0;
        for(int i = 0; i < maxlength; i = i + 2) {
            String s1 = standard.substring(i, i + 2);
            int n1 = s1.charAt(0);
            int c1 = s1.charAt(1);
            String s2 = transmitted.substring(i, i + 2);
            int n2 = s2.charAt(0);
            int c2 = s1.charAt(1);

            total = total + n1;
        }

        return "0/0";
    }

    /**
     * 计算两个游程编码压缩字符串之间的错误率
     *
     * 原理：将压缩字符串解析为(字符, 连续出现次数)的序列，然后逐段比较，
     * 统计不同字符的总长度占总长度的比例。
     *
     * 例如：standard = "3a2b1c" 表示 "aaabbc"
     *      transmitted = "2a3b1c" 表示 "aabbbc"
     *      比较展开后的字符串 "aaabbc" vs "aabbbc"
     *      第3位 a≠b, 第4位 b≠b? 实际是第3位 b vs b? 让我们看代码逻辑...
     *
     * @param standard 标准的压缩字符串，格式如 "3a2b1c"
     * @param transmitted 待校验的压缩字符串，格式如 "2a3b1c"
     * @return 错误率字符串，格式为 "不同字符数/总字符数"
     */
    String compressedErrorRate2(String standard, String transmitted) {
        // 解析两个压缩字符串为 (字符, 计数) 的列表
        // expected: [(a,3), (b,2), (c,1)] 表示 "aaabbc"
        // actual:   [(a,2), (b,3), (c,1)] 表示 "aabbbc"
        java.util.List<long[]> expected = parse(standard), actual = parse(transmitted);

        // 双指针遍历两个游程序列
        int left = 0, right = 0;                    // 当前处理的段索引
        long leftRemaining = expected.isEmpty() ? 0 : expected.get(0)[1];  // 左侧当前段剩余未比较字符数
        long rightRemaining = actual.isEmpty() ? 0 : actual.get(0)[1];     // 右侧当前段剩余未比较字符数
        long different = 0, total = 0;              // 累计不同字符数 和 总字符数

        while (left < expected.size()) {            // 只要标准序列还有未处理的段

            // 当前左侧段已用完，移到下一段
            if (leftRemaining == 0) {
                left++;
                leftRemaining = left < expected.size() ? expected.get(left)[1] : 0;
                continue;
            }

            // 右侧序列已耗尽 → 剩余所有左侧字符都视为错误
            if (right >= actual.size()) {
                different += leftRemaining;
                total += leftRemaining;
                leftRemaining = 0;
                continue;
            }

            // 当前右侧段已用完，移到下一段
            if (rightRemaining == 0) {
                right++;
                rightRemaining = right < actual.size() ? actual.get(right)[1] : 0;
                continue;
            }

            // 取两段中较小的长度进行比较
            long compared = Math.min(leftRemaining, rightRemaining);
            total += compared;

            // 字符不同则计入错误
            if (expected.get(left)[0] != actual.get(right)[0])
                different += compared;

            // 两段都消耗掉 compared 个字符
            leftRemaining -= compared;
            rightRemaining -= compared;
        }

        // 返回错误率字符串
        return different + "/" + total;
    }

    /**
     * 解析游程编码字符串
     *
     * 输入格式：数字 + 字符 交替出现，如 "3a2b1c"
     * 解析结果：[(a,3), (b,2), (c,1)]
     *
     * @param text 游程编码字符串
     * @return 列表，每个元素为 [字符的ASCII码, 重复次数]
     */
    private java.util.List<long[]> parse(String text) {
        java.util.List<long[]> result = new java.util.ArrayList<>();
        long count = 0;

        for (int index = 0; index < text.length(); index++) {
            char value = text.charAt(index);

            if (value >= '0' && value <= '9') {
                // 数字字符 → 累加计数（支持多位数，如 "12a"）
                count = count * 10 + value - '0';
            } else {
                // 非数字字符 → 作为游程的字符值，将(字符, 计数)加入结果
                result.add(new long[]{value, count});
                count = 0;  // 重置计数
            }
        }
        return result;
    }


    public static void main(String[] args) {

    }

}
