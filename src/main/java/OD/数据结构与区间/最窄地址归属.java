package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 18:13
 */
public class 最窄地址归属 {
    /**
     * IP地址段匹配问题：找到每个查询IP匹配的最佳城市
     *
     * 问题描述：
     * 给定一组IP地址范围（CIDR风格），每个范围对应一个城市。
     * 对每个查询IP，找到所有覆盖它的IP范围中，范围最短、start最小的那个城市。
     *
     * @param ranges 字符串数组，格式："城市=起始IP,结束IP"
     *              例如："Beijing=192.168.1.0,192.168.1.255"
     * @param queries IP地址字符串数组
     * @return 每个查询对应的城市名，如果没有匹配返回空字符串
     */

// 内部类：表示一个IP范围
    static class Range {
        String city;      // 城市名称
        long start, end;  // 起始和结束IP（已转为long）
        long length;      // 范围长度 = end - start
        int order;        // 原始输入顺序（用于稳定性）

        Range(String c, long s, long e, int o) {
            city = c;
            start = s;
            end = e;
            length = e - s;
            order = o;
        }
    }

    /**
     * 将点分十进制IP转为64位整数
     * 例如："192.168.1.1" → 3232235777
     */
    long batch64Ip(String value) {
        long total = 0;
        // 按"."分割，每部分转为0-255的整数
        for (String part : value.split("[.]"))
            total = total * 256 + Long.parseLong(part);
        return total;
    }

    /**
     * 主方法：为每个查询IP找到匹配的城市
     *
     * 算法思路：
     * 1. 解析并排序所有IP范围（按start升序）
     * 2. 排序所有查询IP（按IP值升序）
     * 3. 使用最小堆维护当前有效的IP范围（按长度升序）
     * 4. 双指针扫描：随着查询IP增大，动态添加和移除范围
     */
    String[] bestMatchingCities(String[] ranges, String[] queries) {
        // 1. 解析所有IP范围
        java.util.List<Range> items = new java.util.ArrayList<>();
        for (int i = 0; i < ranges.length; i++) {
            // 解析格式："城市=起始IP,结束IP"
            int equal = ranges[i].indexOf('=');           // 等号位置
            int comma = ranges[i].indexOf(',', equal + 1); // 逗号位置

            String city = ranges[i].substring(0, equal);
            String startIp = ranges[i].substring(equal + 1, comma);
            String endIp = ranges[i].substring(comma + 1);

            items.add(new Range(
                    city,
                    batch64Ip(startIp),  // 起始IP转long
                    batch64Ip(endIp),    // 结束IP转long
                    i                    // 原始顺序
            ));
        }

        // 2. 排序IP范围：按start升序，start相同按原始顺序
        items.sort((a, b)
                -> a.start != b.start ? Long.compare(a.start, b.start)
                : Integer.compare(a.order, b.order));

        // 3. 准备查询：转为long并保存原始索引
        long[][] ordered = new long[queries.length][2];
        for (int i = 0; i < queries.length; i++) {
            ordered[i][0] = batch64Ip(queries[i]);  // IP值
            ordered[i][1] = i;                       // 原始索引
        }
        // 按IP升序排序
        java.util.Arrays.sort(ordered, (a, b) -> Long.compare(a[0], b[0]));

        // 4. 最小堆：按长度升序，长度相同按start升序
        java.util.PriorityQueue<Range> heap = new java.util.PriorityQueue<>(
                (a, b)
                        -> a.length != b.length ? Long.compare(a.length, b.length)      // 最短优先
                        : a.start != b.start    ? Long.compare(a.start, b.start)        // 最小start优先
                        : Integer.compare(a.order, b.order)     // 原始顺序优先
        );

        // 5. 双指针扫描
        String[] answers = new String[queries.length];
        java.util.Arrays.fill(answers, "");  // 默认无匹配

        int position = 0;  // 指向下一个待添加的IP范围

        // 遍历所有查询（已按IP升序）
        for (long[] query : ordered) {
            long ip = query[0];
            int originalIndex = (int) query[1];

            // 5.1 添加所有start ≤ 当前IP的范围到堆中
            while (position < items.size() && items.get(position).start <= ip) {
                heap.add(items.get(position++));
            }

            // 5.2 移除所有end < 当前IP的范围（已失效）
            while (!heap.isEmpty() && heap.peek().end < ip) {
                heap.remove();  // 移除堆顶（当前最小的范围）
            }

            // 5.3 如果堆不为空，堆顶就是最佳匹配
            if (!heap.isEmpty()) {
                answers[originalIndex] = heap.peek().city;
            }
        }

        return answers;
    }
}
