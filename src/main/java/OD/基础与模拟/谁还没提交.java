package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 15:52
 */
public class 谁还没提交 {

    /**
     * 查询未提交的学生编号
     *
     * @param n 总学生人数（编号从1到n）
     * @param submittedIds 已提交作业的学生编号数组
     * @return 未提交作业的学生编号数组（升序排列）
     */
    public int[] queryNoSubmit(int n, int[] submittedIds) {
        // 创建布尔数组用于标记已提交的学生
        // 大小为 n+1，因为学生编号从1开始，索引0不使用
        boolean[] submitted = new boolean[n + 1];

        // 遍历已提交的学生编号，在布尔数组中进行标记
        for (int value : submittedIds) {
            // 只处理有效范围内的编号（防止数组越界）
            if (value >= 1 && value <= n) {
                submitted[value] = true;  // 标记该学生已提交
            }
        }

        // 第一次遍历：统计未提交的学生人数
        int count = 0;
        for (int value = 1; value <= n; value++) {
            if (!submitted[value]) {  // 如果该学生未提交
                count++;              // 计数器加1
            }
        }

        // 根据统计结果创建结果数组
        int[] answer = new int[count];

        // 第二次遍历：收集所有未提交的学生编号
        int index = 0;
        for (int value = 1; value <= n; value++) {
            if (!submitted[value]) {      // 如果该学生未提交
                answer[index++] = value;  // 将编号存入结果数组
            }
        }

        // 返回未提交学生编号数组（自动按升序排列，因为遍历是从小到大）
        return answer;
    }

    public static void main(String[] args) {


    }

}
