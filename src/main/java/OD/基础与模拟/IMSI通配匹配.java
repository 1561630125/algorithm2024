package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 14:48
 */
public class IMSI通配匹配 {

    /**
     * 检查给定的IMSI值是否匹配指定的模式字符串。
     * 支持通配符：'?'匹配任意单个数字（但有奇偶位置限制），'*'匹配任意序列（包括空序列）。
     * 匹配规则特殊：'?'只能出现在偶数索引位置（0-based），否则匹配失败。
     *
     * @param pattern 匹配模式，可包含 '*' 和 '?' 通配符
     * @param value   待匹配的IMSI字符串
     * @return 如果value匹配pattern则返回true，否则false
     */
    private boolean imsiMatches(String pattern, String value) {
        // 全通配符直接匹配任意值
        if (pattern.equals("*")) return true;

        int sourceSize = value.length(), patternSize = pattern.length(), index = 0;

        // 从左到右逐个字符匹配（非通配符或'?'）
        while (index < sourceSize && index < patternSize) {
            char current = pattern.charAt(index);

            // 普通字符：必须完全相等
            if (current == value.charAt(index)) {
                index++;
                continue;
            }

            // '?'通配符：只能出现在偶数索引，否则失败
            if (current == '?') {
                if (index % 2 == 0) return false;
                index++;
                continue;
            }

            // '*'通配符：匹配任意剩余序列
            if (current == '*') {
                // 如果'*'是模式末尾，则匹配剩余所有字符
                if (index == patternSize - 1) return true;

                // 尝试用'*'后的后缀从右向左匹配value的剩余部分
                String suffix = pattern.substring(index + 1);
                int position = suffix.length() - 1, source = sourceSize - 1;

                while (source >= index && position >= 0) {
                    char expected = suffix.charAt(position);

                    // 普通字符匹配
                    if (expected == value.charAt(source)) {
                        source--;
                        position--;
                    }
                    // '?'匹配：必须出现在偶数索引，否则失败
                    else if (expected == '?') {
                        if (source % 2 == 0) break;
                        source--;
                        position--;
                    }
                    // 不匹配则终止
                    else break;
                }
                // 如果后缀全部匹配完（position < 0）则成功
                return position < 0;
            }

            // 其他字符不匹配
            return false;
        }

        // 循环结束后处理边界情况：
        // 要么两者长度相等，要么模式比value多一个'*'（表示匹配空串）
        return patternSize == sourceSize || (patternSize == sourceSize + 1 && pattern.charAt(patternSize - 1) == '*');
    }

    /**
     * 从给定的模式数组中筛选出所有匹配指定IMSI的模式，并排序后返回。
     *
     * @param patterns 待筛选的IMSI模式数组
     * @param imsi     待匹配的IMSI字符串
     * @return 按字典序排序的匹配模式数组
     */
    String[] matchImsiConfigurations(String[] patterns, String imsi) {
        java.util.ArrayList<String> result = new java.util.ArrayList<>();

        // 遍历所有模式，保留匹配的
        for (String pattern : patterns) {
            if (imsiMatches(pattern, imsi)) {
                result.add(pattern);
            }
        }

        // 按字典序排序
        java.util.Collections.sort(result);

        // 转换为数组返回
        return result.toArray(new String[0]);
    }
}
