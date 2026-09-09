package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 21:58
 */
public class 考勤达标 {

    /**
     * 判断每位员工是否获得出勤奖励
     *
     * 奖励规则（考勤系统经典问题）：
     * 1. 缺勤(absent)次数不能超过1次
     * 2. 不能有连续迟到(late)或早退(leaveearly)
     * 3. 任意连续7天内，非出席(present)次数不能超过3次
     *
     * 判断逻辑：所有条件必须同时满足才返回true
     *
     * @param records 二维数组，每个元素是某员工的出勤记录
     *                每条记录是字符串数组，包含：present/absent/late/leaveearly
     * @return boolean数组，每个元素对应一个员工的奖励判定结果
     */
    public boolean[] attendanceAwards2(String[][] records) {
        // 存储每个员工的判定结果
        boolean[] result = new boolean[records.length];

        // 遍历每个员工
        for (int row = 0; row < records.length; row++) {
            int absent = 0;      // 缺勤总次数（条件1）
            int window = 0;      // 当前7天窗口内的非present次数（条件3）
            boolean valid = true; // 当前员工是否满足所有条件

            // 遍历该员工每天的记录
            for (int index = 0; index < records[row].length; index++) {
                String status = records[row][index];

                /**
                 * 条件1：缺勤(absent)次数不能超过1次
                 */
                if (status.equals("absent")) {
                    absent++;
                }
                if (absent > 1) {
                    valid = false;  // 缺勤2次及以上，奖励取消
                }

                /**
                 * 条件2：不能有连续迟到(late)或早退(leaveearly)
                 *
                 * 检查当前状态是否迟到/早退，并且前一天也是迟到/早退
                 */
                if (index > 0 &&
                        (status.equals("late") || status.equals("leaveearly")) &&
                        (records[row][index - 1].equals("late") ||
                                records[row][index - 1].equals("leaveearly"))) {
                    valid = false;  // 连续两天迟到/早退，奖励取消
                }

                /**
                 * 条件3：任意连续7天内，非present次数不能超过3次
                 *
                 * 使用滑动窗口技术（固定大小7天）
                 */

                // 3.1 当前天如果不是present，窗口计数+1
                if (!status.equals("present")) {
                    window++;
                }

                // 3.2 当窗口超过7天，移除7天前的记录
                // index >= 7 表示窗口已满，需要滑动
                if (index >= 7 && !records[row][index - 7].equals("present")) {
                    window--;  // 7天前的记录离开窗口
                }

                // 3.3 检查当前7天窗口（从index-6到index）是否超过3次
                // index >= 6 表示已经有7天的数据了
                if (index >= 6 && window > 3) {
                    valid = false;  // 连续7天内非present超过3次
                }
            }

            result[row] = valid;
        }

        return result;
    }


    boolean[] attendanceAwards(String[][] records) {
        return new boolean[0];
    }

}
