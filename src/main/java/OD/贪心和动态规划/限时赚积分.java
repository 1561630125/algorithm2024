package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:52
 */
public class 限时赚积分 {
    long maxTaskPoints(int availableTime, long[][] tasks) {
        long[][] ordered = tasks.clone();
        java.util.Arrays.sort(ordered, java.util.Comparator.comparingLong(task -> task[0]));
        java.util.PriorityQueue<Long> selected = new java.util.PriorityQueue<>();
        long total = 0;
        for (long[] task : ordered) {
            long capacity = Math.min(task[0], Math.max(availableTime, 0));
            if (capacity <= 0 || task[1] <= 0)
            selected.add(task[1]); total += task[1];
            if (selected.size() > capacity) total -= selected.remove();
        }
        return total;
    }
}
