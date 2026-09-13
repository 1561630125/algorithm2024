package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 17:33
 */
public class 目录子树总大小 {

    class Solution {

        /**
         * 计算某个目录（queryId）的"目录树总大小"。
         *
         * 输入 directories 每条形如:
         *   "id size"                     → 无子节点
         *   "id size child1,child2,..."   → 有子节点
         *   "id size (child1,child2,...)" → 有子节点（带括号）
         *
         * 总大小 = 自身大小 + 所有子孙目录大小之和。
         *
         * @param queryId     要查询的目录 id
         * @param directories 目录记录数组
         * @return 目录树总大小；queryId 不存在时返回 0
         */
        long directoryTreeSize(long queryId, String[] directories) {

            // id → 该目录自身大小
            java.util.Map<Long, Long> sizes = new java.util.HashMap<>();

            // id → 子节点 id 列表
            java.util.Map<Long, long[]> children = new java.util.HashMap<>();

            // ---------- 1. 解析每条目录记录 ----------
            for (String record : directories) {

                // 按空白切分成最多 3 段: [id, size, 子节点串]
                // 用 " +" 匹配任意长度的空白，trim 去掉首尾空白
                String[] parts = record.trim().split(" +", 3);

                if (parts.length < 2)
                    continue;   // 记录不合法，跳过

                long id = Long.parseLong(parts[0]);
                sizes.put(id, Long.parseLong(parts[1]));

                // 没有第三段 → 没有子节点
                if (parts.length < 3) {
                    children.put(id, new long[0]);
                    continue;
                }

                // ---------- 2. 解析子节点串 ----------
                String body = parts[2].trim();

                // 形如 "(1,2,3)" → 去掉括号
                if (body.startsWith("(") && body.endsWith(")"))
                    body = body.substring(1, body.length() - 1);

                // 空串 → 无子节点
                if (body.isEmpty()) {
                    children.put(id, new long[0]);
                    continue;
                }

                // 按逗号切分，逐个解析为 long
                String[] items = body.split(",");
                long[] list = new long[items.length];
                for (int i = 0; i < items.length; i++)
                    list[i] = Long.parseLong(items[i]);
                children.put(id, list);
            }

            // ---------- 3. 递归求目录树总大小 ----------
            // 用 visited 防止环 / 重复访问
            return batch65DirectoryTotal(
                    queryId, sizes, children, new java.util.HashSet<>());
        }

        /**
         * 递归计算以 id 为根的子树总大小。
         *
         * @param id       当前目录 id
         * @param sizes    id → 自身大小
         * @param children id → 子节点列表
         * @param visited  已访问集合，防止环导致无限递归
         * @return 子树总大小
         */
        long batch65DirectoryTotal(long id, java.util.Map<Long, Long> sizes,
                                   java.util.Map<Long, long[]> children, java.util.Set<Long> visited) {

            // id 不存在，或已经访问过（有环/重复）→ 贡献 0
            if (!sizes.containsKey(id) || !visited.add(id))
                return 0;

            // 自身大小
            long total = sizes.get(id);

            // 累加所有子目录的子树大小
            for (long child : children.getOrDefault(id, new long[0]))
                total += batch65DirectoryTotal(child, sizes, children, visited);

            return total;
        }
    }

}
