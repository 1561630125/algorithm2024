package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 12:52
 */
public class 九键输入 {

    static String decodeKeypadInput(String sequence) {

        char[] chars = sequence.toCharArray();
        boolean dig = true;
        boolean inter = false;
        StringBuilder out = new StringBuilder();
        char lastop = '$';
        int count = 0;
        char[][] board = new char[][]{{' '},{',','.'},
                {'a','b','c'},{'d','e','f'},
                {'g','h','i'},{'j','k','l'},
                {'m','n','o'},{'p','q','r','s'},
                {'t','u','v'},{'w','x','y','z'}};

        for (char op : chars) {
            if ('0' <= op && '9' >= op) {
                if (op == lastop && inter) continue;
                if (dig) {
                    out.append(op);
                }else {
                    int index = Integer.parseInt(String.valueOf(op));
                    out.append(board[index][count % board[index].length]);
                }
                count = op == lastop ? count + 1 : 0;
                lastop = op;
            } else if ('#' == op) {
                dig = false;
            } else {
                inter = true;
            }
        }

        return out.toString();
    }


    String decodeKeypadInput2(String sequence) {
        // 键盘映射：索引对应数字键，存储该键上的字符序列
        // 0: 空格和点号，1: 标点符号，2-9: 字母（2-6、8为3个字母，7、9为4个字母）
        String[] mapping = {" ", ",.", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        StringBuilder output = new StringBuilder();
        boolean letterMode = false;  // false=数字模式，true=字母模式

        for (int index = 0; index < sequence.length(); index++) {
            char character = sequence.charAt(index);

            // ========== 处理数字键 ==========
            if (character >= '0' && character <= '9') {
                if (!letterMode) {
                    // 数字模式：直接输出数字字符本身
                    output.append(character);
                } else {
                    // 字母模式：统计连续相同数字的个数，计算对应字符
                    int next = index;
                    // 统计从当前位置开始连续相同数字的数量
                    while (next < sequence.length() && sequence.charAt(next) == character)
                        next++;

                    // 获取该数字键对应的字符选项
                    String options = mapping[character - '0'];
                    // 按击键次数循环选取字符（多击循环：按4次回到第1个字符）
                    // (next - index - 1) 是连续按键次数减1（因为从0开始计数）
                    output.append(options.charAt((next - index - 1) % options.length()));

                    // 跳过已处理的连续数字
                    index = next - 1;
                }

                // ========== 模式切换键 '#' ==========
            } else if (character == '#') {
                letterMode = !letterMode;  // 切换数字/字母模式

                // ========== 暂停符 '/' ==========
            } else if (character != '/') {
                // 遇到非数字、非'#'、非'/'的字符，立即终止解码（直接break）
                break;
            }
            // 如果是 '/'，则忽略（作为暂停符，用于分隔连续按键）
        }

        return output.toString();
    }

    public static void main(String[] args) {

        char c = '9';

        System.out.println(decodeKeypadInput("1#8/8"));

    }

}
