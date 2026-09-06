package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 16:59
 */
public class TLV标签定位 {

    /**
     * 解析TLV（Tag-Length-Value）格式的十六进制字符串数据包
     *
     * TLV格式说明：
     * - Tag: 2个十六进制字符（1字节），表示数据类型标识
     * - Length: 2个十六进制字符（1字节），表示Value部分的字节长度
     * - Value: 长度由Length字段指定，以十六进制字符串表示
     *
     * 示例：包 "01020304" 表示 Tag=0x01, Length=0x02, Value=3个字节（"0304"）
     *
     * @param packet 十六进制字符串形式的TLV数据包，如 "01020304AABBCC"
     * @param tags   需要查询的Tag数组，按顺序返回对应的信息
     * @return 二维数组，每个元素为 long[2]，其中：
     *         - [0]: 对应Tag的Value长度（字节数）
     *         - [1]: 对应Tag的Value在数据包中的起始位置（字节偏移，从0开始）
     *         如果某个Tag未找到，对应元素为默认值 {0, 0}
     */
    static public long[][] parseTlv(String packet, long[] tags) {
        // 存储已解析的Tag信息：Key=Tag值，Value={长度, Value起始位置}
        java.util.Map<Long, long[]> values = new java.util.HashMap<>();

        int index = 0; // 当前解析位置（字符索引，每个字节占2个字符）

        // 循环解析数据包，直到剩余字符不足以构成完整的TLV头（至少4个字符）
        while (index + 3 < packet.length()) {
            long tag, length;

            // 尝试解析Tag和Length字段（各占2个十六进制字符）
            try {
                tag = Long.parseLong(packet.substring(index, index + 2), 16);
                length = Long.parseLong(packet.substring(index + 2, index + 4), 16);
            } catch (NumberFormatException error) {
                // 如果解析失败（非十六进制字符），停止解析
                break;
            }

            // 计算下一个TLV结构的起始位置
            // 公式：当前索引 + 4（Tag和Length占4个字符）+ length * 2（每个字节占2个字符）
            long next = (long) index + 4 + length * 2;

            // 边界检查：确保下一个结构在包范围内，且不会溢出
            if (next > packet.length() || next < Integer.MIN_VALUE)
                break;

            // 存储当前Tag的信息：
            // - 长度字段（字节数）
            // - Value的起始位置（字节偏移 = 字符偏移 / 2 + 2，因为Tag和Length占2字节）
            //   注：index/2得到当前TLV起始字节位置，+2跳过Tag和Length字段
            values.put(tag, new long[] {length, index / 2 + 2});

            // 移动到下一个TLV结构
            index = (int) next;
        }

        // 构建输出结果：按调用者指定的tags顺序返回
        long[][] output = new long[tags.length][2];
        for (int i = 0; i < tags.length; i++) {
            long[] value = values.get(tags[i]);
            if (value != null) {
                // 克隆数组以避免外部修改内部存储
                output[i] = value.clone();
            }
            // 如果未找到，output[i] 默认为 {0, 0}
        }

        return output;
    }

    public static void main(String[] args) {
        String packet = "0A01FF1002ABCDD1F01EE";
        long[] tags = new long[]{10,16,31};
        System.out.println(Arrays.deepToString(parseTlv(packet,tags)));

    }

}
