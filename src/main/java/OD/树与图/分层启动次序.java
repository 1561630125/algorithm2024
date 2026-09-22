package OD.树与图;

import java.util.*;

/**
 * 考点：DFS拓扑排序；入度
 *
 * @author faming.yang@hand-china.com 2026-09-14 14:02
 */
public class 分层启动次序 {

    static class Solution {
        /**
         * 根据任务依赖规则（rules），输出一个合法的启动顺序。
         * <p>
         * 每条 rule 形如 "A->B"，表示 A 依赖 B（B 必须先启动，A 才能启动）。
         * 要求：
         * 1. 同一批可启动的任务按字典序排序；
         * 2. 返回拓扑排序结果；
         * 3. 若存在循环依赖（无法全部启动），返回 {"ERROR"}。
         */
        String[] startupTaskOrder(String[] rules) {
            // nodes：所有出现过的任务节点
            java.util.Set<String> nodes = new java.util.HashSet<>();

            // indegree：每个任务的「入度」，即它还有多少个未完成的依赖
            java.util.Map<String, Integer> indegree = new java.util.HashMap<>();

            // reverse：反向图，记录「某个依赖被哪些任务依赖」
            // 即 reverse.get(B) = 所有依赖 B 的任务列表
            java.util.Map<String, java.util.List<String>> reverse = new java.util.HashMap<>();

            // 1. 解析每条规则，构建图和入度
            for (String rule : rules) {
                int split = rule.indexOf("->");
                if (split < 0)          // 非法规则，跳过
                    continue;

                String task       = rule.substring(0, split);        // "A->B" 中的 A
                String dependency = rule.substring(split + 2);       // "A->B" 中的 B

                // 两个节点都加入集合
                nodes.add(task);
                nodes.add(dependency);

                // A 依赖 B → A 的入度 +1（A 需要等 B 完成）
                indegree.put(task, indegree.getOrDefault(task, 0) + 1);

                // B 作为依赖，至少保证出现在 indegree 中（默认 0）
                indegree.putIfAbsent(dependency, 0);

                // 反向边：B → A（B 完成后可以释放 A）
                reverse.computeIfAbsent(dependency, key -> new java.util.ArrayList<>())
                        .add(task);
            }

            // output：最终启动顺序
            java.util.List<String> output = new java.util.ArrayList<>();

            // 2. 逐层（批）处理：每一批挑出入度为 0 的任务
            while (true) {
                // 找出当前所有入度为 0 的任务（即依赖已全部就绪）
                java.util.List<String> ready = new java.util.ArrayList<>();
                for (String node : nodes)
                    if (indegree.getOrDefault(node, 0) == 0)
                        ready.add(node);

                // 同批任务按字典序排序（题目要求）
                java.util.Collections.sort(ready);

                // 如果没有可启动任务，说明剩下的节点形成环，退出
                if (ready.isEmpty())
                    break;

                // 3. 将本批任务加入输出，并从图中移除
                for (String node : ready) {
                    output.add(node);
                    nodes.remove(node);       // 从待处理节点集中删除
                    indegree.remove(node);    // 从入度表中删除
                }

                // 4. 释放本批任务所影响的「下游任务」，让它们的入度 -1
                for (String node : ready)
                    for (String dependent :
                            reverse.getOrDefault(node, java.util.Collections.emptyList()))
                        if (nodes.contains(dependent))  // 只处理尚未启动的任务
                            indegree.put(dependent, indegree.get(dependent) - 1);
            }

            // 5. 若所有节点都已处理完，返回结果；否则说明有环，返回 ERROR
            return nodes.isEmpty()
                    ? output.toArray(new String[0])
                    : new String[] {"ERROR"};
        }
    }


    static class Solution2 {
        /**
         * 用 DFS 实现拓扑排序。
         * A->B 表示 A 依赖 B（B 必须先启动）。
         * 若有环，返回 {"ERROR"}。
         */
        String[] startupTaskOrder(String[] rules) {
            // 邻接表：dependency -> 依赖它的任务列表（方向：被依赖者 → 依赖者）
            Map<String, List<String>> graph = new HashMap<>();
            Set<String> nodes = new HashSet<>();

            // 1. 建图
            for (String rule : rules) {
                int split = rule.indexOf("->");
                if (split < 0) continue;

                String task       = rule.substring(0, split);
                String dependency = rule.substring(split + 2);

                nodes.add(task);
                nodes.add(dependency);

                // dependency 完成后，可以释放 task
                graph.computeIfAbsent(dependency, k -> new ArrayList<>()).add(task);
                graph.putIfAbsent(task, new ArrayList<>());  // 保证独立节点也在图中
            }

            // 2. 状态数组：0=未访问，1=访问中，2=已完成
            Map<String, Integer> state = new HashMap<>();
            for (String node : nodes)
                state.put(node, 0);

            // 结果用双端队列头插，实现「后序逆序」
            LinkedList<String> result = new LinkedList<>();

            // 3. 对每个未访问节点做 DFS（处理图不连通的情况）
            for (String node : nodes) {
                if (state.get(node) == 0) {
                    if (!dfs(node, graph, state, result))
                        return new String[] {"ERROR"};  // 检测到环
                }
            }

            return result.toArray(new String[0]);
        }

        /**
         * DFS 递归。
         * @return false 表示检测到环
         */
        private boolean dfs(String u,
                            Map<String, List<String>> graph,
                            Map<String, Integer> state,
                            LinkedList<String> result) {
            // 标记为「访问中」
            state.put(u, 1);

            // 遍历所有邻居
            for (String v : graph.getOrDefault(u, Collections.emptyList())) {
                int st = state.getOrDefault(v, 0);
                if (st == 1) {
                    // 遇到「访问中」的节点 → 存在环
                    return false;
                }
                if (st == 0) {
                    if (!dfs(v, graph, state, result))
                        return false;
                }
            }

            // 所有后代处理完，标记「已完成」，头插结果
            state.put(u, 2);
            result.addFirst(u);
            return true;
        }
    }


    public static void main(String[] args) {
        Solution2 solution2 = new Solution2();
        String[] rules = new String[]{"C->A", "B->A", "D->B"};


        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution2.startupTaskOrder(rules)));
        System.out.println(Arrays.toString(solution.startupTaskOrder(rules)));
    }

}
