package OD.搜索与枚举;

import java.util.Arrays;
import java.util.Collections;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 22:06
 */
public class 交替升降电梯 {

    // ===== 成员变量 =====
    private long elevatorTarget;              // 目标净上升值
    private long elevatorTotal;               // 所有步数的总和
    private long elevatorMinimum;             // 当前找到的最小差值 |rise - target|
    private long[] elevatorValues;            // 降序排序后的步数数组
    private long[] elevatorBest;              // 当前最优的排列序列
    private boolean[] elevatorSelected;       // 标记哪些步数被选为"上升"
    private int elevatorUpwardCount;          // 需要选为"上升"的步数个数

    // 比较两个序列的字典序（从前往后逐位比较）
    // left > right 返回 true
    private boolean elevatorGreater(long[] left, long[] right) {
        for (int i = 0; i < left.length; i++) {
            if (left[i] != right[i])
                return left[i] > right[i];
        }
        return false;
    }

    // 回溯：枚举所有"选哪些步数作为上升"的组合
    // index        当前考虑到的下标
    // currentSum   已选为"上升"的步数之和
    // selectedCount 已选为"上升"的步数个数
    private void elevatorVisit(int index, long currentSum, int selectedCount) {

        // 剪枝：选的上升步数超过规定数量
        if (selectedCount > elevatorUpwardCount)
            return;

        // 已经选够了 elevatorUpwardCount 个"上升"步数
        if (selectedCount == elevatorUpwardCount) {

            // 净上升 = 上升总和 - 下降总和
            // 下降总和 = 总和 - 上升总和
            long rise = currentSum - (elevatorTotal - currentSum);

            // 剪枝：净上升已经超过 target，继续加只会更大
            if (rise > elevatorTarget)
                return;

            long difference = Math.abs(rise - elevatorTarget);

            // 根据"上升/下降"的划分，构造最终的排列序列
            // 上升的放在偶数位（0,2,4...），下降的放在奇数位（1,3,5...）
            long[] sequence = new long[elevatorValues.length];
            int upward = 0, downward = 1;
            for (int step = 0; step < elevatorValues.length; step++) {
                if (elevatorSelected[step]) {
                    sequence[upward] = elevatorValues[step];
                    upward += 2;
                } else {
                    sequence[downward] = elevatorValues[step];
                    downward += 2;
                }
            }

            // 更新最优：
            // 1. 差值更小
            // 2. 差值相同但序列字典序更大
            if (difference < elevatorMinimum
                    || (difference == elevatorMinimum
                    && (elevatorBest.length == 0 || elevatorGreater(sequence, elevatorBest)))) {
                elevatorMinimum = difference;
                elevatorBest = sequence;
            }
            return;
        }

        // 枚举下一个选为"上升"的步数
        for (int next = index; next < elevatorValues.length; next++) {
            long nextSum = currentSum + elevatorValues[next];

            // 剪枝：加上这个后净上升已经超过 target
            if (nextSum - (elevatorTotal - nextSum) > elevatorTarget)
                continue;

            elevatorSelected[next] = true;
            elevatorVisit(next + 1, nextSum, selectedCount + 1);
            elevatorSelected[next] = false;  // 回溯
        }
    }

    // 主函数：求满足条件的排列
    long[] secretElevatorSequence(long target, long[] steps) {

        // 1. 把 steps 装箱成 Long[]，方便降序排序
        Long[] boxed = new Long[steps.length];
        elevatorTotal = 0L;
        for (int i = 0; i < steps.length; i++) {
            boxed[i] = steps[i];
            elevatorTotal += steps[i];
        }

        // 2. 降序排序
        java.util.Arrays.sort(boxed, java.util.Collections.reverseOrder());

        // 3. 拆箱回 long[]
        elevatorValues = new long[steps.length];
        for (int i = 0; i < steps.length; i++)
            elevatorValues[i] = boxed[i];

        // 4. 初始化状态
        elevatorTarget = target;
        elevatorUpwardCount = (steps.length + 1) / 2;  // 上升步数 = 向上取整的一半
        elevatorMinimum = elevatorTotal + target;      // 初始差值设为一个足够大的值
        elevatorSelected = new boolean[steps.length];
        elevatorBest = new long[0];

        // 5. 回溯搜索
        elevatorVisit(0, 0L, 0);

        return elevatorBest;
    }



    class ElevatorSequence {

        private long target;
        private long total;
        private long minimum;
        private long[] values;      // 降序排序后的步数
        private long[] best;
        private boolean[] selected;
        private int upwardCount;

        public long[] secretElevatorSequence(long target, long[] steps) {
            // 降序排序
            Long[] boxed = new Long[steps.length];
            total = 0;
            for (int i = 0; i < steps.length; i++) {
                boxed[i] = steps[i];
                total += steps[i];
            }
            Arrays.sort(boxed, Collections.reverseOrder());
            values = new long[steps.length];
            for (int i = 0; i < steps.length; i++) values[i] = boxed[i];

            this.target = target;
            upwardCount = (steps.length + 1) / 2;
            minimum = total + target;
            selected = new boolean[steps.length];
            best = new long[0];

            backtrack(0, 0L, 0);
            return best;
        }

        // 和你 permuteK 的 backtrack 结构一样
        // start 保证组合不重复，currentSum 是已选上升之和，count 是已选个数
        private void backtrack(int start, long currentSum, int count) {

            // 选够 upwardCount 个上升
            if (count == upwardCount) {
                long rise = currentSum - (total - currentSum);
                if (rise > target) return;

                long difference = Math.abs(rise - target);

                // 构造序列：上升放偶数位，下降放奇数位
                long[] sequence = new long[values.length];
                int up = 0, down = 1;
                for (int i = 0; i < values.length; i++) {
                    if (selected[i]) {
                        sequence[up] = values[i];
                        up += 2;
                    } else {
                        sequence[down] = values[i];
                        down += 2;
                    }
                }

                // 更新最优
                if (difference < minimum
                        || (difference == minimum
                        && (best.length == 0 || greater(sequence, best)))) {
                    minimum = difference;
                    best = sequence;
                }
                return;
            }

            // 从 start 开始枚举，保证组合不重复（对应你 permuteK 里从 start 开始）
            for (int i = start; i < values.length; i++) {
                long nextSum = currentSum + values[i];
                if (nextSum - (total - nextSum) > target) continue;

                selected[i] = true;
                backtrack(i + 1, nextSum, count + 1);  // 传 i+1，组合不重复
                selected[i] = false;
            }
        }

        private boolean greater(long[] a, long[] b) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) return a[i] > b[i];
            }
            return false;
        }
    }
}
