package OD.搜索与枚举;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 16:24
 */
public class 预算组设备 {
     class Device {
        public long type;
        public long reliability;
        public long price;

        public Device(long type, long reliability, long price) {
            this.type = type;
            this.reliability = reliability;
            this.price = price;
        }

        public long getType() {
            return type;
        }

        public long getReliability() {
            return reliability;
        }

        public long getPrice() {
            return price;
        }
    }

    long dfs(LinkedList<Device> devices, long remain, long[] types, long budget) {
        if (remain == 0 && budget >= 0) {
//            Optional<Device> min = devices.stream().min(Comparator.comparingLong(Device::getReliability));
//            if (min.isPresent()) return min.get().getReliability();

            OptionalLong min1 = devices.stream().mapToLong(Device::getReliability).min();
            if (min1.isPresent()) return min1.getAsLong();
        }

        long cur = 0L;
        for(int i = 0; i < devices.size(); i++) {
            cur += devices.get(i).getPrice();


        }

        return 0L;
    }

    long maxDeviceReliability(long budget, int typeCount, long[][] components) {
        int length = components.length;
        LinkedList<Device> devices = new LinkedList<>();
        for(int i = 0; i < length; i++) {
            long type = components[i][0];
            long reliability = components[i][1];
            long price = components[i][2];
            devices.add(new Device(type,reliability,price));
        }


        Map<Long, List<Device>> deviceMap = devices.stream().collect(Collectors.groupingBy(Device::getType));
        long[] types = new long[deviceMap.size()];


        return -1L;
    }


    /**
     * 在预算 budget 内，给 typeCount 种类型各选一个组件，
     * 使所有组件中的最小可靠性尽可能大
     *
     * components[i] = {type, reliability, price}
     *
     * @return 最大的「最小可靠性」；无解返回 -1
     */
    long maxDeviceReliability2(long budget, int typeCount, long[][] components) {

        // 参数校验
        if (typeCount <= 0 || components == null)
            return -1;

        // 1. 收集所有出现过的可靠性值，去重并升序排序
        java.util.TreeSet<Long> candidateSet = new java.util.TreeSet<>();
        for (long[] component : components) {
            if (component == null || component.length != 3)
                return -1;
            // 只收集合法类型的组件
            if (component[0] >= 0 && component[0] < typeCount)
                candidateSet.add(component[1]);
        }

        // TreeSet 转数组，方便二分
        long[] candidates = new long[candidateSet.size()];
        int position = 0;
        for (long value : candidateSet)
            candidates[position++] = value;

        // 2. 二分答案：找最大的可行可靠性
        int left = 0, right = candidates.length - 1;
        long answer = -1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            // 判断：能否让每种类型都选到可靠性 >= candidates[middle] 的组件，且总价 <= budget
            if (feasible(budget, typeCount, components, candidates[middle])) {
                answer = candidates[middle];  // 可行，记录，尝试更大
                left = middle + 1;
            } else {
                right = middle - 1;           // 不可行，尝试更小
            }
        }

        return answer;
    }

    /**
     * 判断：能否给每种类型都选一个可靠性 >= target 的组件，且总价 <= budget
     *
     * @param target 要求的最小可靠性
     * @return 是否可行
     */
    boolean feasible(long budget, int typeCount, long[][] components, long target) {

        // prices[type]：该类型中可靠性 >= target 的最便宜组件的价格
        long[] prices = new long[typeCount];
        java.util.Arrays.fill(prices, Long.MAX_VALUE);

        for (long[] component : components) {
            int type = (int) component[0];

            // 只考虑合法类型，且可靠性 >= target
            if (type >= 0 && type < typeCount
                    && component[1] >= target
                    && component[2] < prices[type]) {
                prices[type] = component[2];  // 更新该类型的最低价
            }
        }

        // 累加每种类型的最低价
        long total = 0;
        for (long price : prices) {
            if (price == Long.MAX_VALUE)
                return false;   // 有类型找不到满足可靠性要求的组件
            total += price;
        }

        return total <= budget;  // 总价是否在预算内
    }


    class Solution {
        private long[][] components;
        private int typeCount;
        private long budget;
        private long best = -1;

        long maxDeviceReliability(long budget, int typeCount, long[][] components) {
            this.components = components;
            this.typeCount = typeCount;
            this.budget = budget;

            // 按类型分组：groups[type] = 该类型所有组件 {reliability, price}
            List<List<long[]>> groups = new ArrayList<>();
            for (int i = 0; i < typeCount; i++) groups.add(new ArrayList<>());
            for (long[] c : components) {
                int type = (int) c[0];
                if (type >= 0 && type < typeCount)
                    groups.get(type).add(new long[]{c[1], c[2]});  // {可靠性, 价格}
            }

            dfs(groups, 0, 0L, Long.MAX_VALUE);
            return best;
        }

        /**
         * @param groups  每种类型的候选组件
         * @param type    当前处理的类型
         * @param cost    已选组件的总价
         * @param minRel  已选组件中的最小可靠性
         */
        private void dfs(List<List<long[]>> groups, int type, long cost, long minRel) {
            // 剪枝：总价超预算
            if (cost > budget) return;

            // 所有类型都选完
            if (type == typeCount) {
                if (minRel != Long.MAX_VALUE)
                    best = Math.max(best, minRel);
                return;
            }

            // 该类型没有可选组件，方案失败
            if (groups.get(type).isEmpty()) return;

            // 枚举该类型选哪个组件
            for (long[] c : groups.get(type)) {
                long rel = c[0], price = c[1];
                dfs(groups, type + 1, cost + price, Math.min(minRel, rel));
            }
        }
    }

}
