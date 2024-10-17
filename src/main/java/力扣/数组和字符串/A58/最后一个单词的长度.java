package 力扣.数组和字符串.A58;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-17 13:53
 */
public class 最后一个单词的长度 {
    public int lengthOfLastWord(String s) {
        int index = s.length() - 1;
        while (s.charAt(index) == ' ') {
            index--;
        }
        int wordLength = 0;
        while (index >= 0 && s.charAt(index) != ' ') {
            wordLength++;
            index--;
        }
        return wordLength;
    }
}
