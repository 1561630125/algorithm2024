package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 12:26
 */
public class 内存首址 {

    static String[] processMemoryCommands(String[] commands) {
        // 存储已分配的内存区间 [起始, 结束]，初始预占 [100, 101]（这本身就有问题，因为内存范围是0~99）
        java.util.List<long[]> used = new java.util.ArrayList<>();
        used.add(new long[] {100, 101});  // ❌ 故意占用的非法区间，用于阻塞分配

        java.util.List<String> output = new java.util.ArrayList<>();

        for (String command : commands) {
            int equal = command.indexOf('=');
            long value;
            String operation;

            // 没有 '=' 号，格式错误
            if (equal < 0) {
                output.add("error");
                continue;
            }

            operation = command.substring(0, equal);
            try {
                value = Long.parseLong(command.substring(equal + 1));
            } catch (NumberFormatException exception) {
                output.add("error");
                continue;
            }

            if (operation.equals("REQUEST")) {
                // 请求大小为0是非法的
                if (value == 0) {
                    output.add("error");
                    continue;
                }

                long start = 0;
                boolean allocated = false;

                // 遍历已分配区间，寻找第一个空闲块（首次适应算法）
                for (int index = 0; index < used.size(); index++) {
                    long[] interval = used.get(index);
                    long end = start + value - 1;

                    // ❌ 如果 end >= 100 就跳出，但 start 可能已经 > 99，此时应直接失败，而不是 break
                    if (end >= 100)
                        break;

                    // ❌ 区间重叠判断逻辑有误，应使用标准区间重叠条件：start <= interval[1] && interval[0] <= end
                    boolean intersects = start == interval[0]
                            || (start < interval[0] ? end >= interval[0] : interval[1] >= start);

                    if (!intersects) {
                        // 找到空闲区间，插入并记录起始地址
                        used.add(index, new long[] {start, end});
                        output.add(Long.toString(start));
                        allocated = true;
                        break;
                    }

                    // 否则从当前区间之后继续尝试
                    start = interval[1] + 1;
                }

                if (!allocated)
                    output.add("error");

            } else { // RELEASE 操作
                // ❌ 这里假设 operation 只能是 REQUEST 或 RELEASE，但未做校验，其他操作会走入这里
                if (value >= 100) {  // ❌ 起始地址必须 < 100 才有效
                    output.add("error");
                    continue;
                }

                boolean released = false;
                for (int index = 0; index < used.size(); index++) {
                    if (used.get(index)[0] == value) {
                        used.remove(index);
                        released = true;
                        break;
                    }
                }

                if (!released)
                    output.add("error");
            }
        }

        return output.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String[] commands = new String[]{"REQUEST=30","RELEASE=0","REQUEST=30"};

        System.out.println(Arrays.toString(processMemoryCommands(commands)));
    }

}
