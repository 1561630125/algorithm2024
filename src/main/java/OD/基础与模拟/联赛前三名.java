package OD.基础与模拟;

import java.util.HashMap;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 15:13
 */
public class 联赛前三名 {

    class team {
        String name;
        int score;
        int count;

        public team(String name, int score, int count) {
            this.name = name;
            this.score = score;
            this.count = count;
        }
    }

    public String[] footballPodium(int teamNum, String[][] matches) {
        // write code here

        HashMap<String, int[]> res = new HashMap<>();

        for (int i = 0; i < matches.length; i++) {
            String t1 = matches[i][0];
            int s1 = Integer.parseInt(matches[i][1]);

            int s2 = Integer.parseInt(matches[i][2]);
            String t2 = matches[i][3];

            if (s1 == s2) {
                if (res.containsKey(t1)) {
                    res.put(t1, new int[]{res.get(t1)[0] + 1, res.get(t1)[1] + s1});
                } else {
                    res.put(t1, new int[]{0, s1});
                }

                if (res.containsKey(t2)) {
                    res.put(t2, new int[]{res.get(t1)[0] + 1, res.get(t2)[1] + s1});
                } else {
                    res.put(t2, new int[]{0, s2});
                }

            } else if (s1 > s2) {
                if (res.containsKey(t1)) {
                    res.put(t1, new int[]{res.get(t1)[0] + 3, res.get(t1)[1] + s1});
                } else {
                    res.put(t1, new int[]{3, s1});
                }

                if (res.containsKey(t2)) {
                    res.put(t2, new int[]{res.get(t2)[0], res.get(t2)[1] + s2});
                } else {
                    res.put(t2, new int[]{3, s2});
                }
            } else {
                if (res.containsKey(t1)) {
                    res.put(t1, new int[]{res.get(t1)[0], res.get(t1)[1] + s1});
                } else {
                    res.put(t1, new int[]{3, s1});
                }

                if (res.containsKey(t2)) {
                    res.put(t2, new int[]{res.get(t2)[0] + 3, res.get(t2)[1] + s2});
                } else {
                    res.put(t2, new int[]{3, s2});
                }
            }
        }
        return new String[]{};

    }


    public String[] footballPodium2(int teamNum, String[][] matches) {
        // ========== 1. 参数合法性校验 ==========
        // teamNum: 球队总数（3-15支）
        // matches: 比赛结果数组，每场格式 [主队, 主队进球, 客队进球, 客队]
        // 比赛场次不能超过单循环赛最大场次 n*(n-1)/2
        if (teamNum < 3 || teamNum > 15 || matches.length > teamNum * (teamNum - 1) / 2)
            return new String[0];  // 参数非法，返回空数组

        // ========== 2. 初始化数据结构 ==========
        // table: 存储每支球队的 [积分, 净胜球]
        // 索引0=积分，索引1=净胜球（进球-失球）
        java.util.Map<String, int[]> table = new java.util.HashMap<>();

        // played: 记录已经比赛过的对阵组合，防止重复比赛
        // 用"队A队B"（字典序）作为唯一标识
        java.util.Set<String> played = new java.util.HashSet<>();

        // ========== 3. 遍历所有比赛记录 ==========
        for (String[] fields : matches) {
            // 3.1 检查每场比赛的字段完整性
            // fields = [主队, 主队进球, 客队进球, 客队]
            if (fields.length != 4 || fields[0].isEmpty() || fields[3].isEmpty()
                    || fields[0].equals(fields[3]))  // 主客队不能相同
                return new String[0];

            // 3.2 解析进球数（可能抛出异常）
            int homeGoals, awayGoals;
            try {
                homeGoals = Integer.parseInt(fields[1]);  // 主队进球
                awayGoals = Integer.parseInt(fields[2]);  // 客队进球
            } catch (NumberFormatException error) {
                return new String[0];  // 进球数不是有效数字
            }

            // 3.3 进球数不能为负
            if (homeGoals < 0 || awayGoals < 0)
                return new String[0];

            // 3.4 检查是否重复比赛（A-B 和 B-A 视为同一场）
            String pair = fields[0].compareTo(fields[3]) < 0
                    ? fields[0] + "" + fields[3]   // 字典序小的在前
                    : fields[3] + "" + fields[0];
            if (!played.add(pair))  // add()返回false表示已存在
                return new String[0];  // 重复比赛，非法

            // ========== 4. 更新球队数据 ==========
            // 获取或创建主队记录 [积分, 净胜球]
            int[] home = table.computeIfAbsent(fields[0], ignored -> new int[2]);
            // 获取或创建客队记录 [积分, 净胜球]
            int[] away = table.computeIfAbsent(fields[3], ignored -> new int[2]);

            // 4.1 更新净胜球（索引1）
            home[1] += homeGoals - awayGoals;  // 主队净胜球 += 主队进球-客队进球
            away[1] += awayGoals - homeGoals;  // 客队净胜球 += 客队进球-主队进球

            // 4.2 更新积分（索引0）
            if (homeGoals > awayGoals) {
                home[0] += 3;  // 主队胜：3分
            } else if (homeGoals < awayGoals) {
                away[0] += 3;  // 客队胜：3分
            } else {
                home[0]++;     // 平局：各1分
                away[0]++;
            }
        }

        // ========== 5. 校验参赛队伍数量 ==========
        // 实际参赛队伍至少3支，不超过总球队数
        // 未参赛队伍不能超过1支（因为要取前三名）
        if (table.size() < 3 || table.size() > teamNum || teamNum - table.size() >= 2)
            return new String[0];

        // ========== 6. 校验积分和净胜球是否唯一 ==========
        // 确保没有两支球队的 "积分:净胜球" 完全相同
        // 这是为了防止无法通过积分和净胜球区分排名
        java.util.Set<String> scores = new java.util.HashSet<>();
        for (int[] score : table.values()) {
            if (!scores.add(score[0] + ":" + score[1]))
                return new String[0];  // 存在相同的积分+净胜球组合
        }

        // ========== 7. 排序取前三名 ==========
        // 7.1 获取所有参赛球队列表
        java.util.List<String> teams = new java.util.ArrayList<>(table.keySet());

        // 7.2 自定义排序：按积分降序，积分相同按净胜球降序
        teams.sort((left, right) -> {
            int[] a = table.get(left);   // 左队数据 [积分, 净胜球]
            int[] b = table.get(right);  // 右队数据 [积分, 净胜球]
            int points = Integer.compare(b[0], a[0]);  // 积分降序（b-a）
            return points != 0 ? points : Integer.compare(b[1], a[1]);  // 净胜球降序
        });

        // 7.3 返回前三名（冠亚季军）
        return teams.subList(0, 3).toArray(new String[0]);
    }
}
