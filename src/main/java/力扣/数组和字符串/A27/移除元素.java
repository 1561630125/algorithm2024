package 力扣.数组和字符串.A27;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-13 10:50
 */
public class 移除元素 {

    public static void main(String[] args) {

    }

    //由于题目要求删除数组中等于 val 的元素，因此输出数组的长度一定小于等于输入数组的长度，我们可以把输出的数组直接写在输入数组上。
    // 可以使用双指针：右指针 right 指向当前将要处理的元素，左指针 left 指向下一个将要赋值的位置
    public int removeElement(int[] nums, int val) {
        int n = nums.length;
        int left = 0;
        for (int right = 0; right < n; right++) {
            if (nums[right] != val) {
                nums[left] = nums[right];
                left++;
            }
        }
        return left;
    }

    //依然使用双指针，两个指针初始时分别位于数组的首尾，向中间移动遍历该序列,方法二避免了需要保留的元素的重复赋值操作。
    public int removeElement2(int[] nums, int val) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            if (nums[left] == val) {
                nums[left] = nums[right - 1];
                right--;
            } else {
                left++;
            }
        }
        return left;
    }


}
