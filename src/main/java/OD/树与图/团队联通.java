package OD.树与图;

/**
 * 考点：并查集
 *
 * @author faming.yang@hand-china.com 2026-09-14 14:47
 */
public class 团队联通 {
    class Solution {
        /**
         * 并查集查找（带路径减半优化）。
         * 找到 x 所在集合的代表元（根）。
         */
        int find(int[] p, int x) {
            // 只要 x 不是自己的父节点，就继续往上找
            while (p[x] != x) {
                // 路径减半：把 x 直接连到「祖父」节点，降低树高
                p[x] = p[p[x]];
                x = p[x];
            }
            return x;
        }

        /**
         * 处理团队消息。
         *
         * messages 中每条消息是一个 long[]，语义由第三个元素 c 决定：
         *   - c == 0：让 a 和 b 结为同一团队（合并两个集合）
         *   - c == 1：查询 a 和 b 是否在同一团队
         *   - 其他：非法消息，输出 "da pian zi"
         *
         * 每条消息产生一条输出（合并操作 c==0 不输出）：
         *   - 查询结果："we are a team" / "we are not a team"
         *   - 非法消息："da pian zi"
         *
         * 若整体输入不合法（n 或 messages 越界），返回 {"Null"}。
         */
        String[] processTeamMessages(int n, long[][] messages) {
            // 1. 整体输入合法性检查
            if (n < 1 || messages.length < 1 || n >= 100000 || messages.length >= 100000)
                return new String[] {"Null"};

            // 2. 初始化并查集：编号 1 ~ n，每个节点自成一个集合
            int[] p = new int[n + 1];
            for (int i = 0; i <= n; i++)
                p[i] = i;

            // out：存放每条消息对应的输出结果
            java.util.List<String> out = new java.util.ArrayList<>();

            // 3. 逐条处理消息
            for (long[] m : messages) {
                // 安全取出三个字段，缺失时默认为 0
                long a = m.length > 0 ? m[0] : 0,
                        b = m.length > 1 ? m[1] : 0,
                        c = m.length > 2 ? m[2] : 0;

                // 4. a、b 越界 → 非法消息
                if (a < 1 || a > n || b < 1 || b > n)
                    out.add("da pian zi");

                    // 5. c == 0：合并 a 和 b 所在的集合
                else if (c == 0) {
                    int x = find(p, (int) a), y = find(p, (int) b);
                    int r = Math.min(x, y);   // 固定用较小根作新根（统一方向）
                    p[x] = p[y] = r;
                }

                // 6. c == 1：查询 a 和 b 是否属于同一集合
                else if (c == 1)
                    out.add(
                            find(p, (int) a) == find(p, (int) b)
                                    ? "we are a team"
                                    : "we are not a team");

                    // 7. c 为其他值 → 非法消息
                else
                    out.add("da pian zi");
            }

            // 8. List<String> 转 String[] 返回
            return out.toArray(new String[0]);
        }
    }


}
