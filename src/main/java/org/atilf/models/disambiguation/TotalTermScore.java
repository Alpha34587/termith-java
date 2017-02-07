package org.atilf.models.disambiguation;

/**
 * @author Simon Meoni Created on 20/12/16.
 */
public class TotalTermScore {
    private float _recall = 0f;
    private float _precision = 0f;
    private float _F1score = 0f;

    public TotalTermScore() {}

    public float getRecall() {
        return _recall;
    }

    public float getPrecision() {
        return _precision;
    }

    public float getF1score() {
        return _F1score;
    }

    public void setRecall(float recall) {
        _recall = recall;
    }

    public void setPrecision(float precision) {
        _precision = precision;
    }

    public void setF1score(float f1score) {
        _F1score = f1score;
    }
}
