package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.LexiconProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 * Created on 04/12/16.
 */
public class ThresholdLexiconCleaner implements Runnable {
    private final Map<String,Float> _coefficientMap;
    private final int _minThreshold;
    private final int _maxThreshold;
    private String _id;
    private CountDownLatch _cleanerCounter;
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecCoefficientInjector.class.getName());

    public ThresholdLexiconCleaner(String id, LexiconProfile lexiconProfile, int minThreshold, int maxThreshold,
                                   CountDownLatch cleanerCounter) {
        this(
                lexiconProfile.getSpecCoefficientMap(),
                minThreshold,
                maxThreshold
        );
        _id = id;
        _cleanerCounter = cleanerCounter;
    }

    public ThresholdLexiconCleaner(Map<String, Float> coefficientMap, int minThreshold, int maxThreshold) {
        _coefficientMap = coefficientMap;
        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;
    }


    public void execute(){
        _coefficientMap.values().removeIf(this::isNotBetweenThreshold);
    }

    boolean isNotBetweenThreshold(Float value) {
        if (value < 0){
            if (_maxThreshold * -1 < value && value < _minThreshold * -1){
                return false;
            }
        }
        else {
            if (_minThreshold < value && value < _maxThreshold){
                return false;
            }
        }
        return true;
    }


    @Override
    public void run() {
        LOGGER.info("threshold cleaning started for : " + _id);
        execute();
        _cleanerCounter.countDown();
        LOGGER.info("threshold cleaning finished for : " + _id);
    }
}
