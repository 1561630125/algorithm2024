package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:05
 */
public class 最长前缀密码 {

    /**
     * 在给定密码数组中，找出满足以下条件的“最佳”密码：
     * 1. 该密码的所有前缀（从第1个字符到完整字符串）都必须存在于给定的密码集合中。
     * 2. 在满足条件1的所有密码中，选择长度最长的；若长度相同，则选择字典序最大的。
     *
     * @param passwords 密码字符串数组，作为候选集合
     * @return 满足条件的最佳密码；如果没有符合条件的密码，返回空字符串
     */
    String longestPrefixPassword(String[] passwords) {
        // 将密码数组转换为HashSet，用于O(1)时间复杂度的存在性检查
        java.util.Set<String> available =
                new java.util.HashSet<>(java.util.Arrays.asList(passwords));

        String best = "";  // 用于追踪当前找到的最佳密码，初始为空字符串

        // 遍历每个候选密码
        for (String password : passwords) {
            boolean valid = true;  // 标记当前密码是否满足“所有前缀都存在”的条件

            // 检查password的所有前缀（从长度1到完整长度）
            for (int length = 1; length <= password.length(); length++) {
                // 如果某个前缀不在可用集合中，则该密码无效
                if (!available.contains(password.substring(0, length))) {
                    valid = false;
                    break;  // 提前终止检查，提升效率
                }
            }

            // 如果密码有效，并且优于当前最佳密码
            if (valid
                    && (password.length() > best.length()   // 优先选择更长的密码
                    || password.length() == best.length() && password.compareTo(best) > 0)) // 长度相同时选字典序更大的
                best = password;  // 更新最佳密码
        }

        return best;  // 返回找到的最佳密码
    }


}
