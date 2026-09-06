package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 15:28
 */
public class 位差大于位同 {

    long countDifferenceDominantPairs(long[] values) {
        // g: 用于统计每个"最高位1位置"已经出现过的数字个数
        // key: 最高位1的位置索引 (0-based)
        // value: 该位置已经出现的数字个数
        java.util.Map<Integer, Long> g = new java.util.HashMap<>();

        // seen: 到目前为止已经遍历过的数字总数
        // result: 累计的满足条件(差异值>相似值)的数字对数量
        long seen = 0, result = 0;

        for (long value : values) {
            // 1. 计算当前数字的"最高位1的位置"
            // 如果value==0，特殊处理为位置0（虽然题目说输入是正整数，但防御性编程）
            // Long.numberOfLeadingZeros(value): 返回value二进制表示中前导0的个数
            // 64 - 前导0个数 = 二进制长度
            // 再减1得到最高位1的位置索引
            int bit = value == 0 ? 0 : 64 - Long.numberOfLeadingZeros(value);

            // 2. 获取之前遍历过的数字中，与当前数字最高位位置相同的个数
            long same = g.getOrDefault(bit, 0L);

            // 3. 核心计算：
            //    seen - same = 之前遍历过的数字中，最高位位置与当前数字不同的个数
            //    这些数字与当前数字配对，必然满足"差异值 > 相似值"
            //    所以直接累加到结果中
            result += seen - same;

            // 4. 更新统计信息
            //    将当前数字计入它所属的最高位位置分组
            g.put(bit, same + 1);
            //    总计数加1
            seen++;
        }

        return result;
    }


    public static void main(String[] args) {

    }

}
