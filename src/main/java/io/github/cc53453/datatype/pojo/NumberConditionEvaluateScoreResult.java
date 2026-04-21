package io.github.cc53453.datatype.pojo;

import lombok.Data;

/**
 * 评分结果类
 */
@Data
public class NumberConditionEvaluateScoreResult {
    private final Double score;      // 实际得分
    private final Double maxScore;   // 最高可能得分
    private final Double normalizedScore; // 标准化得分 (0-1)
    
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