package 力扣.堆.A215;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-30 10:29
 */
public class 快速排序 {

}


/**
 * 快速排序: Java
 *
 * @author skywang
 * @date 2014/03/11
 */
/*（九）Java算法：快速排序（详细图解）_java 快速排序-CSDN博客
https://blog.csdn.net/Alian_1223/article/details/123036590
*/
class QuickSort {
    public static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            //log.info("开始排从【arr[{}]】到【arr[{}]】数据", left, right);
            int pivot = partition(arr, left, right);
            //log.info("返回的基准位置是：{},分区排序后的结果：{}", pivot, arr);
            // 基准元素一定比左边的数大，所以左边分区最大值是：pivot - 1，分区范围是[left, pivot - 1]
            quickSort(arr, left, pivot - 1);
            // 基准元素一定比右边的数小，所以右边分区最小值是：pivot + 1，分区范围是[pivot + 1, right]
            quickSort(arr, pivot + 1, right);
        }
    }

    public static int partition(int[] arr, int left, int right) {
        // 定义基准元素
        int pivotValue = arr[left];
        // 遍历（条件就是分区左边索引小于右边索引）
        while (left < right) {
            // 从右边right开始遍历，找到一个数比基准数小
            while (left < right && arr[right] >= pivotValue) {
                // 未找到，继续往前找
                right--;
            }
            // 找到了，则把找到小值放到此时左边索引的位置
            // 第一次进入时,基准元素已存放到临时值pivotValue了，第一次就相当于放到基准位置了，同时，arr[right]也腾出了一个位置
            arr[left] = arr[right];
            // 从左边left开始遍历，找到一个数比基准数大
            while (left < right && arr[left] <= pivotValue) {
                // 未找到，继续往后找
                left++;
            }
            // 找到了，则把找到大值放到此时右边索引的位置（也就是腾出的位置）
            // 同时，arr[left]也腾出了一个位置
            arr[right] = arr[left];
        }
        // left等于right说明遍历结束了，把基准元素插入到腾出的位置，也就是arr[left]或者arr[right]
        arr[left] = pivotValue;
        // 返回基准元素插入的位置
        return left;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{19, 28, 8, 23, 10, 21, 9};
        //log.info("要排序的初始化数据：{}", arr);
        //从小到大排序
        quickSort(arr, 0, arr.length - 1);
       // log.info("最后排序后的结果：{}", arr);
    }

}