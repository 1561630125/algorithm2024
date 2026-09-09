package OD.基础与模拟.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:10
 */
public class 热点网址排行 {


    /**
     * 热门网站查询处理（类似实时热搜榜）
     *
     * 题目背景：
     * 输入一系列操作行，每行要么是"查询命令"（纯数字），要么是"网站访问记录"（字符串）
     *
     * 处理逻辑：
     * 1. 遇到网站名称 → 该网站的访问次数 +1
     * 2. 遇到数字 N → 输出当前访问次数最多的前 N 个网站（按次数降序，次数相同则按网站名升序）
     *
     * 例如：
     * 输入: ["google", "facebook", "3", "google", "2"]
     * 处理:
     *   - "google": count=1
     *   - "facebook": count=1
     *   - "3": 输出 "facebook,google"（按名称升序）
     *   - "google": count=2
     *   - "2": 输出 "google,facebook"（按次数降序）
     *
     * @param lines 输入的命令行数组
     * @return 所有查询命令的输出结果数组
     */
    String[] hotWebsiteQueries(String[] lines) {
        // ========== 数据结构初始化 ==========
        // counts: 存储每个网站当前的访问次数
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();

        // result: 存储所有查询命令的输出结果
        java.util.List<String> result = new java.util.ArrayList<>();

        // ========== 逐行处理输入 ==========
        for (String rawLine : lines) {
            String line = rawLine.trim();  // 去除首尾空格

            // ========== 情况1: 当前行是纯数字（查询命令） ==========
            // 正则表达式 "\\d+" 匹配一个或多个数字
            if (!line.isEmpty() && line.matches("\\d+")) {
                int limit = Integer.parseInt(line);  // 要输出的前N个网站

                // 获取所有网站的键（网站名称）
                java.util.List<String> ranked = new java.util.ArrayList<>(counts.keySet());

                // ========== 排序：按访问次数降序，次数相同按网站名升序 ==========
                ranked.sort((first, second) -> {
                    // 先按访问次数降序（次数多的在前）
                    int byCount = Integer.compare(counts.get(second), counts.get(first));
                    // 如果次数相同，按网站名字典序升序
                    return byCount != 0 ? byCount : first.compareTo(second);
                });

                // ========== 取前 limit 个，拼接成字符串 ==========
                // Math.min(limit, ranked.size()) 防止越界
                String output = String.join(",",
                        ranked.subList(0, Math.min(limit, ranked.size())));
                result.add(output);
            }
            // ========== 情况2: 当前行是网站名称（访问记录） ==========
            else {
                // 该网站访问次数 +1
                counts.put(line, counts.getOrDefault(line, 0) + 1);
            }
        }

        // 将结果列表转换为字符串数组返回
        return result.toArray(new String[0]);
    }


}
