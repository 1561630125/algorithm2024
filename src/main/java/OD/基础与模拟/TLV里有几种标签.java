package OD.基础与模拟;

import java.util.HashSet;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 21:04
 */
public class TLV里有几种标签 {

    public int countTagCategories(int[] tag) {

        HashSet<Integer> res = new HashSet<>();
        
        for(int i = 0; i < tag.length; i++) {
            int i1 = tag[0];
            int i2 = tag[1];

            int lenght = 2 + i2;
            if (lenght <= 4) {
                lenght = 4;
            }else {
                lenght = lenght % 4 == 0 ? lenght : lenght + (4 - lenght % 4);
            }

            if (lenght < tag.length) {
                res.add(i1);
            }
            i = i + lenght;
        }
        return res.size();
    }

    /**
     * 统计标签数组中不同类别（tag）的数量。
     *
     * 数据格式说明：
     * - 数组按块（block）组织，每个块包含一个标签类别和其对应的标签值序列
     * - 块结构：[tag, length, value1, value2, ...]
     *   - tag: 类别标识（必须 0~99）
     *   - length: 后续值的个数（必须 > 0）
     *   - values: length 个标签值（必须 0~99）
     * - 每个块的总字节长度会被补齐到 4 的倍数（用于内存对齐）
     *
     * @param tag 标签数组（整型数组）
     * @return 不同标签类别的数量；如果格式无效则返回 0
     */
    public int countTagCategories2(int[] tag) {
        // 使用 Set 自动去重，统计不同的 tag 值
        java.util.Set<Integer> categories = new java.util.HashSet<>();

        int index = 0;

        // 遍历数组中的每个块
        while (index < tag.length) {

            // ----- 1. 验证长度字段是否存在且合法 -----
            // 如果当前块没有足够的空间存放 length 字段，或者 length <= 0，则格式无效
            if (index + 1 >= tag.length || tag[index + 1] <= 0) {
                return 0;  // 无效格式
            }

            // ----- 2. 计算当前块的总长度（对齐到 4 的倍数）-----
            // 块结构：tag(1个) + length(1个) + values(length个)
            // 原始长度 = 2 + tag[index + 1]
            // 对齐公式：((原始长度 + 3) / 4) * 4  即向上取整到 4 的倍数
            int length = ((2 + tag[index + 1] + 3) / 4) * 4;
            // 例如：如果 length=1，则 2+1=3，对齐后为 4
            //      如果 length=2，则 2+2=4，对齐后为 4
            //      如果 length=3，则 2+3=5，对齐后为 8

            // ----- 3. 验证块长度是否超出数组边界 -----
            if (index + length > tag.length) {
                return 0;  // 数据不完整
            }

            // ----- 4. 验证块内所有值是否在有效范围 [0, 99] -----
            for (int position = index; position < index + length; position++) {
                if (tag[position] < 0 || tag[position] > 99) {
                    return 0;  // 非法值
                }
            }

            // ----- 5. 记录当前块的 tag 类别 -----
            categories.add(tag[index]);  // tag 在块的首位

            // ----- 6. 移动到下一个块 -----
            index += length;
        }

        // 返回不同 tag 类别的数量
        return categories.size();
    }

}
