package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 19:57
 */
public class 异常打卡筛选 {

    /**
     * 从打卡记录中找出异常记录。
     * 异常条件满足以下任意一条即视为异常：
     * 1. 同一条记录中，打卡时间（第4列）和签退时间（第5列）不一致（即 parts[index][3] != parts[index][4]）。
     * 2. 同一员工（第0列）的任意两条记录之间，记录序号（第1列）相差小于60，但记录数值（第2列）相差大于5。
     *
     * 输入格式说明（每条记录为逗号分隔的字符串，列索引从0开始）：
     *   - 第0列：员工ID
     *   - 第1列：记录序号（整数，用于判断时间相近）
     *   - 第2列：记录数值（整数，用于判断数值差异）
     *   - 第3列：打卡时间（字符串）
     *   - 第4列：签退时间（字符串）
     *
     * @param records 打卡记录数组，每条记录为逗号分隔的字符串，不含表头
     * @return 所有异常记录的拼接字符串，以分号分隔；若无异常则返回 "null"
     */
    String findAbnormalClockRecords(String[] records) {
        // 分割每条记录为字符串数组，并初始化异常标记数组
        String[][] parts = new String[records.length][];
        boolean[] abnormal = new boolean[records.length];

        // 第一轮：检查同一条记录内打卡与签退时间是否一致
        for (int index = 0; index < records.length; index++) {
            parts[index] = records[index].split(",", -1); // 使用 -1 保留空字段
            if (!parts[index][3].equals(parts[index][4]))
                abnormal[index] = true;
        }

        // 第二轮：检查同一员工的不同记录之间是否存在“序号接近但数值差异大”的异常
        for (int left = 0; left < records.length; left++) {
            for (int right = left + 1; right < records.length; right++) {
                // 同一员工
                if (parts[left][0].equals(parts[right][0])
                        // 记录序号（第1列）相差小于60
                        && Math.abs(Integer.parseInt(parts[left][1]) - Integer.parseInt(parts[right][1])) < 60
                        // 记录数值（第2列）相差大于5
                        && Math.abs(Integer.parseInt(parts[left][2]) - Integer.parseInt(parts[right][2])) > 5) {
                    abnormal[left] = true;
                    abnormal[right] = true;
                }
            }
        }

        // 收集所有异常记录，用分号拼接
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < records.length; index++) {
            if (abnormal[index]) {
                if (result.length() > 0)
                    result.append(';');
                result.append(records[index]);
            }
        }

        // 若无异常则返回 "null"，否则返回拼接结果
        return result.length() == 0 ? "null" : result.toString();
    }

}
