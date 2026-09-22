package OD.贪心和动态规划;

/**
 * 考点：贪心
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:11
 */
public class 高矮交错排队 {

    class Solution {
        /**
         * 把 tokens 中的数字字符串重新排列成"锯齿形"（wiggle）：
         * a[0] > a[1] < a[2] > a[3] < a[4] ...
         * 即：奇数下标（1、3、5…）要小于前一个，
         * 偶数下标（2、4、6…）要大于前一个。
         * <p>
         * 如果输入中有任何非法（非纯数字）的 token，返回空数组。
         */
        String[] arrangeHeights(String[] tokens) {
            // 把字符串数组解析成整型数组
            int[] values = new int[tokens.length];
            for (int index = 0; index < tokens.length; index++) {
                // 用正则校验：整个字符串必须全部是 0-9 的数字
                // 空串、负数、带空格、含字母都会失败 → 返回空数组
                if (!tokens[index].matches("[0-9]+"))
                    return new String[0];
                try {
                    // 解析为 int；超出 int 范围会抛异常
                    values[index] = Integer.parseInt(tokens[index]);
                } catch (NumberFormatException error) {
                    return new String[0];
                }
            }

            // 单遍贪心扫描，构造锯齿形序列
            for (int index = 1; index < values.length; index++) {
                // 判断当前位置是否"违反"了锯齿规则：
                //   index 为奇数 → 要求 a[index] < a[index-1]，
                //                  若 a[index] > a[index-1] 则违反
                //   index 为偶数 → 要求 a[index] > a[index-1]，
                //                  若 a[index] < a[index-1] 则违反
                boolean wrong = index % 2 == 1 ? values[index] > values[index - 1]
                        : values[index] < values[index - 1];
                // 违反就交换当前与前一个元素，交换后本位置一定满足规则
                if (wrong) {
                    int temporary = values[index];
                    values[index] = values[index - 1];
                    values[index - 1] = temporary;
                }
            }

            // 把结果整型数组转回字符串数组
            String[] result = new String[values.length];
            for (int index = 0; index < values.length; index++)
                result[index] = Integer.toString(values[index]);
            return result;
        }
    }
}
