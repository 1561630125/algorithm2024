package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 14:52
 */
public class 删位取小 {

    String minimumAfterRemovingDigits(String number, int removeCount) {


        int diff = number.length() - removeCount;
        int begin = 0;
        int end = diff;
        StringBuilder str = new StringBuilder();
        while (str.toString().length() < diff) {

        }
        for(int i = begin; i < end; i++) {
            if (number.charAt(i) - '0' < number.charAt(i-1)) {
                str.append(number.charAt(i));
            }
        }

        return "0";
    }


    String minimumAfterRemovingDigits2(String number, int count) {
        // 用 StringBuilder 当单调栈：栈里始终保持“尽量小”的数字序列
        StringBuilder stack = new StringBuilder();

        // 遍历原数字的每一个字符
        for (char value : number.toCharArray()) {

            // 单调栈核心：如果栈顶数字比当前数字大，且还有删除次数，
            // 就把栈顶弹掉（删掉这个较大的数字），让更小的数字上位
            while (stack.length() > 0 && count > 0
                    && stack.charAt(stack.length() - 1) > value) {
                stack.deleteCharAt(stack.length() - 1); // 弹出栈顶（删除该位）
                count--;                                // 消耗一次删除机会
            }

            // 当前数字入栈
            stack.append(value);
        }

        // 如果删完了还没用完 count（说明后面数字是递增的），
        // 就从末尾继续删，因为末尾的数字最大
        while (count > 0 && stack.length() > 0) {
            stack.deleteCharAt(stack.length() - 1);
            count--;
        }

        // 去掉前导零
        int start = 0;
        while (start < stack.length() && stack.charAt(start) == '0')
            start++;

        // 如果全是零（或删空了），返回 "0"
        return start == stack.length() ? "0" : stack.substring(start);
    }

}
