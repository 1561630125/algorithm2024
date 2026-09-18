package OD.基础与模拟;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-18 13:37
 */
public class 快递驿站计费系统 {

    class Cost{
        int room;
        int cost;

        public Cost(int room, int cost) {
            this.room = room;
            this.cost = cost;
        }
    }

    public int[][] calculateParcelFees(int[][] records) {
        // write code here

        LinkedList<Cost> costs = new LinkedList<>();

        HashMap<Integer, Integer> costMap = new HashMap<>();

        int length = records.length;
        for(int i = 0; i < length; i++) {
            int startTime = records[i][0];
            int endTime = records[i][1];
            int room = records[i][2];

            int diffTime = endTime - startTime;
            int total = 0;
            if (diffTime <= 12) {
                total += 0;
            }else {
                int extraTime = diffTime - 12;
                total += (extraTime - 12 + 11) / 12;
            }
            if (costMap.containsKey(room)) {
                costMap.put(room, costMap.get(room) + total);
            }else {
                costMap.put(room, total);
            }

        }

        for(Map.Entry<Integer, Integer> entry : costMap.entrySet()) {
            costs.add(new Cost(entry.getKey(),entry.getValue()));
        }

        costs.sort((a,b) -> {
            if (a.cost != b.cost) return b.cost - a.cost;
            return a.room - b.room;
        });


        return new int[0][];
    }

    public class Solution {
        public int[][] calculateParcelFees(int[][] records) {
            // fees：key = 房间号，value = 该房间的总费用
            java.util.Map<Integer, Integer> fees = new java.util.HashMap<>();

            // ---------- 1. 遍历每条记录，累加费用 ----------
            for (int[] record : records) {
                // 每条记录格式：[起始, 结束, 房间号]
                // 费用 = max(0, (结束 - 起始 - 1) / 12)
                //   - (结束 - 起始 - 1) 是某种"时长"
                //   - 除以 12 表示每 12 个单位收 1 费
                //   - Math.max(0, ...) 保证非负
                //   - 整数除法，向 0 取整
                int fee = Math.max(0, (record[1] - record[0] - 1) / 12);

                // 累加到对应房间
                fees.put(record[2], fees.getOrDefault(record[2], 0) + fee);
            }

            // ---------- 2. 取出所有房间号 ----------
            java.util.List<Integer> rooms = new java.util.ArrayList<>(fees.keySet());

            // ---------- 3. 排序 ----------
            // 规则：
            //   1. 费用高的在前（降序）
            //   2. 费用相同时，房间号小的在前（升序）
            rooms.sort((left, right) -> {
                // 先按费用降序
                int byFee = Integer.compare(fees.get(right), fees.get(left));
                // 费用不同，直接返回
                // 费用相同，按房间号升序
                return byFee != 0 ? byFee : Integer.compare(left, right);
            });

            // ---------- 4. 转成二维数组 ----------
            int[][] result = new int[rooms.size()][2];
            for (int i = 0; i < rooms.size(); i++) {
                int room = rooms.get(i);
                result[i][0] = room;              // 第一列：房间号
                result[i][1] = fees.get(room);    // 第二列：总费用
            }
            return result;
        }
    }

}
