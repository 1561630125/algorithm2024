package OD.数据结构与区间;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 16:02
 */
public class 下一个高级优先包 {

    static class Data {
        int index;
        int pri;
        int wei;

        public Data(int index, int pri, int wei) {
            this.index = index;
            this.pri = pri;
            this.wei = wei;
        }
    }

    /**
     * 下一个高优先级包
     *
     * @param dataPacket int[][] — 每行依次为数据值和权重
     * @return int[]
     */
    static public int[] findPacket(int[][] dataPacket) {
        // write code here

        LinkedList<Data> data = new LinkedList<>();
        for (int i = 0; i < dataPacket.length; i++) {
            int pri = dataPacket[i][0];
            int wei = dataPacket[i][1];
            data.add(new Data(i + 1, pri, wei));
        }

        int[] res = new int[data.size()];
        for(int i = 0; i < data.size()-1; i++) {
            int j = i+1;
            Data cur = data.get(i);
            Data next = data.get(j);
            while (j <data.size() && !(cur.wei == next.wei && cur.pri < next.pri)) {
                j++;
            }

            if (j == data.size()) {
                res[i] = 0;
            }else {
                res[i] = next.index;
            }
        }

        return res;
    }

    static public int[] findPacket3(int[][] dataPacket) {
        LinkedList<Data> data = new LinkedList<>();
        for (int i = 0; i < dataPacket.length; i++) {
            int pri = dataPacket[i][0];
            int wei = dataPacket[i][1];
            data.add(new Data(i + 1, pri, wei));
        }

        int[] res = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Data cur = data.get(i);
            int j = i + 1;
            // 每次循环动态取 j 位置的元素，而不是固定 next
            while (j < data.size()) {
                Data next = data.get(j);
                if (cur.wei == next.wei && cur.pri < next.pri) break;
                j++;
            }
            // j 越界说明没找到，填 0；否则填该包的编号
            res[i] = (j == data.size()) ? 0 : data.get(j).index;
        }

        return res;
    }




    static public int[] findPacket2(int[][] dataPacket) {
        // groups: key = 权重 wei，value = 该权重下所有包
        // 每个包用 int[]{原始下标, 数据值 pri} 表示
        java.util.Map<Integer, java.util.List<int[]>> groups = new java.util.HashMap<>();

        // 1. 按权重 wei 分组
        for (int index = 0; index < dataPacket.length; index++) {
            groups.computeIfAbsent(
                    dataPacket[index][1],                     // key = wei
                    ignored -> new java.util.ArrayList<>()    // 不存在就新建列表
            ).add(new int[] {
                    index,                 // 原始下标（后面用来填答案）
                    dataPacket[index][0]   // 数据值 pri
            });
        }

        // 结果数组，默认全是 0（表示没找到）
        int[] answer = new int[dataPacket.length];

        // 2. 对每个权重分组，单独用单调栈求「下一个更大的 pri」
        /*for (java.util.List<int[]> packets : groups.values()) {

            // 栈里存的是「还没找到下一个更大 pri」的包
            // 从栈底到栈顶，pri 严格递减
            java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();

            for (int[] packet : packets) {

                // 当前包的 pri 比栈顶大 → 栈顶那些包找到了答案
                // 因为当前包是它们后面第一个 pri 更大的包
                while (!stack.isEmpty() && stack.peekLast()[1] < packet[1]) {
                    int[] popped = stack.removeLast();     // 弹出栈顶
                    answer[popped[0]] = packet[0] + 1;     // 记录答案：当前包编号（下标+1）
                }

                // 当前包入栈，等待后面更大的包
                stack.addLast(packet);
            }

            // 循环结束后栈里剩下的包都没找到更大的 pri
            // answer 对应位置保持默认值 0
        }*/

        for (java.util.List<int[]> packets : groups.values()) {
            java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
            for (int[] packet : packets) {
                while (!stack.isEmpty() && stack.peek()[1] < packet[1]) {
                    answer[stack.pop()[0]] = packet[0] + 1;
                }
                stack.push(packet);
            }

            System.out.println("---");
        }

        return answer;
    }


    public static void main(String[] args) {
        int[][] dataPacket = new int[][]{{9,1},{8,1},{6,1}};
        int[][] dataPacket2 = new int[][]{{6,2},{5,1},{5,1},{4,1},{7,1},{7,2},{9,1}};

        System.out.println(Arrays.toString(findPacket3(dataPacket2)));
//        System.out.println(Arrays.toString(findPacket(dataPacket2)));
        System.out.println(Arrays.toString(findPacket2(dataPacket2)));
    }

}
