package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 17:38
 */
public class 数字对阵 {

    /**
     * 田忌赛马最优策略：计算田忌最多能获得的净胜场数
     *
     * @param a 田忌的马的速度数组（可任意顺序）
     * @param b 齐王的马的速度数组（可任意顺序）
     * @return 最大净胜场数（赢的场数 - 输的场数），平局不计分
     */
    int maxSequenceScore(int[] a, int[] b) {
        if (a == null || b == null || a.length != b.length)
            return 0;

        int[] leftValues = java.util.Arrays.copyOf(a, a.length);
        int[] rightValues = java.util.Arrays.copyOf(b, b.length);
        java.util.Arrays.sort(leftValues);
        java.util.Arrays.sort(rightValues);

        int leftA = 0, rightA = a.length - 1;
        int leftB = 0, rightB = b.length - 1;
        int score = 0;

        while (leftA <= rightA) {
            // 情况1：田忌最快的马 > 齐王最快的马 → 赢
            if (leftValues[rightA] > rightValues[rightB]) {
                score++;
                rightA--;
                rightB--;
            }
            // 情况2：田忌最快的马 == 齐王最快的马，尝试用最慢的马赢
            else if (leftValues[rightA] == rightValues[rightB]) {
                if (leftValues[leftA] > rightValues[leftB]) {
                    // 田忌最慢的马 > 齐王最慢的马 → 赢
                    score++;
                    leftA++;
                    leftB++;
                } else {
                    // 田忌最慢的马 ≤ 齐王最慢的马 → 用最慢马消耗齐王最快马
                    if (leftValues[leftA] < rightValues[rightB])
                        score--;  // 输
                    leftA++;
                    rightB--;
                }
            }
            // 情况3：田忌最快的马 < 齐王最快的马 → 用最慢马消耗
            else {
                // 此时 leftValues[rightA] < rightValues[rightB]
                // 田忌最慢的马去消耗齐王最快的马（必输或平）
                if (leftValues[leftA] < rightValues[rightB])
                    score--;  // 输
                leftA++;
                rightB--;
            }
        }
        return score;
    }

}
