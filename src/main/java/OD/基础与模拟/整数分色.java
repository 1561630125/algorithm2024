package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 12:22
 */
public class 整数分色 {


    long minimumColorCount(long[] numbers) {
        // 1. 克隆数组并排序
        //    排序是为了让较小的数字先被处理，这样大数字可以被小数字"覆盖"
        //    比如 [6, 2, 3] 排序后为 [2, 3, 6]，6可以被2或3覆盖
        long[] ordered = numbers.clone();
        java.util.Arrays.sort(ordered);

        // 2. colors列表存储"颜色"（即作为基数的数字）
        //    每个颜色代表一种除数，列表中的数字都是"互不整除"的
        java.util.List<Long> colors = new java.util.ArrayList<>();

        // 3. 遍历排序后的每个数字
        for (long number : ordered) {
            boolean covered = false;

            // 3.1 检查当前数字能否被已有的某种颜色整除
            //    条件：color != 0（防止除以0）且 number % color == 0
            for (long color : colors)
                if (color != 0 && number % color == 0) {
                    covered = true;
                    break;  // 找到一个能整除的就不用继续找了
                }

            // 3.2 如果当前数字不能被任何已有颜色整除，则它自身成为新颜色
            if (!covered)
                colors.add(number);
        }

        // 4. 返回颜色的数量（即最少需要多少种基数）
        return colors.size();
    }

}
