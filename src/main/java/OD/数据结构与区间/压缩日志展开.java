package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 18:30
 */
public class 压缩日志展开 {

    /**
     * 判断是否为闰年
     * <p>
     * 闰年规则：
     * - 能被400整除 → 闰年
     * - 能被4整除但不能被100整除 → 闰年
     * - 其他 → 平年
     *
     * @param year 年份
     * @return true=闰年，false=平年
     */
    boolean batch65Leap(int year) {
        // 优先级：|| 和 && 的混合使用
        // year % 400 == 0 或 (year % 4 == 0 且 year % 100 != 0)
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }

    /**
     * 将时间字符串增加1分钟
     * <p>
     * 输入格式：yyyyMMddHHmm（14位数字字符串）
     * 例如："202409091430" → 2024-09-09 14:30
     *
     * @param value 时间字符串
     * @return 增加1分钟后的时间字符串
     */
    String batch65AddMinute(String value) {
        // 1. 解析时间字符串的各个部分
        int year = Integer.parseInt(value.substring(0, 4));   // 年份
        int month = Integer.parseInt(value.substring(4, 6));  // 月份
        int day = Integer.parseInt(value.substring(6, 8));    // 日期
        int hour = Integer.parseInt(value.substring(8, 10));  // 小时
        int minute = Integer.parseInt(value.substring(10, 12)) + 1;  // 分钟+1

        // 2. 获取当前月份的天数（考虑闰年）
        int[] lengths = {
                31,                                // 1月
                batch65Leap(year) ? 29 : 28,      // 2月
                31,                                // 3月
                30,                                // 4月
                31,                                // 5月
                30,                                // 6月
                31,                                // 7月
                31,                                // 8月
                30,                                // 9月
                31,                                // 10月
                30,                                // 11月
                31                                 // 12月
        };

        // 3. 进位处理
        if (minute == 60) {
            minute = 0;
            hour++;
            if (hour == 24) {
                hour = 0;
                day++;
                if (day > lengths[month - 1]) {
                    day = 1;
                    month++;
                    if (month == 13) {
                        month = 1;
                        year++;
                    }
                }
            }
        }

        // 4. 格式化输出（固定宽度，补零）
        return String.format(
                java.util.Locale.ROOT, "%04d%02d%02d%02d%02d",
                year, month, day, hour, minute
        );
    }

    /**
     * 展开压缩的日志记录，提取查询时间范围内的详细日志
     * <p>
     * 输入格式：
     * - queryRange: [startTime, endTime] 查询时间范围
     * - records: 压缩日志记录数组，每条记录为 [startTime, endTime, kpi]
     * <p>
     * 输出：展开后的每条日志 "时间,KPI值"
     *
     * @param queryRange 查询时间范围 [start, end]
     * @param records    压缩日志记录 [start, end, kpi]
     * @return 展开后的日志数组
     */
    String[] expandCompressedLogs(String[] queryRange, String[][] records) {
        // 1. 验证查询范围
        if (queryRange.length < 2)
            return new String[0];

        String queryStart = queryRange[0];
        String queryEnd = queryRange[1];
        java.util.List<String> output = new java.util.ArrayList<>();

        // 2. 遍历所有记录（假设已按时间排序）
        for (String[] record : records) {
            // 验证记录格式
            if (record.length < 3)
                continue;

            String start = record[0];
            String end = record[1];
            String kpi = record[2];

            // 3. 剪枝：如果记录开始时间 > 查询结束时间，后续记录更晚，可以提前结束
            if (start.compareTo(queryEnd) > 0)
                break;

            // 4. 剪枝：如果记录结束时间 < 查询开始时间，跳过
            if (end.compareTo(queryStart) < 0)
                continue;

            // 5. 计算与查询范围的重叠区间
            String current = start.compareTo(queryStart) > 0 ? start : queryStart;
            String finish = end.compareTo(queryEnd) < 0 ? end : queryEnd;

            // 6. 展开重叠区间的每一分钟
            while (current.compareTo(finish) <= 0) {
                output.add(current + "," + kpi);
                current = batch65AddMinute(current);
            }
        }

        return output.toArray(new String[0]);
    }

}
