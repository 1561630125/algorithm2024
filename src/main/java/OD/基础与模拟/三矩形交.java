package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:39
 */
public class 三矩形交 {




    /**
     * 计算三个矩形重叠区域的面积（交集面积）。
     *
     * <p>矩形表示方式：每个矩形由 [x, y, width, height] 定义
     * <ul>
     *   <li>(x, y) 为矩形左上角坐标</li>
     *   <li>width 为矩形宽度（向右延伸）</li>
     *   <li>height 为矩形高度（向下延伸，注意坐标系：y 向下为正）</li>
     * </ul>
     *
     * <p>算法原理：多个矩形交集仍为矩形，其边界由所有矩形对应方向边界的极值决定
     * <ul>
     *   <li>左边界 = max(所有矩形左边界) —— 取最靠右的左边界</li>
     *   <li>右边界 = min(所有矩形右边界) —— 取最靠左的右边界</li>
     *   <li>下边界 = max(所有矩形下边界) —— 取最靠上的下边界（坐标系 y 向下）</li>
     *   <li>上边界 = min(所有矩形上边界) —— 取最靠下的上边界</li>
     * </ul>
     * 若左边界 < 右边界 且 下边界 < 上边界，则存在重叠区域。
     *
     * @param rectangles 包含恰好 3 个矩形的二维数组，每个矩形为长度为 4 的 long[] 数组
     *                   [x, y, width, height]，其中 height 为正值表示向下延伸
     * @return 三个矩形的交集面积（长 × 宽），若无交集则返回 0L
     */
    long threeRectangleIntersectionArea(long[][] rectangles) {
        // 确保恰好有 3 个矩形
        if (rectangles.length != 3) return 0L;

        // 初始化交集矩形的边界：
        // 左/下边界取最小值（Long.MIN_VALUE），右/上边界取最大值（Long.MAX_VALUE）
        // 这样在后续迭代中，任何矩形的边界都会"收缩"交集范围
        long left = Long.MIN_VALUE,   // 交集左边界（初始为负无穷）
                right = Long.MAX_VALUE,  // 交集右边界（初始为正无穷）
                bottom = Long.MIN_VALUE, // 交集下边界（初始为负无穷，注意 y 向下为正）
                top = Long.MAX_VALUE;    // 交集上边界（初始为正无穷）

        // 遍历每个矩形，逐步收缩交集边界
        for (long[] rectangle : rectangles) {
            // 验证矩形数据格式：必须包含 4 个元素
            if (rectangle.length != 4) return 0L;

            // 解析矩形参数
            long x = rectangle[0];          // 左上角 x 坐标
            long y = rectangle[1];          // 左上角 y 坐标（y 向下为正）
            long otherX = x + rectangle[2]; // 右下角 x 坐标（左 + 宽度）
            long otherY = y - rectangle[3]; // 右下角 y 坐标（上 - 高度，因为 y 向下为正）

            // 计算当前矩形的四个边界
            long rectLeft = Math.min(x, otherX);     // 左边界（较小 x）
            long rectRight = Math.max(x, otherX);    // 右边界（较大 x）
            long rectBottom = Math.min(y, otherY);   // 下边界（较小 y，即更靠上）
            long rectTop = Math.max(y, otherY);      // 上边界（较大 y，即更靠下）

            // 更新交集边界（取极值）：
            // - 左边界取所有矩形左边界的最大值（最靠右的左边）
            // - 右边界取所有矩形右边界的最小值（最靠左的右边）
            // - 下边界取所有矩形下边界的最大值（最靠下的下边，注意 y 轴方向）
            // - 上边界取所有矩形上边界的最小值（最靠上的上边）
            left = Math.max(left, rectLeft);
            right = Math.min(right, rectRight);
            bottom = Math.max(bottom, rectBottom);
            top = Math.min(top, rectTop);
        }

        // 计算交集矩形的宽度和高度：
        // - 宽度 = 右边界 - 左边界（若右 <= 左，则无交集）
        // - 高度 = 上边界 - 下边界（若上 <= 下，则无交集）
        // 使用 Math.max(0L, ...) 确保负值归零（无交集时面积为 0）
        long width = Math.max(0L, right - left);
        long height = Math.max(0L, top - bottom);

        // 返回交集面积 = 宽度 × 高度
        return width * height;
    }


    public static void main(String[] args) {

    }

}
