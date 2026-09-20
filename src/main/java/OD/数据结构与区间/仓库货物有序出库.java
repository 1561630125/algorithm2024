package OD.数据结构与区间;

import java.util.Arrays;

/**
 * 考点：栈+贪心模拟
 *
 * @author faming.yang@hand-china.com 2026-09-20 14:18
 */
public class 仓库货物有序出库 {
    static public int minimumStorageMoves(int n, int[] goods) {
        // write code here

        /*ArrayDeque<Integer> stack = new ArrayDeque<>();
        ArrayDeque<Integer> backStack = new ArrayDeque<>();

        int res = 0;
        for (int i = 0; i < goods.length; i++) {
            // 7 4 6 3 5
            // 7 3 6 4 5
            while (!stack.isEmpty() && goods[stack.peek()] < goods[i]) {
                backStack.push(stack.pop());
                res++;
            }
            stack.push(i);
        }*/

        int res = 0;
        int[] arr = new int[n + 1];
        int index = 0;
        for (int i = goods.length - 1; i > 0; i--) {
            if (goods[i] > goods[i - 1]) {
                res++;
                arr[index++] = i - 1;
            }
        }

        for (int i = index - 1; i > 0; i--) {
            if (goods[arr[i]] > goods[arr[i - 1]]) {
                res++;
            }
        }


        return res;
    }

    public static void main(String[] args) {
        int[] goods = new int[]{6, 5, 2, 4, 3, 1};
        System.out.println(minimumStorageMoves(goods.length, goods));
    }



    /**
     * 借助一个栈式临时区，使货物按编号严格升序出库，求最少双向暂存次数。
     * 主库和临时区都只能操作栈顶，允许双向转移；
     * 不要修改 goods。
     */
    public class Solution {
        public int minimumStorageMoves(int n, int[] goods) {
            // main 表示主库，复制一份 goods，后续会模拟主库栈顶操作
            // 注意：没有直接修改 goods
            int[] main = Arrays.copyOf(goods, n);

            // temporary 表示临时区，也是一个栈
            int[] temporary = new int[n];

            // ordered 是 goods 排序后的结果，表示最终应该按这个顺序出库
            int[] ordered = goods.clone();
            Arrays.sort(ordered);

            // a 表示 main 中当前有效元素个数，也就是 main 栈顶位置为 a - 1
            int a = n;

            // b 表示 temporary 中当前有效元素个数，也就是 temporary 栈顶位置为 b - 1
            int b = 0;

            // moves 记录双向暂存次数，即元素在 main 和 temporary 之间移动的次数
            int moves = 0;

            // 按升序依次决定每个目标货物 target 如何出库
            for (int target : ordered) {
                // 先判断 target 当前是否在 main 中
                boolean inMain = false;
                for (int i = 0; i < a; i++) {
                    if (main[i] == target) {
                        inMain = true;
                        break; // 这里可以提前结束，原代码没写 break，但逻辑等价
                    }
                }

                if (inMain) {
                    // 如果 target 在 main 中：
                    // 需要把 main 栈顶到 target 之间的所有货物先移到 temporary，
                    // 直到 target 暴露在 main 栈顶。
                    while (main[a - 1] != target) {
                        temporary[b++] = main[--a];
                        moves++;
                    }

                    // target 已经在 main 栈顶，直接出库，main 有效元素减一
                    a--;
                } else {
                    // 如果 target 不在 main 中，那么它一定在 temporary 中：
                    // 需要把 temporary 栈顶到 target 之间的所有货物先移回 main，
                    // 直到 target 暴露在 temporary 栈顶。
                    while (temporary[b - 1] != target) {
                        main[a++] = temporary[--b];
                        moves++;
                    }

                    // target 已经在 temporary 栈顶，直接出库，temporary 有效元素减一
                    b--;
                }
            }

            // 返回总移动次数
            return moves;
        }
    }
}
