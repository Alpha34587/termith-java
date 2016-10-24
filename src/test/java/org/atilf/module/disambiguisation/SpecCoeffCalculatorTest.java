package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import org.atilf.models.GlobalLexic;
import org.atilf.models.RLexic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffCalculatorTest {

    private GlobalCorpus corpus1;
    private GlobalCorpus corpus2;
    private GlobalLexic globalLexic;
    private RLexic rLexic;
    private Map<String, LexicalProfile> subLexic;
    private SubLexic subCorpus1;
    private SubLexic subCorpus2;
    private Map<String,double[]> specificities;

    @Before
    public void setUp() throws Exception {
        globalLexic = new GlobalLexic(new HashMap<>(),new HashMap<>());
        subLexic = new HashMap<>();
        corpus1 = new GlobalCorpus("src/test/resources/corpus/tei/test1.xml", globalLexic);
        corpus2 = new GlobalCorpus("src/test/resources/corpus/tei/test2.xml", globalLexic);
        subCorpus1 = new SubLexic("src/test/resources/corpus/tei/test1.xml", subLexic);
        subCorpus2 = new SubLexic("src/test/resources/corpus/tei/test2.xml", subLexic);
        corpus1.execute();
        corpus2.execute();
        rLexic = new RLexic(globalLexic);
        subCorpus1.execute();
        subCorpus2.execute();
        specificities = new HashMap<>();
        specificities.put("entry-8318_lexOn",new double[]{1.3425, 1.3425, 1.3425, 1.3425, 3.4531, 3.4531});
        specificities.put("entry-990_lexOff",new double[]{1.8162, 1.8162, 1.8162, 1.8162, 1.8162, 1.8162});
        specificities.put("entry-7263_lexOn",new double[]{3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302,
                3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302, 3.9302,});
        specificities.put("entry-13471_lexOn",new double[]{1.3425, 1.3425, 1.3425, 1.3425, 3.4531, 3.4531, 1.3425});

    }

    //TODO complete test
    @Test
    public void reduceToLexicalProfile() throws Exception {

    }

    @Test
    public void computeSpecCoeff() throws Exception {
        subLexic.forEach(
                (key,value) -> {
                    Assert.assertArrayEquals(
                            specificities.get(key),
                            new SpecCoeffCalculator(value, rLexic, globalLexic).computeSpecCoeff(),
                            0);}
        );
    }
}