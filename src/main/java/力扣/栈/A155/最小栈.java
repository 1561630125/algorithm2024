package 力扣.栈.A155;

import java.util.Deque;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-24 10:46
 */
public class 最小栈 {

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(4);
        minStack.push(5);
        minStack.push(6);

        System.out.println(minStack);
    }


}
//辅助栈
class MinStack {
    Deque<Integer> xStack;
    Deque<Integer> minStack;

    public MinStack() {
        xStack = new LinkedList<Integer>();
        minStack = new LinkedList<Integer>();
        minStack.push(Integer.MAX_VALUE);
    }

    public void push(int x) {
        xStack.push(x);
        minStack.push(Math.min(minStack.peek(), x));
    }

    public void pop() {
        xStack.pop();
        minStack.pop();
    }

    public int top() {
        return xStack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
