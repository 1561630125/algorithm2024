package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 15:41
 */
public class 最短走位替换 {


    /**
     * 计算使行走路线完美平衡所需的最短子串替换长度
     *
     * 问题背景：
     * 给定一个由 'W','A','S','D' 组成的移动序列（分别代表上、左、下、右），
     * 需要修改最少的连续子串，使四种移动出现次数相等（完美平衡）。
     *
     * 例如：moves = "WASDA"
     * - 长度5，无法被4整除，返回-1
     * - 如果可以，目标每种字符出现 length/4 次
     *
     * @param moves 由 'W','A','S','D' 组成的字符串
     * @return 需要替换的最短连续子串长度，如果无法平衡返回-1
     */
    int minPerfectWalkReplacement(String moves) {
        int length = moves.length();

        // 1. 长度必须能被4整除，否则无法平均分配四种移动
        // 因为需要四种字符出现次数相等，每种出现 length/4 次
        if (length % 4 != 0) return -1;

        // 2. 统计每种字符出现的总次数
        // 数组索引映射：0='W', 1='A', 2='S', 3='D'
        int[] counts = new int[4];
        for (int index = 0; index < length; index++) {
            counts[moveIndex(moves.charAt(index))]++;
        }
        // 例如 "WASD" → counts = [1,1,1,1]，平衡 ✅
        // 例如 "WWAA" → counts = [2,2,0,0]，不平衡 ❌

        // 3. 计算每种字符需要减少的数量（即需要替换掉的数量）
        int[] required = new int[4];
        int target = length / 4;  // 目标：每种字符应出现的次数
        int totalRequired = 0;
        for (int index = 0; index < 4; index++) {
            // 如果某种字符出现次数超过目标，需要将多余的替换掉
            required[index] = Math.max(0, counts[index] - target);
            totalRequired += required[index];
        }
        // 例如 "WWAA" (length=4, target=1)
        // counts = [2,2,0,0]
        // required = [1,1,0,0]，需要替换2个字符

        // 4. 如果所有字符已经平衡，不需要任何替换
        if (totalRequired == 0) return 0;

        // 5. 使用滑动窗口找到最短子串，该子串包含所有需要替换的字符
        // 窗口内的字符可以被替换，所以窗口必须包含所有"多余"的字符
        int[] window = new int[4];  // 当前窗口中各字符的计数
        int left = 0;
        int best = length;  // 初始化最大可能长度

        // 滑动窗口：right 指针向右扩展
        for (int right = 0; right < length; right++) {
            // 将右指针指向的字符加入窗口
            window[moveIndex(moves.charAt(right))]++;

            // 当窗口满足条件（覆盖了所有需要替换的字符）时，尝试收缩左边界
            while (covers(window, required)) {
                // 更新最佳答案：当前窗口长度
                best = Math.min(best, right - left + 1);

                // 移除左指针指向的字符，尝试缩小窗口
                window[moveIndex(moves.charAt(left++))]--;
            }
            // 如果不满足条件，继续扩展右指针
        }

        return best;
    }

    /**
     * 将字符映射为数组索引
     * @param move 移动字符 'W','A','S','D'
     * @return 对应的数组索引 0,1,2,3
     */
    private int moveIndex(char move) {
        return "WASD".indexOf(move);
        // 'W' → 0, 'A' → 1, 'S' → 2, 'D' → 3
    }

    /**
     * 检查窗口是否覆盖了所有需要的字符
     * @param window 当前窗口内各字符的计数
     * @param required 需要替换的字符数量（每种字符需要减少的数量）
     * @return 如果窗口包含所有需要替换的字符，返回true
     */
    private boolean covers(int[] window, int[] required) {
        for (int index = 0; index < 4; index++) {
            if (window[index] < required[index]) return false;
            // 窗口必须至少有 required[index] 个第index种字符
            // 因为替换时可以用这些字符替换掉其他字符
        }
        return true;
    }


}
