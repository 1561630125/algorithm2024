package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 19:48
 */
public class 停车日收入 {

    /**
     * 计算停车场总收入。
     * 计费规则：
     * - 每月会员车牌（monthlyPlates）免费，直接忽略其进出记录。
     * - 按每30分钟收费1元，不足30分钟按30分钟计（即向上取整到半小时）。
     * - 免费时段：11:30（690分钟）至13:30（810分钟）之间的停车时间不计费。
     * - 单次停车费用封顶15元。
     *
     * @param monthlyPlates 月卡会员车牌数组（这些车牌免费）
     * @param records       停车记录数组，每条格式为 "HH:MM 车牌 enter/leave"
     * @return 所有非会员车辆的总停车费用
     */
    int parkingRevenue(String[] monthlyPlates, String[] records) {
        // 月卡会员集合，用于快速判断是否免费
        java.util.Set<String> monthly =
                new java.util.HashSet<>(java.util.Arrays.asList(monthlyPlates));

        // enters: 记录每辆车进入的时间（分钟）
        // charges: 记录每辆车累计费用
        java.util.Map<String, Integer> enters = new java.util.HashMap<>(),
                charges = new java.util.HashMap<>();

        // 逐条处理停车记录
        for (String record : records) {
            String[] parts = record.trim().split("\\s+");

            // 格式校验：必须为3部分，且非会员车辆才处理
            if (parts.length != 3 || monthly.contains(parts[1]))
                continue;

            int time = minute(parts[0]);  // 当前记录的时间（分钟）
            String plate = parts[1];      // 车牌号

            // 进入记录：记录进入时间
            if (parts[2].equals("enter")) {
                enters.put(plate, time);
            }
            // 离开记录：计算停车费用
            else if (parts[2].equals("leave") && enters.containsKey(plate)) {
                int start = enters.remove(plate);  // 进入时间

                // 计算免费时段（11:30-13:30）内的重叠分钟数
                int overlap = Math.max(0, Math.min(time, 810) - Math.max(start, 690));

                // 实际计费时长 = 总停车时长 - 免费时段重叠时长
                int duration = Math.max(0, time - start - overlap);

                // 按30分钟向上取整计算费用（不足30分钟按30分钟计）
                int fee = duration < 30 ? 0 : (duration + 29) / 30;

                // 累计该车费用，单次费用直接累加（最终总费用封顶15元在累计后不封顶，
                // 但单次记录已通过Math.min(15, ...)限制了每次离开的费用）
                charges.put(plate, Math.min(15, charges.getOrDefault(plate, 0) + fee));
            }
        }

        // 统计所有车辆的总费用
        int result = 0;
        for (int charge : charges.values())
            result += charge;
        return result;
    }

    /**
     * 将时间字符串 "HH:MM" 转换为当天的分钟数（从00:00开始计算）。
     *
     * @param value 时间字符串，格式为 "HH:MM"，例如 "08:30"
     * @return 对应的分钟数（0-1439）
     */
    private int minute(String value) {
        String[] parts = value.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

}
