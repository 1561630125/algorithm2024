package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 22:17
 */
public class 星号密码 {


    class Solution {
        String decryptPassword(String cipher) {
            String value = cipher;
            for (int number = 26; number >= 1; number--)
                value = value.replace(Integer.toString(number) + (number > 9 ? "*" : ""),
                        Character.toString((char) ('a' + number - 1)));
            return value;
        }
    }


    class Solution2 {
        String decryptPassword(String cipher) {
            StringBuilder result = new StringBuilder();
            int i = 0;
            while (i < cipher.length()) {
                // 先尝试匹配两位数 + '*'
                if (i + 2 < cipher.length()
                        && Character.isDigit(cipher.charAt(i))
                        && Character.isDigit(cipher.charAt(i + 1))
                        && cipher.charAt(i + 2) == '*') {
                    int num = Integer.parseInt(cipher.substring(i, i + 2));
                    if (num >= 10 && num <= 26) {
                        result.append((char) ('a' + num - 1));
                        i += 3;
                        continue;
                    }
                }
                // 再尝试匹配单位数 1-9
                if (Character.isDigit(cipher.charAt(i))) {
                    int num = cipher.charAt(i) - '0';
                    if (num >= 1 && num <= 9) {
                        result.append((char) ('a' + num - 1));
                        i++;
                        continue;
                    }
                }
                // 无法识别的字符，原样保留
                result.append(cipher.charAt(i));
                i++;
            }
            return result.toString();
        }
    }

}
