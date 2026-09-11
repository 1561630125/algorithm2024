package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 21:45
 */
public class 书本叠放层数 {

    int maxBookStack(long[][] books) {

        for(int i = 0; i < books.length; i++) {
            long height = books[i][0];
            long weight = books[i][1];


        }

        return 0;
    }

    /**
     * 求最多能叠多少本书（俄罗斯套娃信封/最长递增子序列的变种）
     * books[i] = {length, width}
     * 叠书条件：下面一本的 length 和 width 都必须严格大于上面一本
     *
     * @param books 每本书的 {长度, 宽度}
     * @return 最多能叠的层数
     */
    int maxBookStack2(long[][] books) {
        // 复制一份，避免修改原数组
        long[][] ordered = books.clone();

        // 排序：长度降序；长度相同时，宽度降序
        java.util.Arrays.sort(ordered, (first, second) -> {
            int byLength = Long.compare(second[0], first[0]);  // 长度大的在前
            return byLength != 0 ? byLength
                    : Long.compare(second[1], first[1]);  // 长度相同，宽度大的在前
        });

        // best[i]：以 ordered[i] 为最上面一本时，最多能叠多少层
        int[] best = new int[ordered.length];
        int answer = 0;

        for (int current = 0; current < ordered.length; current++) {
            best[current] = 1;  // 至少可以单独放一本

            // 枚举之前的所有书，看能不能放在 current 下面
            for (int previous = 0; previous < current; previous++) {
                // 因为排序是降序，previous 在 current 前面，
                // 所以 ordered[previous] 的 length 和 width 都 >= ordered[current]
                // 只有严格大于时，才能把 current 叠在 previous 上面
                if (ordered[previous][0] > ordered[current][0]
                        && ordered[previous][1] > ordered[current][1]) {
                    best[current] = Math.max(best[current], best[previous] + 1);
                }
            }

            answer = Math.max(answer, best[current]);
        }

        return answer;
    }



    int maxBookStack3(long[][] books) {
        long[][] ordered = books.clone();
        java.util.Arrays.sort(ordered, (first, second) -> {
            int byLength = Long.compare(second[0], first[0]);
            return byLength != 0 ? byLength : Long.compare(second[1], first[1]);
        });

        int n = ordered.length;
        int[] memo = new int[n];
        java.util.Arrays.fill(memo, -1);

        int answer = 0;
        for (int i = 0; i < n; i++) {
            answer = Math.max(answer, dfs(ordered, i, memo));
        }
        return answer;
    }

    /**
     * 以 ordered[index] 为最上面一本，最多能叠多少层
     */
    private int dfs(long[][] ordered, int index, int[] memo) {
        if (memo[index] != -1)
            return memo[index];

        int best = 1;  // 至少单独一本
        for (int next = 0; next < ordered.length; next++) {
            if (next == index) continue;
            // ordered[next] 要能放在 ordered[index] 下面：两个维度都严格大于
            if (ordered[next][0] > ordered[index][0]
                    && ordered[next][1] > ordered[index][1]) {
                best = Math.max(best, dfs(ordered, next, memo) + 1);
            }
        }
        return memo[index] = best;
    }

}
