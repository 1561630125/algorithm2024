package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 17:33
 */
public class 评论分层 {

    // 分词后的字符串数组
    private String[] t;

    // 当前解析到的位置（下标）
    private int p;

    // 按层级存放结果：l.get(depth) 是第 depth 层的所有名字
    private java.util.ArrayList<java.util.ArrayList<String>> l;

    /**
     * 递归解析一个节点
     *
     * @param d 当前节点的层级（深度），根节点是 0
     *
     * 输入格式：每个节点是「名字,子节点个数」
     * 例如 "A,2" 表示节点 A 有 2 个子节点，后面紧跟这 2 个子节点的定义
     */
    private void parse(int d) {

        // 1. 确保 l 里有第 d 层的列表（不够就补）
        while (l.size() <= d)
            l.add(new java.util.ArrayList<>());

        // 2. 读取当前节点的名字
        String s = t[p++];

        // 3. 读取当前节点的子节点个数
        int n = Integer.parseInt(t[p++]);

        // 4. 把名字加入对应层级
        l.get(d).add(s);

        // 5. 递归解析 n 个子节点，层级 +1
        for (int i = 0; i < n; i++)
            parse(d + 1);
    }

    /**
     * 把输入字符串解析成按层级分组的二维数组
     *
     * @param x 输入字符串，形如 "A,2,B,1,C,0,D,0,E,0"
     * @return o[d] 是第 d 层的所有节点名
     */
    String[][] commentLevels(String x) {

        // 1. 按逗号切分，-1 表示保留末尾空串
        t = x.split(",", -1);

        // 2. 重置解析位置
        p = 0;

        // 3. 初始化层级列表
        l = new java.util.ArrayList<>();

        // 4. 依次解析每个顶层节点（可能有多个根）
        while (p < t.length)
            parse(0);

        // 5. 把 List<List<String>> 转成 String[][]
        String[][] o = new String[l.size()][];
        for (int i = 0; i < o.length; i++)
            o[i] = l.get(i).toArray(new String[0]);

        return o;
    }

}
