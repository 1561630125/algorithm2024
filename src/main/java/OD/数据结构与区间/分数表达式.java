package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 17:03
 */
public class 分数表达式 {

    /**
     * 分数表达式求值器
     *
     * 题目背景：
     * 计算包含分数运算的数学表达式，支持 +、-、*、/ 和括号
     * 所有数字都是整数，结果用最简分数表示
     *
     * 例如：
     * "1/2 + 1/3" → "5/6"
     * "2 * (1/3 + 1/6)" → "1"
     * "1/0" → "ERROR"（除零错误）
     *
     * 功能特点：
     * 1. 支持整数和分数（如 "3/4"）
     * 2. 支持四则运算：+ - * /
     * 3. 支持括号改变优先级
     * 4. 自动约分到最简分数
     * 5. 错误处理（除零、语法错误）
     */
    static class Solution {
        /**
         * 计算分数表达式
         *
         * @param expression 数学表达式字符串
         * @return 最简分数结果，如果是整数则返回字符串形式，否则返回 "a/b" 格式
         */
        String evaluateFractionExpression(String expression) {
            // 创建解析器
            Parser parser = new Parser(expression);

            // 解析并计算结果（返回 [分子, 分母]）
            long[] value = parser.sum();

            // 如果有错误，返回 "ERROR"
            if (parser.error)
                return "ERROR";

            // 如果分母为1，输出整数；否则输出分数
            return value[1] == 1 ? Long.toString(value[0]) : value[0] + "/" + value[1];
        }

        /**
         * 表达式解析器（递归下降解析器）
         *
         * 文法规则：
         * sum     → product (('+' | '-') product)*
         * product → factor (('*' | '/') factor)*
         * factor  → number | '(' sum ')'
         * number  → [0-9]+ ('/' [0-9]+)?
         *
         * 所有数字存储为分数形式 [分子, 分母]
         */
        private final class Parser {
            private final String expression;  // 待解析的表达式
            private int index;                // 当前解析位置
            private boolean error;            // 是否发生错误

            Parser(String expression) {
                this.expression = expression;
            }

            // ========== 语法分析：sum（加减法） ==========
            /**
             * 解析加减法表达式
             * sum → product (('+' | '-') product)*
             *
             * @return 分数结果 [分子, 分母]
             */
            long[] sum() {
                // 解析第一个乘积项
                long[] value = product();

                while (true) {
                    skipSpaces();  // 跳过空格

                    // 如果到达结尾或不是加减运算符，返回当前值
                    if (index >= expression.length()
                            || (expression.charAt(index) != '+' && expression.charAt(index) != '-'))
                        return value;

                    // 读取运算符
                    char operator = expression.charAt(index++);

                    // 解析右侧的乘积项
                    long[] right = product();

                    // 根据运算符计算：如果是减法，分子取负
                    long signed = operator == '+' ? right[0] : -right[0];

                    // 通分相加： a/b + c/d = (a*d + c*b) / (b*d)
                    // value[0]/value[1] + right[0]/right[1]
                    // = (value[0]*right[1] + right[0]*value[1]) / (value[1]*right[1])
                    value = normalize(
                            value[0] * right[1] + signed * value[1],
                            value[1] * right[1]
                    );
                }
            }

            // ========== 语法分析：product（乘除法） ==========
            /**
             * 解析乘除法表达式
             * product → factor (('*' | '/') factor)*
             *
             * @return 分数结果 [分子, 分母]
             */
            private long[] product() {
                // 解析第一个因子
                long[] value = factor();

                while (true) {
                    skipSpaces();  // 跳过空格

                    // 如果到达结尾或不是乘除运算符，返回当前值
                    if (index >= expression.length()
                            || (expression.charAt(index) != '*' && expression.charAt(index) != '/'))
                        return value;

                    // 读取运算符
                    char operator = expression.charAt(index++);

                    // 解析右侧的因子
                    long[] right = factor();

                    // 根据运算符计算
                    if (operator == '*') {
                        // 乘法： (a/b) * (c/d) = (a*c) / (b*d)
                        value = normalize(value[0] * right[0], value[1] * right[1]);
                    } else {
                        // 除法： (a/b) / (c/d) = (a*d) / (b*c)
                        value = normalize(value[0] * right[1], value[1] * right[0]);
                    }
                }
            }

            // ========== 语法分析：factor（因子） ==========
            /**
             * 解析因子
             * factor → number | '(' sum ')'
             * number → [0-9]+ ('/' [0-9]+)?
             *
             * @return 分数结果 [分子, 分母]
             */
            private long[] factor() {
                skipSpaces();  // 跳过空格

                // ====== 情况1：括号表达式 ======
                if (index < expression.length() && expression.charAt(index) == '(') {
                    index++;  // 跳过 '('

                    // 递归解析括号内的表达式
                    long[] value = sum();

                    skipSpaces();  // 跳过空格

                    // 期望遇到 ')'
                    if (index < expression.length() && expression.charAt(index) == ')')
                        index++;  // 跳过 ')'
                    // 如果缺少 ')'，解析器不会报错，但会导致后续解析异常
                    // 这里应该设置 error = true，但原代码没有处理

                    return value;
                }

                // ====== 情况2：数字 ======
                long value = 0;

                // 解析整数部分
                while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
                    value = value * 10 + expression.charAt(index++) - '0';
                }

                // 注意：原代码只支持整数，不支持 "1/2" 这样的分数输入
                // 实际上应该解析为分数，但这里返回 [value, 1]
                // 也就是说输入 "1/2" 会被解析为 1 和 2 两个独立的数字
                // 这是一个设计缺陷！

                return new long[] {value, 1};
            }

            // ========== 工具方法：分数约分 ==========
            /**
             * 将分数约分到最简形式
             *
             * @param numerator 分子
             * @param denominator 分母
             * @return 最简分数 [分子, 分母]
             */
            private long[] normalize(long numerator, long denominator) {
                // 除零错误
                if (denominator == 0) {
                    error = true;
                    return new long[] {0, 1};
                }

                // 确保分母为正
                if (denominator < 0) {
                    numerator = -numerator;
                    denominator = -denominator;
                }

                // 计算最大公约数并约分
                long divisor = gcd(numerator, denominator);
                return new long[] {numerator / divisor, denominator / divisor};
            }

            // ========== 工具方法：最大公约数 ==========
            /**
             * 计算两个数的最大公约数（欧几里得算法）
             *
             * @param first 第一个数
             * @param second 第二个数
             * @return 最大公约数（如果结果为0，返回1）
             */
            private long gcd(long first, long second) {
                first = Math.abs(first);
                second = Math.abs(second);

                while (second != 0) {
                    long remainder = first % second;
                    first = second;
                    second = remainder;
                }

                // 如果两个数都是0，gcd为0，返回1避免除零
                return first == 0 ? 1 : first;
            }

            // ========== 工具方法：跳过空格 ==========
            /**
             * 跳过表达式中的空白字符
             */
            private void skipSpaces() {
                while (index < expression.length() && Character.isWhitespace(expression.charAt(index)))
                    index++;
            }
        }
    }

    public static void main(String[] args) {

        Solution solution = new Solution();

        System.out.println(solution.evaluateFractionExpression("1*"));
    }

}
