package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 18:01
 */
public class 岛路逃生 {

    /**
     * 计算岛屿上最终能生存下来的人数
     *
     * 问题描述：
     * 在一条直线上，每个人有特定的强度值：
     * - 正数：向右走的人，强度为绝对值
     * - 负数：向左走的人，强度为绝对值
     *
     * 当两个人相遇时（向右走的人和向左走的人），会发生碰撞：
     * - 强度大的人生存，强度减少（减去对方的强度）
     * - 强度相同，两人同归于尽
     * - 相同方向的人不会相遇（速度相同，保持距离）
     *
     * @param people 每个人的强度值（正数向右，负数向左），不能为0
     * @return 最终生存的人数，如果输入为空返回-1，如果包含0返回-1
     */
    int countIslandSurvivors(int[] people) {
        // 边界检查：空数组或包含0（无效强度）
        if (people.length == 0) return -1;

        // 使用数组模拟栈，存储生存者
        int[] survivors = new int[people.length];
        int size = 0;  // 栈顶指针

        // 遍历每一个人
        for (int value : people) {
            // 强度为0无效
            if (value == 0) return -1;

            int current = value;  // 当前待处理的人

            // 关键逻辑：当当前人向左走（负数）且栈顶有人向右走（正数）时，会发生碰撞
            // 循环处理可能的多轮碰撞（例如：[5, 10, -5] 中 -5 只碰 10，不碰 5）
            while (current < 0 && size > 0 && survivors[size - 1] > 0) {
                int rightStrength = survivors[size - 1];  // 栈顶（向右走的人）的强度
                int leftStrength = -current;               // 当前（向左走的人）的强度

                if (rightStrength > leftStrength) {
                    // 向右走的人更强：生存，但强度减少
                    survivors[size - 1] = rightStrength - leftStrength;
                    current = 0;  // 当前向左走的人死亡
                } else if (rightStrength < leftStrength) {
                    // 向左走的人更强：栈顶向右走的人死亡，继续比较
                    size--;       // 弹出栈顶（死亡）
                    current = -(leftStrength - rightStrength);  // 当前人强度减少，继续向左走
                } else {
                    // 强度相同：同归于尽
                    size--;       // 弹出栈顶（死亡）
                    current = 0;  // 当前人也死亡
                }
            }

            // 如果当前人还活着，入栈
            if (current != 0) {
                survivors[size++] = current;
            }
        }

        return size;  // 栈中元素数量 = 生存人数
    }

}
