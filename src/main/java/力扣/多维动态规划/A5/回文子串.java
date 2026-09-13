package 力扣.多维动态规划.A5;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 23:46
 */
public class 回文子串 {


    // 中心扩展法
    class Solution {
        public int countSubstrings(String s) {
            int n = s.length();
            int ans = 0;
            for (int i = 0; i < 2 * n - 1; i++) {
                int l = i / 2, r = (i + 1) / 2;
                while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                    ans++;
                    l--;
                    r++;
                }
            }
            return ans;
        }
    }

    class Solution2 {
        public int countSubstrings(String s) {
            // Manacher 模板
            // 将 s 改造为 t，这样就不需要讨论 len(s) 的奇偶性，因为新串 t 的每个回文子串都是奇回文串（都有回文中心）
            // s 和 t 的下标转换关系：
            // (si+1)*2 = ti
            // ti/2-1 = si
            // ti 为偶数，对应奇回文串（从 2 开始）
            // ti 为奇数，对应偶回文串（从 3 开始）
            int n = s.length();
            char[] t = new char[n * 2 + 3];
            Arrays.fill(t, '#');
            t[0] = '^';
            for (int i = 0; i < n; i++) {
                t[i * 2 + 2] = s.charAt(i);
            }
            t[n * 2 + 2] = '$';

            // 定义一个奇回文串的回文半径=(长度+1)/2，即保留回文中心，去掉一侧后的剩余字符串的长度
            // halfLen[i] 表示在 t 上的以 t[i] 为回文中心的最长回文子串的回文半径
            // 即 [i-halfLen[i]+1,i+halfLen[i]-1] 是 t 上的一个回文子串
            int[] halfLen = new int[t.length - 2];
            halfLen[1] = 1;

            int ans = 0;
            // boxR 表示当前右边界下标最大的回文子串的右边界下标+1
            // boxM 为该回文子串的中心位置，二者的关系为 r=mid+halfLen[mid]
            int boxM = 0;
            int boxR = 0;
            for (int i = 2; i < halfLen.length; i++) {
                int hl = 1;
                if (i < boxR) {
                    // 记 i 关于 boxM 的对称位置 i'=boxM*2-i
                    // 若以 i' 为中心的最长回文子串范围超出了以 boxM 为中心的回文串的范围（即 i+halfLen[i'] >= boxR）
                    // 则 halfLen[i] 应先初始化为已知的回文半径 boxR-i，然后再继续暴力匹配
                    // 否则 halfLen[i] 与 halfLen[i'] 相等
                    hl = Math.min(halfLen[boxM * 2 - i], boxR - i);
                }

                // 暴力扩展
                while (t[i - hl] == t[i + hl]) {
                    hl++;
                    boxM = i;
                    boxR = i + hl;
                }

                halfLen[i] = hl;
                ans += hl / 2;
            }

            return ans;
        }
    }

}
