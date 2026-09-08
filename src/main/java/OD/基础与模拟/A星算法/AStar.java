package OD.基础与模拟.A星算法;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 20:27
 */
public class AStar {
    // 节点类
    static class Node {
        int x, y;          // 坐标
        int g;             // 起点到该节点的实际代价
        int h;             // 该节点到终点的启发式估计
        int f;             // 总代价 = g + h
        Node parent;       // 父节点，用于回溯路径

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void updateF() {
            this.f = this.g + this.h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    // 方向（上、下、左、右、左上、右上、左下、右下）
    private static final int[][] DIRS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };
    // 对角移动代价（√2 ≈ 1.414，为简化用 14/10）
    private static final int DIAGONAL_COST = 14;
    private static final int STRAIGHT_COST = 10;




    /**
     * A* 算法主方法
     * @param grid 地图（0=可通行，1=障碍物）
     * @param start 起点
     * @param end 终点
     * @return 路径节点列表（从起点到终点）
     */
    public static List<Node> findPath(int[][] grid, Node start, Node end) {
        int rows = grid.length;
        int cols = grid[0].length;

        // 检查起点和终点是否有效
        if (!isValid(grid, start.x, start.y) || !isValid(grid, end.x, end.y)) {
            return null;
        }

        // 开放列表（优先队列，按 f 值排序）
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        // 关闭列表（已访问的节点）
        Set<Node> closedList = new HashSet<>();
        // 记录每个节点是否在开放列表中（用于快速查找）
        Map<Node, Node> openMap = new HashMap<>();

        // 初始化起点
        start.g = 0;
        start.h = calculateHeuristic(start, end);
        start.updateF();
        openList.add(start);
        openMap.put(start, start);

        while (!openList.isEmpty()) {
            // 取出 f 值最小的节点
            Node current = openList.poll();
            openMap.remove(current);

            // 到达终点
            if (current.equals(end)) {
                return buildPath(current);
            }

            // 移入关闭列表
            closedList.add(current);

            // 遍历邻居
            for (int[] dir : DIRS) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                // 检查边界和障碍物
                if (!isValid(grid, nx, ny)) {
                    continue;
                }

                Node neighbor = new Node(nx, ny);

                // 如果已在关闭列表中，跳过
                if (closedList.contains(neighbor)) {
                    continue;
                }

                // 计算新的 g 值（考虑对角移动成本）
                int moveCost = (dir[0] != 0 && dir[1] != 0) ? DIAGONAL_COST : STRAIGHT_COST;
                int newG = current.g + moveCost;

                // 如果邻居已在开放列表中
                if (openMap.containsKey(neighbor)) {
                    Node existing = openMap.get(neighbor);
                    if (newG < existing.g) {
                        // 找到更短路径，更新
                        existing.g = newG;
                        existing.parent = current;
                        existing.updateF();
                        // 优先队列需要重新排序（删除后重新添加）
                        openList.remove(existing);
                        openList.add(existing);
                    }
                } else {
                    // 新节点加入开放列表
                    neighbor.g = newG;
                    neighbor.h = calculateHeuristic(neighbor, end);
                    neighbor.updateF();
                    neighbor.parent = current;
                    openList.add(neighbor);
                    openMap.put(neighbor, neighbor);
                }
            }
        }

        // 开放列表为空，无路径
        return null;
    }


    /**
     * 启发式函数：曼哈顿距离
     */
    private static int calculateHeuristic(Node a, Node b) {
        // 曼哈顿距离（四方向移动）
        // return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);

        // 欧几里得距离（允许任意方向移动）
        // return (int) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));

        // 对角线距离（允许八方向移动）
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return STRAIGHT_COST * (dx + dy) + (DIAGONAL_COST - 2 * STRAIGHT_COST) * Math.min(dx, dy);
    }

    /**
     * 检查节点是否可通行
     */
    private static boolean isValid(int[][] grid, int x, int y) {
        int rows = grid.length;
        int cols = grid[0].length;
        return x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] == 0;
    }

    /**
     * 从终点回溯构建路径
     */
    private static List<Node> buildPath(Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }


    public static void main(String[] args) {
        // 地图：0=空地，1=障碍物
        int[][] grid = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
        };

        AStar.Node start = new AStar.Node(0, 0);
        AStar.Node end = new AStar.Node(5, 7);

        List<AStar.Node> path = AStar.findPath(grid, start, end);

        if (path == null) {
            System.out.println("无法找到路径！");
        } else {
            System.out.println("路径长度: " + path.size());
            System.out.println("路径: " + path);
            // 输出: 路径: [(0,0), (0,1), (0,2), (1,2), (2,2), (3,2), ...]
        }
    }

}
