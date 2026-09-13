package OD.数组与字符串;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 14:07
 */
public class 设计选手排名 {

    int[] rankShooters(int[] ids, int[] scores) {

        HashMap<Integer,LinkedList<Integer>> hashMap = new HashMap<>();
        for(int i = 0; i < scores.length; i++) {
            if (hashMap.containsKey(ids[i])){
                hashMap.get(ids[i]).add(scores[i]);
            }else {
                LinkedList<Integer> linkedList = new LinkedList<>();
                linkedList.add(scores[i]);
                hashMap.put(ids[i], linkedList);
            }
        }

        HashMap<Integer,Integer> hashMap2 = new HashMap<>();
        Set<Map.Entry<Integer, LinkedList<Integer>>> entrySet = hashMap.entrySet();
        for(Map.Entry<Integer, LinkedList<Integer>> entry : entrySet) {
            Integer key = entry.getKey();
            LinkedList<Integer> value = entry.getValue();
            if (value.size() >= 3) {
                value.sort(Comparator.reverseOrder());
                int sum = value.get(0) + value.get(1) + value.get(2);
                hashMap2.put(sum,key);
            }
        }

        return new int[0];
    }


    class Solution {

        /**
         * 计算每个选手的前三名成绩之和，并按规则排序返回选手 id。
         *
         * @param ids    选手 id 数组
         * @param scores 对应的成绩数组（ids[i] 对应的成绩是 scores[i]）
         * @return 排序后的选手 id 数组
         */
        int[] rankShooters(int[] ids, int[] scores) {

            // 1. 分组：把同一个选手（id）的所有成绩收集到一个 List 里
            //    key   = 选手 id
            //    value = 该选手的所有成绩
            java.util.Map<Integer, java.util.List<Integer>> grouped = new java.util.HashMap<>();

            // 遍历 ids / scores，取两者较短的长度，避免越界
            for (int index = 0; index < Math.min(ids.length, scores.length); index++) {
                // computeIfAbsent：如果该 id 还没出现过，就先创建一个空 List 再返回；
                // 然后把当前成绩加入该 id 对应的 List
                grouped.computeIfAbsent(ids[index], ignored -> new java.util.ArrayList<>())
                        .add(scores[index]);
            }

            // 2. 计算每个选手的“前三名成绩之和”，存入 ranking
            //    每个元素是一个 long[]{总分, 选手id}
            java.util.List<long[]> ranking = new java.util.ArrayList<>();

            for (java.util.Map.Entry<Integer, java.util.List<Integer>> entry : grouped.entrySet()) {
                java.util.List<Integer> values = entry.getValue(); // 该选手的所有成绩

                // 成绩不足 3 个，无法取前三名，跳过
                if (values.size() < 3) continue;

                // 把成绩从大到小排序（降序）
                values.sort(java.util.Collections.reverseOrder());

                // 取前三名相加作为总分，和选手 id 一起放入 ranking
                // 用 long 是为了防止 int 相加溢出
                ranking.add(new long[]{
                        (long) values.get(0) + values.get(1) + values.get(2),
                        entry.getKey()
                });
            }

            // 3. 排序 ranking
            //    规则：
            //      先按总分【降序】（分高的排前面）
            //      总分相同，再按选手 id【升序】（id 小的排前面）
            ranking.sort((left, right) ->
                    left[0] != right[0]
                            ? Long.compare(right[0], left[0])   // 总分降序
                            : Long.compare(left[1], right[1])   // id 升序
            );

            // 4. 把排序后的选手 id 取出来，放进 int[] 返回
            int[] result = new int[ranking.size()];
            for (int index = 0; index < result.length; index++) {
                result[index] = (int) ranking.get(index)[1];
            }
            return result;
        }
    }

}
