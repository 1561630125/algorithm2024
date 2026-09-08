package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 12:43
 */
public class 加减用数 {

    int minOperations(int s, int t, int a, int b) {
        int difference = Math.abs(s - t);
        if (difference == 0 || difference % b == 0) return 0;
        int operations = 1;
        while ((difference - operations * a) % b != 0
                && (difference + operations * a) % b != 0) {
            operations++;
        }
        return operations;
    }

}
