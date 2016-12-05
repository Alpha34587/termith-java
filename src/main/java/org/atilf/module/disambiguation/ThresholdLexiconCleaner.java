package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.LexiconProfile;

import java.util.Map;

/**
 * @author Simon Meoni
 * Created on 04/12/16.
 */
public class ThresholdLexiconCleaner implements Runnable {
    private final Map<String,Float> _coefficientMap;
    private final int _minThreshold;
    private final int _maxThreshold;
    private final int _negMinThreshold;
    private final int _negMaxThreshold;

    public ThresholdLexiconCleaner(LexiconProfile lexiconProfile, int minThreshold, int maxThreshold,
                                   int negMinThreshold, int negMaxThreshold) {
        this(
                lexiconProfile.getSpecCoefficientMap(),
                minThreshold,
                maxThreshold,
                negMinThreshold,
                negMaxThreshold
        );
    }

    public ThresholdLexiconCleaner(Map<String,Float> coefficientMap, int minThreshold, int maxThreshold,
                                   int negMinThreshold, int negMaxThreshold) {
        _coefficientMap = coefficientMap;
        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;
        _negMinThreshold = negMinThreshold;
        _negMaxThreshold = negMaxThreshold;
    }


    public void execute(){
        _coefficientMap.values().removeIf(this::isNotBetweenThreshold);
    }

    public boolean isNotBetweenThreshold(Float value) {
        return false;
    }


    @Override
    public void run() {

    }
}
