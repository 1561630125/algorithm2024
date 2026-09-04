package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 12:37
 */
public class 最强信号坐标 {


    /**
     * 寻找最佳信号位置
     * 在给定访问点集合的矩形区域内，找到信号质量总和最大的位置
     *
     * 信号质量计算规则：
     * - 使用切比雪夫距离（Chebyshev distance）计算到每个访问点的距离
     * - 如果距离在半径范围内，信号质量 = floorDivide(信号强度, 距离 + 1)
     * - 总信号质量为所有访问点贡献之和
     *
     * @param accessPoints 访问点数组，每个点包含 [x坐标, y坐标, 信号强度]
     * @param radius 信号覆盖半径
     * @return 最佳信号位置 [x坐标, y坐标]，如果访问点为空则返回 [0, 0]
     */
    int[] bestSignalLocation(int[][] accessPoints, int radius) {
        // 处理空输入：如果没有访问点，直接返回原点
        if (accessPoints.length == 0)
            return new int[]{0, 0};

        // 计算所有访问点的最大坐标值，确定搜索范围
        int maxX = 0;
        int maxY = 0;
        for (int[] point : accessPoints) {
            maxX = Math.max(maxX, point[0]);  // 获取最大X坐标
            maxY = Math.max(maxY, point[1]);  // 获取最大Y坐标
        }

        // 初始化最佳信号质量和最佳位置
        long bestQuality = Long.MIN_VALUE;  // 使用最小值确保任何质量都能更新
        int[] best = new int[]{0, 0};       // 默认位置为原点

        // 遍历矩形区域内的每个位置（从0到最大坐标）
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                // 计算当前位置的信号质量总和
                long quality = 0;

                // 遍历所有访问点，累加信号贡献
                for (int[] point : accessPoints) {
                    // 使用切比雪夫距离：max(|x1-x2|, |y1-y2|)
                    // 这种距离适合模拟正方形覆盖区域
                    int distance = Math.max(Math.abs(x - point[0]), Math.abs(y - point[1]));

                    // 如果距离在覆盖半径内，计算该访问点对当前位置的信号贡献
                    if (distance <= radius) {
                        // 信号质量 = floorDivide(信号强度, 距离 + 1)
                        // 距离越近，信号越强；距离越远，信号衰减越明显
                        quality += floorDivide(point[2], distance + 1);
                    }
                }

                // 更新最佳位置：如果当前质量优于之前记录的最佳质量
                if (quality > bestQuality) {
                    bestQuality = quality;
                    best[0] = x;
                    best[1] = y;
                }
            }
        }

        return best;
    }

    /**
     * 执行整数除法，向负无穷方向舍入（floor division）
     *
     * 例如：
     * - floorDivide(10, 3) = 3   (10/3 = 3.33... 向下取整 = 3)
     * - floorDivide(-10, 3) = -4  (-10/3 = -3.33... 向下取整 = -4)
     *
     * @param value 被除数
     * @param divisor 除数（必须为正数）
     * @return 向下取整的商
     */
    private long floorDivide(long value, long divisor) {
        // 标准整数除法向零舍入（截断小数部分）
        long quotient = value / divisor;

        // 如果被除数为负数且不能被整除，需要再减1以实现向下取整
        // 例如：-10 / 3 = -3（向零舍入），但我们需要 -4（向下取整）
        if (value < 0 && value % divisor != 0) {
            quotient--;  // 调整为向下取整
        }
        return quotient;
    }

}
