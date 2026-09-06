package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 22:53
 */
public class 词内翻转 {

    String reverseWordsInPlace(String sentence) {

        StringBuilder res = new StringBuilder();

        int index = 0;
        for(int i = 0; i < sentence.length(); i++) {
            if (!Character.isLetter(sentence.charAt(i))) {

                String reversed = new StringBuilder(sentence.substring(index,i)).reverse().toString();
                res.append(reversed);
                res.append(sentence.charAt(i));
            } else {

                index++;
            }



        }


        return sentence;
    }


    /**
     * 反转句子中每个单词的字符顺序，同时保留单词之间的分隔符（非字母字符）。
     *
     * 示例：
     * 输入: "Hello, World!"
     * 输出: "olleH, dlroW!"
     *
     * 注意：该方法保留原有分隔符（空格、标点等），只反转字母序列，
     * 不反转整个句子，而是按单词为单位进行反转。
     *
     * @param sentence 待处理的原始句子
     * @return 处理后的字符串，其中每个单词被反转，分隔符位置不变
     */
    String reverseWordsInPlace2(String sentence) {
        // output: 存储最终结果，累积已处理的部分
        // word: 临时存储当前正在构建的单词（连续的字母）
        StringBuilder output = new StringBuilder(), word = new StringBuilder();

        // 遍历句子中的每一个字符
        for (char value : sentence.toCharArray()) {
            // 判断当前字符是否为字母（如果是字母，则属于当前单词的一部分）
            if (Character.isLetter(value)) {
                // 是字母 → 添加到当前单词缓冲区
                word.append(value);
            } else {
                // 遇到非字母字符（空格、标点、数字等）→ 表示当前单词结束

                // 1. 反转当前单词（将 "Hello" 变为 "olleH"）
                output.append(word.reverse());

                // 2. 清空 word 缓冲区，准备处理下一个单词
                word.setLength(0);

                // 3. 保留原分隔符（非字母字符），添加到输出中
                output.append(value);
            }
        }
        // 循环结束后，处理最后一个单词（因为最后一个单词后面可能没有分隔符）
        // 如果句子以字母结尾，需要将最后一个单词也反转并追加
        return output.append(word.reverse()).toString();
    }

    public static void main(String[] args) {

    }

}
