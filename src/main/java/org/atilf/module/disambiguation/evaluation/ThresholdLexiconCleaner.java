package org.atilf.module.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.module.Module;

import java.util.Map;

/**
 * @author Simon Meoni
 * Created on 04/12/16.
 */
public class ThresholdLexiconCleaner extends Module {
    private final Map<String,Float> _coefficientMap;
    private final int _minThreshold;
    private final int _maxThreshold;
    private String _id;

    public ThresholdLexiconCleaner(String id, TermithIndex termithIndex, int minThreshold,
                                   int maxThreshold){
        super(termithIndex);
        _coefficientMap = _termithIndex.getContextLexicon().get(id).getSpecCoefficientMap();
        _id = id;
        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;
    }

    ThresholdLexiconCleaner(Map<String, Float> coefficientMap, int minThreshold, int maxThreshold) {
        _coefficientMap = coefficientMap;
        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;
    }


    @Override
    public void execute(){
        _logger.debug("threshold cleaning started for : {}",_id);
        _coefficientMap.values().removeIf(this::isNotBetweenThreshold);
        _logger.debug("threshold cleaning finished for : {}",_id);
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
}
