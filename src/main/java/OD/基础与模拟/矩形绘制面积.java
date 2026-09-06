package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 15:49
 */
public class 矩形绘制面积 {


    int finalDrawingArea2(String types, int[][] rectangles) {

        int SIZE = 201;
        int OFFSET = 100;
        boolean[][] grid = new boolean[SIZE][SIZE];
        for (int index = 0; index < rectangles.length; index++) {
            int[] rectangle = rectangles[index];

            int xStart = Math.min(rectangle[0], rectangle[2]) + OFFSET;
            int xEnd = Math.max(rectangle[0], rectangle[2]) + OFFSET;
            int yStart = Math.min(rectangle[1], rectangle[3]) + OFFSET;
            int yEnd = Math.max(rectangle[1], rectangle[3]) + OFFSET;

            for (int x = xStart; x < xEnd; x++) {
                for (int y = yStart; y < yEnd; y++) {
                    // 擦除
                    grid[x][y] = types.charAt(index) == 'd'; // 绘制
                }
            }

        }

        int area = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j]) {
                    area++;
                }
            }
        }

        return area;

    }


    public static void main(String[] args) {

    }

    /**
     * 计算最终绘制区域的总面积
     * @param types 一个字符串，每个字符对应rectangles中相同索引矩形的操作类型：
     *              'd' 表示绘制（填充），其他字符表示擦除（清空）
     * @param rectangles 二维数组，每个元素是 [x1, y1, x2, y2] 表示一个矩形
     * @return 最终被绘制的总面积（int类型）
     */
    int finalDrawingArea(String types, int[][] rectangles) {
        // 如果没有矩形，面积为0
        if (rectangles.length == 0)
            return 0;

        // 使用TreeSet收集所有矩形的x坐标和y坐标，自动排序并去重
        java.util.TreeSet<Integer> xSet = new java.util.TreeSet<>();
        java.util.TreeSet<Integer> ySet = new java.util.TreeSet<>();
        for (int[] rectangle : rectangles) {
            xSet.add(rectangle[0]);  // 左边界
            xSet.add(rectangle[2]);  // 右边界
            ySet.add(rectangle[1]);  // 下边界
            ySet.add(rectangle[3]);  // 上边界
        }

        // 将TreeSet转换为数组，用于后续坐标索引查找
        int[] xs = xSet.stream().mapToInt(Integer::intValue).toArray();
        int[] ys = ySet.stream().mapToInt(Integer::intValue).toArray();

        // 创建二维布尔数组，表示每个小网格是否被绘制
        // 网格数量 = (x坐标间隔数) × (y坐标间隔数)
        boolean[][] painted = new boolean[Math.max(ys.length - 1, 0)][Math.max(xs.length - 1, 0)];

        // 遍历所有矩形，根据types中的字符决定绘制或擦除
        for (int index = 0; index < rectangles.length; index++) {
            int[] rectangle = rectangles[index];
            // 规范化矩形坐标（确保left<right, bottom<top）
            int left = Math.min(rectangle[0], rectangle[2]);
            int right = Math.max(rectangle[0], rectangle[2]);
            int bottom = Math.min(rectangle[1], rectangle[3]);
            int top = Math.max(rectangle[1], rectangle[3]);

            // 判断当前矩形是绘制(true)还是擦除(false)
            // 如果index超出types长度，默认为擦除(false)
            boolean value = index < types.length() && types.charAt(index) == 'd';

            // 在坐标数组中二分查找，找到矩形边界对应的网格索引
            // 遍历矩形覆盖的所有网格单元
            for (int y = java.util.Arrays.binarySearch(ys, bottom);
                 y < java.util.Arrays.binarySearch(ys, top); y++) {
                for (int x = java.util.Arrays.binarySearch(xs, left);
                     x < java.util.Arrays.binarySearch(xs, right); x++)
                    painted[y][x] = value;  // 设置网格状态：绘制或擦除
            }
        }

        // 计算最终面积：累加所有被绘制网格的实际面积
        long area = 0;
        for (int y = 0; y + 1 < ys.length; y++) {
            for (int x = 0; x + 1 < xs.length; x++) {
                if (painted[y][x]) {  // 如果该网格被绘制
                    // 实际面积 = 网格宽度 × 网格高度
                    area += (long) (xs[x + 1] - xs[x]) * (ys[y + 1] - ys[y]);
                }
            }
        }

        // 返回int类型的面积（假设面积不会超出int范围）
        return (int) area;
    }

}
