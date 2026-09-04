package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:20
 */
public class 扑克顺子拆分 {

    String[] findStraights(String[] cards) {
        String[] sorted = cards.clone();
        java.util.Arrays.sort(sorted, java.util.Comparator.comparingInt(this::rank));
        java.util.List<java.util.List<String>> sequences = new java.util.ArrayList<>();
        for (String card : sorted) {
            boolean matched = false;
            for (java.util.List<String> sequence : sequences) {
                if (!card.equals("2") && rank(card) == rank(sequence.get(sequence.size() - 1)) + 1) {
                    sequence.add(card);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                java.util.List<String> sequence = new java.util.ArrayList<>();
                sequence.add(card);
                sequences.add(sequence);
            }
        }
        java.util.List<String> result = new java.util.ArrayList<>();
        for (java.util.List<String> sequence : sequences) {
            if (sequence.size() >= 5) result.add(String.join(" ", sequence));
        }
        return result.toArray(new String[0]);
    }

    private int rank(String card) {
        String[] order = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        for (int index = 0; index < order.length; index++) if (order[index].equals(card)) return index;
        return order.length;
    }

}
