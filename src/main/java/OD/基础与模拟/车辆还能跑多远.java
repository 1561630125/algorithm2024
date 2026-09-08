package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 22:34
 */
public class 车辆还能跑多远 {
    /**
     * 车辆还能跑多远
     *
     * @param capacity   double — 电池标称容量
     * @param efficiency double — 百公里能耗
     * @param type       int — 车型类型
     * @return long
     */
    public long calculateRange(double capacity, double efficiency, int type) {
        // write code here

        double floor = Math.floor(capacity);

        double d80 = Math.min(80, floor) * 0.95;
        double d160 = floor <= 80 ? 0 : Math.min(floor - 80, 80) * 0.98;

        double d300 = floor <= 160 ? 0 : Math.min(floor - 160, 160) * 0.98;

        return 0;
    }

    /**
     * 计算分段收费金额
     * @param distance 行驶里程 (KM)
     * @param basePrice 基础单价 (元/KM)
     * @return 总费用
     */
    public static double calculate(double distance, double basePrice) {
        if (distance <= 0) {
            return 0;
        }

        double totalCost = 0.0;
        double remaining = distance;

        // 1. 处理超过 160 KM 的部分 (系数 0.92)
        if (remaining > 160) {
            double segment = remaining - 160;
            totalCost += segment * basePrice * 0.92;
            remaining = 160;
        }

        // 2. 处理 80 ~ 160 KM 的部分 (系数 0.95)
        if (remaining > 80) {
            double segment = remaining - 80;
            totalCost += segment * basePrice * 0.95;
            remaining = 80;
        }

        // 3. 处理 0 ~ 80 KM 的部分 (系数 0.98)
        if (remaining > 0) {
            totalCost += remaining * basePrice * 0.98;
        }

        return totalCost;
    }


    public long calculateRange2(double capacity, double efficiency, int type) {
        int units = (int)capacity;
        double equivalent = 0;
        for (int unit = 1; unit <= units; unit++) {
            equivalent += unit <= 80 ? .95 : unit <= 160 ? .98 : 1;
        }
        double[] factors = {1, .92, .85};
        return (long)Math.floor(
                equivalent / efficiency * 100 * .956 * factors[type] + .5
        );
    }


    public static void main(String[] args) {

    }

}
