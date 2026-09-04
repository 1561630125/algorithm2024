package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:36
 */
public class 字母串摘要 {

    /**
     * 生成字符串的摘要信息
     *
     * 核心逻辑：
     * 1. 过滤出所有英文字母，统一转为小写
     * 2. 统计每个字母在整个文本中的总出现次数（remaining数组）
     * 3. 按连续字符分组，每组的count取值为：连续长度>1时用连续长度，否则用该字母剩余总次数
     * 4. 按count降序排序，count相同时按字母升序排序
     * 5. 输出格式：字母+数字（如 a3b2c1）
     *
     * @param text  输入文本（可包含非字母字符）
     * @return      格式化的字符串摘要
     */
    String stringSummary(String text) {
        // ========== 第一步：过滤字母，统一转小写 ==========
        StringBuilder filtered = new StringBuilder();
        int[] remaining = new int[26];  // 记录每个字母在全文中的总出现次数

        for (int index = 0; index < text.length(); index++) {
            char value = text.charAt(index);
            // 只保留英文字母（大小写都保留）
            if ((value >= 'a' && value <= 'z') || (value >= 'A' && value <= 'Z')) {
                char lower = Character.toLowerCase(value);
                filtered.append(lower);
                remaining[lower - 'a']++;  // 统计总次数
            }
        }

        // ========== 第二步：按连续字符分组 ==========
        java.util.List<Group> groups = new java.util.ArrayList<>();

        for (int index = 0; index < filtered.length();) {
            // 找出从index开始连续相同的字符
            int end = index + 1;
            while (end < filtered.length() && filtered.charAt(end) == filtered.charAt(index)) {
                end++;
            }

            char value = filtered.charAt(index);
            int length = end - index;  // 连续长度

            // 从总次数中减去当前连续段的长度
            remaining[value - 'a'] -= length;

            // 关键逻辑：count的取值规则
            // - 如果连续长度 > 1，使用连续长度作为count
            // - 如果连续长度 == 1，使用该字母剩余的未连续出现次数
            //   注意：remaining已经扣除了当前段，所以是指"后续还会出现多少次"
            int count = length > 1 ? length : remaining[value - 'a'];

            groups.add(new Group(value, count));
            index = end;  // 跳到下一个不同的字符
        }

        // ========== 第三步：排序 ==========
        // 规则：先按count降序（大数字优先），count相同则按字母升序（a-z）
        groups.sort((left, right)
                -> left.count != right.count
                ? Integer.compare(right.count, left.count)  // count降序
                : Character.compare(left.value, right.value)); // 字母升序

        // ========== 第四步：输出结果 ==========
        StringBuilder result = new StringBuilder();
        for (Group group : groups) {
            result.append(group.value).append(group.count);
        }
        return result.toString();
    }

    /**
     * 内部类：表示一个字符组
     * value: 字符本身（小写字母）
     * count: 该组的数量值（可能表示连续长度，也可能表示剩余出现次数）
     */
    private static class Group {
        final char value;
        final int count;
        Group(char value, int count) {
            this.value = value;
            this.count = count;
        }
    }


}
