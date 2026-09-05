package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 16:21
 */
public class 曝光偏移量 {

    /**
     * 计算在给定曝光补偿值k下，新图像的平均像素值
     * @param img 原始像素数组
     * @param k 曝光补偿值
     * @return 新图像的平均像素值（double类型）
     */
    static double calculateAverage(long[] img, int k) {
        long  sum = 0;
        for (long pixel : img) {
            // 加上曝光补偿值
            long newPixel = pixel + k;
            // 截断到 [0, 255] 范围
            if (newPixel < 0) {
                newPixel = 0;
            } else if (newPixel > 255) {
                newPixel = 255;
            }
            sum += newPixel;
        }
        // 返回平均值，用double保留小数精度
        return (double) sum / img.length;
    }

    static long automaticExposureOffset(long[] pixels) {

        int bestK = 0;
        double bestDiff = Double.MAX_VALUE;

        for (int k = -127; k <= 128; k++) {
            double avg = calculateAverage(pixels, k);
            double diff = Math.abs(avg - 128);

            if (diff < bestDiff || (diff == bestDiff && k < bestK)) {
                bestDiff = diff;
                bestK = k;
            }
        }

        return bestK;  // int 自动转为 long
    }


    /**
     * 计算最优曝光补偿值（优化版）
     *
     * 核心思路：不是遍历所有k值，而是基于当前总和与目标总和的差距，
     * 从理论最优值开始，逐步向两侧探索，找到使调整后总和最接近目标总和的k。
     *
     * @param pixels 原始像素数组
     * @return 最优曝光补偿值k
     */
    static long automaticExposureOffset2(long[] pixels) {
        // 边界处理：空数组直接返回0
        if (pixels.length == 0) return 0;

        // 目标总和：所有像素值都变成128时的总和
        // 使用long避免溢出（像素长度最大100，128*100=12800，用long更安全）
        long target = (long) pixels.length * 128;

        // 计算原始像素总和
        long total = 0;
        for (long pixel : pixels) total += pixel;

        // 如果原始总和已经等于目标值，不需要任何调整，k=0即为最优
        if (total == target) return 0;

        // 【关键优化1】计算理论最优偏移量
        // 假设没有截断限制，最简单的曝光值应该是 (目标总和 - 当前总和) / 像素数量
        // 这个值是理论上使平均值等于128所需要的偏移量
        // 但由于截断的存在，实际最优值可能不是这个值，需要进一步探索
        long best = (target - total) / pixels.length;

        // 计算在理论最优偏移量下的调整后总和
        long bestTotal = adjustedTotal(pixels, best);
        // 计算调整后总和与目标总和的误差（绝对值）
        long bestError = Math.abs(bestTotal - target);

        // ============================================================
        // 情况1：原始总和小于目标值（图像偏暗，需要正向曝光，k > 0）
        // ============================================================
        if (total < target) {
            // 从理论最优值开始，逐渐增大k（向右探索）
            while (true) {
                // 尝试候选值：当前最优值 + 1
                long candidate = best + 1;
                // 计算候选值下的调整后总和
                long candidateTotal = adjustedTotal(pixels, candidate);
                // 计算候选值的误差
                long candidateError = Math.abs(candidateTotal - target);

                // 如果误差不再减小，说明已经找到最优解，返回当前best
                // 注意：这里用 >= 是因为题目要求多个解时取较小的k
                // 因为我们在递增探索，当前best比candidate小，所以取当前best
                if (candidateError >= bestError) return best;

                // 否则，更新最优值为候选值
                best = candidate;
                bestTotal = candidateTotal;
                bestError = candidateError;
            }
        }

        // ============================================================
        // 特殊情况：理论最优值为0，直接返回
        // 注意：此时total > target（图像偏亮），但best=0说明理论最优偏移量是0
        // 说明原始总和已经非常接近目标值，不需要调整
        // ============================================================
        if (best == 0) return 0;

        // ============================================================
        // 情况2：原始总和大于目标值（图像偏亮，需要负向曝光，k < 0）
        // ============================================================
        // 从理论最优值开始，逐渐减小k（向左探索）
        while (true) {
            // 尝试候选值：当前最优值 - 1
            long candidate = best - 1;
            // 计算候选值下的调整后总和
            long candidateTotal = adjustedTotal(pixels, candidate);
            // 计算候选值的误差
            long candidateError = Math.abs(candidateTotal - target);

            // 【关键逻辑】如果候选值误差更大，说明已经找到最优解
            if (candidateError > bestError) return best;

            // 【关键逻辑2】处理平局情况：如果误差相同且调整后总和也相同
            // 说明这两个k值产生了相同的结果（可能因为截断导致）
            // 此时需要找到能使结果发生变化的最小k值（即最接近当前best的拐点）
            if (candidateError == bestError && candidateTotal == bestTotal) {
                // 寻找下一个使像素值发生变化的临界点
                // 对于每个像素，计算它还需要增加多少才能达到255（即达到饱和）
                // change = 255 - pixel 表示该像素从当前值到饱和还需要加多少
                // 我们需要找到最大的change值，且这个change < best（即在当前best之前）
                long nextChange = Long.MIN_VALUE;
                for (long pixel : pixels) {
                    long change = 255L - pixel;
                    // 如果这个change小于当前best（说明在向左探索时，达到这个change时会发生变化）
                    // 并且大于已记录的最大change，则更新
                    if (change < best && change > nextChange) nextChange = change;
                }

                // 如果没有找到这样的变化点，说明所有像素都已经饱和，无法再变化
                if (nextChange == Long.MIN_VALUE) return best;

                // 否则，直接跳到这个变化点（而不是一步一步移动）
                // 这样可以跳过大量无变化的状态，大幅提升效率
                best = nextChange;
                bestTotal = adjustedTotal(pixels, best);
                bestError = Math.abs(bestTotal - target);
                continue; // 继续循环，在新的best基础上继续探索
            }

            // 正常情况下，候选值更优，更新最优值
            best = candidate;
            bestTotal = candidateTotal;
            bestError = candidateError;
        }
    }

    /**
     * 计算在给定曝光补偿值offset下，所有像素调整后的总和
     * 调整规则：每个像素加上offset后，截断到[0, 255]范围
     *
     * @param pixels 原始像素数组
     * @param offset 曝光补偿值（可以为负数）
     * @return 调整后所有像素的总和
     */
    private static long adjustedTotal(long[] pixels, long offset) {
        long sum = 0;
        for (long pixel : pixels) {
            // 先加偏移量，再用Math.max/min截断到[0, 255]
            // Math.max(0, ...) 保证最小值不小于0
            // Math.min(255, ...) 保证最大值不大于255
            sum += Math.max(0, Math.min(255, pixel + offset));
        }
        return sum;
    }


    public static void main(String[] args) {
        System.out.println(automaticExposureOffset(new long[]{128,129,128,129}));
        System.out.println(automaticExposureOffset2(new long[]{128,129,128,129}));
    }

}
