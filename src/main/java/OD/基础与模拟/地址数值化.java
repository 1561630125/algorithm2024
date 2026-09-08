package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 12:14
 */
public class 地址数值化 {

    String ipv4ToInteger(String ip) {
        // 1. 按分隔符"#"分割字符串
        //    注意：这里使用"#"作为分隔符，但标准IPv4是用"."分割的
        //    使用"-1"限制符是为了保留末尾的空字符串（防止"1.2.3."这种格式丢失最后一个空段）
        String[] sections = ip.split("#", -1);

        // 2. 校验段数：IPv4必须正好4段（如 A.B.C.D）
        if (sections.length != 4) return "invalid IP";

        // 3. 用long类型存储最终结果（虽然IPv4是32位，但用long防止中间计算溢出）
        long value = 0;

        // 4. 遍历每一段
        for (int index = 0; index < sections.length; index++) {
            String section = sections[index];

            // 4.1 检查空段（如"1..2.3"）
            if (section.isEmpty()) return "invalid IP";

            // 4.2 检查前导零（如"01"或"0.1.2.3"中的"01"）—— 严格模式不允许
            //    但注意：单独的"0"是允许的，所以条件写成 length>1 且 第一位是'0'
            if (section.length() > 1 && section.charAt(0) == '0') return "invalid IP";

            // 4.3 检查每个字符是否都是数字（防止"12a"这种）
            for (int offset = 0; offset < section.length(); offset++) {
                if (!Character.isDigit(section.charAt(offset))) return "invalid IP";
            }

            // 4.4 将字符串转为int数字
            int number;
            try {
                number = Integer.parseInt(section);
            } catch (NumberFormatException error) {
                return "invalid IP";  // 理论上不会触发，因为前面已检查过数字
            }

            // 4.5 范围校验：
            //     - 第一段（网络号）：必须是 1~128（A类地址范围，这里限定为A类）
            //     - 后续段（主机号）：必须是 0~255
            if (index == 0 ? number < 1 || number > 128 : number < 0 || number > 255) {
                return "invalid IP";
            }

            // 4.6 核心转换公式：左移8位（乘256）再加当前段
            //    例如：192.168.1.1
            //    第1段后: value = 192
            //    第2段后: value = 192*256 + 168 = 49200
            //    第3段后: value = 49200*256 + 1 = 12595201
            //    第4段后: value = 12595201*256 + 1 = 3224374273
            value = value * 256 + number;
        }

        // 5. 返回整数字符串（用Long是因为value可能超过int范围，如3224374273 > 2^31-1）
        return Long.toString(value);
    }


    public static void main(String[] args) {

    }

}
