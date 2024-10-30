package 力扣.堆.A502;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-30 14:48
 */
public class IPO {

}
//利用堆的贪心算法
class Solution {
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        int curr = 0;
        int[][] arr = new int[n][2];

        for (int i = 0; i < n; ++i) {
            arr[i][0] = capital[i];
            arr[i][1] = profits[i];
        }
        Arrays.sort(arr, (a, b) -> a[0] - b[0]);//将项目按照所需资本的从小到大进行排序

        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> y - x);
        for (int i = 0; i < k; ++i) {
            while (curr < n && arr[curr][0] <= w) { //从所有投入资本小于等于 w 的项目中选择利润最大的项目 j
                pq.add(arr[curr][1]);
                curr++;
            }
            if (!pq.isEmpty()) {
                w += pq.poll();
            } else {
                break;
            }
        }

        return w;
    }
}

