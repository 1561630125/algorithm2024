package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 17:31
 */
public class 不等式验算 {

    static String evaluateInequalities(
            double[][] coefficients,
            int[] variables,
            double[] targets,
            String[] operators
    ) {
            boolean c1 = true;
            double c2 = -Double.MAX_VALUE;

            for (int i = 0; i < coefficients.length; i++) {
                double res = 0;
                for (int j = 0; j < coefficients[i].length; j++) {
                    res += coefficients[i][j] * variables[j];
                    double target = targets[i];
                    String operator = operators[i];
                    boolean flag;
                    double sub = res - target;
                    if (">".equals(operator)) {
                        flag = sub > 0;
                    } else if (">=".equals(operator)) {
                        flag = sub >= 0;
                    } else if ("<".equals(operator)) {
                        flag = sub < 0;
                    } else if ("<=".equals(operator)) {
                        flag = sub <= 0;
                    } else {
                        flag = sub == 0;
                    }
                    c1 = c1 && flag;
                    c2 = Math.max(sub, c2);
                }
            }

            return (c1 ? "true " : "false ") + (int) c2;
    }


    public static void main(String[] args) {
        double[][] coefficients = new double[][]{{2.3, 3, 5.6, 7, 6}, {11, 3, 8.6, 25, 1}, {0.3, 9, 5.3, 66, 7.8}};
        int[] variables = new int[]{1, 3, 2, 7, 5};
        double[] targets = new double[]{340, 670, 80.6};
        String[] operators = new String[]{"<=", "<=", "<="};
        System.out.println(evaluateInequalities(coefficients, variables, targets, operators));
    }

}


class Solution2 {
    String evaluateInequalities(double[][] coefficients, int[] variables, double[] targets,
                                String[] operators) {
        int rows = coefficients.length;

        // ---------- 1. 边界检查 ----------
        // 没有不等式，或 targets/operators 行数不匹配 → 返回 false 0
        if (rows == 0 || targets.length != rows || operators.length != rows)
            return "false 0";

        // differences[row] 保存第 row 个不等式的 "左边 - 右边"
        double[] differences = new double[rows];

        // ---------- 2. 计算每个不等式的差值 ----------
        for (int row = 0; row < rows; row++) {
            // 系数个数不能超过变量个数
            if (coefficients[row].length > variables.length)
                return "false 0";

            double value = 0;
            // 计算左边：Σ coefficients[row][col] * variables[col]
            for (int column = 0; column < coefficients[row].length; column++)
                value += coefficients[row][column] * variables[column];

            // 差值 = 左边 - 目标值
            differences[row] = value - targets[row];
        }

        // ---------- 3. 检查是否所有不等式都满足 ----------
        boolean satisfied = true;
        for (int row = 0; row < rows; row++)
            if (!satisfies(differences[row], operators[row])) {
                satisfied = false;
                break;   // 只要有一个不满足，整体就 false
            }

        // ---------- 4. 求所有差值的最大值 ----------
        double maximum = differences[0];
        for (double difference : differences)
            maximum = Math.max(maximum, difference);

        // ---------- 5. 输出结果 ----------
        // 格式："true 最大值" 或 "false 最大值"
        // (int)maximum 把最大值转成整数
        return (satisfied ? "true" : "false") + " " + (int) maximum;
    }

    /**
     * 判断差值 value 是否满足给定的运算符
     *
     * @param value    左边 - 右边
     * @param operator 运算符字符串
     * @return 是否满足
     */
    private boolean satisfies(double value, String operator) {
        if (operator.equals(">"))
            return value > 0;       // 左边 > 右边
        if (operator.equals(">="))
            return value >= 0;      // 左边 >= 右边
        if (operator.equals("<"))
            return value < 0;       // 左边 < 右边
        if (operator.equals("<="))
            return value <= 0;      // 左边 <= 右边
        if (operator.equals("="))
            return value == 0;      // 左边 == 右边
        return false;               // 未知运算符
    }
}
