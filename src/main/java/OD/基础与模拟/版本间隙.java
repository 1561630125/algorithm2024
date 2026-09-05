package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 18:16
 */
public class 版本间隙 {

    static long calNum(String str) {
        long l1 = 0;
        for (int j = 0; j < str.length(); j++) {
            l1 = l1 * 10 + (str.charAt(j) - '0');
        }
        return l1;
    }

    static long availableVersionCount(String first, String second) {
        String[] firstArr = first.split("\\.");
        String[] secondArr = second.split("\\.");
        int length1 = firstArr.length;
        int length2 = secondArr.length;
        int maxLength = Math.max(length1, length2);

        for (int i = 0; i < maxLength; i++) {
            String s1 = i >= length1 ? "0" : firstArr[i];
            String s2 = i >= length2 ? "0" : secondArr[i];

            long l1 = calNum(s1);
            long l2 = calNum(s2);

            if (l1 < l2) {
                for (int j = i + 1; j < length2; j++) {
                    if (calNum(secondArr[j]) != 0) {
                        return 0L;
                    }
                }
                return l2 - l1 - 1;
            } else if (l1 > l2) {
                return 0;
            }
        }
        return 0L;
    }


    static long availableVersionCount2(String first, String second) {
        // 按 "." 分割版本号，保留空字符串（-1 参数确保 "1.0." 能分割出尾部空串）
        String[] firstParts = first.split("\\.", -1),
                secondParts = second.split("\\.", -1);

        // 取两个版本号段数较多者作为循环边界
        int size = Math.max(firstParts.length, secondParts.length);

        for (int index = 0; index < size; index++) {
            // 获取当前段的数值，缺失的段补 0
            java.math.BigInteger left = index < firstParts.length
                    ? new java.math.BigInteger(firstParts[index])
                    : java.math.BigInteger.ZERO,
                    right = index < secondParts.length
                            ? new java.math.BigInteger(secondParts[index])
                            : java.math.BigInteger.ZERO;

            int comparison = left.compareTo(right);
            if (comparison == 0)
                continue;  // 当前段相等，继续比较下一段

            // 如果 left < right（即 first 版本号更小）
            if (comparison < 0) {
                boolean zero = true;
                // 检查 second 版本号从下一段开始是否全部为 0
                for (int rest = index + 1; rest < size; rest++)
                    if (!(rest < secondParts.length
                            ? new java.math.BigInteger(secondParts[rest])
                            : java.math.BigInteger.ZERO)
                            .equals(java.math.BigInteger.ZERO))
                        zero = false;

                // 如果 second 从下一段开始全是 0，则计算差值减 1 并返回
                if (zero)
                    return right.subtract(left).subtract(java.math.BigInteger.ONE).longValue();
            }
            break;  // 否则跳出循环（first > second 或 second 后续不全为 0）
        }
        return 0L;  // 版本相等 或 first > second 或 second 后续不全为 0
    }


    public static void main(String[] args) {
        String first = "5.0";
        String second = "4.9";
        System.out.println(availableVersionCount(first, second));
        System.out.println(availableVersionCount2(first, second));
    }

}
