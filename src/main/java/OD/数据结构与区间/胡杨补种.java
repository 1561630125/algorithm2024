package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 14:56
 */
public class 胡杨补种 {


    static int maxPoplarRun(int total, int[] dead, int replacements) {
        // 获取死亡杨树的数量
        int count = dead.length;

        // 创建一个新数组，包含所有死亡位置，并在末尾添加一个哨兵值 total+1
        int[] ordered = java.util.Arrays.copyOf(dead, count + 1);
        ordered[count] = total + 1;

        // 对数组进行排序，方便后续计算连续存活区间
        java.util.Arrays.sort(ordered);

        // 限制 replacements 在有效范围内（0 到 count 之间）
        replacements = Math.min(Math.max(replacements, 0), count);

        int best = 0;  // 记录最长的连续存活杨树数量

        // 遍历每个可能的起始死亡位置
        for (int index = 0; index <= count; index++) {
            // 计算当前区间的左边界：如果 index 为 0，从第 1 棵树开始；
            // 否则从上一棵死亡树的下一棵树开始
            int left = index == 0 ? 1 : ordered[index - 1] + 1;

            // 计算当前区间的右边界：如果 index + replacements 超出数组范围，
            // 则使用哨兵值 total+1 的前一棵树；否则使用第 index+replacements 棵死亡树的前一棵树
            int right = index + replacements >= count
                    ? ordered[count] - 1 : ordered[index + replacements] - 1;

            // 计算当前区间长度（right - left + 1），更新最大值
            best = Math.max(best, right - left + 1);
        }

        // 返回最长的连续存活杨树数量
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxPoplarRun(10, new int[]{5}, 1));
    }


}
