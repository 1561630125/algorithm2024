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
