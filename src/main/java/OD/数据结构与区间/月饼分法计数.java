package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 12:50
 */
public class 月饼分法计数 {


    class Solution {
        // 员工总数（缓存为成员变量，避免递归时反复传参）
        int batch63Employees;
        // 记忆化表：key = "当前处理到第几人,上一人拿到的月饼数,剩余月饼数"，value = 方案数
        java.util.Map<String, Long> batch63Memo;



        /**
         公式在算什么
         公式左边就是在算：剩下这些人，按“每人比前一个多 3”的最大增长速度，总共能消耗多少月饼。

         last + 3：剩余序列里第二个人（相对于当前）最多能拿多少？不对，这里是从当前这个人开始算的。

         算了，直接看公式对应的等差数列：

         text
         last+3, last+6, last+9, ..., last+3k
         一共 k = batch63Employees - number 项。

         等差数列求和 = k * (首项 + 末项) / 2

         首项 = last + 3
         末项 = last + 3k

         所以和 = k * (2*last + 3 + 3k) / 2

         代码里为了不出现除法，两边乘 2：

         text
         k * (2*(last+3) + (k-1)*3) < remaining * 2
         这就是代码里那行公式。它等价于：

         text
         等差数列和 < remaining
         */
        /**
         * 深度优先搜索
         * @param number    当前正在给第 number 个员工分配（从 0 开始计数）
         * @param last      上一个员工分到的月饼数
         * @param remaining 还剩多少个月饼没分
         * @return          从当前状态出发的合法方案数
         */
        long batch63Count(int number, int last, int remaining) {
            // 把状态编码成字符串作为记忆化 key
            String key = number + "," + last + "," + remaining;
            Long known = batch63Memo.get(key);
            if (known != null)
                return known;   // 已经算过，直接返回

            // ===== 可行性剪枝 =====
            // 剪枝 1（上限）：如果剩下每个员工都按"最少不低于 last"来分，
            //   即每人至少 last 个，那么总需求 = (剩余人数) * last。
            //   若这个需求 > remaining，说明月饼不够分，方案数为 0。
            if ((batch63Employees - number) * (long) last > remaining
                    // 剪枝 2（下限）：为了让最后一个人也能满足"与前一人的差 ≤ 3"，
                    //   剩余序列必须尽可能小地增长。最小的增长方式是：
                    //   从 last 开始尽量往下压，但受"差 ≤ 3"约束，
                    //   后面每人最小也只能是 last, last, ...（受下界 3 限制的推导）。
                    //   这里用推导出的最小总消耗公式去比较：
                    //   若即使按最小消耗分，需要的月饼数 > 剩余月饼数，则无解。
                    || (2L * (last + 3) + (batch63Employees - number - 1) * 3L)
                    * (batch63Employees - number)
                    < remaining * 2L)
                return 0;

            // ===== 递归边界：处理最后一个员工 =====
            if (number == batch63Employees - 1)
                // 最后一人拿 remaining 个，要求：
                //   1) remaining >= last（不少于上一人，保证单调不降的下界约束）
                //   2) remaining - last <= 3（与上一人差不超过 3）
                return remaining >= last && remaining - last <= 3 ? 1 : 0;

            // ===== 递归：枚举当前员工可以拿的数量 =====
            // 取值范围是 [last, last + 3]，保证与上一人相差不超过 3
            long result = 0;
            for (int value = last; value <= last + 3; value++)
                result += batch63Count(number + 1, value, remaining - value);

            // 记录到记忆化表并返回
            batch63Memo.put(key, result);
            return result;
        }

        /**
         * 主函数：计算分配方案数
         * @param employees  员工数
         * @param mooncakes  月饼数
         */
        long countMooncakeDistributions(int employees, int mooncakes) {
            // 边界：员工数非正，或月饼不够每人至少 1 个
            if (employees <= 0 || mooncakes < employees)
                return 0;
            // 只有 1 个员工，或月饼数刚好等于员工数（每人恰好 1 个），只有 1 种方案
            if (employees == 1 || employees == mooncakes)
                return 1;

            // 初始化成员变量和记忆化表
            batch63Employees = employees;
            batch63Memo = new java.util.HashMap<>();

            // 枚举第一个员工拿多少个月饼
            // 上界 mooncakes / employees：第一个员工不能拿太多，否则后面的人不够分
            long result = 0;
            for (int first = 1; first <= mooncakes / employees; first++)
                result += batch63Count(1, first, mooncakes - first);
            return result;
        }
    }


}
