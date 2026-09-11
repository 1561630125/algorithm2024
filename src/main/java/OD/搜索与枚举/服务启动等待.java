package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 20:20
 */
public class 服务启动等待 {

    int integrationTime(int[][] useTime, int service) {
        // service 是 1-based 编号，范围检查
        if (service < 1 || service > useTime.length)
            return 0;

        // memo 用于记忆化：memo[i] 表示服务 i 完成所需的总时间
        // 用 Integer 而不是 int，是为了用 null 表示"还没算过"
        Integer[] memo = new Integer[useTime.length];

        // service - 1 转成 0-based 下标
        return finish(service - 1, useTime, memo);
    }

    // 计算服务 index 完成所需的总时间
    private int finish(int index, int[][] useTime, Integer[] memo) {
        // 1. 如果已经算过，直接返回，避免重复计算
        if (memo[index] != null)
            return memo[index];

        // 2. 找 index 的所有前置依赖中，完成时间最长的那个
        int dependency = 0;
        for (int other = 0; other < useTime.length; other++) {
            // useTime[index][other] == 1 表示 index 依赖 other
            if (other != index && useTime[index][other] == 1) {
                // 递归计算 other 的完成时间，取最大值
                dependency = Math.max(dependency, finish(other, useTime, memo));
            }
        }

        // 3. 总时间 = 自身耗时 + 最长依赖耗时
        // useTime[index][index] 是服务 index 自身的耗时
        return memo[index] = useTime[index][index] + dependency;
    }

}
