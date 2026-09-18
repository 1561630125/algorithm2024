package OD.基础与模拟;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 15:10
 */
public class 绕吗间找词 {

    static int diff(String str) {
        /*int[] charnum = new int[26];
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i)) && Character.isLowerCase(str.charAt(i))) {
                charnum[str.charAt(i) - 'a']++;
            }
        }
        int sum = 0;
        for (int i = 0; i < charnum.length; i++) {
            if (charnum[i] > 0) sum++;
        }
        return sum;
        */

        java.util.Set<Character> characters = new java.util.HashSet<>();
        for (int index = 0; index < str.length(); index++)
            characters.add(str.charAt(index));
        return characters.size();


    }

    static String bestValidSubstring(String scrambled, String reference) {
        int diff = diff(reference);

        LinkedList<String> valid = new LinkedList<>();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < scrambled.length(); i++) {
            char charAt = scrambled.charAt(i);
            /*if (!(Character.isLetter(charAt) || Character.isDigit(charAt))) {
                continue;
            }*/

            if (('a' <= charAt && 'f' >= charAt) || ('0' <= charAt && '9' >= charAt)) {
                if (!"".equals(temp.toString())) {
                    valid.add(temp.toString());
                    temp = new StringBuilder();
                }
                continue;
            }
            temp.append(charAt);
            if (i == scrambled.length() - 1 && !temp.toString().isEmpty()) {
                valid.add(temp.toString());
            }
        }

        List<String> collect = valid.stream().filter(item -> diff(item) <= diff)
                .sorted((a ,b) -> {
                    if (diff(a) != diff(b)) return diff(b) - diff(a);
                    return b.compareTo(a);
                }).collect(Collectors.toList());

        return collect.get(0).isEmpty() ? "Not Found" : collect.get(0);
    }


    static String bestValidSubstring2(String scrambled, String reference) {
        int limit = distinct(reference), bestDistinct = -1;
        String best = "", current = "";
        for (int index = 0; index <= scrambled.length(); index++) {
            char character = index < scrambled.length() ? scrambled.charAt(index) : '0';
            boolean delimiter = index == scrambled.length() || character >= '0' && character <= '9'
                    || character >= 'a' && character <= 'f';
            if (!delimiter)
                current += character;
            else if (!current.isEmpty()) {
                int count = distinct(current);
                if (count <= limit
                        && (count > bestDistinct
                        || count == bestDistinct && current.compareTo(best) > 0)) {
                    best = current;
                    bestDistinct = count;
                }
                current = "";
            }
        }
        return bestDistinct < 0 ? "Not Found" : best;
    }

    String bestValidSubstring3(String scrambled, String reference) {
        // limit：reference 中不同字符的个数，作为合法片段的"不同字符数上限"
        int limit = distinct(reference);

        // bestDistinct：当前最优片段的不同字符个数，初始 -1 表示还没有
        int bestDistinct = -1;

        // best：当前最优片段
        String best = "";

        // current：正在累积的当前片段
        String current = "";

        // ---------- 遍历 scrambled，多遍历一位用于收尾 ----------
        // index == scrambled.length() 时用 '0' 作为虚拟分隔符，强制收尾
        for (int index = 0; index <= scrambled.length(); index++) {
            char character = index < scrambled.length() ? scrambled.charAt(index) : '0';

            // 判断当前字符是不是分隔符：
            //   - 已到末尾（虚拟分隔符）
            //   - 数字 '0'~'9'
            //   - 字母 'a'~'f'
            boolean delimiter = index == scrambled.length() ||
                    character >= '0' && character <= '9' ||
                    character >= 'a' && character <= 'f';

            if (!delimiter) {
                // 不是分隔符，累积到当前片段
                current += character;
            } else if (!current.isEmpty()) {
                // 遇到分隔符且当前片段非空，处理这个片段

                // 计算当前片段的不同字符个数
                int count = distinct(current);

                // 判断是否更优：
                //   1. 不同字符个数 <= limit（合法）
                //   2. 满足以下之一：
                //      a. 不同字符个数更多
                //      b. 个数相同但字典序更大
                if (count <= limit && (count > bestDistinct ||
                        count == bestDistinct && current.compareTo(best) > 0)) {
                    best = current;
                    bestDistinct = count;
                }

                // 清空，准备下一个片段
                current = "";
            }
        }

        // 没有合法片段返回 "Not Found"
        return bestDistinct < 0 ? "Not Found" : best;
    }

    /**
     * 计算字符串中不同字符的个数
     */
    static private int distinct(String value) {
        java.util.Set<Character> characters = new java.util.HashSet<>();
        for (int index = 0; index < value.length(); index++)
            characters.add(value.charAt(index));
        return characters.size();
    }

    public static void main(String[] args) {
        String scrambled = "-1admyffc-1ptaagghi-1smeersst-1mnrt";
        String reference = "ssyyfgh";

        System.out.println(bestValidSubstring2(scrambled,reference));

        System.out.println(bestValidSubstring(scrambled,reference));

//        System.out.println(bestValidSubstring2("123admyffc79ptaagghi2222smeersst88mnrt","ssyyfgh"));
    }

}
