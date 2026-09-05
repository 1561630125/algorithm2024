package OD.基础与模拟;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 22:09
 */
public class 多屏穿插 {
    static int[] recommendDiverse(int windows, int size, int[][] lists) {

        int count = windows * size;

        int[] res = new int[count];
        LinkedList<Object> data = new LinkedList<>();
        for(int i = 0; i < lists.length; i++) {
            res[0] = lists[i][0];

        }




        return new int[0];
    }


    static int[] recommendDiverse2(int windows, int size, int[][] lists) {
        // ============ 1. 参数校验 ============
        if (windows <= 0 || size <= 0 || lists == null || lists.length == 0)
            return new int[0];  // 无效参数返回空数组

        // 计算总需求数量，防止溢出
        long requested = (long) windows * size;
        if (requested > Integer.MAX_VALUE)
            return new int[0];  // 超出int范围无法返回

        int total = (int) requested;  // 总需要选取的元素个数

        // ============ 2. 轮流从各列表中取元素 ============
        int[] positions = new int[lists.length];  // 记录每个列表当前取到的位置
        int[] selected = new int[total];          // 存储按轮次取出的元素
        int count = 0;        // 已选取总数
        int listIndex = 0;    // 当前轮到哪个列表
        int stalled = 0;      // 连续"卡住"（取不到元素）的次数

        // 主循环：直到取够总数 或 所有列表都空了
        while (count < total && stalled < lists.length) {
            int taken = 0;  // 当前轮从这个列表取了多少个

            // 防御性编程：如果列表为null，当作空数组
            int[] list = lists[listIndex] == null ? new int[0] : lists[listIndex];

            // 从当前列表连续取 windows 个元素（或直到该列表取完）
            while (taken < windows && positions[listIndex] < list.length && count < total) {
                selected[count++] = list[positions[listIndex]++];
                taken++;
            }

            // 更新卡住计数：如果本轮一个都没取到，stalled+1；否则重置为0
            stalled = taken == 0 ? stalled + 1 : 0;

            // 循环到下一个列表（轮询）
            listIndex = (listIndex + 1) % lists.length;
        }

        // ============ 3. 检查是否取够了 ============
        if (count != total)
            return new int[0];  // 元素不够，无法完成推荐

        // ============ 4. 重排结果 ============
        // selected 中存储的是按"列表轮询"方式取出的：
        // [list0_1, list1_1, list2_1, ..., list0_2, list1_2, ...]
        // 需要重排为按"窗口"分组：
        // [窗口0的所有元素, 窗口1的所有元素, ...]

        int[] result = new int[total];
        int output = 0;

        // 外层遍历窗口，内层遍历每个窗口内的位置
        for (int window = 0; window < windows; window++)
            for (int column = 0; column < size; column++)
                result[output++] = selected[column * windows + window];

        return result;
    }

    public static void main(String[] args) {
        int windows = 3;
        int size = 3;
        int[][] lists = new int[][]{{1,4,7,10},{20,21,22,23},{30,31,32,33}};

        System.out.println(Arrays.toString(recommendDiverse2(windows, size, lists)));


    }

}
