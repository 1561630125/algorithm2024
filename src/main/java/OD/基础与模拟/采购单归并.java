package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 13:57
 */
public class 采购单归并 {

    /**
     * 构建采购订单
     *
     * 功能说明：
     * 1. 处理采购请求列表，过滤状态异常的请求
     * 2. 价格 > 100 的请求直接生成订单
     * 3. 价格 <= 100 的请求按ID合并数量，并应用折扣规则
     * 4. 最终按ID升序、数量降序排列订单
     *
     * 折扣规则：
     * - 如果合并后的价格 < 100 且 数量 >= 100，应用9折优惠
     * - 折扣计算：price * 9 / 10（向上取整）
     * - 否则保持原价
     *
     * 请求格式：[id, quantity, price, status]
     * - id: 商品ID
     * - quantity: 购买数量
     * - price: 单价
     * - status: 订单状态（0表示有效，其他值表示无效）
     *
     * 订单格式：[id, quantity, price]
     *
     * @param requests 采购请求数组
     * @return 生成的采购订单数组，按ID升序、数量降序排列
     */
    long[][] buildPurchaseOrders(long[][] requests) {
        // 存储低价商品（价格 <= 100）的合并信息
        // Key: 商品ID, Value: [总数量, 单价]
        java.util.Map<Long, long[]> low = new java.util.LinkedHashMap<>();

        // 存储所有最终订单
        java.util.List<long[]> orders = new java.util.ArrayList<>();

        // 遍历所有采购请求
        for (long[] request : requests) {
            // 解析请求参数
            long id = request[0];        // 商品ID
            long quantity = request[1];   // 购买数量
            long price = request[2];      // 单价
            long status = request[3];     // 订单状态

            // 跳过状态异常（非0）的请求
            if (status != 0) {
                continue;
            }

            // 高价商品（价格 > 100）直接生成订单
            if (price > 100) {
                orders.add(new long[] {id, quantity, price});
            } else {
                // 低价商品（价格 <= 100）按ID合并数量
                long[] entry = low.get(id);
                if (entry == null) {
                    // 如果该ID还没有记录，创建新记录
                    entry = new long[] {0, price};  // [总数量, 单价]
                    low.put(id, entry);
                }
                // 累加数量
                entry[0] += quantity;
            }
        }

        // 处理合并后的低价商品
        for (java.util.Map.Entry<Long, long[]> item : low.entrySet()) {
            long id = item.getKey();
            long quantity = item.getValue()[0];    // 总数量
            long price = item.getValue()[1];       // 单价

            // 应用折扣规则：
            // 如果单价 < 100 且 总数量 >= 100，应用9折优惠
            // 折扣计算：(price * 9 + 9) / 10 实现向上取整
            long finalPrice;
            if (price < 100 && quantity >= 100) {
                // 例如：price=99, 99*9=891, (891+9)/10=900/10=90
                // 向上取整确保不会因为整数除法损失优惠
                finalPrice = (price * 9 + 9) / 10;
            } else {
                finalPrice = price;
            }

            // 生成订单
            orders.add(new long[] {id, quantity, finalPrice});
        }

        // 排序订单：
        // 1. 按ID升序排列（从小到大）
        // 2. 如果ID相同，按数量降序排列（从大到小）
        orders.sort((a, b) -> {
            // 先按ID升序比较
            int result = Long.compare(a[0], b[0]);
            if (result != 0) {
                return result;
            }
            // ID相同，按数量降序比较（b[1] - a[1]）
            return Long.compare(b[1], a[1]);
        });

        // 将List转换为二维数组返回
        return orders.toArray(new long[0][]);
    }

}
