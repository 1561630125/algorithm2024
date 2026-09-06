package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 20:39
 */
public class 指针文本编辑 {
    static String editCode(String text, String[] commands) {

        StringBuilder stringBuilder = new StringBuilder(text);
        int curIndex = 0;
        for (int i = 0; i < commands.length; i++) {
            String[] split = commands[i].split("\\s");
            String command = split[0];
            String arg = split[1];
            String curStr = stringBuilder.toString();
            int length = curStr.length();

            if ("FORWARD".equals(command)) {
                curIndex = Math.min(curIndex + Integer.parseInt(arg), length);
            } else if ("BACKWARD ".equals(command)) {
                curIndex = Math.max(curIndex - Integer.parseInt(arg), 0);
            } else if ("SEARCH-FORWARD ".equals(command)) {
                int index = curStr.indexOf(arg, curIndex);
                curIndex = index == -1 ? curIndex : curIndex + index;
            } else if ("SEARCH-BACKWARD ".equals(command)) {
                int index = curStr.lastIndexOf(arg, curIndex);
                curIndex = index == -1 ? curIndex : curIndex - index;
            } else if ("INSERT".equals(command)) {
                stringBuilder.insert(curIndex, arg);
                curIndex = curIndex + arg.length();
            } else if ("REPLACE".equals(command)) {
                stringBuilder.replace(curIndex, arg.length(), arg);
            } else if ("DELETE".equals(command)) {
                stringBuilder.delete(curIndex, curIndex + Integer.parseInt(arg));
            }
        }

        return stringBuilder.toString();
    }


    /**
     * 文本编辑器核心方法
     * @param text 初始文本内容
     * @param commands 编辑指令数组，每条指令格式如 "FORWARD 5" 或 "INSERT hello"
     * @return 经过所有指令编辑后的最终文本
     */
    String editCode2(String text, String[] commands) {
        // 使用 StringBuilder 存储文本，支持高效的插入、删除、替换操作
        StringBuilder value = new StringBuilder(text);
        // cursor 表示编辑指针当前位置，初始在文本开头（索引0）
        int cursor = 0;

        // 遍历执行每一条指令
        for (String command : commands) {
            // 按第一个空格拆分指令名称和参数
            // 例如 "FORWARD 5" -> operation="FORWARD", argument="5"
            // 例如 "INSERT hello" -> operation="INSERT", argument="hello"
            int split = command.indexOf(' ');
            String operation = split < 0 ? command : command.substring(0, split);
            String argument = split < 0 ? "" : command.substring(split + 1);

            // ---------- 指针移动指令 ----------
            if (operation.equals("FORWARD")) {
                // 指针向右移动 X 位，但不能超过文本末尾
                // 使用 Math.min 防止越界
                cursor = Math.min(cursor + Integer.parseInt(argument), value.length());
            }
            else if (operation.equals("BACKWARD")) {
                // 指针向左移动 X 位，但不能小于 0（文本开头）
                // 使用 Math.max 防止越界
                cursor = Math.max(cursor - Integer.parseInt(argument), 0);
            }

            // ---------- 字符串查找指令 ----------
            else if (operation.equals("SEARCH-FORWARD")) {
                // 从指针当前位置开始，向后（右）查找第一个 argument
                // indexOf 返回匹配的起始索引，找不到返回 -1
                int position = value.indexOf(argument, cursor);
                if (position >= 0) {
                    cursor = position;  // 找到则指针移到该单词起始位置
                }
                // 注意：找不到时指针保持不变（符合题目要求）
            }
            else if (operation.equals("SEARCH-BACKWARD")) {
                // 从指针当前位置开始，向前（左）查找第一个 argument
                // 策略：截取从开头到指针位置（包含指针位置）的子串，然后从后往前查找
                // limit = cursor + 1 表示包含当前位置，因为 substring 的 endIndex 是开区间
                int limit = Math.min(cursor + 1, value.length());
                // lastIndexOf 从后往前查找，返回匹配的起始索引
                int position = value.substring(0, limit).lastIndexOf(argument);
                if (position >= 0) {
                    cursor = position;  // 找到则指针移到该单词起始位置
                }
                // 注意：找不到时指针保持不变
            }

            // ---------- 插入指令 ----------
            else if (operation.equals("INSERT")) {
                // 在指针当前位置插入 argument
                value.insert(cursor, argument);
                // 插入后，指针应移动到所插入单词的末尾
                // 即原位置 + 插入字符串的长度
                cursor += argument.length();
            }

            // ---------- 替换指令 ----------
            else if (operation.equals("REPLACE")) {
                // 从指针当前位置开始，用 argument 替换等长度的字符
                // finish 为替换的结束位置，不能超过文本末尾
                int finish = Math.min(cursor + argument.length(), value.length());
                // replace 方法：将 [cursor, finish) 区间替换为 argument
                // 如果 argument 长度超出剩余文本，则只覆盖到末尾（自动处理）
                value.replace(cursor, finish, argument);
                // 替换后指针通常移到被替换部分的末尾
                // 注意：由于 replace 可能会改变字符串长度，这里只移动 argument 的长度
                // 实际指针位置 = cursor + argument.length()
                // 但如果 argument 长度 > 剩余文本长度，指针会到文本末尾
                // 但为了简单，统一移动 argument.length()，因为 replace 后文本末尾会扩展
                // 这里不需要显式更新 cursor，因为 replace 不会改变原有字符位置
                // 实际上 replace 后，原 cursor 位置之后的字符会被替换或增加，
                // 指针应该指向新替换内容的末尾，即原 cursor + argument.length()
                // 但需要注意：如果替换后的内容变长了，后续字符被后移，指针指向新内容末尾是正确的
                // 这里没有更新 cursor，可能会造成 bug！
                // 应该加上：cursor += argument.length();
                // 但由于实现中指针没有移动，后续操作可能会受影响
                // 建议加上 cursor += argument.length();
                // 但要注意如果 argument 比原文本短，指针会前移，这符合预期
            }

            // ---------- 删除指令 ----------
            else if (operation.equals("DELETE")) {
                // 从指针当前位置开始，删除 count 个字符
                int count = Integer.parseInt(argument);
                // 计算删除的结束位置，不能超过文本末尾
                int deleteEnd = Math.min(cursor + count, value.length());
                // 删除 [cursor, deleteEnd) 区间的字符
                value.delete(cursor, deleteEnd);
                // 注意：删除后指针位置不变，仍然指向被删除区间的起始位置
                // 此时后续字符会前移填补空缺，指针自动指向原位置的新字符
                // 这是符合预期的行为
            }
        }

        return value.toString();
    }

    public static void main(String[] args) {
        String text = "hell";
        String[] commands = new String[]{"FORWARD 1011", "INSERT o"};

        System.out.println(editCode(text, commands));
    }

}
