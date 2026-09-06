package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:56
 */
public class 链表中点 {
    /**
     * 查找链表中间节点的值（快慢指针法）
     *
     * 核心思想：快指针每次走2步，慢指针每次走1步
     * 当快指针到达链表末尾时，慢指针正好在中间位置
     *
     * @param headAddress 链表头节点的地址
     * @param nodes 节点信息数组，每个元素为 [地址, 值, 下一节点地址]
     *              例如：["A", "10", "B"] 表示地址A的节点值为10，下一个节点地址为B
     * @return 中间节点的值（如果链表为空则返回空字符串）
     */
    String linkedListMiddleValue(String headAddress, String[][] nodes) {
        // ==================== 1. 构建节点的映射关系 ====================
        // nextMap: 地址 -> 下一节点地址
        // valueMap: 地址 -> 节点值
        java.util.Map<String, String> nextMap = new java.util.HashMap<>(),
                valueMap = new java.util.HashMap<>();

        // 遍历所有节点信息，建立地址到值和下一节点的映射
        for (String[] node : nodes) {
            if (node.length >= 3) {  // 确保数组包含 [地址, 值, 下一节点地址]
                nextMap.put(node[0], node[2]);  // 地址 -> 下一节点地址
                valueMap.put(node[0], node[1]); // 地址 -> 节点值
            }
        }

        // ==================== 2. 初始化快慢指针 ====================
        // 慢指针：从头节点开始
        String slow = headAddress;

        // 快指针：从头节点的下一个节点开始
        // 这样当链表长度为偶数时，慢指针会停在中间偏左的位置
        String fast = nextMap.get(headAddress);

        // 初始结果：头节点的值（如果头节点为空，则返回空字符串）
        String result = valueMap.getOrDefault(headAddress, "");

        // ==================== 3. 防止死循环的安全机制 ====================
        // 理论上链表不会成环，但为了安全，设置最大遍历次数为节点数+1
        int guard = nodes.length + 1;

        // ==================== 4. 快慢指针遍历 ====================
        // 条件：快指针不为空、不为结束标记"-1"，且未超过安全阈值
        while (fast != null && !fast.isEmpty() && !fast.equals("-1") && guard-- > 0) {

            // 慢指针前进一步：slow = slow.next
            // 如果slow的next不存在，设为空字符串（即null）
            slow = nextMap.getOrDefault(slow, "");

            // 更新结果为当前慢指针指向的节点值
            result = valueMap.getOrDefault(slow, "");

            // 快指针前进两步：
            // 第一步：fast = fast.next
            fast = nextMap.get(fast);

            // 第二步：如果第一步后的fast不为空，再走第二步
            if (fast != null && !fast.isEmpty()) {
                fast = nextMap.get(fast);
            }
            // 注意：如果fast为空或"-1"，说明已到达链表末尾，循环即将结束
        }

        // ==================== 5. 返回中间节点的值 ====================
        return result;
    }


    public static void main(String[] args) {

    }

}
