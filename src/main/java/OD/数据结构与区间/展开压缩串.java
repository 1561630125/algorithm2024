package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 12:26
 */
public class 展开压缩串 {

    static class Parser2 {
        String compressed;
        int index;

        public Parser2(String compressed) {
            this.compressed = compressed;
            this.index = 0;
        }

        /** 对外入口：解析整个表达式 */
        String parse() {
            StringBuilder sb = new StringBuilder();
            while (index < compressed.length()) {
                char c = compressed.charAt(index);

                if (c == ']') {
                    break;   // 交给上层处理
                }

                if (Character.isDigit(c)) {
                    // 1. 读数字 k
                    int num = 0;
                    while (index < compressed.length()
                            && Character.isDigit(compressed.charAt(index))) {
                        num = num * 10 + compressed.charAt(index) - '0';
                        index++;
                    }

                    // 2. 期望 '['
                    if (index >= compressed.length() || compressed.charAt(index) != '[') {
                        throw new IllegalArgumentException("缺少 '['");
                    }
                    index++;  // 跳过 '['

                    // 3. 递归解析括号内部，返回内部字符串
                    String inner = parse();

                    // 4. 期望 ']'
                    if (index >= compressed.length() || compressed.charAt(index) != ']') {
                        throw new IllegalArgumentException("缺少 ']'");
                    }
                    index++;  // 跳过 ']'

                    // 5. 重复 k 次
                    for (int i = 0; i < num; i++) {
                        sb.append(inner);
                    }
                } else if (Character.isLetter(c)) {
                    // 普通字母，直接追加
                    sb.append(c);
                    index++;
                } else {
                    throw new IllegalArgumentException("非法字符: " + c);
                }
            }
            return sb.toString();
        }
    }


    static class Parser {
        int index;
        String compressed;
        StringBuilder sb = new StringBuilder();

        public Parser(String compressed) {
            this.compressed = compressed;
            this.index = 0;
        }


        String parse() {
            while (true) {
                if (index >= compressed.length() || ']' == compressed.charAt(index)) {
                    return sb.toString();
                }
                int num = 0;
                if (Character.isDigit(compressed.charAt(index))) {
                    num = (int)compressed.charAt(index) - '0';
                    index++;
                    while (Character.isDigit(compressed.charAt(index))) {
                        num = num * 10 + (int)compressed.charAt(index) - '0';
                        index++;
                    }
                }

                String innerStr = "";
                if ('[' == compressed.charAt(index)) {
                    index++;
                    // 继续遇到[
                    if ('[' == compressed.charAt(index)){
                        parse();
                    }
                    // 遇到字符
                    innerStr = innerStr();
                    if (']' == compressed.charAt(index)) {
                        index++;
                    }
                }
                for(int i = 0; i < num; i++) {
                    sb.append(innerStr);
                }
            }
        }


        String innerStr(){
            StringBuilder sb = new StringBuilder();
            if (isLetter(compressed.charAt(index))) {
                sb.append(compressed.charAt(index));
                index++;
                while (isLetter(compressed.charAt(index))) {
                    sb.append(compressed.charAt(index));
                    index++;
                }
            }
            return sb.toString();
        }

        boolean isLetter(char chr) {
            if (Character.isLetter(chr) && '[' != chr && ']' != chr) {
                return true;
            }
            return false;
        }
    }


    /**
     * 解压缩字符串。
     * 支持形如 "3[a]" -> "aaa"、"2[ab3[c]]" -> "abcccabccc" 的嵌套结构。
     *
     * @param compressed 压缩后的字符串，如 "3[a2[bc]]"
     * @return 展开后的字符串
     */
    static String decompressMessage2(String compressed) {
        // prefixes：栈，保存每一层 '[' 之前的"已拼好的字符串前缀"
        java.util.Deque<String> prefixes = new java.util.ArrayDeque<>();

        // repeats：栈，保存每一层 '[' 对应的重复次数 k
        java.util.Deque<Integer> repeats = new java.util.ArrayDeque<>();

        // current：当前正在构建的字符串（当前层的"内容"）
        String current = "";

        // repeat：正在解析的多位数 k（可能有多位，如 12、345）
        int repeat = 0;

        for (char character : compressed.toCharArray()) {

            if (Character.isDigit(character)) {
                // 遇到数字：累积 k。用 *10 是为了处理多位数字，如 "12" 要读成 12
                // character - '0' 把字符 '0'~'9' 转成整数 0~9
                repeat = repeat * 10 + character - '0';

            } else if (character == '[') {
                // 遇到 '['：进入新的一层（开始一个 k[...] 块）
                // 1) 把"外层已构建的前缀"压栈，等 ] 时再取回来接上
                prefixes.push(current);
                // 2) 把当前这层的重复次数 k 压栈
                repeats.push(repeat);
                // 3) 重置状态，开始构建这一层内部的字符串
                current = "";
                repeat = 0;

            } else if (character == ']') {
                // 遇到 ']'：当前层结束，需要展开
                // value = 这一层里面构建好的字符串（如 "bc"）
                String value = current;

                // 取回外层前缀，作为新的 current 基础
                current = prefixes.pop();

                // 把 value 重复 k 次后拼到外层前缀后面
                // repeats.pop() 就是这一层对应的 k
                int times = repeats.pop();
                StringBuilder sb = new StringBuilder(current);
                for (int i = 0; i < times; i++) {
                    sb.append(value);
                }
                current = sb.toString();

            } else {
                // 普通字母：直接追加到当前层字符串
                current += character;
            }
        }

        // 循环结束，current 就是最终结果
        return current;
    }

    static String decompressMessage(String compressed) {

        return new Parser2(compressed).parse();
    }

    public static void main(String[] args) {
        System.out.println(decompressMessage("3[k3[q]]3[mn]"));

        System.out.println(decompressMessage2("3[k3[q]]3[mn]"));
    }




}
