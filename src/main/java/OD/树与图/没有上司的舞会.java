package OD.树与图;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 17:45
 */
public class 没有上司的舞会 {


    static public class Main {

        static int n;                       // 职员数量
        static int[] happy;                 // happy[i] = 职员 i 的快乐值
        static List<Integer>[] children;    // children[u] = u 的所有直接下属
        static boolean[] hasBoss;           // hasBoss[i] = 职员 i 是否有上司（用来找根）
        static long[][] dp;                 // dp[u][0/1]

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            // ---------- 1. 读入数据 ----------
            n = sc.nextInt();
            happy = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                happy[i] = sc.nextInt();
            }

            // 建树（这里用"上司 -> 下属"的邻接表）
            children = new List[n + 1];
            for (int i = 1; i <= n; i++) {
                children[i] = new ArrayList<>();
            }
            hasBoss = new boolean[n + 1];

            // 读入 n-1 条边：每行 "u v" 表示 v 的上司是 u
            for (int i = 0; i < n - 1; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                children[u].add(v);
                hasBoss[v] = true;
            }

            // ---------- 2. 找根节点（没有上司的那个人）----------
            int root = 1;
            for (int i = 1; i <= n; i++) {
                if (!hasBoss[i]) {
                    root = i;
                    break;
                }
            }

            // ---------- 3. 树形 DP ----------
            dp = new long[n + 1][2];
            dfs(root);

            // ---------- 4. 输出答案 ----------
            System.out.println(Math.max(dp[root][0], dp[root][1]));
        }

        /**
         * 后序遍历：先算所有孩子，再算自己
         */
        static void dfs(int u) {
            dp[u][0] = 0;              // 不选 u：初始为 0
            dp[u][1] = happy[u];       // 选 u：初始为 u 自己的快乐值

            for (int v : children[u]) {
                dfs(v);                // 先递归处理下属

                // 不选 u → 孩子可选可不选，取较大的
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);

                // 选 u → 孩子必须不选
                dp[u][1] += dp[v][0];
            }
        }
    }

}
