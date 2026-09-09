package OD.数据结构与区间;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 22:45
 */
public class 组队人数 {

    static long maximumTeamCount(long[] abilities, long minimum) {
        Arrays.sort(abilities);

        int left = 0;
        int right = abilities.length - 1;
        int res = 0;

        while (left < right) {
            if (abilities[right] >= minimum) {
                res++;
                right--;
            } else if (abilities[right] + abilities[left] >= minimum) {
                res++;
                right--;
                left++;
            } else if (abilities[right] + abilities[left] < minimum) {
                left++;
            }

            if (left == right && abilities[left] >= minimum) {
                res++;
            }
        }
        return res;
    }

    static long maximumTeamCount2(long[] abilities, long minimum) {
        abilities = abilities.clone();
        java.util.Arrays.sort(abilities);
        int left = 0, right = abilities.length - 1;
        long answer = 0;
        while (right >= left && abilities[right] >= minimum) {
            right--;
            answer++;
        }
        while (left < right) {
            if (abilities[left] + abilities[right] >= minimum) {
                answer++;
                left++;
                right--;
            } else
                left++;
        }
        return answer;
    }

    public static void main(String[] args) {
        long[] a = new long[]{1,1,1,1,1};
        System.out.println(maximumTeamCount(a,1));
        System.out.println(maximumTeamCount2(a,1));
    }

}
