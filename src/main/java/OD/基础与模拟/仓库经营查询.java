package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 20:20
 */
public class 仓库经营查询 {

    /**
     * 统计多个仓库在指定时间范围内的经营数据
     *
     * 数据结构说明：
     * warehouse 数组按 [每个仓库每天的数据] 连续存储
     * 每个仓库每天有 3 个数据：[入库量, 出库量, 损耗量]
     *
     * 例如：warehouse = [w1_day0_in, w1_day0_out, w1_day0_loss, w1_day1_in, ...]
     *
     * @param warehouse 仓库数据数组（一维展开存储）
     * @param queries 查询数组，每个查询为 [仓库ID, 起始天, 结束天]
     * @param numOfWarehouse 仓库总数
     * @return 每个查询的结果 [净出库, 总损耗, 是否高损耗率]
     *         - 净出库 = 出库 - 入库
     *         - 总损耗 = 累计损耗
     *         - 高损耗率 = (损耗 > 总流转量的5%) ? 1 : 0
     */
    static public int[][] getWareHouseReport(
            int[] warehouse,           // 仓库数据（一维数组）
            int[][] queries,           // 查询条件 [仓库ID, 开始天, 结束天]
            int numOfWarehouse         // 仓库总数
    ) {
        // 计算总天数：总数据量 ÷ 3（每个仓库每天3个数据）÷ 仓库数
        int days = warehouse.length / 3 / numOfWarehouse;

        // 结果数组：每个查询返回 3 个整数值
        int[][] answer = new int[queries.length][3];

        // 遍历每个查询
        for (int index = 0; index < queries.length; index++) {
            int incoming = 0,   // 累计入库量
                    outgoing = 0,   // 累计出库量
                    loss = 0;       // 累计损耗量

            // 查询条件：[仓库ID, 起始天, 结束天]
            int warehouseId = queries[index][0];
            int startDay = queries[index][1];
            int endDay = queries[index][2];

            // 遍历指定日期范围
            for (int day = startDay; day <= endDay; day++) {
                // 【关键】计算该仓库该天的数据在 warehouse 数组中的起始索引
                // 公式：base = (仓库ID * 总天数 + 第几天) * 3
                // 例如：仓库0第0天 → base=0，仓库0第1天 → base=3
                int base = (warehouseId * days + day) * 3;

                // 累加数据（连续3个元素：入库、出库、损耗）
                incoming += warehouse[base];      // 入库
                outgoing += warehouse[base + 1];  // 出库
                loss += warehouse[base + 2];      // 损耗
            }

            // 计算总流转量（出库 + 入库）
            int denominator = outgoing + incoming;

            // 构建结果数组：
            // [0] = 净出库 = 出库 - 入库（正数表示出库多，库存减少）
            // [1] = 总损耗
            // [2] = 是否高损耗率（损耗 > 总流转量的5%）
            answer[index] = new int[] {
                    outgoing - incoming,  // 净出库量
                    loss,                  // 总损耗量
                    // 高损耗率判断：
                    // - 如果总流转量为0：有损耗则高损耗率(1)，否则不高(0)
                    // - 否则：损耗 > 总流转量的5% (损耗×20 > 总流转量)
                    denominator == 0 ? (loss > 0 ? 1 : 0)
                            : (loss * 20 > denominator ? 1 : 0)
            };
        }

        return answer;
    }


    public static void main(String[] args) {
        // ============ 准备数据 ============
        // 2个仓库，3天数据
        int numOfWarehouse = 2;

        // 仓库数据按 [仓库][天][入库,出库,损耗] 顺序存储
        int[] warehouse = {
                // ===== 仓库 0 =====
                100, 80, 5,   // day0: 入库100, 出库80, 损耗5
                120, 90, 3,   // day1: 入库120, 出库90, 损耗3
                110, 95, 2,   // day2: 入库110, 出库95, 损耗2

                // ===== 仓库 1 =====
                50, 40, 1,    // day0: 入库50, 出库40, 损耗1
                60, 45, 2,    // day1: 入库60, 出库45, 损耗2
                55, 50, 0     // day2: 入库55, 出库50, 损耗0
        };

        // ============ 准备查询 ============
        int[][] queries = {
                {0, 0, 2},  // 查询1: 仓库0, 第0天到第2天（全部天数）
                {0, 0, 1},  // 查询2: 仓库0, 第0天到第1天（前2天）
                {1, 1, 2},  // 查询3: 仓库1, 第1天到第2天（后2天）
                {1, 0, 0}   // 查询4: 仓库1, 第0天到第0天（仅第0天）
        };

        // ============ 执行查询 ============
        int[][] result = getWareHouseReport(warehouse, queries, numOfWarehouse);

        // ============ 输出结果 ============
        System.out.println("========== 仓库经营报告 ==========");
        for (int i = 0; i < result.length; i++) {
            System.out.printf("查询%d: [净出库=%d, 总损耗=%d, 高损耗率=%d]%n",
                    i + 1, result[i][0], result[i][1], result[i][2]);
        }
    }

}
