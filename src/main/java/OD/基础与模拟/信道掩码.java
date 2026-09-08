package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 23:58
 */
public class 信道掩码 {

    /**
     * 计算两个通道掩码的交集、并集以及并集中最长连续 1 的位数。
     * <p>
     * 该方法支持十进制、二进制（0b 前缀）和十六进制（0x 前缀）三种输入格式。
     * <p>
     * 功能说明：
     * <ul>
     *     <li><b>交集（intersection）</b>：两个掩码中同时为 1 的位（bitwise AND）</li>
     *     <li><b>并集（union）</b>：两个掩码中任意一个为 1 的位（bitwise OR）</li>
     *     <li><b>最长连续 1 位数（best）</b>：在并集中，最长连续 1 的个数</li>
     * </ul>
     *
     * 应用场景：通道掩码通常用于表示音频/视频通道的启用状态，
     * 每个 bit 代表一个通道（0=禁用，1=启用）。
     *
     * @param maskStrA 第一个掩码字符串，支持 "0b1010"、"0xFF"、"255" 等格式
     * @param maskStrB 第二个掩码字符串，支持相同格式
     * @return 长度为 3 的 long 数组：
     *         [0] = 交集（intersection）
     *         [1] = 并集（union）
     *         [2] = 并集中最长连续 1 的位数（best）
     *
     * @see #value(String) 字符串解析方法
     */
    public long[] calculateChannelMask(String maskStrA, String maskStrB) {
        // 将两个掩码字符串解析为 long 值
        long left = value(maskStrA);
        long right = value(maskStrB);

        // 计算交集：两个掩码都启用的通道
        long intersection = left & right;

        // 计算并集：至少一个掩码启用的通道
        long union = left | right;

        // 计算并集中最长连续 1 的位数
        int best = 0;      // 记录最长连续 1 的长度
        int current = 0;   // 记录当前连续 1 的长度

        for (int bit = 0; bit < 64; bit++) {
            // 检查第 bit 位是否为 1
            if (((union >>> bit) & 1L) == 1L) {
                current++;  // 遇到 1，当前连续长度 +1
            } else {
                current = 0; // 遇到 0，重置当前连续长度
            }
            best = Math.max(best, current);  // 更新最大值
        }

        return new long[] { intersection, union, best };
    }

    /**
     * 将字符串解析为无符号 long 值。
     * <p>
     * 支持三种输入格式：
     * <ul>
     *     <li><b>十进制</b>：直接数字，如 "255"、"4294967295"</li>
     *     <li><b>二进制</b>：以 "0b" 开头，如 "0b11111111"</li>
     *     <li><b>十六进制</b>：以 "0x" 开头，如 "0xFF"、"0x7FFFFFFFFFFFFFFF"</li>
     * </ul>
     * <p>
     * 使用无符号解析（parseUnsignedLong），支持 0 到 2^64-1 的完整范围。
     *
     * @param text 待解析的字符串
     * @return 解析后的无符号 long 值
     * @throws NumberFormatException 如果字符串格式不合法
     */
    private long value(String text) {
        // 二进制格式：以 "0b" 开头
        if (text.startsWith("0b")) {
            return Long.parseUnsignedLong(text.substring(2), 2);
        }

        // 十六进制格式：以 "0x" 开头
        if (text.startsWith("0x")) {
            return Long.parseUnsignedLong(text.substring(2), 16);
        }

        // 默认：十进制格式
        return Long.parseUnsignedLong(text, 10);
    }


}
