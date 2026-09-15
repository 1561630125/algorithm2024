package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 19:53
 */
public class 请求排期 {

    public class Solution {
        public int maximumInferenceBatch(int[][] requests) {
            // 每个 request = [start, end]，表示一个区间
            // 按区间结束时间 end 升序排序；
            // 若 end 相同，则按 start 升序排序
            java.util.Arrays.sort(
                    requests,
                    (left, right) -> left[1] != right[1]
                            ? Integer.compare(left[1], right[1])   // 先比 end
                            : Integer.compare(left[0], right[0])   // end 相同比 start
            );

            int answer = 0;        // 已选区间数量
            int lastEnd = 0;       // 上一个被选区间的结束时间
            boolean selected = false;  // 是否已经选过区间

            for (int[] request : requests) {
                // 选择条件：
                //   - 还没选过任何区间（第一个直接选），或
                //   - 当前区间的开始时间 > 上一个区间的结束时间（不重叠）
                if (!selected || request[0] > lastEnd) {
                    answer++;              // 选中这个区间
                    lastEnd = request[1];  // 更新最后结束时间
                    selected = true;
                }
            }
            return answer;
        }
    }


    public class Solution2 {
        public int maximumInferenceBatch(int[][] requests) {
            int n = requests.length;
            if (n == 0) return 0;

            // 按 end 升序排序（与贪心一致）
            java.util.Arrays.sort(requests,
                    (a, b) -> a[1] != b[1]
                            ? Integer.compare(a[1], b[1])
                            : Integer.compare(a[0], b[0]));

            // dp[i] = 前 i 个区间（1-indexed）能选出的最多不重叠区间数
            int[] dp = new int[n + 1];
            dp[0] = 0;

            for (int i = 1; i <= n; i++) {
                // 选择 1：不选第 i 个区间
                dp[i] = dp[i - 1];

                // 选择 2：选第 i 个区间，往前找第一个不重叠的区间 j
                int start = requests[i - 1][0];
                for (int j = i - 1; j >= 1; j--) {
                    // 端点相接算重叠，所以用 <=
                    if (requests[j - 1][1] <= start) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                        break;
                    }
                }
                // 如果前面没有任何区间不重叠，说明第 i 个区间可以单独作为第一个
                dp[i] = Math.max(dp[i], 1);
            }
            return dp[n];
        }
    }

    public class Solution3 {
        public int maximumInferenceBatch(int[][] requests) {
            int n = requests.length;
            if (n == 0) return 0;

            java.util.Arrays.sort(requests,
                    (a, b) -> a[1] != b[1]
                            ? Integer.compare(a[1], b[1])
                            : Integer.compare(a[0], b[0]));

            int[] ends = new int[n];      // 排序后的 end 数组，用于二分
            for (int i = 0; i < n; i++)
                ends[i] = requests[i][1];

            int[] dp = new int[n + 1];
            dp[0] = 0;

            for (int i = 1; i <= n; i++) {
                int start = requests[i - 1][0];

                // 在 ends[0..i-2] 中找最后一个 <= start 的位置
                int lo = 0, hi = i - 2, p = -1;
                while (lo <= hi) {
                    int mid = (lo + hi) / 2;
                    if (ends[mid] <= start) {
                        p = mid;        // 记录可行位置
                        lo = mid + 1;   // 继续往右找更大的
                    } else {
                        hi = mid - 1;
                    }
                }

                dp[i] = dp[i - 1];              // 不选
                dp[i] = Math.max(dp[i], dp[p + 1] + 1);  // 选，p+1 是 1-indexed
            }
            return dp[n];
        }
    }

}
