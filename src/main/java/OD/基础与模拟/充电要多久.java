package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 0:04
 */
public class 充电要多久 {

    /**
     * 计算电池从初始 SOC（State of Charge，荷电状态）充电到目标 SOC 所需的时间。
     * <p>
     * 该实现模拟了锂电池的**三段式充电策略**，不同 SOC 区间使用不同的充电电流：
     * <ul>
     *     <li><b>0% ~ 20%</b>：涓流充电（预充电），电流为最大电流的 20%</li>
     *     <li><b>20% ~ 80%</b>：恒流充电（快速充电），使用最大电流</li>
     *     <li><b>80% ~ 100%</b>：恒压充电（涓流充电），电流线性递减</li>
     * </ul>
     * <p>
     * 第三阶段（80%~100%）电流随 SOC 线性下降：
     * <pre>
     * 电流 = maxCurrent * (1 - (SOC - 80) * 0.045)
     *
     * SOC=80%  → 电流 = maxCurrent * 1.0      (100% 电流)
     * SOC=90%  → 电流 = maxCurrent * 0.55     (55% 电流)
     * SOC=100% → 电流 = maxCurrent * 0.1      (10% 电流)
     * </pre>
     * <p>
     * 最后结果四舍五入到 1 位小数（保留 1 位小数）。
     *
     * @param batteryCapacity 电池容量（单位：mAh）
     * @param maxCurrent      最大充电电流（单位：mA）
     * @param initialSOC      初始 SOC（百分比，0~100）
     * @param targetSOC       目标 SOC（百分比，0~100）
     * @return 充电时间（单位：小时），保留 1 位小数
     */
    public double calculate_charging_time(
            int batteryCapacity,
            int maxCurrent,
            int initialSOC,
            int targetSOC
    ) {
        // ========== 阶段1：0% ~ 20% 涓流充电 ==========
        // 电流为最大电流的 20%
        double result = segment(
                batteryCapacity,
                initialSOC,
                Math.min(targetSOC, 20),  // 最多充到 20%
                maxCurrent * 0.2
        );

        // ========== 阶段2：20% ~ 80% 恒流充电 ==========
        // 使用最大电流快速充电
        result += segment(
                batteryCapacity,
                Math.max(initialSOC, 20),   // 从至少 20% 开始
                Math.min(targetSOC, 80),    // 最多充到 80%
                maxCurrent
        );

        // ========== 阶段3：80% ~ 100% 恒压充电 ==========
        // 电流线性递减（保护电池，避免过充）
        if (targetSOC > Math.max(initialSOC, 80)) {
            int left = Math.max(initialSOC, 80);

            // 起始电流：SOC=80% 时为 100% 电流
            double start = maxCurrent * (1 - (left - 80) * 0.045);

            // 结束电流：SOC=targetSOC 时的电流
            double end = maxCurrent * (1 - (targetSOC - 80) * 0.045);

            // 使用平均电流计算（梯形积分近似）
            result += segment(batteryCapacity, left, targetSOC, (start + end) / 2);
        }

        // ========== 四舍五入到 1 位小数 ==========
        // Math.floor(result * 10 + 0.5 + 1e-9) / 10 实现四舍五入
        // 1e-9 用于修正浮点数精度误差
        return Math.floor(result * 10 + .5 + 1e-9) / 10;
    }

    // 四舍五入
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * 计算在恒定电流下，电池从 left% SOC 充到 right% SOC 所需的时间。
     * <p>
     * 计算公式：
     * <pre>
     * 时间 = 电池容量 * (SOC差值) / 100 / 电流
     *
     * 推导：
     * 充电量 = 电池容量 * (right - left) / 100
     * 时间 = 充电量 / 电流
     * </pre>
     *
     * @param capacity 电池容量（mAh）
     * @param left     起始 SOC（百分比）
     * @param right    结束 SOC（百分比）
     * @param current  充电电流（mA）
     * @return 充电时间（小时），如果 left >= right 则返回 0
     */
    private double segment(long capacity, int left, int right, double current) {
        return right <= left ? 0 : capacity * (right - left) / 100.0 / current;
    }


}
