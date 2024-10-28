package 力扣.回溯.A39;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-28 10:46
 */
public class 组合总和 {

}


class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        List<Integer> combine = new ArrayList<Integer>();
        dfs(candidates, target, ans, combine, 0);
        return ans;
    }

    public void dfs(int[] candidates, int target, List<List<Integer>> ans, List<Integer> combine, int idx) {
        if (idx == candidates.length) {
            return;
        }
        if (target == 0) {
            ans.add(new ArrayList<Integer>(combine));
            return;
        }
        // 直接跳过
        dfs(candidates, target, ans, combine, idx + 1);
        // 选择当前数
        if (target - candidates[idx] >= 0) {
            combine.add(candidates[idx]);
            dfs(candidates, target - candidates[idx], ans, combine, idx);//每个数字可以被无限制重复选取，因此搜索的下标仍为 idx。
            combine.remove(combine.size() - 1);
        }
    }
}
