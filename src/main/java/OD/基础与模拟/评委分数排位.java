package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 12:25
 */
public class 评委分数排位 {

    /**
     * 获取得分最高的前三名玩家编号（从1开始计数）
     * 排序规则：
     * 1. 按总得分从高到低排序
     * 2. 若总得分相同，按各分数段（10分到0分）的出现次数从高到低排序
     * 3. 若仍然相同，按玩家编号从小到大排序
     *
     * @param scores 二维数组，每行代表一轮比赛，每列代表该轮中每个玩家的得分
     * @param playerCount 玩家总数
     * @return 包含前三名玩家编号的数组（从1开始），如果输入不合法则返回空数组
     */
    int[] topScoringPlayers(int[][] scores, int playerCount) {
        // 验证输入：轮数必须在3-10之间，玩家数必须在3-100之间
        if (scores.length < 3 || scores.length > 10 || playerCount < 3 || playerCount > 100)
            return new int[0];

        // total[i] 记录玩家i的总得分（索引从0开始）
        int[] total = new int[playerCount];

        // counts[i][j] 记录玩家i获得j分的次数（j从0到10）
        // 使用11长度是因为分数范围是1-10，索引0位置不使用但保留便于处理
        int[][] counts = new int[playerCount][11];

        // 遍历每一轮比赛
        for (int[] row : scores) {
            // 验证：每轮得分数组长度不能超过玩家总数
            if (row.length > playerCount)
                return new int[0];

            // 遍历该轮中每个玩家的得分
            for (int index = 0; index < row.length; index++) {
                int score = row[index];
                // 验证：得分必须在1-10分之间
                if (score < 1 || score > 10)
                    return new int[0];

                // 累加总得分
                total[index] += score;
                // 记录该得分的出现次数
                counts[index][score]++;
            }
        }

        // 创建玩家索引数组，用于排序
        Integer[] order = new Integer[playerCount];
        for (int index = 0; index < playerCount; index++)
            order[index] = index;

        // 自定义排序：按总得分、各分数段次数、编号进行排序
        Arrays.sort(order, (left, right) -> {
            // 第一优先级：总得分从高到低排序
            if (total[left] != total[right])
                return Integer.compare(total[right], total[left]);

            // 第二优先级：从10分到0分，比较各分数段出现次数（从高到低）
            for (int score = 10; score >= 0; score--) {
                if (counts[left][score] != counts[right][score]) {
                    // 次数多的排在前面（从高到低）
                    return Integer.compare(counts[right][score], counts[left][score]);
                }
            }

            // 第三优先级：编号从小到大排序
            return Integer.compare(left, right);
        });

        // 返回前三名玩家编号（+1是因为题目要求从1开始计数）
        return new int[] {
                order[0] + 1,  // 第一名
                order[1] + 1,  // 第二名
                order[2] + 1   // 第三名
        };
    }

}
