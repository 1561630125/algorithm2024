package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 13:21
 */
public class 点集正方形 {

    /**
     * 检查给定坐标点是否存在于点集中
     *
     * @param points 点集数组，每个点包含 [x, y] 坐标
     * @param x 要检查的x坐标
     * @param y 要检查的y坐标
     * @return 如果点存在则返回true，否则返回false
     */
    private boolean squarePointExists(long[][] points, long x, long y) {
        // 遍历所有点，查找匹配的坐标
        for (long[] point : points) {
            // 检查点格式正确且坐标匹配
            if (point.length == 2 && point[0] == x && point[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算给定点集中可以组成正方形的数量
     *
     * 算法原理：
     * 1. 枚举所有点对作为正方形的一条边（对角线或边）
     * 2. 根据几何关系计算正方形的另外两个顶点
     * 3. 检查这两个顶点是否在点集中
     * 4. 每对点会产生两个可能的方向（顺时针和逆时针）
     *
     * 数学推导：
     * 给定两个点 A(x1,y1) 和 B(x2,y2) 作为正方形的一条边
     * 向量 AB = (dx, dy) = (x2-x1, y2-y1)
     * 正方形另外两个顶点可以通过旋转90度得到：
     *
     * 顺时针旋转：C = (x1 - dy, y1 + dx), D = (x2 - dy, y2 + dx)
     * 逆时针旋转：C = (x1 + dy, y1 - dx), D = (x2 + dy, y2 - dx)
     *
     * 其中 dx = x2 - x1, dy = y2 - y1
     *
     * @param points 点集数组，每个点包含 [x, y] 坐标
     * @return 可以组成的正方形数量
     */
    long countReferenceSquares(long[][] points) {
        // 验证：所有点必须包含2个坐标值
        for (long[] point : points) {
            if (point.length != 2) {
                return 0L;  // 格式不正确，返回0
            }
        }

        long count = 0L;  // 正方形计数器

        // 枚举所有点对作为正方形的边
        for (int first = 0; first < points.length; first++) {
            long x1 = points[first][0];  // 第一个点的x坐标
            long y1 = points[first][1];  // 第一个点的y坐标

            for (int second = first + 1; second < points.length; second++) {
                long x2 = points[second][0];  // 第二个点的x坐标
                long y2 = points[second][1];  // 第二个点的y坐标

                // 计算向量差
                long deltaY = y1 - y2;  // y方向的差
                long deltaX = x1 - x2;  // x方向的差

                // 方向1：顺时针旋转90度
                // 计算正方形另外两个顶点
                // C = (x1 - deltaY, y1 + deltaX)
                // D = (x2 - deltaY, y2 + deltaX)
                if (squarePointExists(points, x1 - deltaY, y1 + deltaX)
                        && squarePointExists(points, x2 - deltaY, y2 + deltaX)) {
                    count++;  // 找到一个正方形
                }

                // 方向2：逆时针旋转90度
                // 计算正方形另外两个顶点
                // C = (x1 + deltaY, y1 - deltaX)
                // D = (x2 + deltaY, y2 - deltaX)
                if (squarePointExists(points, x1 + deltaY, y1 - deltaX)
                        && squarePointExists(points, x2 + deltaY, y2 - deltaX)) {
                    count++;  // 找到另一个正方形（可能和方向1是同一个）
                }
            }
        }

        // 由于每个正方形被计算了4次（4条边都被枚举到），需要除以4
        // 例如：正方形有4条边 AB, BC, CD, DA，每条边都会被枚举一次
        // 且在枚举每条边时，可能会产生两个方向，但只有一个是正确的
        // 所以每个正方形会被计数4次
        return count / 4L;
    }

}
