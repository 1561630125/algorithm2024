package OD.贪心和动态规划;

import java.util.HashMap;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:23
 */
public class aaa {

    String longestOpponentStraight(String[] handCards, String[] playedCards) {
        String[] ranks = {"3","4"};
        HashMap<String, Integer> index = new HashMap<>();
        for(int i = 0; i < ranks.length; i++) {
            index.put(ranks[i],i);
        }
        int[] used = new int[ranks.length];

        for(int i = 0; i < handCards.length; i++) {
            if (index.containsKey(handCards[i])){
                used[index.get(handCards[i])]++;
            }
        }
        for(int i = 0; i < playedCards.length; i++) {
            if (index.containsKey(playedCards[i])){
                used[index.get(playedCards[i])]++;
            }
        }

        int bestStart = -1, bestLength = 0, start = 0;
        for(int i = 0; i < ranks.length; i++) {
            if (used[i] < 4) {
                continue;
            }
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


    public static void main(String[] args) {
        String[] ranks = {"3","4"};
        int[] used = new int[ranks.length];


    }
}
