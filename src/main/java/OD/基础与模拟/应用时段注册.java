package OD.基础与模拟;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 12:53
 */
public class 应用时段注册 {

    /**
     * 应用注册记录类
     * 用于存储每个应用的注册信息
     */
    private static class AppRecord {
        String name;        // 应用名称
        int priority;       // 应用优先级（数值越高优先级越高）
        int start;          // 开始时间（分钟数，从00:00开始计算）
        int end;            // 结束时间（分钟数，从00:00开始计算）

        AppRecord(String name, int priority, int start, int end) {
            this.name = name;
            this.priority = priority;
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 查询指定时间点的活跃应用
     *
     * 功能说明：
     * 1. 处理应用注册请求，根据优先级和时间冲突进行调度
     * 2. 高优先级应用可以抢占低优先级应用的时间段
     * 3. 返回指定时间点正在运行的应用名称
     *
     * 处理规则：
     * - 如果发生时间冲突，优先级低的应用被拒绝注册
     * - 如果优先级相同，先注册的应用保留（后来的被拒绝）
     * - 高优先级应用可以覆盖（替换）低优先级应用
     * - 如果时间区间无效（start >= end），自动忽略
     *
     * @param registrations 注册请求数组，格式：["name priority start end", ...]
     *                      时间格式为 "HH:MM"
     * @param queryTime 查询时间点，格式为 "HH:MM"
     * @return 活跃应用名称，如果没有应用则返回 "NA"
     */
    String activeApp(String[] registrations, String queryTime) {
        // 存储所有已注册的应用记录
        List<AppRecord> registered = new ArrayList<>();

        // 逐个处理注册请求
        for (String registration : registrations) {
            // 使用正则表达式按空白字符分割（支持多个空格或制表符）
            String[] parts = registration.trim().split("\\s+");

            // 验证格式：必须包含4个部分（名称、优先级、开始时间、结束时间）
            if (parts.length != 4) continue;

            // 创建应用记录
            AppRecord current = new AppRecord(
                    parts[0],                           // 应用名称
                    Integer.parseInt(parts[1]),         // 优先级
                    minutes(parts[2]),                  // 开始时间（转换为分钟）
                    minutes(parts[3])                   // 结束时间（转换为分钟）
            );

            // 验证时间区间：开始时间必须小于结束时间
            if (current.start >= current.end) continue;

            // 存储与新应用冲突的已注册应用索引
            java.util.List<Integer> conflicts = new java.util.ArrayList<>();
            boolean rejected = false;  // 标记新应用是否被拒绝

            // 检查与已注册应用的时间冲突
            for (int index = 0; index < registered.size(); index++) {
                AppRecord old = registered.get(index);

                // 判断两个时间区间是否重叠
                // 重叠条件：max(start1, start2) < min(end1, end2)
                if (Math.max(current.start, old.start) < Math.min(current.end, old.end)) {
                    conflicts.add(index);  // 记录冲突应用的索引

                    // 如果已注册应用优先级 >= 新应用优先级，则新应用被拒绝
                    if (old.priority >= current.priority) {
                        rejected = true;
                    }
                }
            }

            // 如果新应用被拒绝，跳过注册
            if (rejected) continue;

            // 移除所有冲突的低优先级应用
            // 从后往前遍历删除，避免索引变化导致的问题
            for (int index = conflicts.size() - 1; index >= 0; index--) {
                registered.remove((int)conflicts.get(index));
            }

            // 注册新应用
            registered.add(current);
        }

        // 查询指定时间点的活跃应用
        int query = minutes(queryTime);  // 将查询时间转换为分钟数

        // 遍历所有已注册应用，查找覆盖查询时间的应用
        for (AppRecord app : registered) {
            // 区间是左闭右开 [start, end)
            if (app.start <= query && query < app.end) {
                return app.name;
            }
        }

        // 没有应用在指定时间活跃
        return "NA";
    }

    /**
     * 将时间字符串转换为分钟数
     *
     * @param value 时间字符串，格式为 "HH:MM"
     * @return 从00:00开始的分钟数
     *
     * 示例：
     * - "00:00" -> 0
     * - "01:30" -> 90
     * - "23:59" -> 1439
     */
    private int minutes(String value) {
        // 按冒号分割，使用 -1 限制保留空字符串（防止末尾冒号）
        String[] parts = value.split(":", -1);
        // 小时 * 60 + 分钟
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }


}
