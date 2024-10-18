package 力扣.数组和字符串.A6;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-18 17:21
 */
public class Z字型变换 {
    //
    public String convert(String s, int numRows) {
        if(numRows < 2) return s;
        List<StringBuilder> rows = new ArrayList<StringBuilder>();
        for(int i = 0; i < numRows; i++) rows.add(new StringBuilder());
        int i = 0, flag = -1;
        for(char c : s.toCharArray()) {
            rows.get(i).append(c);
            if(i == 0 || i == numRows -1) flag = - flag;
            i += flag;
        }
        StringBuilder res = new StringBuilder();
        for(StringBuilder row : rows) res.append(row);
        return res.toString();
    }


}
