package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:48
 */
public class 圆圈报数 {
    /**
     * 圆圈报数游戏（约瑟夫环问题）
     * 100个人围成一圈，从1开始报数，数到step的人出列，直到剩余人数小于step
     *
     * @param step 报数步长（必须满足 1 < step < 100）
     * @return 剩余人员的编号列表（逗号分隔），如果step不合法则返回"ERROR!"
     */
    static String remainingCirclePlayers(int step) {
        // ==================== 1. 参数校验 ====================
        // 根据题目要求，step必须在(1, 100)范围内
        if (step <= 1 || step >= 100)
            return "ERROR!";

        // ==================== 2. 初始化游戏参与者 ====================
        // 创建ArrayList存储100个人的编号（1~100）
        // 使用ArrayList是因为需要频繁的随机访问和删除操作
        java.util.List<Integer> players = new java.util.ArrayList<>();
        for (int i = 1; i <= 100; i++)
            players.add(i);

        // ==================== 3. 模拟报数删除过程 ====================
        // position: 当前报数的起始位置索引（从0开始）
        // 初始从第1个人（索引0）开始报数
        int position = 0;

        // 循环条件：当剩余人数 >= step 时继续游戏
        // 当剩余人数 < step 时停止，因为不够报一轮了
        while (players.size() >= step) {
            // 核心算法：计算要删除的人的位置
            // 从当前位置开始，报数到step的人需要前进 (step-1) 步
            // 例如：step=3，当前位置在索引0，数1（索引0），数2（索引1），数3（索引2）
            // 所以需要前进 2 步，即 (0+3-1) % 当前人数
            // 取模运算 % 实现环形效果：当超出列表末尾时，从开头继续
            position = (position + step - 1) % players.size();

            // 删除该位置的人（出列）
            // 注意：删除后，被删元素的后一个元素会移动到position位置
            // 正好作为下一轮报数的起点，无需调整position
            players.remove(position);
        }

        // ==================== 4. 构建输出字符串 ====================
        // 将剩余的玩家编号用逗号连接成字符串
        StringBuilder out = new StringBuilder();
        for (int value : players) {
            if (out.length() > 0)  // 不是第一个元素则添加逗号
                out.append(',');
            out.append(value);
        }

        // 返回最终结果
        return out.toString();
    }

    public static void main(String[] args) {


        System.out.println(remainingCirclePlayers(2));
    }

}
