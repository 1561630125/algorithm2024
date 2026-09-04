package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:11
 */
public class 高矮交错排队 {
    String[] arrangeHeights(String[] tokens) {
        int[] values = new int[tokens.length];
        for (int index = 0; index < tokens.length; index++) {
            if (!tokens[index].matches("[0-9]+")) return new String[0];
            try { values[index] = Integer.parseInt(tokens[index]); } catch (NumberFormatException error) { return new String[0]; }
        }
        for (int index = 1; index < values.length; index++) {
            boolean wrong = index % 2 == 1 ? values[index] > values[index - 1] : values[index] < values[index - 1];
            if (wrong) { int temporary = values[index]; values[index] = values[index - 1]; values[index - 1] = temporary; }
        }
        String[] result = new String[values.length];
        for (int index = 0; index < values.length; index++) result[index] = Integer.toString(values[index]);
        return result;
    }
}
