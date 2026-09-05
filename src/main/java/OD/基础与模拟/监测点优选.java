package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 23:59
 */
public class 监测点优选 {
    long[][] selectNucleicAcidSites(
            int startHour,
            int startMinute,
            int deadlineHour,
            int deadlineMinute,
            long[][] sites
    ) {
        return new long[0][];
    }

    /**
     * 选择最优核酸检测点
     * @param startHour 出发小时
     * @param startMinute 出发分钟
     * @param deadlineHour 截止小时
     * @param deadlineMinute 截止分钟
     * @param sites 检测点数组 [ID, 距离(公里), 当前排队人数]
     * @return 符合条件的检测点 [ID, 总耗时(分钟), 总费用(元)]
     */
    long[][] selectNucleicAcidSites2(
            int startHour, int startMinute, int deadlineHour, int deadlineMinute, long[][] sites) {

        // 1. 时间转换：将时分转换为分钟数，便于计算
        long start = startHour * 60L + startMinute;        // 出发时间（分钟）
        long deadline = Math.min(deadlineHour * 60L + deadlineMinute, 1200L); // 截止时间，最晚20:00

        // 2. 定义检测点人数变化的区间规则（每分钟变化量）
        // 格式：[区间起始, 区间结束, 分子, 分母]
        // 表示在该时间段内，每分钟增加 (分子/分母) 人
        long[][] intervals = {
                {480, 600, 2, 1},     // 8:00-10:00：每分钟 +2人（实际应为+3人，可能此处简化）
                {600, 720, -4, 5},    // 10:00-12:00：每分钟 -0.8人（非高峰期，排队减少）
                {720, 840, 9, 1},     // 12:00-14:00：每分钟 +9人（实际应为+10人，可能此处简化）
                {840, 1080, -4, 5},   // 14:00-18:00：每分钟 -0.8人（非高峰期）
                {1080, 1200, 19, 1}   // 18:00-20:00：每分钟 +19人（实际应为+20人，可能此处简化）
        };

        // 3. 存储符合条件的检测点
        java.util.List<long[]> result = new java.util.ArrayList<>();

        // 4. 遍历每个检测点
        for (long[] site : sites) {
            long id = site[0];           // 检测点ID
            long distance = site[1];     // 距离（公里）
            long waiting = site[2];      // 当前排队人数
            long arrival = start + distance * 10;  // 到达时间 = 出发时间 + 路程时间（每公里10分钟）
            long cost = distance * 10;   // 费用 = 距离 × 10元/公里

            // 5. 提前过滤：如果到达时间已经超过截止时间，直接跳过
            if (deadline <= arrival)
                continue;

            // 6. 如果早于8点到达，需要等到8点才开始检测
            if (arrival < 480)
                arrival = 480;
            else {
                // 7. 计算路程中排队人数的动态变化
                // 遍历每个时间段，计算从出发到到达期间人数变化量
                for (long[] interval : intervals) {
                    // 计算当前时间段与[出发时间, 到达时间]的交集时长
                    long duration = Math.max(0,
                            Math.min(arrival, interval[1]) - Math.max(start, interval[0]));

                    if (duration > 0) {
                        long numerator = interval[2];    // 分子
                        long denominator = interval[3];  // 分母

                        // 计算人数变化量（注意处理负数和整数除法）
                        long delta = numerator >= 0
                                ? duration * numerator / denominator  // 正增长：直接乘除
                                : -((duration * (-numerator) + denominator - 1) / denominator); // 负增长：向上取整

                        // 更新排队人数（不能小于0）
                        waiting = Math.max(0, waiting + delta);
                    }
                }
            }

            // 8. 计算总耗时 = 路程时间 + 等待时间（排队人数×每人1分钟）
            long total = arrival - start + waiting;

            // 9. 判断是否能在截止时间前完成检测
            if (start + total < deadline)
                result.add(new long[] {id, total, cost});
        }

        // 10. 按优选规则排序：
        //     规则1：总耗时最少优先
        //     规则2：耗时相同，费用最少优先
        //     规则3：时间和费用都相同，ID最小优先
        result.sort((a, b)
                -> a[1] != b[1] ? Long.compare(a[1], b[1])  // 按总耗时排序
                : a[2] != b[2] ? Long.compare(a[2], b[2])   // 按费用排序
                : Long.compare(a[0], b[0]));                // 按ID排序

        // 11. 返回结果数组
        return result.toArray(new long[0][]);
    }


    public static void main(String[] args) {

    }

}
