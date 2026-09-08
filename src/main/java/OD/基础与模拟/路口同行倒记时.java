package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 22:09
 */
public class 路口同行倒记时 {

    /**
     *
     * 路口通行计时
     * @param R int — 东西方向红灯时长
     * @param W int — 东西方向绿灯时长
     * @param directs string[] — 车辆方向序列
     * @param arrivalTime int[] — 车辆到达时间序列
     * @return int[]
     */
    public int[] calculateWaitTime(int R, int W, String[] directs, int[] arrivalTime) {
        // write code here


        for(int i = 0; i < directs.length; i++) {
            String direct = directs[i];
            int time = arrivalTime[i];
            if ("E".equals(direct) || "W".equals(direct)) {
//                [0,R-1] [R,R+W-1
                int mod = time % (R + W);
                if (0<= mod && mod <= R -1) {
                    // red
                    int wait = R -mod;
                } else {
                    // green

                }
            }else {

            }


        }

        return new int[0];
    }

    /**
     * 计算十字路口车辆的等待时间
     *
     * 交通灯规则：
     * - 东西方向（E/W）：在绿灯期间（0 到 R-1）通行
     * - 南北方向（S/N）：在红灯期间（R 到 R+W-1）通行
     * - 一个完整周期 = R（东西绿灯）+ W（南北绿灯）
     *
     * 车辆规则：
     * - 同一方向的车需要按顺序通行（不能超车）
     * - 每辆车通过路口需要 1 个时间单位
     * - 如果到达时不是绿灯，需要等待到下一个绿灯周期
     *
     * @param R 东西方向绿灯持续时间
     * @param W 南北方向绿灯持续时间
     * @param directs 车辆方向数组（"E"/"W"/"S"/"N"）
     * @param arrivalTime 每辆车的到达时间
     * @return int[]{等待时间, 最后完成时间}
     */
    public int[] calculateWaitTime2(int R, int W, String[] directs, int[] arrivalTime) {
        // 一个完整交通灯周期
        int cycle = R + W;

        // 记录每个方向上一辆车通过路口的时间（用于保持顺序）
        java.util.Map<String, Integer> next = new java.util.HashMap<>();
        for (String key : new String[]{"E", "W", "S", "N"}) {
            next.put(key, 0);
        }

        // 遍历每辆车
        for (int index = 0; index < directs.length; index++) {
            String direction = directs[index];
            int arrival = arrivalTime[index];

            // 当前方向上一辆车通过的时间，确保车辆按顺序通行
            int lastPassTime = next.get(direction);

            // 计算这辆车最早能开始通行的时间
            // 不能早于到达时间，也不能早于前一辆车通过的时间
            int startTime = Math.max(arrival, lastPassTime);

            // 如果到达时是红灯，需要等待到下一个绿灯周期
            while (isRedLight(direction, startTime, R, cycle)) {
                startTime++;
            }

            // 车辆通过路口（需要 1 个时间单位）
            // 记录该方向下一辆车可通行的时间
            next.put(direction, startTime + 1);
        }

        // 所有车辆完成通行的最后时间
        int lastCompletionTime = java.util.Collections.max(next.values());

        // 返回：第一辆车的等待时间，以及最后完成时间
        // 等待时间 = 最后完成时间 - 第一辆车到达时间
        return new int[]{lastCompletionTime - arrivalTime[0], lastCompletionTime};
    }

    /**
     * 判断指定方向的车辆在给定时间是否遇到红灯
     *
     * @param direction 方向（E/W/S/N）
     * @param time 当前时间
     * @param R 东西方向绿灯时长
     * @param cycle 完整周期
     * @return true 表示红灯，需要等待；false 表示绿灯，可以通行
     */
    private boolean isRedLight(String direction, int time, int R, int cycle) {
        int phase = time % cycle;

        // E/W：东西方向在 [0, R) 时间段为绿灯
        // S/N：南北方向在 [R, cycle) 时间段为绿灯
        if (direction.matches("E|W")) {
            return phase >= R;  // 东西方向在 R 之后是红灯
        } else {  // S/N
            return phase < R;   // 南北方向在 R 之前是红灯
        }
    }

    public static void main(String[] args) {

    }

}
