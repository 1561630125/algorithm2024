package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 12:47
 */
public class VLAN删号 {
    class Solution {

        /**
         * 从 VLAN 池（一个区间列表）中删除指定的 vlanId。
         *
         * 输入 pool 形如 "1-10,15,20-30"，表示若干 VLAN 区间。
         * 需要把 vlanId 从这些区间里"抠掉"，输出剩余的部分。
         *
         * @param pool    原始 VLAN 区间字符串
         * @param vlanId  要删除的单个 VLAN 号
         * @return        删除后的区间字符串
         */
        String removeVlanId(String pool, long vlanId) {

            // ---- 内部类：表示一个 VLAN 区间 [start, end]，order 记录原始位置 ----
            class Range {
                long start, end;
                int order;
                Range(long s, long e, int o) {
                    start = s;
                    end = e;
                    order = o;
                }
            }

            // ---- 1. 按逗号拆分，-1 表示保留末尾空串（split 默认会丢弃尾部空串） ----
            String[] parts = pool.split(",", -1);

            // ---- 2. 解析每个片段为 Range ----
            java.util.List<Range> ranges = new java.util.ArrayList<>();
            for (int i = 0; i < parts.length; i++) {
                int dash = parts[i].indexOf('-');
                long start = dash < 0
                        ? Long.parseLong(parts[i])                       // 单值："15"
                        : Long.parseLong(parts[i].substring(0, dash));   // 区间："1-10" 的 1
                long end   = dash < 0
                        ? start                                          // 单值：start=end
                        : Long.parseLong(parts[i].substring(dash + 1));  // "1-10" 的 10
                ranges.add(new Range(start, end, i));
            }

            // ---- 3. 按 start 升序排序；start 相同则按原始顺序稳定排序 ----
            ranges.sort((a, b)
                    -> a.start != b.start
                    ? Long.compare(a.start, b.start)
                    : Integer.compare(a.order, b.order));

            // ---- 4. 遍历每个区间，处理 vlanId 是否落在其中 ----
            java.util.List<String> output = new java.util.ArrayList<>();
            for (Range range : ranges) {

                if (vlanId >= range.start && vlanId <= range.end) {
                    // ===== 情况A：vlanId 落在本区间内，需要"挖掉"它 =====

                    // 左半段 [start, vlanId-1]
                    if (vlanId - 1 > range.start)
                        output.add(range.start + "-" + (vlanId - 1));  // 剩余 > 1 个数 → 区间
                    else if (vlanId - 1 == range.start)
                        output.add(Long.toString(range.start));         // 剩余恰好 1 个数 → 单值
                    // 若 vlanId-1 < range.start，说明左边没剩东西，不输出

                    // 右半段 [vlanId+1, end]
                    if (vlanId + 1 < range.end)
                        output.add((vlanId + 1) + "-" + range.end);     // 剩余 > 1 个数 → 区间
                    else if (vlanId + 1 == range.end)
                        output.add(Long.toString(range.end));            // 剩余恰好 1 个数 → 单值
                    // 若 vlanId+1 > range.end，说明右边没剩东西，不输出

                } else {
                    // ===== 情况B：vlanId 不在本区间，原样输出 =====
                    output.add(range.start == range.end
                            ? Long.toString(range.start)              // 单值
                            : range.start + "-" + range.end);         // 区间
                }
            }

            return String.join(",", output);
        }
    }
}
