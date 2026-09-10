package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 17:12
 */
public class 数组最长跳跃段 {

    /**
     * 数组中的最长跳跃段
     *
     * @param nums int[] — 待分析整数序列
     * @return int
     */
    static public int longestNonContinuous(int[] nums) {
        if (nums.length == 1) return 1;

        int left = 0;
        int res = 0;
        for (int i = 1; i < nums.length; ) {

            int pre = nums[left];
            while (i< nums.length && Math.abs(nums[i] - pre) > 1) {
                pre = nums[i];
                i++;
            }
            res = Math.max(i - left, res);
            left++;
        }

        return res;
    }


    public int longestNonContinuous2(int[] nums) {
        if (nums.length == 0)
            return 0;
        int answer = 1, start = 0;
        for (int end = 1; end < nums.length; end++) {
            if (Math.abs((long)nums[end] - nums[end - 1]) <= 1)
                start = end;
            answer = Math.max(answer, end - start + 1);
        }
        return answer;
    }

    public static void main(String[] args) {

        System.out.println(longestNonContinuous(new int[]{0,2,4,6}));
    }
}


