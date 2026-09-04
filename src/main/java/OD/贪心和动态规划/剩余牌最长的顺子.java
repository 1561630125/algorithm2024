package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:30
 */
public class 剩余牌最长的顺子 {
    String longestOpponentStraight(String[] handCards, String[] playedCards) {
        String[] ranks = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] used = new int[ranks.length];
        java.util.HashMap<String, Integer> index = new java.util.HashMap<>();
        for (int i = 0; i < ranks.length; i++)
            index.put(ranks[i], i);
        for (String card : handCards)
            if (index.containsKey(card))
                used[index.get(card)]++;
        for (String card : playedCards)
            if (index.containsKey(card))
                used[index.get(card)]++;
        int bestStart = -1, bestLength = 0, start = 0;
        for (int i = 0; i <= ranks.length; i++) {
            if (i < ranks.length && used[i] < 4)
                continue;
            int length = i - start;
            if (length >= 5 && length >= bestLength) {
                bestStart = start;
                bestLength = length;
            }
            start = i + 1;
        }
        if (bestLength == 0)
            return "NO-CHAIN";
        return String.join(
                "-", java.util.Arrays.copyOfRange(ranks, bestStart, bestStart + bestLength));
    }
}
