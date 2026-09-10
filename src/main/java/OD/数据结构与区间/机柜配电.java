package OD.数据结构与区间;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 14:05
 */
public class 机柜配电 {

    long minimumPowerBoxes2(String s) {
        int n = s.length();
        List<Integer> mPos = new ArrayList<>();

        // 1. 记录所有 M 的位置
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'M') {
                mPos.add(i);
            }
        }

        boolean[] used = new boolean[n]; // 标记 I 是否已被使用
        long answer = 0;

        // 2. 依次处理每个 M
        for (int idx : mPos) {
            // 优先匹配右边的 I：MI
            if (idx + 1 < n && s.charAt(idx + 1) == 'I' && !used[idx + 1]) {
                used[idx + 1] = true;
                answer++;
            }
            // 否则匹配左边的 I：IM
            else if (idx - 1 >= 0 && s.charAt(idx - 1) == 'I' && !used[idx - 1]) {
                used[idx - 1] = true;
                answer++;
            }
            // 两边都没有可用的 I
            else {
                return -1;
            }
        }

        return answer;
    }


    long minimumPowerBoxes(String s) {
        long answer = 0;  // 记录需要的“能量盒”数量，也就是处理掉的 M 的组数

        // 遍历字符串，i 手动控制步进
        for (int i = 0; i < s.length();) {

            // 只关心字符 'M'，其它字符（如 'I'）直接跳过
            if (s.charAt(i) == 'M') {

                // 情况1：M 后面紧跟着 I，形如 "...MI..."
                // 用一个盒子同时覆盖 M 和它右边的 I
                if (i + 1 < s.length() && s.charAt(i + 1) == 'I') {
                    answer++;   // 消耗一个盒子
                    i += 3;     // 跳过 M、I，以及再后面一个字符（避免重复使用）
                }
                // 情况2：M 前面紧跟着 I，形如 "...IM..."
                // 用一个盒子同时覆盖左边的 I 和当前的 M
                else if (i > 0 && s.charAt(i - 1) == 'I') {
                    answer++;   // 消耗一个盒子
                    i++;        // 只跳过当前 M，继续往后看
                }
                // 情况3：M 左右都不是 I，无法配对
                else {
                    return -1;  // 无法满足条件，直接返回 -1
                }
            } else {
                // 当前字符不是 M，继续往后扫描
                i++;
            }
        }

        return answer;  // 返回最少需要的盒子数量
    }

}
