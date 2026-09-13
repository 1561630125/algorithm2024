package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 13:56
 */
public class 日志时间排序 {
    class Solution {

        // 对日志列表按时间排序，返回一个新的有序列表
        java.util.List<String> sortLogs(java.util.List<String> logs) {
            // 复制一份原始列表，避免修改传入的 logs（保护入参）
            java.util.List<String> result = new java.util.ArrayList<>(logs);

            // 使用 Comparator，根据 timestamp(...) 计算出的毫秒值进行比较并排序
            // comparingInt 需要传入一个 “把元素转成 int” 的函数
            // this::timestamp 就是 “用当前对象的 timestamp 方法” 来做这个转换
            result.sort(java.util.Comparator.comparingInt(this::timestamp));

            // 返回排好序的列表
            return result;
        }

        // 把形如 "HH:MM:SS.mmm" 的时间字符串转成从 0 点开始算起的毫秒数
        private int timestamp(String value) {
            // 按 ":" 或 "." 分割，例如 "12:34:56.789"
            // 会得到 ["12", "34", "56", "789"]
            String[] parts = value.split("[:.]");

            int hour = Integer.parseInt(parts[0]);        // 小时
            int minute = Integer.parseInt(parts[1]);      // 分钟
            int second = Integer.parseInt(parts[2]);      // 秒
            int millisecond = Integer.parseInt(parts[3]); // 毫秒

            // 统一换算成毫秒：
            // 小时 -> 分钟 -> 秒 -> 毫秒
            return ((hour * 60 + minute) * 60 + second) * 1000 + millisecond;
        }
    }

}
