package OD.数据结构与区间;

import java.util.Locale;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 21:08
 */
public class 固定瑕疵元音段 {


    /**
     * 寻找包含最多 flaw 个非元音字符的最长连续元音子串
     *
     * 核心思路：
     * 1. 找出所有元音字母在原字符串中的位置
     * 2. 在这些位置中找最长的连续区间，使得区间内非元音字符数 <= flaw
     *
     * 非元音字符数 = 区间跨度 - 区间内元音个数
     *              = (positions[right] - positions[left]) - (right - left)
     *
     * @param value 输入字符串
     * @param flaw  允许的非元音字符数量（容错数）
     * @return 满足条件的最长子串长度
     */
    static public int longestVowelSubstringWithFlaw2(String value, int flaw) {
        // 边界检查：空字符串或非法容错数直接返回0
        if (value == null || flaw < 0)
            return 0;

        // positions数组：存储所有元音字母在原字符串中的索引位置
        // 例如：value = "abaei", 元音在索引 0,2,3,4 → positions = [0,2,3,4,0,0,...]
        int[] positions = new int[value.length()];
        int count = 0;  // 元音字母的总个数

        // 第一遍扫描：收集所有元音的位置
        for (int index = 0; index < value.length(); index++) {
            if (isVowel(value.charAt(index))) {
                positions[count++] = index;
            }
        }

        int best = 0;   // 记录找到的最长子串长度
        int left = 0;   // 滑动窗口的左边界（在positions数组上的索引）

        // 第二遍扫描：用滑动窗口在元音位置数组上找最优区间
        // right 是窗口右边界（在positions数组上的索引）
        for (int right = 0; right < count; right++) {
            /**
             * 计算当前窗口 [left, right] 中的非元音字符数：
             *
             * positions[right] - positions[left]  = 窗口跨越的总长度（从第一个元音到最后一个元音）
             * right - left                       = 窗口内元音的个数（减1的关系）
             * 差值                               = 窗口中非元音的个数
             *
             * 例如：value = "aXbYe", 元音在 [0,3,4]
             * left=0, right=2: 跨度=4-0=4, 元音数=2, 非元音=4-2=2 ✓
             */
            int loop = 0;
            while (left <= right && positions[right] - positions[left] - (right - left) > flaw) {
                System.out.println("++++" + value.substring(positions[left],positions[right]+1));
                left++;  // 如果非元音数超过容错，缩小左边界
                /*loop++;
                if (loop > 1) {
                    System.out.println(value.charAt(positions[left]) +" " + value.charAt(positions[right]) + " " + right);
                    System.out.println("-----");
                }*/
            }

            /**
             * 当窗口内非元音数恰好等于flaw时，这是一个合法的子串
             *
             * 子串长度 = 最后一个元音位置 - 第一个元音位置 + 1
             * 因为我们要包含从第一个元音到最后一个元音的整个区间
             *
             * 注意：这实际包含了两端的元音和中间的所有字符（包括非元音）
             */
            if (left <= right && positions[right] - positions[left] - (right - left) == flaw) {
                int currentLength = positions[right] - positions[left] + 1;
                best = Math.max(best, currentLength);
                System.out.println("----" + value.substring(positions[left],positions[right]+1));
            }
        }
        System.out.println("00000");
        return best;
    }

    /**
     * 判断字符是否为元音（支持大小写）
     */
    static private boolean isVowel(char value) {
        // 检查字符是否在 "aeiouAEIOU" 字符串中
        return "aeiouAEIOU".indexOf(value) >= 0;
    }

    public static void main(String[] args) {
        System.out.println(longestVowelSubstringWithFlaw2("asdbuiodevauufgh",1));
    }


    int longestVowelSubstringWithFlaw(String value, int flaw) {

        // 判断首、尾字符是否是元音字母，并统计瑕疵
        String toLowerCase = value.toLowerCase(Locale.ROOT);


        return 0;
    }
}
