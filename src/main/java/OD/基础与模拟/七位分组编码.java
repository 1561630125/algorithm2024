package OD.基础与模拟;

import java.math.BigInteger;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 13:42
 */
public class 七位分组编码 {

    /**
     * 将大整数编码为十六进制字符串（变长编码）
     *
     * 编码规则（类似 Base-128 变长编码）：
     * 1. 每7位作为一个数据块（因为 2^7 = 128）
     * 2. 每个数据块用2个十六进制字符表示（1字节）
     * 3. 除最后一个字节外，所有字节的最高位（bit 7）设为1，表示"还有后续"
     * 4. 最后一个字节的最高位设为0，表示"结束"
     *
     * 编码示例：
     * - value = 0x00  → 编码为 "00"
     * - value = 0x7F  → 编码为 "7F"  (127)
     * - value = 0x80  → 编码为 "8001" (128: 0x80 | 0x80 = 0x80, 0x01)
     * - value = 0x3FFF → 编码为 "FF7F" (16383: 0x7F | 0x80 = 0xFF, 0x7F)
     *
     * @param value 要编码的大整数（非负）
     * @return 十六进制编码字符串
     */
    String encodeInteger(BigInteger value) {
        // 十六进制字符表
        final char[] hex = "0123456789ABCDEF".toCharArray();

        // 用于提取低7位的掩码：0111 1111 = 127
        final BigInteger mask = BigInteger.valueOf(127);

        // 构建编码结果
        StringBuilder result = new StringBuilder();

        // 循环处理，直到 value 变为 0
        do {
            // 1. 提取当前字节的低7位（保留最低的7位）
            int current = value.and(mask).intValue();
            // 例如：value = 0x1234 (4660)
            // 二进制：0001 0010 0011 0100
            // & 0x7F = 0011 0100 = 0x34

            // 2. 将 value 右移7位，准备处理下一个字节
            value = value.shiftRight(7);
            // 继续示例：value = 0x1234 >> 7 = 0x24 (36)

            // 3. 如果还有更多字节需要编码，设置最高位（bit 7）为1
            // 0x80 = 1000 0000，作为"继续标志"
            if (value.signum() != 0) {
                current |= 0x80;  // 设置最高位
                // 示例：0x34 | 0x80 = 0xB4 (1011 0100)
            }
            // 注意：如果是最后一个字节，最高位保持为0

            // 4. 将当前字节转换为2个十六进制字符
            // 高4位 (current >>> 4)
            result.append(hex[current >>> 4]);
            // 低4位 (current & 0x0F)
            result.append(hex[current & 0x0F]);
            // 示例：0xB4 → "B4"

        } while (value.signum() != 0);  // 当 value 还有值时继续

        return result.toString();
    }

}
