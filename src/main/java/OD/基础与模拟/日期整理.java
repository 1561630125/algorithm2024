package OD.基础与模拟;

import java.util.LinkedList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 15:58
 */
public class 日期整理 {
    public String[] formatDates(String[] dates) {
        // write code here

        int[] dayMon = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        List<String> res = new LinkedList<>();

        for (int i = 0; i < dates.length; i++) {
            String date = dates[i];
            String[] dateArr = date.split("-", -1);
            if (dateArr.length != 3) {
                continue;
            }
            String year = dateArr[0];
            int yearInt = Integer.parseInt(year);
            String month = dateArr[1];
            int monthInt = Integer.parseInt(month);
            String day = dateArr[2];
            int dayInt = Integer.parseInt(day);

            if (yearInt < 1000 || yearInt > 2000) {
                continue;
            }
            if (monthInt < 1 || monthInt > 12) {
                continue;
            }

            boolean rYear = false;
            if (yearInt % 400 == 0 || (yearInt % 4 == 0 && yearInt % 100 != 0)) {
                rYear = true;
            }

            int limit = dayMon[monthInt];
            if (rYear && monthInt == 2) {
                limit = limit + 1;
            }
            if (dayInt < 1 || dayInt > limit) {
                continue;
            }

            res.add(year + String.format("%02d", monthInt) + String.format("%02d", dayInt));
        }
        return new String[0];
    }

    /**
     * 格式化日期字符串数组，将各种格式的日期统一转换为 "yyyy-MM-dd" 格式
     *
     * @param dates 包含日期字符串的数组，支持 "yyyy/MM/dd" 或 "yyyy-MM-dd" 格式
     * @return 格式化后的日期数组（已排序且去重），如果无有效日期则返回 ["NULL"]
     */
    public String[] formatDates2(String[] dates) {
        // 使用 TreeSet 自动排序（自然顺序）并去重
        java.util.SortedSet<String> answer = new java.util.TreeSet<>();

        // 各月份的天数数组（索引 0 代表 1 月，索引 11 代表 12 月）
        int[] days = {
                31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };

        // 遍历输入的每个日期字符串
        for (String raw : dates) {
            // 判断分隔符：支持 "/" 或 "-"，如果都不包含则跳过
            String delimiter = raw.contains("/") ? "/" : raw.contains("-") ? "-" : null;
            if (delimiter == null)
                continue;  // 跳过不包含有效分隔符的字符串

            // 使用 Pattern.quote() 转义分隔符，防止被当作正则表达式特殊字符
            String[] parts = raw.split(java.util.regex.Pattern.quote(delimiter));
            if (parts.length != 3)
                continue;  // 必须有 3 个部分：年、月、日

            try {
                // 解析年、月、日（注意：这里假设顺序是 年-月-日）
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                // 获取该月份的天数上限（先按平年计算）
                int limit = month >= 1 && month <= 12 ? days[month - 1] : 0;

                // 如果是 2 月且是闰年，天数上限改为 29 天
                if (month == 2 && (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)))
                    limit = 29;

                // 验证日期是否合法：
                // - 年份范围：1000 ~ 2100
                // - 日期在 1 到该月天数上限之间
                if (year >= 1000 && year <= 2100 && day >= 1 && day <= limit)
                    // 格式化为 "yyyy-MM-dd" 并加入集合（自动去重和排序）
                    answer.add(String.format("%04d-%02d-%02d", year, month, day));
            }
            catch (NumberFormatException ignored) {
                // 如果年份/月份/日期无法解析为数字，跳过该条数据
            }
        }

        // 如果没有有效日期，返回 ["NULL"]
        if (answer.isEmpty())
            return new String[]{"NULL"};

        // 将 TreeSet 转为 String 数组返回（已排序且无重复）
        return answer.toArray(new String[0]);
    }


    public static void main(String[] args) {

    }

}
