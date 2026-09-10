package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 13:48
 */
public class 等和最长段 {

    static long longestTargetSumWindow(long[] numbers, long target) {

        long[] arr = new long[numbers.length];
        arr[0] = numbers[0];
        for(int i = 1; i < numbers.length; i++) {
            arr[i] = arr[i-1]+numbers[i];
        }

        int left = 0;
        int res = -1;
        for(int i = 0; i < numbers.length; i++) {
            if (arr[i] - arr[left] == target) {
                res = Math.max(res, i - left);
            }
            while (arr[i] - arr[left] > target){
                left++;
            }
        }
        return res;
    }

    static long longestTargetSumWindow2(long[] numbers, long target) {
        int left = 0, right = 0;
        long total = 0, answer = -1;
        while (right < numbers.length) {
            total += numbers[right++];
            while (total > target && left < right)
                total -= numbers[left++];
            if (total == target)
                answer = Math.max(answer, right - left);
        }
        return answer;
    }

    public static void main(String[] args) {
        long[] arr = new long[]{1,3,3,3,5,5,1,1};
        int tart = 12;
        System.out.println(longestTargetSumWindow(arr, tart));
        System.out.println(longestTargetSumWindow2(arr, tart));
    }

}
