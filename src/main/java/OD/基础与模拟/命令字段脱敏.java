package OD.基础与模拟;

import java.util.ArrayList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 12:47
 */
public class 命令字段脱敏 {

    /**
     * 对命令字符串中的指定字段进行脱敏处理（遮盖为 ******）
     *
     * 功能说明：
     * 1. 以下划线 "_" 作为字段分隔符解析命令字符串
     * 2. 支持双引号包裹的字段，其中可以包含下划线而不作为分隔符
     * 3. 将指定索引位置的字段替换为 "******"
     * 4. 重新拼接所有字段并返回
     *
     * @param index 需要脱敏的字段索引（从0开始计数）
     * @param command 原始命令字符串，格式如 "field1_field2_field3" 或带引号的 "field1_"field2_with_underscore"_field3"
     * @return 脱敏后的命令字符串，如果索引无效则返回 "ERROR"
     */
    String redactCommandField(int index, String command) {
        // 使用动态数组存储解析出的各个字段
        ArrayList<String> fields = new ArrayList<>();

        // 用于构建当前字段的字符串缓冲区
        StringBuilder current = new StringBuilder();

        // 标记是否处于双引号包裹的字符串内部
        // true: 在引号内，下划线不作为分隔符
        // false: 在引号外，下划线作为分隔符
        boolean quoted = false;

        // 逐字符遍历命令字符串
        for (int offset = 0; offset < command.length(); offset++) {
            char value = command.charAt(offset);

            if (value == '"') {
                // 遇到双引号：切换引号状态，并将引号保留在字段内容中
                quoted = !quoted;
                current.append(value);

            } else if (value == '_' && !quoted) {
                // 遇到下划线且不在引号内：作为字段分隔符
                // 如果当前字段有内容，将其添加到字段列表
                if (current.length() > 0) {
                    fields.add(current.toString());
                    current.setLength(0);  // 清空缓冲区，准备下一个字段
                }
                // 注意：分隔符本身（下划线）不被保留

            } else {
                // 普通字符：添加到当前字段
                current.append(value);
            }
        }

        // 处理最后一个字段（如果存在）
        if (current.length() > 0) {
            fields.add(current.toString());
        }

        // 验证索引是否有效
        if (index < 0 || index >= fields.size()) {
            return "ERROR";  // 索引越界返回错误信息
        }

        // 将指定索引位置的字段替换为脱敏字符串
        fields.set(index, "******");

        // 使用下划线重新连接所有字段，返回脱敏后的命令
        return String.join("_", fields);
    }


}
