package OD.基础与模拟.稀疏表;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:48
 */
/**
 * 通用稀疏表 - 支持最小值和最大值查询
 */
class SparseTable {
    private int[][] stMin;  // st[k][i] 表示从 i 开始，长度为 2^k 的区间最小值
    private int[][] stMax;
    private int[] log;  // log[i] 表示 i 的以2为底的对数向下取整
    private int n;

    public SparseTable(int[] arr) {
        this.n = arr.length;
        int K = (int)(Math.log(n) / Math.log(2)) + 1;  // 需要多少层

        // 初始化两个表
        stMin = new int[K][n];
        stMax = new int[K][n];

        // 第0层：长度为 2^0 = 1 的区间，就是元素本身
        for (int i = 0; i < n; i++) {
            stMin[0][i] = arr[i];
            stMax[0][i] = arr[i];
        }

        // 构建上层
        // 从第1层开始，每层长度翻倍
        for (int k = 1; k < K; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                stMin[k][i] = Math.min(
                        stMin[k-1][i],
                        stMin[k-1][i + (1 << (k-1))]
                );
                stMax[k][i] = Math.max(
                        stMax[k-1][i],
                        stMax[k-1][i + (1 << (k-1))]
                );
            }
        }

        // 预处理 log
        log = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            log[i] = log[i / 2] + 1;
        }
    }

    /**
     * 查询区间最小值
     */
    public int queryMin(int L, int R) {
        int k = log[R - L + 1];
        return Math.min(
                stMin[k][L],
                stMin[k][R - (1 << k) + 1]
        );
    }

    /**
     * 查询区间最大值
     */
    public int queryMax(int L, int R) {
        int k = log[R - L + 1];
        return Math.max(
                stMax[k][L],
                stMax[k][R - (1 << k) + 1]
        );
    }

    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};

        SparseTable st = new SparseTable(arr);

        // 测试查询
        System.out.println("arr[2..6] 最小值: " + st.queryMin(2, 6));  // 1
        System.out.println("arr[2..6] 最大值: " + st.queryMax(2, 6));  // 9
        System.out.println("arr[0..10] 最小值: " + st.queryMin(0, 10)); // 1
        System.out.println("arr[0..10] 最大值: " + st.queryMax(0, 10)); // 9

        // 验证结果
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 2; i <= 6; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        System.out.println("暴力验证 [2..6]: min=" + min + ", max=" + max);
    }
}
