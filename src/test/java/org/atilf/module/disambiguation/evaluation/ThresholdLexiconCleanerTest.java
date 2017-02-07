package org.atilf.module.disambiguation.evaluation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni Created on 05/12/16.
 */
public class ThresholdLexiconCleanerTest {

    private static Map<String,Float> coefficientMap = new HashMap<>();
    private static ThresholdLexiconCleaner _thresholdLexiconCleaner;

    @BeforeClass
    public static void setUp() throws Exception {
        coefficientMap.put("chat", -3f);
        coefficientMap.put("chaussure", -13f);
        coefficientMap.put("fromage", 3f);
        coefficientMap.put("carré", 13f);
        coefficientMap.put("cercle",10f);
        _thresholdLexiconCleaner = new ThresholdLexiconCleaner(coefficientMap,
                3, 13);
    }

    @Test
    public void execute() throws Exception {
        _thresholdLexiconCleaner.execute();
        String word = "cercle";
        Assert.assertEquals("the size of the map must be equals",1,coefficientMap.size());
        Assert.assertTrue("the coefficient must contains only this entry : " + word,
                coefficientMap.containsKey(word));
    }

    @Test
    public void isNotBetweenThreshold() throws Exception {
        Assert.assertTrue("this method must return true",_thresholdLexiconCleaner.isNotBetweenThreshold(-13f));
        Assert.assertFalse("this method must return false",_thresholdLexiconCleaner.isNotBetweenThreshold(4f));
    }
}