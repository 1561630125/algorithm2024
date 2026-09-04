package OD.基础与模拟;

import java.util.LinkedList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 21:12
 */
public class 分段大小写转换 {

    public static String transformSplitString(int groupSize, String value) {
        String[] split = value.split("-");
        List<String> res = new LinkedList<>();

        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                res.add(split[i]);
                continue;
            }
            newStr.append(split[i]);
        }

        for (int i = 0; i < newStr.toString().length(); ) {
            int min = Math.min(i + groupSize, newStr.toString().length());
            String substring = newStr.substring(i, min);

            int low = 0;
            int letter = 0;
            for (int j = 0; j < substring.length(); j++) {
                char charAt = substring.charAt(j);
                if (Character.isLetter(charAt)) {
                    letter++;
                }
                if (Character.isLetter(charAt) && Character.isLowerCase(charAt)) {
                    low++;
                }
            }

            StringBuilder newSubStr = new StringBuilder();
            if (low > letter - low) {
                newSubStr.append(substring.toLowerCase());
            } else if (low < letter - low) {
                newSubStr.append(substring.toUpperCase());
            } else {
                newSubStr.append(substring);
            }
            res.add(newSubStr.toString());
            i = i + groupSize;
        }


        return String.join("-", res);
    }

    /**
     * 将输入字符串按指定规则进行拆分和转换。
     * <p>
     * 处理规则：
     * 1. 如果 groupSize <= 0 或 value 为 null，返回空字符串。
     * 2. 查找第一个 '-' 作为分隔符，将字符串分为前缀和后缀。
     * 3. 若没有 '-'，则直接返回原字符串。
     * 4. 后缀中所有 '-' 会被移除，然后按 groupSize 大小分组。
     * 5. 对每个分组，统计其中小写字母（a-z）和大写字母（A-Z）的数量：
     *    - 若小写字母较多，则整个分组转为小写；
     *    - 若大写字母较多，则整个分组转为大写；
     *    - 若数量相等，则保持原样。
     * 6. 最终结果为：前缀 + 用 '-' 连接的各个转换后的分组。
     *
     * @param groupSize 每组的大小（必须大于 0）
     * @param value     待处理的字符串（可为 null）
     * @return 转换后的字符串，若输入无效则返回空字符串
     *
     * @example
     * transformSplitString2(3, "abc-DEF-ghiJKL")
     * // 前缀 "abc"，后缀去除 '-' 后为 "DEFghiJKL"
     * // 分组: "DEF"（大写多→转为大写）, "ghi"（小写多→转为小写）, "JKL"（大写多→转为大写）
     * // 结果: "abc-DEF-ghi-JKL"
     *
     * transformSplitString2(2, "pre-ABcd-efGH")
     * // 前缀 "pre"，后缀去除 '-' 后为 "ABcdefGH"
     * // 分组: "AB"（大写多→"AB"）, "cd"（小写多→"cd"）, "ef"（小写多→"ef"）, "GH"（大写多→"GH"）
     * // 结果: "pre-AB-cd-ef-GH"
     *
     * transformSplitString2(2, "no-separator") // 无 '-'，返回原字符串 "no-separator"
     * transformSplitString2(0, "any")          // groupSize <= 0，返回 ""
     * transformSplitString2(2, null)           // value 为 null，返回 ""
     */
    String transformSplitString2(int groupSize, String value) {
        // 参数校验：groupSize 必须大于 0，且 value 不能为 null
        if (groupSize <= 0 || value == null)
            return "";

        // 查找第一个 '-' 的位置，作为前缀和后缀的分隔
        int separator = value.indexOf('-');
        if (separator < 0)
            return value; // 没有分隔符，直接返回原字符串

        // 提取前缀，并移除后缀中所有的 '-'（只保留字母）
        String prefix = value.substring(0, separator),
                suffix = value.substring(separator + 1).replace("-", "");

        // 使用 StringBuilder 构建最终结果，初始内容为前缀
        StringBuilder result = new StringBuilder(prefix);

        // 按 groupSize 步长遍历后缀字符串
        for (int start = 0; start < suffix.length(); start += groupSize) {
            // 获取当前分组（最后一组可能长度不足 groupSize）
            String group = suffix.substring(start, Math.min(start + groupSize, suffix.length()));

            // 统计当前分组中的小写和大写字母数量
            int lower = 0, upper = 0;
            for (int index = 0; index < group.length(); index++) {
                char valueChar = group.charAt(index);
                if (valueChar >= 'a' && valueChar <= 'z')
                    lower++;
                else if (valueChar >= 'A' && valueChar <= 'Z')
                    upper++;
            }

            // 根据大小写数量决定分组转换方式：
            // - 小写多 → 全转小写
            // - 大写多 → 全转大写
            // - 相等   → 保持原样
            result.append('-').append(lower > upper ? group.toLowerCase(java.util.Locale.ROOT)
                    : upper > lower ? group.toUpperCase(java.util.Locale.ROOT)
                    : group);
        }

        return result.toString();
    }


    public static void main(String[] args) {
        int groupSize = 3;
        String value = "12abc-abCABc-3aB@";
        System.out.println(transformSplitString(groupSize, value));
    }


}
