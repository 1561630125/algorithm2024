package OD.数组与字符串;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-19 13:48
 */
public class 设计能量管理系统 {

    class energy{
        long index;
        long begin;
        long end;
        long power;

        public energy(long index, long begin, long end, long power) {
            this.index = index;
            this.begin = begin;
            this.end = end;
            this.power = power;
        }
    }

    public int calculateNetEnergy(String[] commands) {
        // write code here

        LinkedList<energy> energy = new LinkedList<>();
        int index = 1;
        long res = 0;
        for (String command : commands) {
            String[] split = command.split(",");
            if ("AddProductionRecord".equals(split[0])) {
                String type = split[1];
                String power = split[2];
                String start = split[3];
                String end = split[4];
                energy.add(new energy(index, Long.parseLong(start), Long.parseLong(end), Long.parseLong(power)));

            } else if ("AddConsumptionRecord".equals(split[0])) {
                String power = split[1];
                String start = split[2];
                String end = split[3];
                energy.add(new energy(index, Long.parseLong(start), Long.parseLong(end), -Long.parseLong(power)));

            } else if ("QueryNetEnergy".equals(split[0])) {
                String version = split[1];
                long start = Long.parseLong(split[2]);
                long end = Long.parseLong(split[3]);

                List<energy> energyList = energy;
                if (!"A".equals(version)) {
                    energyList = energy.stream().filter(item -> item.index <= Long.parseLong(version)).collect(Collectors.toList());
                }
                double cur = 0;
                for(int i = 0; i < energyList.size(); i++) {
                    energy item = energyList.get(i);
                    if (item.begin > end || item.end < start) {
                        // 不在范围内
                        cur += 0;
                    } else if (start <= item.begin && item.end <= end) {
                        // 全在范围内
                        cur += item.power;
                    }else {
                        if (item.begin <= start) {
                            cur += (double) item.power * (item.end - start) / (item.end - item.begin);
                        } else {
                            cur += (double) item.power * (end - item.begin) / (item.end - item.begin);
                        }
                    }
                }
                return (int) Math.round(cur);
            }

            index++;
        }
        return 0;
    }


    /**
     * 按指定版本和查询时间窗精确计算净能量，并在所有贡献累加后按绝对值 0.5
     * 向远离零的规则取整。
     *
     * - version 为 "A" 时使用所有记录，否则只使用前 version 条记录。
     * - 实现不得在累加贡献或取整前使用浮点近似。
     * - 时间戳以 64 位整数处理。
     */
    public class Solution {
        public int calculateNetEnergy(String[] commands) {

            // ========== 1. 解析最后一条查询命令 ==========
            String[] query = commands[commands.length - 1].split(",");
            // query[0] = "Query"（命令名）
            // query[1] = 版本号，可能是 "A"（全部）或数字（前 N 条）
            // query[2] = 查询起始时间 qs
            // query[3] = 查询结束时间 qe

            // limit：决定参与计算的记录条数
            int limit = query[1].equals("A")
                    ? commands.length - 1                 // "A" → 除最后一条查询外全部
                    : Integer.parseInt(query[1]);          // 数字 → 前 N 条

            // 查询时间窗 [qs, qe]，用 BigInteger 以支持 64 位整数
            java.math.BigInteger qs = new java.math.BigInteger(query[2]);
            java.math.BigInteger qe = new java.math.BigInteger(query[3]);

            // ========== 2. 用分数表示净能量：numerator / denominator ==========
            // 初始为 0/1
            java.math.BigInteger numerator   = java.math.BigInteger.ZERO;
            java.math.BigInteger denominator = java.math.BigInteger.ONE;

            // ========== 3. 遍历每条记录，累加贡献 ==========
            for (int index = 0; index < limit; index++) {
                String[] fields = commands[index].split(",");

                // fields[0] 是命令名
                // 生产记录：AddProductionRecord, amount, start, end   → 偏移 2
                // 消耗记录：AddConsumptionRecord, amount, start, end  → 偏移 1
                boolean production = fields[0].equals("AddProductionRecord");
                int offset = production ? 2 : 1;

                // raw：原始带符号数值（生产为正、消耗为负，或反之，取决于数据）
                java.math.BigInteger raw = new java.math.BigInteger(fields[offset]);
                // amount：贡献大小（取绝对值）
                java.math.BigInteger amount = raw.abs();
                // 该条记录的时间区间 [start, end]
                java.math.BigInteger start = new java.math.BigInteger(fields[offset + 1]);
                java.math.BigInteger end   = new java.math.BigInteger(fields[offset + 2]);

                // ========== 4. 计算与查询窗口的重叠区间 ==========
                // low  = max(start, qs)
                // high = min(end,   qe)
                java.math.BigInteger low  = start.max(qs);
                java.math.BigInteger high = end.min(qe);

                // overlap = 重叠时长（若 high > low），否则为 0
                java.math.BigInteger overlap = high.compareTo(low) > 0
                        ? high.subtract(low)
                        : java.math.BigInteger.ZERO;

                // duration = 该记录的完整时长
                java.math.BigInteger duration = end.subtract(start);

                // ========== 5. 计算这条记录的贡献 ==========
                // 贡献 = amount × overlap / duration
                // 为了在累加时保持精确，用分数形式累加：
                //   numerator/denominator += amount × overlap / duration
                // 即：
                //   numerator   = numerator × duration + (± amount × overlap × denominator)
                //   denominator = denominator × duration

                // term = denominator × amount × overlap
                java.math.BigInteger term =
                        denominator.multiply(amount).multiply(overlap);

                // ========== 6. 判断贡献的正负号 ==========
                // production 为 true 表示生产记录；raw.signum() >= 0 表示原始值为正
                // 两者同号 → 贡献为正；异号 → 贡献为负
                int contributionSign =
                        production == (raw.signum() >= 0) ? 1 : -1;

                // 更新分子：先通分（×duration），再加上带符号的贡献
                numerator = numerator.multiply(duration)
                        .add(contributionSign > 0 ? term : term.negate());

                // 更新分母
                denominator = denominator.multiply(duration);
            }

            // ========== 7. 四舍五入：向远离零方向取整，阈值 0.5 ==========
            // 取出符号，对绝对值做四舍五入
            int sign = numerator.signum();
            java.math.BigInteger absolute = numerator.abs();

            // 商 qr[0]，余数 qr[1]
            java.math.BigInteger[] qr = absolute.divideAndRemainder(denominator);
            java.math.BigInteger answer = qr[0];

            // 若 2 × 余数 >= 分母，说明小数部分 >= 0.5，进位
            // （等价于判断 remainder/denominator >= 0.5）
            if (qr[1].shiftLeft(1).compareTo(denominator) >= 0) {
                answer = answer.add(java.math.BigInteger.ONE);
            }

            // 恢复符号并转成 int 返回
            return sign < 0
                    ? answer.negate().intValueExact()
                    : answer.intValueExact();
        }
    }

}
