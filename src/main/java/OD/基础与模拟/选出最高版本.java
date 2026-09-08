package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 22:58
 */
public class 选出最高版本 {

    public class Solution {

        /**
         * 内部静态类，用于表示一个语义化版本（Semantic Version）的所有组成部分。
         * 包括原始字符串、核心版本号、预发布标识、构建元数据等。
         */
         class Version {
            String raw;          // 原始版本字符串
            String preRaw;       // 预发布原始字符串（不含核心版本）
            String buildRaw;     // 构建元数据原始字符串（不含核心版本和预发布）
            long[] core;         // 核心版本号数组 [major, minor, patch]
            String[] pre;        // 预发布标识符数组（按 '.' 分割）
            String[] build;      // 构建元数据标识符数组（按 '.' 分割）
        }

        /**
         * 解析版本字符串，将其转换为 Version 对象。
         * <p>
         * 解析规则遵循语义化版本 2.0.0（SemVer 2.0.0）规范：
         * <ul>
         *     <li>格式：<code>主版本.次版本.补丁版本[-预发布][+构建]</code></li>
         *     <li>核心版本必须为三位非负整数，且不大于 2147483647</li>
         *     <li>预发布和构建标识符由字母、数字、连字符组成，且不能以连字符开头或结尾</li>
         *     <li>不允许出现多个 '+' 号</li>
         *     <li>'+' 后不能为空</li>
         * </ul>
         *
         * @param raw 待解析的版本字符串
         * @return 解析后的 Version 对象，如果格式不合法则返回 null
         */
        Version parse(String raw) {
            if (raw.chars().filter(value -> value == '+').count() > 1)
                return null;
            String[] plus = raw.split("\\+", -1);
            if (plus.length > 1 && plus[1].isEmpty())
                return null;
            String[] dash = plus[0].split("-", 2), core = dash[0].split("\\.", -1);
            if (core.length != 3)
                return null;
            Version value = new Version();
            value.raw = raw;
            value.core = new long[3];
            try {
                for (int i = 0; i < 3; i++) {
                    if (!core[i].matches("0|[1-9][0-9]*"))
                        return null;
                    value.core[i] = Long.parseLong(core[i]);
                    if (value.core[i] > 2147483647L)
                        return null;
                }
            } catch (Exception error) {
                return null;
            }
            value.preRaw = dash.length > 1 ? dash[1] : null;
            value.buildRaw = plus.length > 1 ? plus[1] : null;
            value.pre = tokens(value.preRaw, true);
            value.build = tokens(value.buildRaw, false);
            return value.pre == null || value.build == null ? null : value;
        }

        /**
         * 将预发布或构建元数据字符串按 '.' 分割为标识符数组。
         * <p>
         * 每个标识符必须符合以下规则：
         * <ul>
         *     <li>只能包含字母（A-Z, a-z）、数字（0-9）或连字符（-）</li>
         *     <li>不能以连字符开头或结尾</li>
         *     <li>若 bounded 为 true（即预发布场景），纯数字标识符的值不能大于 2147483647</li>
         * </ul>
         *
         * @param raw     待分割的原始字符串（可能为 null）
         * @param bounded 是否为预发布场景（true 表示需对纯数字标识符做大小限制）
         * @return 分割后的标识符数组，若任一标识符不合法则返回 null；若 raw 为 null 则返回空数组
         */
        String[] tokens(String raw, boolean bounded) {
            if (raw == null)
                return new String[0];
            String[] parts = raw.split("\\.", -1);
            for (String part : parts) {
                if (!part.matches("[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"))
                    return null;
                if (bounded && part.matches("[0-9]+"))
                    try {
                        if (Long.parseLong(part) > 2147483647L)
                            return null;
                    } catch (Exception error) {
                        return null;
                    }
            }
            return parts;
        }

        /**
         * 比较两个标识符数组的字典序顺序（用于预发布或构建元数据的比较）。
         * <p>
         * 比较规则：
         * <ul>
         *     <li>若对应位置都是数字，则按数值大小比较（使用 BigInteger 避免溢出）</li>
         *     <li>否则按字符串字典序比较（ASCII 顺序）</li>
         *     <li>若所有公共位置都相等，则较长的数组较大</li>
         * </ul>
         *
         * @param a 第一个标识符数组
         * @param b 第二个标识符数组
         * @return 负数表示 a < b，正数表示 a > b，0 表示相等
         */
        int parts(String[] a, String[] b) {
            for (int i = 0; i < Math.min(a.length, b.length); i++) {
                int value;
                if (a[i].matches("[0-9]+") && b[i].matches("[0-9]+"))
                    value = new java.math.BigInteger(a[i]).compareTo(new java.math.BigInteger(b[i]));
                else
                    value = a[i].compareTo(b[i]);
                if (value != 0)
                    return value;
            }
            return Integer.compare(a.length, b.length);
        }

        /**
         * 比较两个 Version 对象，遵循语义化版本 2.0.0 的优先级规则。
         * <p>
         * 比较优先级（从高到低）：
         * <ol>
         *     <li>核心版本号（major, minor, patch）</li>
         *     <li>预发布版本（有预发布的版本低于无预发布的版本）</li>
         *     <li>预发布标识符逐项比较（数字按数值，字符串按字典序）</li>
         *     <li>构建元数据（有构建的版本高于无构建的版本）</li>
         *     <li>构建元数据标识符逐项比较（规则同预发布）</li>
         * </ol>
         *
         * @param a 第一个 Version 对象
         * @param b 第二个 Version 对象
         * @return 负数表示 a < b，正数表示 a > b，0 表示相等
         */
        int compare(Version a, Version b) {
            for (int i = 0; i < 3; i++)
                if (a.core[i] != b.core[i])
                    return Long.compare(a.core[i], b.core[i]);

            if ((a.preRaw != null) != (b.preRaw != null))
                return a.preRaw != null ? 1 : -1;
            if (a.preRaw != null) {
                int value = parts(a.pre, b.pre);
                if (value != 0)
                    return value;
            }

            if ((a.buildRaw != null) != (b.buildRaw != null))
                return a.buildRaw != null ? 1 : -1;
            return a.buildRaw == null ? 0 : parts(a.build, b.build);
        }

        /**
         * 从给定的版本字符串数组中找出语义化版本最大的一个。
         * <p>
         * 如果数组中存在任何一个无效的版本字符串，则返回空字符串。
         * 比较规则由 {@link #compare(Version, Version)} 定义。
         *
         * @param versions 版本字符串数组
         * @return 最大语义化版本的原始字符串；若存在无效版本则返回 ""；若数组为空则返回 null
         */
        public String maximumSemanticVersion(String[] versions) {
            Version best = null;
            for (String item : versions) {
                Version value = parse(item);
                if (value == null)
                    return "";
                if (best == null || compare(value, best) > 0)
                    best = value;
            }
            return best.raw;
        }
    }

}
