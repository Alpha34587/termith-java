package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import org.atilf.models.GlobalLexic;
import org.atilf.models.RLexic;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
    }

//    @Test
//    public void execute() throws Exception {
//
//        subLexic.values().forEach(
//                value -> new SpecCoeffCalculator(value,rLexic,globalLexic).execute()
//        );
//    }
//TODO complete test
    @Test
    public void reduceToLexicalProfile() throws Exception {

    }

    @Test
    public void computeSpecCoeff() throws Exception {

    }
}