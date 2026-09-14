package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 22:22
 */
public class 商品多键排序 {

    class Solution {
        /**
         * 根据给定的排序方向，对二维数组 products 的每一行进行多列排序。
         *
         * @param directions 每一列对应的排序方向：
         *                   1  表示该列升序（ascending）
         *                   其它值（通常为 0 或 -1）表示该列降序（descending）
         * @param products   待排序的二维数组，每一行是一条记录，
         *                   每一行的列数应与 directions 的长度一致
         * @return 排序后的新二维数组（不会修改原数组）；
         * 若入参非法或行列不匹配，则返回空二维数组或提前返回的结果
         */
        int[][] sortProducts(int[] directions, int[][] products) {

            // 边界检查：方向数组或产品数组为 null，直接返回空二维数组
            if (directions == null || products == null) return new int[0][];

            // 创建结果数组，行数与 products 相同（不直接修改原数组，保证不可变性）
            int[][] result = new int[products.length][];

            // 逐行复制数据，同时做合法性校验
            for (int row = 0; row < products.length; row++) {

                // 某一行本身为 null，视为非法输入，返回空二维数组
                if (products[row] == null) return new int[0][];

                // 深拷贝当前行，避免后续排序影响原数组
                result[row] = java.util.Arrays.copyOf(products[row], products[row].length);

                // 若某行的列数与 directions 长度不一致，直接返回已复制的部分结果
                // （注意：此时只复制到当前行，后续行尚未处理）
                if (result[row].length != directions.length) return result;
            }

            // 使用自定义比较器对结果数组按“多列 + 指定方向”进行排序
            java.util.Arrays.sort(result, (left, right) -> {

                // 从左到右依次比较每一列
                for (int index = 0; index < directions.length; index++) {

                    // 先按数值比较当前列的升序结果
                    int order = Integer.compare(left[index], right[index]);

                    // 若当前列相等，则继续比较下一列
                    if (order != 0) {
                        // directions[index] == 1 表示升序，直接返回 order
                        // 否则（降序）返回 -order，即反转比较结果
                        return directions[index] == 1 ? -order : order;
                    }
                }

                // 所有列都相等，视为相同元素
                return 0;
            });

            // 返回排序后的二维数组
            return result;
        }
    }

}
