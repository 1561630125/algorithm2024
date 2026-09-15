package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 12:52
 */
public class 层级上缴 {

    class Solution {

        /**
         * 递归计算某个节点（员工/部门）的总收入。
         *
         * 规则：
         *   总收入 = 自身收入 + Σ(每个直接下属的"总收入的 15%"的向下取整)
         *
         * 注意：下属的"总收入"是递归算出来的，所以这是一个自底向上的递归。
         *
         * @param id        当前节点 id
         * @param income    节点 id -> 自身收入
         * @param children  节点 id -> 直接下属 id 列表
         * @return          当前节点的总收入
         */
        long batch67Income(long id,
                           java.util.Map<Long, Long> income,
                           java.util.Map<Long, java.util.List<Long>> children) {

            // 1. 先取自己的收入（没有记录就是 0）
            long total = income.getOrDefault(id, 0L);

            // 2. 遍历每个直接下属，递归算下属的总收入
            for (long child : children.getOrDefault(id, java.util.Collections.emptyList())) {
                // 下属总收入的 15%，整数除法（向下取整）
                // 即：先 / 100 再 * 15，避免先 *15 溢出，也更贴近题目语义
                total += (batch67Income(child, income, children) / 100) * 15;
            }

            // 3. 把算好的总收入写回 income，避免重复计算（记忆化）
            income.put(id, total);
            return total;
        }

        /**
         * 找"老板"（唯一没有上级的根节点），并返回 [老板id, 老板总收入]。
         *
         * 输入 records: 每行是 [员工id, 上级id, 自身收入]
         *   - 员工id：当前节点
         *   - 上级id：当前节点的直接上级（-1 表示没有上级？本代码用 record[1] < 0 过滤）
         *   - 自身收入
         *
         * @return long[]{老板id, 老板总收入}；找不到根时返回 {0, 0}
         */
        long[] bossTotalIncome(long[][] records) {

            // income:  id -> 自身收入
            java.util.Map<Long, Long> income = new java.util.HashMap<>();

            // children: 上级id -> [下属id, ...]
            java.util.Map<Long, java.util.List<Long>> children = new java.util.HashMap<>();

            // childIds: 所有"当过别人下属"的 id 集合（这些不可能是老板）
            java.util.Set<Long> childIds = new java.util.HashSet<>();

            // ids: 所有出现过的 id（TreeSet 保证有序，便于输出确定）
            java.util.Set<Long> ids = new java.util.TreeSet<>();

            // ---- 1. 遍历记录，建图和索引 ----
            for (long[] record : records) {
                // 跳过非法记录：字段不足 3 个，或上级 id 为负
                if (record.length < 3 || record[1] < 0)
                    continue;

                // 记录该节点的自身收入
                income.put(record[0], record[2]);

                // 建立 上级 -> 下属 的反向索引
                children
                        .computeIfAbsent(record[1], key -> new java.util.ArrayList<>())
                        .add(record[0]);

                // 该节点当过下属
                childIds.add(record[0]);

                // 双方都要纳入 id 全集
                ids.add(record[0]);
                ids.add(record[1]);
            }

            // ---- 2. 找出老板：出现过、但从没当过别人下属的 id ----
            for (long id : ids)
                if (!childIds.contains(id))
                    return new long[] {id, batch67Income(id, income, children)};

            // ---- 3. 没找到根（例如空输入或全是环）----
            return new long[] {0, 0};
        }
    }
}
