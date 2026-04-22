package io.github.cc53453.datatype.pojo;

import lombok.Data;

/**
 * 评分结果类
 */
@Data
public class NumberConditionEvaluateScoreResult {
	/**
	 * 实际得分
	 */
    private final Double score;
    /**
     *  最高可能得分
     */
    private final Double maxScore;
    /**
     *  标准化得分 (0-1)
     */
    private final Double normalizedScore;
    
    /**
     * 构造函数
     * @param score 实际得分
     * @param maxScore 理论最高分
     */
    public NumberConditionEvaluateScoreResult(Double score, Double maxScore) {
        this.score = score;
        this.maxScore = maxScore;
        if (maxScore==0) {
            this.normalizedScore = 0.0;
        } else {
            this.normalizedScore = score / maxScore;
        }
    }
    
    /**
     * 获取0-100的整数分数
     * @return 标准化到0~1的分数*100后去尾
     */
    public int getScore100() {
        Double score100=normalizedScore*100;
        return score100.intValue();
    }
    
    @Override
    public String toString() {
        return String.format("ScoreResult{score=%s, maxScore=%s, normalized=%s}",
            score, maxScore, normalizedScore);
    }
}