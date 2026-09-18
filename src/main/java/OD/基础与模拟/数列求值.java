package OD.基础与模拟;

import java.util.Arrays;
import java.util.Scanner;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-17 21:00
 */
public class 数列求值 {

    public static class Main {
        public static void main(String[] args) throws Exception {
            String input = new String(System.in.readAllBytes());
        }


        int find(int[] arr, int index) {
            if (index < 8) {
                return index;
            }

            while (index != 0 ){
                int max1 = 6;
                int max2 = 7;
                int min1 = 1;
                int min2 = 2;

                int res = max1 + max2 - max1 - min2;
                max1 = max2;
                max2 = res;
                min1 = min2;
                min2 = index - 7 + min1;
            }


            //8 6+7 - (1+2) = 13 - 3 = 10
            //9 7+10 - (2+3) = 17 - 5 = 12
            //10 10+12 - (3+4) = 22- 7 = 15
            //11 12+15 - (4+5) = 27 - 9 = 18
            //12 15+17 - (5+6) = 32- 11 = 21
            //13 17+21 - (6+7) = 38 - 13 = 25
            //14 21+25 - (7+10) = 46 - 17  =29
            //15 25+29 - (10+12) = 32
            //16 29+32 - (12+15) = 34
            return 0;
        }
    }

    public static class Main1 {
        public static void main(String[] args) throws Exception {
            Scanner scanner = new Scanner(System.in);

            // ---------- 读入 n ----------
            int n = scanner.nextInt();

            // ---------- 基础情况：n <= 7 ----------
            // 直接输出 n（前 7 项就是 1..7）
            if (n <= 7) {
                System.out.print(n);
                return;
            }

            // ---------- 滑动窗口初始化 ----------
            // window 保存最近 7 项，初始为 1..7
            long[] window = {1, 2, 3, 4, 5, 6, 7};

            // value 保存当前递推出来的最新值
            long value = 0;

            // ---------- 从第 8 项开始递推 ----------
            for (int position = 8; position <= n; position++) {

                // 1. 复制窗口并排序
                long[] ordered = window.clone();
                Arrays.sort(ordered);

                // 2. 计算新值：
                //    最大两个之和 - 最小两个之和
                //    ordered[6] 最大，ordered[5] 次大
                //    ordered[0] 最小，ordered[1] 次小
                value = ordered[6] + ordered[5] - ordered[0] - ordered[1];

                // 3. 窗口右移一位：丢掉最旧的（window[0]）
                //    System.arraycopy(src, srcPos, dest, destPos, length)
                //    把 window[1..6] 复制到 window[0..5]
                System.arraycopy(window, 1, window, 0, 6);

                // 4. 新值放到窗口末尾
                window[6] = value;
            }

            // ---------- 输出第 n 项 ----------
            System.out.print(value);
        }
    }

}
