package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:58
 */
public class 分配投资资金 {

    int maxInvestmentReturn(int budget, int maximumRisk, int perProductLimit, int[][] products) {
        java.util.List<Integer> eligible = new java.util.ArrayList<>();
        for (int[] product : products)
            if (product.length >= 2 && product[1] <= maximumRisk)
                eligible.add(product[0]);
        eligible.sort(java.util.Collections.reverseOrder());
        int remaining = Math.max(budget, 0);
        long weighted = 0;
        for (int expected : eligible) {
            if (remaining <= 0 || perProductLimit <= 0)
                break;
            int invested = Math.min(perProductLimit, remaining);
            weighted += (long) invested * expected;
            remaining -= invested;
        }
        return (int) (weighted >= 0 ? (weighted + 50) / 100 : -((-weighted + 50) / 100));
    }

}
