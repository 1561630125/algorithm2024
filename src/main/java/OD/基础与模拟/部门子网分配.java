package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 23:17
 */
public class 部门子网分配 {

    /**
     * 根据给定的 CIDR 网络和子网需求，分配指定数量的子网。
     * <p>
     * 分配策略：
     * <ul>
     *     <li>根据所有子网中最大的主机需求，计算出合适的子网掩码</li>
     *     <li>确保子网掩码不小于原始网络的掩码（即子网不能比原始网络更大）</li>
     *     <li>确保原始网络能够容纳所需数量的子网</li>
     *     <li>按顺序分配连续的子网地址段</li>
     * </ul>
     *
     * @param cidr          原始网络的 CIDR 表示法，格式如 "10.0.0.0/24"
     * @param n             需要分配的子网数量
     * @param requirements  每个子网所需的主机数量（包括网络地址和广播地址）
     * @return              分配好的子网 CIDR 字符串数组，如果无法分配则返回空数组
     */
    public String[] allocateSubnets(String cidr, int n, int[] requirements) {
        // 解析 CIDR，获取 IP 地址和原始掩码位数
        String[] network = cidr.split("/");
        int mask = Integer.parseInt(network[1]);

        // 找出所有子网需求中的最大值
        int maximum = 0;
        for (int value : requirements)
            maximum = Math.max(maximum, value);

        // 计算能容纳最大主机数的子网大小（2 的幂次方）
        // 从 4 开始（最小子网至少需要 4 个地址：网络地址+2个可用+广播地址）
        long size = 4;
        while (size - 2 < maximum)
            size *= 2;

        // 计算子网的 CIDR 前缀长度
        // Long.numberOfTrailingZeros(size) 返回 size 的二进制表示中末尾 0 的个数
        // 例如：size=32(100000) → trailingZeros=5 → subnet=32-5=27 (/27)
        int subnet = 32 - Long.numberOfTrailingZeros(size);

        // 验证是否可行：
        // 1. 子网掩码不能比原始网络更小（subnet >= mask）
        // 2. 原始网络能容纳的子网数量必须 >= 需求数量
        //    Math.pow(2, subnet - mask) 计算原始网络能划分出多少个子网
        if (subnet < mask || Math.pow(2, subnet - mask) < n)
            return new String[0];

        // 将原始网络的 IP 地址转换为长整数表示
        long base = ip(network[0]);

        // 分配子网
        String[] answer = new String[n];
        for (int index = 0; index < n; index++) {
            // 每个子网的起始地址 = 基础地址 + index * 子网大小
            // 然后转换为点分十进制格式，并加上 CIDR 前缀
            answer[index] = text(base + index * size) + "/" + subnet;
        }
        return answer;
    }

    /**
     * 将点分十进制的 IP 地址转换为长整数表示。
     * <p>
     * 转换方式：将 IP 地址的 4 个字节按大端序组合成一个 32 位整数。
     * 例如：192.168.1.1 → 0xC0A80101 → 3232235777
     *
     * @param text 点分十进制格式的 IP 地址，如 "192.168.1.1"
     * @return IP 地址的长整数表示
     */
    private long ip(String text) {
        long value = 0;
        for (String part : text.split("\\."))
            value = value * 256 + Integer.parseInt(part);
        return value;
    }

    /**
     * 将长整数表示的 IP 地址转换回点分十进制格式。
     * <p>
     * 转换方式：从 32 位整数中提取 4 个字节，按大端序排列。
     *
     * @param value IP 地址的长整数表示
     * @return 点分十进制格式的 IP 地址，如 "192.168.1.1"
     */
    private String text(long value) {
        return ((value >> 24) & 255)
                + "." + ((value >> 16) & 255)
                + "." + ((value >> 8) & 255)
                + "." + (value & 255);
    }


}
