package 力扣.Kadane算法.A53;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-29 14:38
 */

/**
 * <img width="640" height="320" src="https://gitee.com/yfmzzzzzz/myimg/raw/master/myimg/image-20241029150218157-2024-10-2915:02:58.png" alt="">
 */

public class 最大子数组和 {

}

//动态规划
class Solution {
    public int maxSubArray(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            pre = Math.max(pre + x, x);
            maxAns = Math.max(maxAns, pre);
        }
        return maxAns;
    }
}

//分治
class Solution2 {
    public class Status {
        public int lSum, rSum, mSum, iSum;

        public Status(int lSum, int rSum, int mSum, int iSum) {
            this.lSum = lSum;
            this.rSum = rSum;
            this.mSum = mSum;
            this.iSum = iSum;
        }
    }

    public int maxSubArray(int[] nums) {
        return getInfo(nums, 0, nums.length - 1).mSum;
    }

    public Status getInfo(int[] a, int l, int r) {
        if (l == r) {
            return new Status(a[l], a[l], a[l], a[l]);
        }
        int m = (l + r) >> 1;
        Status lSub = getInfo(a, l, m);
        Status rSub = getInfo(a, m + 1, r);
        return pushUp(lSub, rSub);
    }

    public Status pushUp(Status l, Status r) {
        int iSum = l.iSum + r.iSum; // [l,r] 的区间和
        int lSum = Math.max(l.lSum, l.iSum + r.lSum); //以 l 为左端点的最大子段和
        int rSum = Math.max(r.rSum, r.iSum + l.rSum); //以 r 为右端点的最大子段和
        int mSum = Math.max(Math.max(l.mSum, r.mSum), l.rSum + r.lSum);
        return new Status(lSum, rSum, mSum, iSum);
    }
}
