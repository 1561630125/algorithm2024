package 力扣.双指针.A11;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-19 23:15
 */
public class 盛水最多的容器 {


    //双指针
    public int maxArea(int[] height) {
        int l = 0, r = height.length - 1;
        int ans = 0;
        while (l < r) {
            int area = Math.min(height[l], height[r]) * (r - l);
            ans = Math.max(ans, area);
            if (height[l] <= height[r]) {
                ++l;
            }
            else {
                --r;
            }
        }
        return ans;
    }
}
