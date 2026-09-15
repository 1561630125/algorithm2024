package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 18:56
 */
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MonotonicQueue {

    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] res = new int[n - k + 1];
        // 双端队列，存下标，对应值单调递减
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // 1. 队尾弹出比当前值小的元素
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            // 2. 当前下标入队
            deque.offerLast(i);
            // 3. 队首滑出窗口则弹出
            if (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            // 4. 窗口形成后记录答案
            if (i >= k - 1) {
                res[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3};
        int k = 3;
        System.out.println(Arrays.toString(maxSlidingWindow(nums, k)));
        // 输出: [3, 3, 5, 5]
    }
}
