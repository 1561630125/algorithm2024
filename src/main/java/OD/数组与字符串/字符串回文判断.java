package OD.数组与字符串;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-18 19:51
 */
public class 字符串回文判断 {
    static public int[] palindromeDeletionIndices(String s) {
        // write code here

        LinkedList<Integer> res = new LinkedList<>();
        int index = 0;
        for(int i = 0; i < s.length(); i++) {
            String leftStr = s.substring(0,i);
            String rightStr = s.substring(i+1);

            String newStr = leftStr + rightStr;
            int left = 0;
            int right = newStr.length() - 1;

            boolean flag = true;
            while (left <= right) {
                if (newStr.charAt(left) == newStr.charAt(right)) {
                    left++;
                    right--;
                }else {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                res.add(i);
            }
        }

        return res.stream().mapToInt(Integer::intValue).toArray();
    }


    public class Solution {

        /**
         * 返回一个下标数组。
         *
         * 注意：从代码逻辑看，它返回的是“与某个中心字符相同的连续段”的所有下标，
         * 而不是“删除后能使剩余串成为回文的下标”。
         * 如果你的题目要求是后者，请使用文末的标准实现。
         */
        public int[] palindromeDeletionIndices(String s) {
            int n = s.length();

            // ------------------------------------------------------------
            // 第一步：双指针从两端向中间匹配，找到第一对不相等的字符
            // 匹配结束后：
            //   [0, left-1] 与 [right+1, n-1] 是对称相等的外层
            //   left 和 right 是第一个不匹配的位置
            // 若整个串本身是回文，则循环结束时 left >= right
            // ------------------------------------------------------------
            int left = 0, right = n - 1;
            while (left < right && s.charAt(left) == s.charAt(right)) {
                left++;
                right--;
            }

            // centers 用于存放“中心位置”，最多 2 个
            // centerCount 记录实际存了几个
            int[] centers = new int[2];
            int centerCount = 0;

            if (left >= right) {
                // --------------------------------------------------------
                // 情况 A：整个串已经是回文，不需要删除任何字符
                // 这里取 n/2 作为“中心位置”。
                // 注意：对偶数长度串，n/2 是中间偏右的位置，并不是严格意义的中心，
                // 这里只是沿用原代码的写法。
                // --------------------------------------------------------
                centers[centerCount++] = n / 2;
            } else {
                // --------------------------------------------------------
                // 情况 B：存在不匹配对 (left, right)
                // 尝试两种删除方案：
                //   side == 0：跳过 left，检查 [left+1, right] 是否回文
                //   side == 1：跳过 right，检查 [left, right-1] 是否回文
                // 若某种方案剩下的子串是回文，则把被跳过的位置记为中心
                // --------------------------------------------------------
                for (int side = 0; side < 2; side++) {
                    // side==0: a = left+1, b = right      （跳过 left）
                    // side==1: a = left,   b = right-1    （跳过 right）
                    int a = left + (side == 0 ? 1 : 0);
                    int b = right - (side == 1 ? 1 : 0);

                    // 在 [a, b] 区间内继续双指针匹配
                    while (a < b && s.charAt(a) == s.charAt(b)) {
                        a++;
                        b--;
                    }

                    // a >= b 表示 [a, b] 区间已经全部匹配完，即该子串是回文
                    if (a >= b) {
                        // 记录被跳过的那个位置作为“中心”
                        // side==0 时跳过的是 left，side==1 时跳过的是 right
                        centers[centerCount++] = (side == 0) ? left : right;
                    }
                }
            }

            // ------------------------------------------------------------
            // 第二步：对每个“中心”，向左右扩展出所有与 s.charAt(center) 相同的
            // 连续字符，把这些下标加入结果。
            //
            // 这里用 int[n] 临时存放，最后用 Arrays.copyOf 截断。
            // 风险：如果两个中心扩展出的区间有重叠，count 可能超过 n，
            //       导致 ArrayIndexOutOfBoundsException。
            //       更安全的做法是用 List<Integer>。
            // ------------------------------------------------------------
            int[] result = new int[n];
            int count = 0;

            for (int i = 0; i < centerCount; i++) {
                int center = centers[i];

                // 以 center 为中心，向左右扩展相同字符
                int a = center;
                int b = center;

                // 向左扩展：只要左边字符与中心字符相同，就继续左移
                while (a > 0 && s.charAt(a - 1) == s.charAt(center)) {
                    a--;
                }

                // 向右扩展：只要右边字符与中心字符相同，就继续右移
                while (b + 1 < n && s.charAt(b + 1) == s.charAt(center)) {
                    b++;
                }

                // 把 [a, b] 区间内所有下标加入结果
                for (int index = a; index <= b; index++) {
                    result[count++] = index;
                }
            }

            // 截断到实际长度
            return Arrays.copyOf(result, count);
        }
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(palindromeDeletionIndices("abca")));
        System.out.println(Arrays.toString(palindromeDeletionIndices("abcd")));
    }
}
