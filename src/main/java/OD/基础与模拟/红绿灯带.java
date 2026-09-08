package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 23:47
 */
public class 红绿灯带 {


    /**
     * 对灯带颜色状态进行指定步数的变换，并检测和利用周期优化执行效率。
     * <p>
     * 该方法模拟一个长度为 16 的灯带，每个位置的颜色为 'R'（红色）或 'G'（绿色）。
     * 变换规则由 {@link #update(String)} 方法定义。
     * <p>
     * 核心优化：使用 HashMap 记录所有出现过的状态，当检测到状态循环时，
     * 跳过完整的周期，直接计算剩余步数后的状态，避免无效的重复计算。
     *
     * @param colors 初始颜色状态字符串，长度为 16，由 'R' 和 'G' 组成
     * @param steps  需要执行的总变换步数
     * @return 经过 steps 步变换后的最终颜色状态字符串
     */
    public String lightStripTransform(String colors, int steps) {
        String state = colors;
        long time = 0;
        java.util.Map<String, Long> seen = new java.util.HashMap<>();

        // 执行变换，直到达到步数限制或发现周期
        while (time < steps && !seen.containsKey(state)) {
            seen.put(state, time++);  // 记录当前状态及其出现的时间步
            state = update(state);    // 执行一步变换
        }

        // 如果因为发现周期而提前退出（而非达到 steps）
        if (time < steps) {
            // 计算周期长度：当前时间 - 状态首次出现的时间
            long cycleLength = time - seen.get(state);

            // 计算还需要执行的步数（跳过完整的周期）
            long remaining = (steps - time) % cycleLength;

            // 执行剩余的步数
            while (remaining-- > 0)
                state = update(state);
        }

        return state;
    }

    /**
     * 对灯带状态执行一步更新。
     * <p>
     * 更新规则（基于元胞自动机）：
     * <ul>
     *     <li>灯带两端（索引 0 和 15）固定为 'R'（红色）</li>
     *     <li>中间位置（索引 1~14）：如果左右相邻颜色相同，则变为 'G'（绿色），否则变为 'R'（红色）</li>
     * </ul>
     *
     * 规则示例：
     * <pre>
     * 位置 5 的左右邻居分别为 'R' 和 'R' → 相同 → 新状态为 'G'
     * 位置 5 的左右邻居分别为 'R' 和 'G' → 不同 → 新状态为 'R'
     * </pre>
     *
     * @param value 当前颜色状态字符串，长度必须为 16
     * @return 更新后的颜色状态字符串
     */
    private String update(String value) {
        StringBuilder answer = new StringBuilder();
        for (int index = 0; index < 16; index++) {
            answer.append(
                    // 两端固定为 'R'
                    index == 0 || index == 15
                            ? 'R'
                            // 中间位置：比较左右邻居
                            : value.charAt(index - 1) == value.charAt(index + 1)
                            ? 'G'   // 左右相同 → 绿色
                            : 'R'   // 左右不同 → 红色
            );
        }
        return answer.toString();
    }

}
