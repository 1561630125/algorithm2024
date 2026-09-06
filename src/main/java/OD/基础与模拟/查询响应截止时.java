package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 17:21
 */
public class 查询响应截止时 {


    long earliestResponseTime(long[][] messages) {

        for(int i = 0; i < messages.length; i++) {

            long start = messages[i][0];
            long M = messages[i][1];
            long end;
            if (M < 128) {
                end = start + M;
            }else {
                long mant = M & 15;
                long exp = (M << 4) & 7;
                long de = (mant | 16) << (exp + 3);
                end = start + de;
            }

        }


        return 0L;
    }


    /**
     * 计算Host发送响应报文的最早时间（即所有报文中截止时间的最小值）
     *
     * @param messages 二维数组，每个元素为 long[2]，其中：
     *                 messages[i][0] = 报文到达时间 T
     *                 messages[i][1] = 最大响应时间字段 MaxRespCode (M)
     * @return 最早响应时间，如果没有报文则返回0
     */
    long earliestResponseTime2(long[][] messages) {
        // result 用于记录当前遍历到的所有报文中的最小截止时间
        // 使用 Long 包装类，初始为 null 以便区分"尚未赋值"的状态
        Long result = null;

        // 遍历每个查询报文
        for (long[] message : messages) {
            // 解构报文：received = 到达时间，code = 最大响应时间字段
            long received = message[0], code = message[1];

            // 判断是否需要使用位运算计算 MaxRespTime
            // 规则：只有当 MaxRespCode >= 128 时，才需要特殊计算
            if (code >= 128) {
                // 提取低4位作为 mantissa（尾数）
                // 15 的二进制为 1111，& 操作保留低4位
                long mantissa = code & 15;

                // 提取第5~7位作为 exponent（指数）
                // 112 的二进制为 1110000，& 操作保留第4~6位（从0开始计数）
                // 然后右移4位，将这3位对齐到最低位
                long exponent = (code & 112) >> 4;

                // 套用公式：MaxRespTime = (mantissa | 0x10) << (exponent + 3)
                // 0x10 = 16，二进制为 10000，与 mantissa 进行或运算
                // 相当于将第4位设为1，再左移 (exponent + 3) 位
                code = (mantissa | 16) << (exponent + 3);
            }
            // 如果 code < 128，则 MaxRespTime = code，无需处理

            // 计算当前报文的截止时间 = 到达时间 + 最大响应时间
            long current = received + code;

            // 更新全局最小截止时间
            // 如果 result 为 null（第一次遍历），则直接赋值
            // 否则取当前最小值与 current 的较小值
            result = result == null ? current : Math.min(result, current);
        }

        // 如果没有任何报文（messages 为空），返回0
        // 否则返回所有报文中最小的截止时间
        return result == null ? 0 : result;
    }


    public static void main(String[] args) {

    }

}
