package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 13:10
 */
public class 淘汰前三名 {

    /**
     * 找出前三名最强选手的索引（基于锦标赛算法）
     *
     * 功能说明：
     * 1. 使用锦标赛排序（淘汰赛）思想，通过两两比较找出最强选手
     * 2. 同时记录亚军和季军候选人
     * 3. 在最后从候选人中选出第三名
     *
     * 排序规则：
     * - 主要按 strength 值从大到小排序
     * - 如果 strength 相同，按索引从小到大排序（编号小者优先）
     *
     * @param strengths 选手实力数组，索引代表选手编号
     * @return 包含前三名选手索引的数组 [冠军, 亚军, 季军]
     *         如果选手少于3人，返回 [-1, -1, -1]
     */
    int[] topThreeCompetitors(long[] strengths) {
        // 验证：至少需要3名选手
        if (strengths.length < 3)
            return new int[]{-1, -1, -1};

        // current：当前轮次所有选手的索引列表
        java.util.List<Integer> current = new java.util.ArrayList<>();
        for (int index = 0; index < strengths.length; index++)
            current.add(index);

        // 存储季军候选人的列表
        java.util.List<Integer> thirdCandidates = new java.util.ArrayList<>();

        int champion = -1;   // 冠军索引
        int runnerUp = -1;   // 亚军索引

        // 锦标赛循环：两两配对比较，直到只剩1个胜者
        while (current.size() > 1) {
            // 存储本轮比赛的胜者和败者
            java.util.List<Integer> winners = new java.util.ArrayList<>();
            java.util.List<Integer> losers = new java.util.ArrayList<>();

            int index = 0;
            // 两两配对进行比赛
            while (index + 1 < current.size()) {
                int left = current.get(index);      // 左边选手
                int right = current.get(index + 1); // 右边选手

                // 比较实力，胜者进入下一轮
                if (strengths[right] > strengths[left]) {
                    // 右边选手更强
                    winners.add(right);  // 右边胜出
                    losers.add(left);    // 左边失败
                } else {
                    // 左边选手更强（或实力相同，左边胜出）
                    winners.add(left);   // 左边胜出
                    losers.add(right);   // 右边失败
                }
                index += 2;  // 处理下一对
            }

            // 如果有落单的选手（奇数个），直接晋级下一轮
            if (index < current.size()) {
                winners.add(current.get(index));
            }

            // 如果只剩1个胜者，说明找到了冠军
            if (winners.size() == 1) {
                champion = winners.get(0);
                // 亚军是冠军在本次比赛中击败的对手
                // 注意：这是亚军候选人，实际亚军需要从所有败者中比较
                runnerUp = losers.get(0);
                break;
            }

            // 记录本轮的所有败者，作为季军候选人
            thirdCandidates = losers;
            // 进入下一轮
            current = winners;
        }

        // 从所有季军候选中选出真正的季军
        // 候选人是所有输给冠军或亚军的选手，其中实力最强的即为季军
        int third = thirdCandidates.get(0);  // 先取第一个候选人作为初始值

        // 遍历所有候选人，找出实力最强的
        for (int candidate : thirdCandidates) {
            // 比较规则：实力强者胜；实力相同则索引小者胜
            if (strengths[candidate] > strengths[third]
                    || (strengths[candidate] == strengths[third] && candidate < third)) {
                third = candidate;
            }
        }

        // 返回前三名：冠军、亚军（从败者中选）、季军
        return new int[]{champion, runnerUp, third};
    }

}
