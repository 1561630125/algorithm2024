package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 22:57
 */
public class 嵌套记录编解码 {


    static class Solution {

        /**
         * 内存中的数据结构：一个节点
         * - position: 位置标识（原样保留的字符串）
         * - type: 类型编码。 "0"=Integer, "1"=String, "2"=Compose（组合节点）
         * - count: 该节点对应的"数据长度"或"子节点编码后总长度"
         * - data: 叶子节点的原始数据（Integer/String）
         * - children: Compose 节点的子节点列表
         */
        static class Item {
            String position, type, data;
            int count;
            java.util.List<Item> children = new java.util.ArrayList<>();

            Item(String p, String t, int c, String d) {
                position = p;   // 位置标识
                type = t;       // 类型
                count = c;      // 长度/计数
                data = d;       // 数据体
            }
        }

        /**
         * 编码过程中是否发生错误（括号不匹配等）
         */
        boolean batch63Failed;

        /**
         * 计算一个节点"头部"的逻辑长度
         * 头部 = position + type + count 的字符串长度 + 3 个分隔符 '#'
         * 注意：这里用的是 type 的字符串长度，而非定长 1
         */
        int batch63Header(Item item) {
            return item.position.length() + item.type.length()
                    + Integer.toString(item.count).length() + 3;
        }

        /**
         * 解析入口：把 "[pos,Type,data]" 形式的字符串解析成 Item 列表
         * <p>
         * 支持的语法：
         * [position,Integer,数字字符串]
         * [position,String,任意字符]
         * [position,Compose,[子项1][子项2]...]   —— Compose 的 data 是一串方括号块
         * <p>
         * 顶层输入必须整体被一对方括号包裹。
         *
         * @return 解析出的 Item 列表；失败时 batch63Failed = true
         */
        java.util.List<Item> batch63Encode(String value) {
            java.util.List<Item> items = new java.util.ArrayList<>();
            if (value.isEmpty())
                return items;

            // 顶层必须以 [ 开头、以 ] 结尾，否则视为格式错误
            if (value.charAt(0) != '[' || value.charAt(value.length() - 1) != ']') {
                batch63Failed = true;
                return items;
            }

            // ---- 1. 用 depth 扫描，按顶层方括号把 value 拆成若干块 ----
            java.util.List<String> blocks = new java.util.ArrayList<>();
            int depth = 0, start = 0;
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (c == '[') {
                    if (depth == 0)
                        start = i;      // 记录新块的起始位置
                    depth++;
                } else if (c == ']') {
                    depth--;
                    if (depth == 0)
                        blocks.add(value.substring(start, i + 1)); // 完整块入列
                    if (depth < 0)
                        batch63Failed = true;   // 出现多余的右括号
                }
            }
            if (depth != 0)
                batch63Failed = true;           // 括号不匹配

            // ---- 2. 逐个块解析 ----
            for (String block : blocks) {
                String body = block.substring(1, block.length() - 1);  // 去掉外层 [ ]

                // 按逗号切前两段：pos,kind
                int first = body.indexOf(','), second = body.indexOf(',', first + 1);
                if (first < 0 || second < 0)
                    continue;   // 格式不对，跳过（注意：未置 batch63Failed）

                String position = body.substring(0, first);
                String kind = body.substring(first + 1, second);
                String data = body.substring(second + 1);

                if (kind.equals("Integer") || kind.equals("String")) {
                    // 叶子节点：count = data 长度，type 用 "0"/"1" 表示
                    items.add(
                            new Item(position,
                                    kind.equals("Integer") ? "0" : "1",
                                    data.length(),
                                    data));
                } else if (kind.equals("Compose")) {
                    // Compose 节点：递归解析其内部的块串
                    java.util.List<Item> children = batch63Encode(data);

                    // count = 所有子节点 头部长度 + 各自 count 之和
                    int count = 0;
                    for (Item child : children)
                        count += child.count + batch63Header(child);

                    Item item = new Item(position, "2", count, "");
                    item.children = children;
                    items.add(item);
                }
                // 其他 kind 直接忽略
            }
            return items;
        }

        /**
         * 把一个 Item 列表序列化为传输字符串
         * 每个节点格式：position#type#count#payload
         * - payload: type=2 时递归编码 children；否则用 data
         * 相邻节点之间不加分隔符（用#已经可以逐字段切分）
         */
        String batch63Encoded(java.util.List<Item> items) {
            StringBuilder out = new StringBuilder();
            for (Item item : items)
                out.append(item.position)
                        .append('#')
                        .append(item.type)
                        .append('#')
                        .append(item.count)
                        .append('#')
                        .append(item.type.equals("2")
                                ? batch63Encoded(item.children)   // Compose 递归
                                : item.data);                     // 叶子直接拼数据
            return out.toString();
        }

        /**
         * 解码时使用的读取游标（当前在 value 中的字符位置）
         */
        int batch63Position;
        /**
         * 解码时累计的"逻辑长度"，用于控制 Compose 递归的解码边界
         */
        int batch63Logical;

        /**
         * 从编码串中解码出 Item 列表
         *
         * @param value  待解码字符串
         * @param target 目标逻辑长度； -1 表示读到字符串末尾
         *               （用于 Compose 递归时，只读它自己那部分）
         */
        java.util.List<Item> batch63Decode(String value, int target) {
            java.util.List<Item> items = new java.util.ArrayList<>();
            int logical = 0;   // 本层已消耗的逻辑长度

            // 循环条件：位置没到末尾，且（target<0 或者本层累计未达 target）
            while (batch63Position < value.length() && (target < 0 || logical < target)) {

                // ---- 1. 读取三个由 '#' 分隔的字段 ----
                String[] fields = new String[3];
                for (int i = 0; i < 3; i++) {
                    int end = value.indexOf('#', batch63Position);
                    if (end < 0)
                        throw new IllegalArgumentException();   // 缺字段
                    fields[i] = value.substring(batch63Position, end);
                    batch63Position = end + 1;
                }

                int count = Integer.parseInt(fields[2]);
                Item item = new Item(fields[0], fields[1], count, "");

                // ---- 2. 根据类型读取 payload ----
                if (fields[1].equals("0") || fields[1].equals("1")) {
                    // 叶子节点：后面紧跟 count 个字符
                    if (count < 0 || batch63Position + count > value.length())
                        throw new IllegalArgumentException();
                    item.data = value.substring(batch63Position, batch63Position + count);
                    batch63Position += count;

                } else if (fields[1].equals("2")) {
                    // Compose：递归解码，且本层累计逻辑必须与 count 一致
                    int before = batch63Logical;
                    item.children = batch63Decode(value, count);
                    if (batch63Logical - before != count)
                        throw new IllegalArgumentException();

                } else {
                    // 未知类型：跳过 count 个字符，并累加逻辑长度后直接 continue
                    if (count < 0 || batch63Position + count > value.length())
                        throw new IllegalArgumentException();
                    batch63Position += count;
                    logical += batch63Header(item) + count;
                    continue;
                }

                items.add(item);
                // 每读完一个节点，逻辑长度 += 头部长度 + count
                logical += batch63Header(item) + count;
            }

            batch63Logical += logical;
            return items;
        }

        /**
         * 把解码出来的 Item 列表还原成原始文本形式
         * 输出格式：[position,Integer|String|Compose,data] 以逗号分隔
         */
        String batch63Decoded(java.util.List<Item> items) {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                if (i > 0)
                    out.append(',');
                Item item = items.get(i);
                out.append('[')
                        .append(item.position)
                        .append(',')
                        .append(item.type.equals("0") ? "Integer"
                                : item.type.equals("1") ? "String"
                                : "Compose")
                        .append(',')
                        .append(item.type.equals("2")
                                ? batch63Decoded(item.children)   // Compose 递归展开
                                : item.data)
                        .append(']');
            }
            return out.toString();
        }

        /**
         * 对外统一入口
         *
         * @param command 1 = 编码（把原始文本转成紧凑传输串）
         *                2 = 解码（把传输串还原成原始文本）
         * @param payload 输入数据
         */
        String serializeTransmission(int command, String payload) {
            if (command == 1) {
                batch63Failed = false;
                java.util.List<Item> items = batch63Encode(payload);
                return batch63Failed ? "ENCODE_ERROR" : batch63Encoded(items);
            }
            if (command == 2)
                try {
                    batch63Position = 0;
                    batch63Logical = 0;
                    java.util.List<Item> items = batch63Decode(payload, -1);
                    // 必须完全消费完，否则视为解码错误
                    if (batch63Position != payload.length())
                        throw new IllegalArgumentException();
                    return batch63Decoded(items);
                } catch (Exception error) {
                    return "DECODE_ERROR";
                }
            return "";
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int command = 1;
        String payload = "[1,String,I am Mary],[2,Integer,23],[3,Long,1000000],[4,Compose,[1,String,I am Kitty],[2,Integer,44]]";

        int command2 = 2;
        String payload2 = "1#1#9#I am Mary2#0#2#233#0#3#8794#2#25#1#1#10#I am Kitty2#0#2#44";

        System.out.println(solution.serializeTransmission(command, payload));
        System.out.println(solution.serializeTransmission(command2, payload2));
    }

}
