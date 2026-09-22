package OD.树与图;

/**
 * 考点：并查集
 *
 * @author faming.yang@hand-china.com 2026-09-14 15:12
 */
public class 按事务切分SQL {

    static public class Solution {
        /**
         * 将 SQL 文本按行切分成若干文件，每个文件最多容纳 splitLine 行。
         *
         * 约束：同一个表（标签以 "T" 开头，如 [T_USER]）的所有语句必须放在同一个文件中，
         *       不能跨文件拆分。求最少需要多少个文件。
         *
         * @param splitLine 每个文件最多容纳的行数（1 ~ 10000）
         * @param sqlText   每一行 SQL 文本（一行可能含多条以 ';' 分隔的语句）
         * @return 最少需要的文件数；输入非法返回 0
         */
        public int splitSQL(int splitLine, String[] sqlText) {
            // 1. 参数合法性检查
            if (splitLine < 1 || splitLine > 10000 || sqlText.length == 0)
                return 0;

            // 2. 并查集初始化：每行初始自成一个集合
            int[] parent = new int[sqlText.length];
            for (int index = 0; index < parent.length; index++)
                parent[index] = index;

            // firstRow：记录每个表标签第一次出现的行号
            java.util.Map<String, Integer> firstRow = new java.util.HashMap<>();

            // 3. 扫描每一行，提取表标签，把同一表的行合并到同一集合
            for (int row = 0; row < sqlText.length; row++)
                // 一行可能含多条以 ';' 分隔的语句，-1 表示保留末尾空串
                for (String raw : sqlText[row].split(";", -1)) {
                    String statement = raw.trim();          // 去掉首尾空白
                    int close = statement.indexOf(']');     // 找标签结束位置 ']'

                    // 形如 [T_XXX] 的语句才处理：以 '[' 开头且 ']' 不在第 0/1 位
                    if (statement.startsWith("[") && close > 1) {
                        String tag = statement.substring(1, close);  // 取出标签名，如 "T_USER"

                        // 只关心以 "T" 开头的表标签
                        if (tag.startsWith("T")) {
                            // 记录该标签第一次出现的行；若已存在返回旧行号
                            Integer first = firstRow.putIfAbsent(tag, row);

                            // 已出现过 → 把「第一次出现的行」与「当前行」合并
                            if (first != null) {
                                int left  = find(first, parent);
                                int right = find(row, parent);
                                if (left != right)
                                    // 固定用较小根作新根（统一方向）
                                    parent[Math.max(left, right)] = Math.min(left, right);
                            }
                        }
                    }
                }

            // 4. 统计每个集合（连通块）的行数
            int[] sizes = new int[sqlText.length];
            for (int row = 0; row < sqlText.length; row++)
                sizes[find(row, parent)]++;    // 根相同的行属于同一集合

            // 5. 贪心装箱：每个文件最多 splitLine 行，同一集合不可拆分
            int files = 0;        // 已用文件数
            int current = 0;      // 当前文件已装入的行数
            for (int size : sizes)
                if (size > 0) {
                    if (current == 0 || current + size > splitLine) {
                        // 当前文件为空，或装不下这个集合 → 开新文件
                        files++;
                        current = size;
                    } else {
                        // 装得下 → 继续装入当前文件
                        current += size;
                    }
                }

            return files;
        }

        /**
         * 并查集查找（递归 + 路径压缩）。
         * 找到 node 所在集合的根，并把路径上所有节点直接连到根。
         */
        private int find(int node, int[] parent) {
            if (parent[node] != node)
                parent[node] = find(parent[node], parent);   // 递归压缩路径
            return parent[node];
        }
    }


    public static void main(String[] args) {
        int splitLine = 2;
        String[] sqlText = new String[]{"A;[T1]C","B;[T1]D"};
        Solution solution = new Solution();

        System.out.println(solution.splitSQL(splitLine,sqlText));
    }


}
