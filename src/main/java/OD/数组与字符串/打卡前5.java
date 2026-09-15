package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:44
 */
public class 打卡前5 {

    class Solution {

        /**
         * 统计所有学生（id）的"出勤天数"，按规则排序后取前 5 名。
         *
         * 排序规则（优先级从高到低）：
         *   1. 出勤次数多的排前面（降序）
         *   2. 次数相同时，第一次出现的 day 小的排前面（升序）
         *   3. 还相同时，id 小的排前面（升序）
         *
         * @param dailyIds 二维数组，dailyIds[day] = 第 day 天出勤的学生 id 列表
         * @return         前 5 名学生的 id 数组；不足 5 个时返回全部
         */
        long[] topAttendanceStudents(long[][] dailyIds) {

            // statistics: id -> [出勤次数, 首次出现的 day]
            java.util.Map<Long, long[]> statistics = new java.util.HashMap<>();

            // ---- 1. 统计每个学生的出勤次数 + 首次出现的 day ----
            for (int day = 0; day < dailyIds.length; day++)
                for (long id : dailyIds[day]) {
                    long[] value = statistics.get(id);
                    if (value == null) {
                        // 首次见到该学生：初始化 [次数=0, 首次day]
                        value = new long[] {0, day};
                        statistics.put(id, value);
                    }
                    value[0]++;   // 出勤次数 +1
                }

            // ---- 2. 取出所有 id，按规则排序 ----
            java.util.List<Long> ordered = new java.util.ArrayList<>(statistics.keySet());

            ordered.sort((a, b) -> {
                long[] x = statistics.get(a), y = statistics.get(b);

                // 规则1：出勤次数降序（多的在前）
                int result = Long.compare(y[0], x[0]);
                if (result == 0)
                    // 规则2：首次出现 day 升序（早的在前）
                    result = Long.compare(x[1], y[1]);
                if (result == 0)
                    // 规则3：id 升序（小的在前）
                    result = Long.compare(a, b);
                return result;
            });

            // ---- 3. 取前 min(5, size) 名 ----
            long[] output = new long[Math.min(5, ordered.size())];
            for (int i = 0; i < output.length; i++)
                output[i] = ordered.get(i);
            return output;
        }
    }

}
