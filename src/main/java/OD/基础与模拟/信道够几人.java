package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 23:34
 */
public class 信道够几人 {
    /**
     * 计算最多能为多少用户分配信道
     * @param r 最大信道阶数（0~19）
     * @param c 各阶信道数量数组，c[i]表示容量为2^i的信道数量
     * @param d 每个用户需要的数据量
     * @return 最多可服务的用户数
     */
    long maximumChannelUsers(int r, long[] c, long d) {
        // 1. 将目标数据量d转换为二进制表示（从低位到高位）
        // 例如 d=30 (11110)，b中存储为 [0,1,1,1,1]（对应2^0到2^4）
        java.util.ArrayList<Long> b = new java.util.ArrayList<>();
        for (long v = d; v > 0; v /= 2)
            b.add(v % 2);

        // 2. 扩展信道数组，确保能够覆盖d的二进制位数
        // 如果原数组长度不足，补齐为0
        long[] x = java.util.Arrays.copyOf(c, Math.max(c.length, b.size()));

        // 3. 统计所有大于等于目标数据量的单信道，直接服务用户
        // 这些信道容量 >= d，一个就能服务一个用户
        long res = 0;
        for (int i = r; i >= b.size(); i--)
            res += x[i];  // 每个大信道直接服务一个用户

        // 4. 循环尝试用剩余信道拼凑出d来服务更多用户
        // 每次成功拼凑出一组，res加1
        while (sub(x, b))
            res++;
        return res;
    }

    /**
     * 计算前n+1个信道（0到n阶）的总容量
     * @param a 信道数组，a[i]表示2^i阶信道的数量
     * @param n 最高阶数
     * @return 总容量 = sum(a[i] * 2^i)
     */
    private long calc(long[] a, int n) {
        long s = 0;
        for (int i = 0; i <= n; i++)
            s += a[i] * (1L << i);  // 1L<<i 即 2^i
        return s;
    }

    /**
     * 核心方法：尝试从剩余信道中消耗一组，来服务一个用户
     * @param x 当前剩余信道数组（会被修改）
     * @param b 目标数据量d的二进制表示（从低位到高位）
     * @return true表示成功消耗一组信道；false表示无法再服务更多用户
     */
    private boolean sub(long[] x, java.util.List<Long> b) {
        // 从高位到低位遍历d的二进制位
        for (int i = b.size() - 1; i >= 0; i--) {
            // 情况1：当前阶信道数量足够，直接消耗
            if (x[i] >= b.get(i)) {
                x[i] -= b.get(i);
                continue;
            }

            // 情况2：当前阶信道不足，需要从低位"借"信道来凑
            // 将b转换为数组方便计算
            long[] bb = new long[b.size()];
            for (int j = 0; j < b.size(); j++)
                bb[j] = b.get(j);

            // 判断：从0到i阶的总容量是否 >= 目标需要的总容量
            // calc(x, i)：当前实际拥有的低阶总容量
            // calc(bb, i)：目标需要的低阶总容量
            if (calc(x, i) >= calc(bb, i)) {
                // 可以用低阶信道凑出不足的部分
                // 关键操作：将高阶（第i位）的需求降到0，并处理借位
                x[i] -= b.get(i);  // 扣除需要的数量（此时x[i]为负数）
                x[i - 1] += x[i] << 1;  // 将不足的部分从低位翻倍补偿
                // 举例：如果x[i] = -2，表示少2个2^i信道
                // 需要从低位借：-2 * 2 = -4个2^(i-1)信道
                // 但这里x[i]是负数，所以x[i-1]实际是减去了相应数量
                x[i] = 0;  // 清空当前阶
            } else {
                // 情况3：低阶总容量也不够，无法拼凑出完整的d
                // 尝试借一个更高阶的信道来拆分
                for (int j = i + 1; j < b.size(); j++)
                    if (x[j] > 0) {  // 找到第一个有剩余的高阶信道
                        x[j]--;  // 消耗一个高阶信道
                        return true;  // 返回true，下次循环继续尝试
                    }
                // 没有任何高阶信道可用，无法再服务更多用户
                return false;
            }
        }
        // 成功消耗一组信道，服务了一个用户
        return true;
    }

}
