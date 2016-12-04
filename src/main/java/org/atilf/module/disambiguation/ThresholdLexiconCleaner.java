package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.LexiconProfile;

/**
 * Created by simon on 04/12/16.
 */
public class ThresholdLexiconCleaner implements Runnable {
    private final LexiconProfile _lexiconProfile;
    private final int _minThreshold;
    private final int _maxThreshold;
    private final int _negMinThreshold;
    private final int _negMaxThreshold;

    public ThresholdLexiconCleaner(LexiconProfile lexiconProfile, int minThreshold, int maxThreshold,
                                   int negMinThreshold, int negMaxThreshold) {
        _lexiconProfile = lexiconProfile;
        _minThreshold = minThreshold;
        _maxThreshold = maxThreshold;
        _negMinThreshold = negMinThreshold;
        _negMaxThreshold = negMaxThreshold;
    }

    public void execute(){

    }

    @Override
    public void run() {
        
    }
}
