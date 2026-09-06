package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:31
 */
public class TLV取值 {

    /**
     * 从TLV（标签-长度-值）格式的字符串数组中，解码并提取指定标签对应的值。
     *
     * <p>TLV格式说明：
     * <ul>
     *   <li>每个TLV条目由三个连续元素组成：标签(tag)、长度高字节、长度低字节</li>
     *   <li>长度值 = 高字节 * 256 + 低字节（大端序，2字节表示长度）</li>
     *   <li>值部分紧跟在长度字节之后，长度为上述计算的长度值</li>
     * </ul>
     *
     * @param tag    要查找的目标标签（如 "9F02" 等十六进制字符串）
     * @param stream 包含TLV数据的字符串数组，每个元素为一个十六进制字节（如 "9F", "02", "00", "06"）
     * @return 如果找到匹配标签，返回该标签对应的值部分（子数组）；否则返回空数组
     */
    String[] decodeTlvValue(String tag, String[] stream) {
        int index = 0; // 当前解析位置，指向TLV条目的起始标签位置

        // 遍历整个字节流，逐个处理TLV条目
        while (index < stream.length) {
            // 边界检查：至少需要3个元素（标签 + 2字节长度）才能构成一个完整的TLV头
            if (index + 2 >= stream.length)
                return new String[0];

            // 计算值部分的长度（大端序，2字节）
            // 长度 = 第1个长度字节 * 256 + 第2个长度字节
            int length = Integer.parseInt(stream[index + 1], 16)   // 高字节
                    + 256 * Integer.parseInt(stream[index + 2], 16); // 低字节

            // 如果当前标签匹配目标标签
            if (stream[index].equals(tag)) {
                // 值部分的起始位置：标签(1) + 长度(2) = 向后偏移3
                int start = index + 3;
                // 结束位置：起始位置 + 长度，但不能超出数组边界
                // 使用 Math.min 防止越界，Math.max 确保至少返回起始位置（防止length为负数导致异常）
                int end = Math.max(start, Math.min(stream.length, start + length));
                // 返回该标签对应的值部分（子数组拷贝）
                return java.util.Arrays.copyOfRange(stream, start, end);
            }

            // 当前标签不匹配，跳过整个TLV条目，定位到下一个TLV的起始位置
            int next = index + 3 + length; // 下一个TLV条目的标签位置

            // 安全防护：如果计算出的下一个位置非法（溢出或小于等于当前位置），终止解析
            if (next <= index)
                return new String[0];

            index = next; // 移动到下一个TLV条目
        }

        // 遍历结束未找到匹配标签，返回空数组
        return new String[0];
    }


    public static void main(String[] args) {

    }

}
