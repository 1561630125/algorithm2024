package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 16:18
 */
public class 车队末车耗时 {

    String transportTime(long distance, long[] speeds) {
        double arrival = 0;

        // 1. 计算所有工具到达目的地的时间，取最大值
        for (int i = 0; i < speeds.length; i++)
            // 第i个工具：行驶时间(distance/speeds[i]) + 出发延迟(i)
            arrival = Math.max(arrival, (double) distance / speeds[i] + i);

        // 2. 减去最后一个工具的出发延迟
        // 如果 speeds.length-1 < 0（空数组），取0避免负数
        double value = arrival - Math.max(speeds.length - 1, 0);

        // 3. 格式化：保留3位小数
        String text = String.format(java.util.Locale.ROOT, "%.3f", value);

        // 4. 去除末尾多余的0（如 "1.230" -> "1.23"）
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) == '0')
            end--;

        // 5. 如果末尾是小数点，也去掉（如 "1." -> "1"）
        if (end > 0 && text.charAt(end - 1) == '.')
            end--;

        text = text.substring(0, end);

        // 6. 处理特殊情况："-0" 显示为 "0"
        return text.equals("-0") ? "0" : text;
    }


}
