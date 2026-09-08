package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 22:02
 */
public class 能量淘汰赛 {


    public int[] collision(int n, int[] energy) {
        int res = energy[0];
        for(int i = 1; i < energy.length; i++) {
            if (res == -1) {
                res = energy[i];
            }

            if (energy[i] > res) {
                res = energy[i];
            }else if (energy[i] == res){
                res = -1;
            }
        }

        return new int[]{0};
    }

    public int[] collision2(int n, int[] energy) {
        Integer field = null;
        for (int index = 0; index < n; index++) {
            int value = energy[index];
            if (field == null || value > field)
                field = value;
            else if (value == field) field = null;
        }
        return field == null ? new int[0] : new int[] { field };
    }


    public static void main(String[] args) {

    }



}
