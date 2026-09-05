package OD.基础与模拟;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 11:49
 */
public class 多币种结算 {

    static long currencyTotal(java.util.List<String> records) {
        HashMap<String, String> unit = new HashMap<>();
        unit.put("CNY","fen");
        unit.put("HKD","cents");
        unit.put("JPY","sen");
        unit.put("EUR","eurocents");
        unit.put("GBP","pence");


        for(int i = 0; i < records.size(); i++) {
            String record = records.get(i);
            Pattern pattern = Pattern.compile("(\\d+)([A-Za-z]+)");
            Matcher matcher = pattern.matcher(record);
            while (matcher.find()) {
                String number = matcher.group(1);
                String str = matcher.group(2);
            }
        }


        return 0L;
    }

    /**
     * 计算记录列表中所有货币金额的总和（折算为人民币分，再转回人民币元）
     * @param records 字符串列表，每条记录包含数字和货币单位，如 "100CNY" 或 "50JPY"
     * @return 总金额（人民币元），去掉了小数部分（因为使用了 long 整型运算）
     */
    long currencyTotal2(java.util.List<String> records) {
        // 定义基础缩放比例（即 1 人民币元 = 6,285,300 个基础单位）
        // 这个基础单位被定义为“人民币分 * 100000”？实际上是为了容纳浮点数精度的整数转换
        final long scale = 6285300L;
        long total = 0L; // 累计总额（以基础单位计）

        for (String record : records) {
            int index = 0; // 当前解析位置

            // 遍历字符串中的每个字符
            while (index < record.length()) {
                // 1. 跳过非数字字符（定位到数字起始位置）
                while (index < record.length() && !Character.isDigit(record.charAt(index)))
                    index++;

                // 2. 解析连续的数字字符，构成金额数值
                long amount = 0L;
                while (index < record.length() && Character.isDigit(record.charAt(index)))
                    amount = amount * 10L + record.charAt(index++) - '0';

                // 3. 记录当前索引（货币单位的起始位置）
                int start = index;
                // 4. 跳过连续的字母字符（即货币单位）
                while (index < record.length() && Character.isLetter(record.charAt(index)))
                    index++;

                // 5. 如果确实读到了货币单位，则进行折算并累加
                if (start < index) {
                    String unit = record.substring(start, index);
                    // amount * scaledRate(unit) : 将金额按照该货币的汇率转换为“基础单位”
                    // 注意：这里可能会发生 long 溢出，但通常金额不会极端大
                    total += amount * scaledRate(unit);
                }
                // 循环继续，处理同一字符串中可能出现的下一个“数字+单位”组合
            }
        }

        // 最后将“基础单位”总额除以 scale，转换回“人民币元”单位
        // 使用 long 除法，会直接丢弃小数部分（向下取整）
        return total / scale;
    }

    /**
     * 返回每种货币单位对应的“基础单位”缩放系数
     * 基础单位定义：使得 1 人民币元 = 6,285,300 个基础单位
     * 这样所有货币金额都可以用整数表示，避免浮点数误差
     */
    private long scaledRate(String unit) {
        // 人民币：1 元 = 100 分 = 6,285,300 基础单位
        if("CNY".equals(unit)) return 628530000L;   // 相当于 100 * scale
        if("fen".equals(unit)) return 6285300L; // 1 分 = scale

        // 日元：1 日元 ≈ 0.0548 元，此处用 34,440,000 基础单位表示 1 日元
        if("JPY".equals(unit)) return 34440000L;
        if("sen".equals(unit)) return 344400L;

        // 港币：1 港元 ≈ 0.813 元
        if("HKD".equals(unit)) return 511000000L;
        if("cents".equals(unit)) return 5110000L; // 1 港币分

        // 欧元：1 欧元 ≈ 7.14 元
        if("EUR".equals(unit)) return 4489500000L;
        if("eurocents".equals(unit)) return 44895000L;

        // 英镑：1 英镑 ≈ 8.33 元
        if("GBP".equals(unit)) return 5237750000L;
        if("pence".equals(unit)) return 52377500L; // 1 便士

        return 0L; // 未知单位返回 0（相当于忽略该金额）
    }


    public static void main(String[] args) {


    }

}
