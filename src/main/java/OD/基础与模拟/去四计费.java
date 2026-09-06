package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:18
 */
public class 去四计费 {
    long actualTaxiFare(long reading) {
        if (reading < 0)
            return 0;
        long result = 0, place = 1;
        while (reading > 0) {
            long digit = reading % 10;
            result += (digit > 4 ? digit - 1 : digit) * place;
            place *= 9;
            reading /= 10;
        }
        return result;
    }


    public static void main(String[] args) {

    }

}
