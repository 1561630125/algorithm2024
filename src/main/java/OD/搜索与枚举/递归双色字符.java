package OD.搜索与枚举;

/**
 * 考点：递归
 *
 * @author faming.yang@hand-china.com 2026-09-12 17:12
 */
public class 递归双色字符 {


    class Solution {

        /**
         * 对一个"对称三角形图案"的每一行进行红/蓝染色，回答若干查询：
         * 查询 (row, position) 问第 row 行第 position 个格子的颜色。
         *
         * 图案构造规则（由代码反推）：
         *   - 每行长度是上一行的一半（向上/向下折叠的对称结构）。
         *   - 每一行关于中点"镜像对称"，折叠时颜色会取反（翻转）。
         *   - 第 2 行第 0 个格子是特殊的"翻转点"。
         *
         * 解题思路（迭代折叠）：
         *   从底层不断向上折叠，判断当前 position 落在左半还是右半：
         *     - 落在右半   → 位置映射到左半（position -= half），颜色不翻转
         *     - 落在左半   → 位置不变，颜色翻转一次（blue = !blue）
         *   折到 row == 2 时再处理那个特殊翻转点。
         *
         * @param queries 每个元素为 {row, position}
         * @return 每个查询对应的颜色字符串 "blue" / "red"
         */
        String[] symmetricColors(long[][] queries) {

            // 结果数组，与查询一一对应
            String[] result = new String[queries.length];

            // 逐个处理每个查询
            for (int i = 0; i < queries.length; i++) {

                long row = queries[i][0];        // 查询所在行
                long position = queries[i][1];   // 查询在该行的位置（列下标）

                // 颜色标记：false = red, true = blue
                // 初始为 red，每次"落在左半"就翻转一次
                boolean blue = false;

                // ---------- 不断向上折叠，直到 row 降到 2 ----------
                while (row > 2) {

                    // half = 2^(row-2)，即当前行"左半"的长度
                    //   用位移计算 2 的幂，注意 1L 保证是 long，避免溢出
                    long half = 1L << (row - 2);

                    if (position >= half) {
                        // position 落在【右半】
                        // 根据对称性，右半映射到左半的对应位置
                        //   位置减去 half，颜色【不翻转】
                        position -= half;
                    } else {
                        // position 落在【左半】
                        // 位置保持不变，但颜色需要【翻转】
                        blue = !blue;
                    }

                    // 上一层继续处理
                    row--;
                }

                // ---------- 到达 row == 2 的特殊情况 ----------
                // 第 2 行第 0 个格子是额外的"翻转点"
                if (row == 2 && position == 0) {
                    blue = !blue;
                }

                // 根据最终颜色标记写出结果
                result[i] = blue ? "blue" : "red";
            }

            return result;
        }
    }


    class Solution2 {

        /**
         * 递归版：判断 (row, position) 的颜色。
         *
         * @return true = blue, false = red
         */
        private boolean color(long row, long position) {

            // ---------- 基例：第 2 行 ----------
            if (row == 2) {
                return position == 0;      // 位置 0 → blue, 位置 1 → red
            }

            // 当前行"左半"的长度 = 2^(row-2)
            long half = 1L << (row - 2);

            if (position >= half) {
                // 右半：映射到下一行的左半，颜色不变
                return color(row - 1, position - half);
            } else {
                // 左半：位置不变，颜色翻转
                return !color(row - 1, position);
            }
        }

        //R
        //BR
        //RBBR
        //BRRBRBBR

        String[] symmetricColors(long[][] queries) {
            String[] result = new String[queries.length];
            for (int i = 0; i < queries.length; i++) {
                boolean blue = color(queries[i][0], queries[i][1]);
                result[i] = blue ? "blue" : "red";
            }
            return result;
        }
    }


}
