package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 23:32
 */
public class 密码本坐标串 {

    class Solution {

        // 明文序列（要匹配的数字序列）
        long[] batch63Plain;

        // 码本（二维网格）
        long[][] batch63Book;

        // 访问标记，防止同一个格子被重复使用
        boolean[][] batch63Visited;

        // 记录当前路径上每个数字所在的行、列
        int[] batch63Rows, batch63Cols;

        // 记录字典序最小的答案字符串
        String batch63Best;

        /**
         * 回溯搜索：在码本中从 (row, col) 开始，匹配 plain[index..]
         *
         * @param index 当前要匹配 plain 的第几个数字
         * @param row   当前格子的行
         * @param col   当前格子的列
         */
        void batch63Search(int index, int row, int col) {
            int size = batch63Book.length;

            // 越界、已访问、格子值不匹配，直接返回
            if (row < 0 || row >= size || col < 0 || col >= size
                    || batch63Visited[row][col]
                    || batch63Book[row][col] != batch63Plain[index])
                return;

            // 选中当前格子
            batch63Visited[row][col] = true;
            batch63Rows[index] = row;
            batch63Cols[index] = col;

            if (index + 1 == batch63Plain.length) {
                // 所有数字都匹配完了，构造答案字符串
                StringBuilder value = new StringBuilder();
                for (int i = 0; i < batch63Plain.length; i++) {
                    if (i > 0)
                        value.append(' ');
                    value.append(batch63Rows[i]).append(' ').append(batch63Cols[i]);
                }
                String candidate = value.toString();

                // 保留字典序最小的答案
                if (batch63Best == null || candidate.compareTo(batch63Best) < 0)
                    batch63Best = candidate;
            } else {
                // 继续匹配下一个数字，四方向移动
                batch63Search(index + 1, row, col + 1);  // 右
                batch63Search(index + 1, row + 1, col);  // 下
                batch63Search(index + 1, row - 1, col);  // 上
                batch63Search(index + 1, row, col - 1);  // 左
            }

            // 回溯：取消当前格子的选中
            batch63Visited[row][col] = false;
        }

        /**
         * 在码本中找一条路径，按顺序匹配 plaintext 的所有数字，
         * 相邻数字必须在相邻格子上（上下左右），每个格子只能用一次
         * 返回字典序最小的坐标串；无解返回 "error"
         *
         * @param plaintext 要匹配的数字序列
         * @param codebook  二维码本
         * @return 坐标串或 "error"
         */
        String encryptCoordinates(long[] plaintext, long[][] codebook) {
            // 空输入
            if (plaintext.length == 0 || codebook.length == 0)
                return "error";

            // 初始化成员变量
            batch63Plain = plaintext;
            batch63Book = codebook;
            batch63Visited = new boolean[codebook.length][codebook.length];
            batch63Rows = new int[plaintext.length];
            batch63Cols = new int[plaintext.length];
            batch63Best = null;

            // 枚举所有可能的起点（值等于 plaintext[0] 的格子）
            for (int row = 0; row < codebook.length; row++)
                for (int col = 0; col < codebook.length; col++)
                    if (codebook[row][col] == plaintext[0])
                        batch63Search(0, row, col);

            return batch63Best == null ? "error" : batch63Best;
        }
    }


    static class Solution2 {
        String encryptCoordinates(long[] plaintext, long[][] codebook) {
            if (plaintext.length == 0 || codebook.length == 0)
                return "error";

            int size = codebook.length;
            String best = null;

            // 队列元素：{index, row, col, visitedMask, path}
            // visitedMask 用位运算表示哪些格子访问过（size <= 64 时可用）
            // path 记录坐标串
            Queue<State> queue = new ArrayDeque<>();

            // 所有起点入队
            for (int row = 0; row < size; row++)
                for (int col = 0; col < size; col++)
                    if (codebook[row][col] == plaintext[0]) {
                        State s = new State();
                        s.index = 0;
                        s.row = row;
                        s.col = col;
                        s.visited = new boolean[size][size];
                        s.visited[row][col] = true;
                        s.path = row + " " + col;
                        queue.offer(s);
                    }

            while (!queue.isEmpty()) {
                State cur = queue.poll();

                // 匹配完所有数字
                if (cur.index + 1 == plaintext.length) {
                    if (best == null || cur.path.compareTo(best) < 0)
                        best = cur.path;
                    continue;
                }

                // 四方向扩展
                int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
                for (int[] d : dirs) {
                    int nr = cur.row + d[0], nc = cur.col + d[1];
                    if (nr < 0 || nr >= size || nc < 0 || nc >= size) continue;
                    if (cur.visited[nr][nc]) continue;
                    if (codebook[nr][nc] != plaintext[cur.index + 1]) continue;

                    State next = new State();
                    next.index = cur.index + 1;
                    next.row = nr;
                    next.col = nc;
                    next.visited = copy(cur.visited);  // 复制 visited
                    next.visited[nr][nc] = true;
                    next.path = cur.path + " " + nr + " " + nc;
                    queue.offer(next);
                }
            }

            return best == null ? "error" : best;
        }

        static class State {
            int index, row, col;
            boolean[][] visited;
            String path;
        }

        boolean[][] copy(boolean[][] src) {
            boolean[][] dst = new boolean[src.length][];
            for (int i = 0; i < src.length; i++)
                dst[i] = src[i].clone();
            return dst;
        }
    }

}
