package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 15:57
 */
public class 归并排序 {

    public class MergeSort {

        // 归并排序主方法
        public  void mergeSort(int[] arr) {
            if (arr == null || arr.length < 2) {
                return;
            }
            int[] temp = new int[arr.length];
            sort(arr, 0, arr.length - 1, temp);
        }

        // 递归拆分
        private  void sort(int[] arr, int left, int right, int[] temp) {
            if (left >= right) {
                return;
            }
            int mid = left + (right - left) / 2;  // 防止溢出
            sort(arr, left, mid, temp);           // 排序左半部分
            sort(arr, mid + 1, right, temp);      // 排序右半部分
            merge(arr, left, mid, right, temp);   // 合并
        }

        // 合并两个有序区间 [left..mid] 和 [mid+1..right]
        private void merge(int[] arr, int left, int mid, int right, int[] temp) {
            int i = left;      // 左区间起点
            int j = mid + 1;   // 右区间起点
            int k = left;      // temp 数组索引

            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }

            // 把左边剩余元素移入 temp
            while (i <= mid) {
                temp[k++] = arr[i++];
            }

            // 把右边剩余元素移入 temp
            while (j <= right) {
                temp[k++] = arr[j++];
            }

            // 把 temp 中的结果复制回原数组
            for (int m = left; m <= right; m++) {
                arr[m] = temp[m];
            }
        }

        public  void main(String[] args) {
            int[] arr = {5, 3, 8, 4, 2, 7, 1, 6};
            mergeSort(arr);
            System.out.println(java.util.Arrays.toString(arr));
            // 输出: [1, 2, 3, 4, 5, 6, 7, 8]
        }
    }



    public class MergeSortIterative {

        public void mergeSort(int[] arr) {
            if (arr == null || arr.length < 2) {
                return;
            }
            int n = arr.length;
            int[] temp = new int[n];

            // 子数组大小从 1 开始翻倍
            for (int size = 1; size < n; size *= 2) {
                for (int left = 0; left < n - size; left += 2 * size) {
                    int mid = left + size - 1;
                    int right = Math.min(left + 2 * size - 1, n - 1);
                    merge(arr, left, mid, right, temp);
                }
            }
        }

        private void merge(int[] arr, int left, int mid, int right, int[] temp) {
            int i = left, j = mid + 1, k = left;

            while (i <= mid && j <= right) {
                temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
            }
            while (i <= mid) temp[k++] = arr[i++];
            while (j <= right) temp[k++] = arr[j++];
            for (int m = left; m <= right; m++) arr[m] = temp[m];
        }

        public void main(String[] args) {
            int[] arr = {5, 3, 8, 4, 2, 7, 1, 6};
            mergeSort(arr);
            System.out.println(java.util.Arrays.toString(arr));
            // 输出: [1, 2, 3, 4, 5, 6, 7, 8]
        }
    }
}
