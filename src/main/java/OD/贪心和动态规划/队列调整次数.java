package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:45
 */
public class 队列调整次数 {

    int minDequeReorders(String[] commands) {
        int size = 0, result = 0;
        boolean ordered = true;
        for (String raw : commands) {
            String command = raw.trim();
            if (command.equals("remove")) {
                if (!ordered) result++;
                ordered = true; size--;
            } else if (command.startsWith("tail")) size++;
            else { if (size > 0) ordered = false; size++; }
        }
        return result;
    }

}
