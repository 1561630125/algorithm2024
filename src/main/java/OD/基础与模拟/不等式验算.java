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
