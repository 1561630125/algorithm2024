package OD.搜索与枚举;

/**
 * 考点：BFS
 *
 * @author faming.yang@hand-china.com 2026-09-13 16:17
 */
public class 依赖关系展开 {


    public class Solution {

        /**
         * 解析依赖。
         *
         * @param directDeps 直接依赖，每项格式：name:version[:excluded1,excluded2,...]
         * @param depRules   依赖规则，每项格式：parent:child:version[:excluded1,...]
         *                   表示 parent 依赖 child 的指定版本
         * @return 按解析顺序排列的 "name:version" 数组
         */
        public String[] resolveDependencies(String[] directDeps, String[] depRules) {

            // ---------- 1. 构建规则表 ----------
            // key = 父依赖名，value = 该父依赖对应的子依赖节点列表
            java.util.Map<String, java.util.List<Node>> rules = new java.util.HashMap<>();
            for (String rule : depRules) {
                String[] fields = rule.split(":", -1);   // -1 保留末尾空串
                rules.computeIfAbsent(
                        fields[0],
                        ignored -> new java.util.ArrayList<>()
                ).add(new Node(
                        fields[1],                                        // 子依赖名
                        fields[2],                                        // 子依赖版本
                        parts(fields.length > 3 ? fields[3] : "")        // 子依赖的排除集
                ));
            }

            // ---------- 2. 初始化 BFS 队列（直接依赖作为起点）----------
            java.util.ArrayDeque<Node> queue = new java.util.ArrayDeque<>();
            for (String direct : directDeps) {
                String[] fields = direct.split(":", -1);
                queue.add(new Node(
                        fields[0],                                        // 名称
                        fields[1],                                        // 版本
                        parts(fields.length > 2 ? fields[2] : "")        // 排除集
                ));
            }

            // ---------- 3. BFS 解析依赖 ----------
            java.util.List<String> answer = new java.util.ArrayList<>();
            java.util.Set<String> added = new java.util.HashSet<>();   // 已加入结果的依赖名

            while (!queue.isEmpty()) {
                Node current = queue.remove();

                // 去重：同名依赖只解析一次（第一次出现者胜出，类似 Maven nearest-wins）
                if (!added.add(current.name))
                    continue;

                answer.add(current.name + ":" + current.version);

                // 查找该依赖的子依赖规则
                for (Node child : rules.getOrDefault(
                        current.name,
                        java.util.Collections.emptyList())) {

                    // 如果子依赖被当前节点的排除集排除，则跳过
                    if (!current.excluded.contains(child.name)) {

                        // 合并排除集：父的排除集 + 子自身的排除集，传给子节点
                        java.util.Set<String> next = new java.util.HashSet<>(current.excluded);
                        next.addAll(child.excluded);

                        queue.add(new Node(child.name, child.version, next));
                    }
                }
            }

            return answer.toArray(new String[0]);
        }

        /**
         * 解析逗号分隔的排除集字符串。
         * 例如 "a,b,c" -> {a, b, c}；空串 -> 空集合
         */
        private java.util.Set<String> parts(String value) {
            java.util.Set<String> answer = new java.util.HashSet<>();
            if (!value.isEmpty())
                java.util.Collections.addAll(answer, value.split(","));
            return answer;
        }

        /** 表示一个依赖节点：名称、版本、需要排除的依赖名集合 */
        private  class Node {
            final String name, version;
            final java.util.Set<String> excluded;

            Node(String name, String version, java.util.Set<String> excluded) {
                this.name = name;
                this.version = version;
                this.excluded = excluded;
            }
        }
    }

}
