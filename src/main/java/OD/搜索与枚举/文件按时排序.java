package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 15:05
 */
public class 文件按时排序 {

    /**
     * 存储一条解析后的条目信息。
     * 每个条目的原始行形如 "----name timestamp"，
     * 通过前面的 "----" 数量表示层级，后面的数字表示时间戳。
     */
    class StoredFileEntry {
        int depth;        // 层级深度，等于前缀 "----" 的组数（每 4 个 '-' 为 1 层）
        String name;      // 条目名称（文件夹名或文件名）
        long timestamp;   // 时间戳；-1 表示这是一个文件夹，非 -1 表示文件

        StoredFileEntry(int depth, String name, long timestamp) {
            this.depth = depth;
            this.name = name;
            this.timestamp = timestamp;
        }
    }

    class Solution {
        /**
         * @param targetFolder 要查找的目标文件夹名
         * @param entries      目录结构字符串数组，每行格式："----名称 时间戳"
         * @return 目标文件夹下所有文件（递归包含子目录）按时间戳升序排列的 "名称 时间戳" 数组；
         *         若目标文件夹不存在或其中无文件，返回 {"No file"}
         */
        String[] sortFolderFiles(String targetFolder, String[] entries) {
            // ---------- 第一步：解析每一行，构建条目列表 ----------
            java.util.List<StoredFileEntry> parsed = new java.util.ArrayList<>();
            for (String line : entries) {
                // 统计前缀中 "----" 的个数，确定层级深度
                int offset = 0;
                while (line.startsWith("----", offset)) offset += 4;

                // 去掉前缀和首尾空白，得到 "名称 时间戳" 形式的内容
                String content = line.substring(offset).trim();

                // 用最后一个空格分隔名称和时间戳（名称本身可能含空格）
                int separator = content.lastIndexOf(' ');
                parsed.add(new StoredFileEntry(
                        offset / 4,                                  // depth：每 4 个 '-' 代表 1 层
                        content.substring(0, separator),             // name
                        Long.parseLong(content.substring(separator + 1)) // timestamp
                ));
            }

            // ---------- 第二步：定位目标文件夹 ----------
            // 文件夹的特征是 timestamp == -1（文件有真实时间戳）
            int targetIndex = -1;
            for (int index = 0; index < parsed.size(); index++) {
                StoredFileEntry entry = parsed.get(index);
                if (entry.name.equals(targetFolder) && entry.timestamp == -1) {
                    targetIndex = index;
                    break;
                }
            }
            if (targetIndex < 0) return new String[]{"No file"}; // 找不到目标文件夹

            // ---------- 第三步：收集目标文件夹下的所有文件 ----------
            int targetDepth = parsed.get(targetIndex).depth;
            java.util.List<StoredFileEntry> files = new java.util.ArrayList<>();

            // 从目标文件夹的下一行开始向后扫描
            for (int index = targetIndex + 1; index < parsed.size(); index++) {
                StoredFileEntry entry = parsed.get(index);
                // 深度 <= 目标深度，说明已离开目标文件夹的子树，停止
                if (entry.depth <= targetDepth) break;
                // 只收集文件（timestamp != -1），忽略子文件夹
                if (entry.timestamp != -1) files.add(entry);
            }

            // ---------- 第四步：按时间戳升序排序 ----------
            files.sort(java.util.Comparator.comparingLong(entry -> entry.timestamp));

            if (files.isEmpty()) return new String[]{"No file"};

            // ---------- 第五步：格式化输出 ----------
            String[] result = new String[files.size()];
            for (int index = 0; index < files.size(); index++) {
                StoredFileEntry entry = files.get(index);
                result[index] = entry.name + " " + entry.timestamp;
            }
            return result;
        }
    }

}
