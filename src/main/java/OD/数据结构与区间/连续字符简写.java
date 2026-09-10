package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 15:45
 */
public class 连续字符简写 {
    static public String compressStr(String value) {
        // write code here
        if (value.length() <= 1) {
            return value;
        }

        char cur = value.charAt(0);
        int count = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < value.length(); ) {
            char charAt = value.charAt(i);
            while (charAt == cur) {
                i++;
                if (i == value.length()) break;
                count++;
                charAt = value.charAt(i);
            }
            sb.append(cur);
            cur = charAt;
            if (count > 1) {
                sb.append(count);
                count = 0;
            }
        }

        return sb.toString();
    }

    public String compressStr2(String value) {
        // 用 StringBuilder 拼接压缩后的结果
        StringBuilder answer = new StringBuilder();

        // index 指向当前这一组相同字符的起始位置
        for (int index = 0; index < value.length(); ) {

            // end 从 index 的下一位开始，向后找第一个与 value.charAt(index) 不同的字符
            int end = index + 1;
            while (
                    end < value.length() && value.charAt(end) == value.charAt(index)
            )
                end++;

            // 此时 [index, end) 区间内都是同一个字符
            // 先把该字符追加到结果里
            answer.append(value.charAt(index));

            // 这一组的长度是 end - index
            // 如果长度不为 1（即出现次数 > 1），才追加次数
            // 长度为 1 时省略数字，比如 "a" 压缩后仍是 "a"，不写成 "a1"
            if (end - index != 1)
                answer.append(end - index);

            // 跳过这一整组，从下一个不同字符继续处理
            index = end;
        }

        return answer.toString();
    }


    public static void main(String[] args) {
        System.out.println(compressStr("aaAAaab"));
        System.out.println(compressStr("a"));
    }

}
