package OD.基础与模拟;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 16:52
 */
public class 订阅消息分发 {

    static class Mes {
        Integer time;
        Integer content;

        public Mes(Integer time, Integer content) {
            this.time = time;
            this.content = content;
        }
    }

    static class Sub {
        Integer index;
        Integer begin;
        Integer end;
        List<Integer> content = new LinkedList<>();

        public Sub(Integer index, Integer begin, Integer end) {
            this.index = index;
            this.begin = begin;
            this.end = end;
        }
    }

    static int[][] simulateMessageQueue(int[][] messages, int[][] subscriptions) {
        int message = messages.length;
        int subscr = subscriptions.length;

        LinkedList<Mes> mes = new LinkedList<>();
        for (int i = 0; i < message; i++) {
            mes.add(new Mes(messages[i][0], messages[i][1]));
        }
        LinkedList<Sub> sub = new LinkedList<>();
        for (int i = 0; i < subscr; i++) {
            sub.add(new Sub(i, subscriptions[i][0], subscriptions[i][1]));
        }

        mes.sort(Comparator.comparingInt(a -> a.time));

        for (int i = 0; i < mes.size(); i++) {
            Mes mes1 = mes.get(i);
            for (int j = sub.size() - 1; j >= 0; j--) {
                Sub sub1 = sub.get(j);
                if (mes1.time >= sub1.begin && mes1.time < sub1.end) {
                    sub1.content.add(mes1.content);
                    break;
                }
            }
        }

        int[][] res = new int[sub.size()][];
        for (int i = 0; i < sub.size(); i++) {
            res[i] = sub.get(i).content.stream().mapToInt(Integer::intValue).toArray();
        }

        return res;
    }


    /**
     * 模拟消息队列的分发过程
     *
     * @param messages      消息数组，每个元素为 int[2]，格式：[时间戳, 消息内容ID]
     * @param subscriptions 订阅数组，每个元素为 int[2]，格式：[起始时间, 结束时间)（左闭右开区间）
     * @return 每个订阅者收到的消息内容ID数组（按消息时间升序排列）
     */
    int[][] simulateMessageQueue2(int[][] messages, int[][] subscriptions) {
        // 1. 空指针保护
        if (messages == null || subscriptions == null)
            return new int[0][];

        // 2. 输入格式校验：每条消息必须是非空且长度为2
        for (int[] message : messages)
            if (message == null || message.length != 2)
                return new int[subscriptions.length][0]; // 返回空结果（每个订阅者空列表）

        // 3. 输入格式校验：每个订阅区间必须是非空且长度为2
        for (int[] subscription : subscriptions)
            if (subscription == null || subscription.length != 2)
                return new int[subscriptions.length][0];

        // 4. 复制消息数组，避免修改原始数据
        int[][] ordered = new int[messages.length][2];
        for (int index = 0; index < messages.length; index++)
            ordered[index] = java.util.Arrays.copyOf(messages[index], 2);

        // 5. 按消息时间戳升序排序（关键：保证顺序处理）
        java.util.Arrays.sort(ordered, java.util.Comparator.comparingInt(message -> message[0]));

        // 6. 为每个订阅者创建接收列表（用于暂存消息内容ID）
        java.util.List<Integer>[] received = new java.util.List[subscriptions.length];
        for (int index = 0; index < received.length; index++)
            received[index] = new java.util.ArrayList<>();

        // 7. 遍历排序后的消息，为每条消息寻找第一个匹配的订阅者
        //    （注意：从后往前遍历订阅者，意味着数组靠后的订阅者优先级更高）
        for (int[] message : ordered) {
            for (int index = subscriptions.length - 1; index >= 0; index--) {
                // 检查消息时间是否落在订阅区间 [start, end) 内
                if (subscriptions[index][0] <= message[0] && message[0] < subscriptions[index][1]) {
                    received[index].add(message[1]); // 将消息内容ID加入该订阅者列表
                    break; // 匹配到第一个（其实是最后一个遍历到的）即停止，每条消息只投递一次
                }
            }
        }

        // 8. 将 List<Integer> 转换为 int[][] 结果
        int[][] result = new int[received.length][];
        for (int row = 0; row < result.length; row++) {
            result[row] = new int[received[row].size()];
            for (int column = 0; column < result[row].length; column++)
                result[row][column] = received[row].get(column);
        }
        return result;
    }


    public static void main(String[] args) {
        int[][] messages = new int[][]{{3, 23}, {2, 12}, {5, 45}, {6, 56}, {4, 34}};
        int[][] subscriptions = new int[][]{{2, 8}, {3, 4}};

        int[][] ints = simulateMessageQueue(messages, subscriptions);
        System.out.println(Arrays.deepToString(ints));
    }
}
