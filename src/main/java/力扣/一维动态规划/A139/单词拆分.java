package 力扣.一维动态规划.A139;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 8:56
 */
public class 单词拆分 {

}


class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordDictSet = new HashSet(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; //dp[i] 表示字符串 s 前 i 个字符组成的字符串 s[0..i−1] 是否能被空格拆分成若干个字典中出现的单词
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDictSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }
}
