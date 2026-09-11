package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 15:12
 */
public class 最晚启动 {


    /**
     * 计算最后启动的引擎。
     *
     * 场景：engineCount 个引擎排成一个环（编号 0 ~ engineCount-1）。
     * 给定一系列触发事件，每个事件 (times[i], positions[i]) 表示：
     *   在 times[i] 时刻，位置 positions[i] 处发生触发，影响会沿环传播。
     * 引擎 engine 被该事件触达的时刻 = times[i] + 两者在环上的最短距离。
     * 每个引擎的启动时刻 = 所有事件中最早触达它的时刻。
     *
     * 返回：启动时刻最晚（最后启动）的那些引擎编号，按升序排列。
     * 若参数非法，返回空数组。
     */
    int[] lastStartedEngines(int engineCount, int[] times, int[] positions) {
        // ---------- 参数校验 ----------
        // 引擎数量必须在 (0, 1000]；times 与 positions 长度一致且非空
        if (engineCount <= 0 || engineCount > 1000
                || times.length != positions.length || times.length == 0)
            return new int[0];

        // ---------- 记录每个引擎的最早触达时刻 ----------
        // arrival[engine] = 该引擎被启动的最早时间，初始为"无穷大"（用 MAX/4 防止后续相加溢出）
        long[] arrival = new long[engineCount];
        java.util.Arrays.fill(arrival, Long.MAX_VALUE / 4L);

        // 逐个事件处理
        for (int event = 0; event < times.length; event++) {
            // 触发位置必须落在合法编号范围内
            if (positions[event] < 0 || positions[event] >= engineCount)
                return new int[0];

            // 对每个引擎，计算该事件传播到它所需的最短环形距离
            for (int engine = 0; engine < engineCount; engine++) {
                int direct = Math.abs(positions[event] - engine); // 直线距离
                int distance = Math.min(direct, engineCount - direct); // 环上最短距离（两个方向取小）


//                int forward  = Math.floorMod(engine - positions[event], engineCount); // 顺时针距离
//                int backward = engineCount - forward;                                 // 逆时针距离
//                int distance = Math.min(forward, backward);

//                int delta = Math.floorMod(engine - positions[event], engineCount); // 保证在 [0, n)
//                int distance = Math.min(delta, engineCount - delta);

                // 用该事件的触达时刻更新引擎的最早启动时间
                arrival[engine] = Math.min(arrival[engine], (long) times[event] + distance);
            }
        }

        // ---------- 找出最晚的启动时刻 ----------
        long lastTime = arrival[0];
        for (long time : arrival) lastTime = Math.max(lastTime, time);

        // ---------- 统计有多少引擎在这一时刻启动 ----------
        int count = 0;
        for (long time : arrival) if (time == lastTime) count++;

        // ---------- 收集这些引擎编号（自然升序） ----------
        int[] result = new int[count];
        for (int engine = 0, index = 0; engine < engineCount; engine++) {
            if (arrival[engine] == lastTime) result[index++] = engine;
        }
        return result;
    }

}
