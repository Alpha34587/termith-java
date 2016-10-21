package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.RLexic;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffCalculatorTest {
    private GlobalCorpus corpus1;
    private GlobalCorpus corpus2;
    private Multiset observedCorpus;
    private RLexic rLexic;
    private Map<String, LexicalProfile> subLexic;
    private SubLexic subCorpus1;
    private SubLexic subCorpus2;

    @Before
    public void setUp() throws Exception {
        observedCorpus = HashMultiset.create();
        subLexic = new HashMap<>();
        corpus1 = new GlobalCorpus("src/test/resources/corpus/tei/test1.xml",observedCorpus);
        corpus2 = new GlobalCorpus("src/test/resources/corpus/tei/test2.xml",observedCorpus);
        subCorpus1 = new SubLexic("src/test/resources/corpus/tei/test1.xml", subLexic);
        subCorpus2 = new SubLexic("src/test/resources/corpus/tei/test2.xml", subLexic);
        corpus1.execute();
        corpus2.execute();
        rLexic = new RLexic(observedCorpus);
        subCorpus1.execute();
        subCorpus2.execute();
    }

    @Test
    public void execute() throws Exception {
        subLexic.values().forEach(
                value -> new SpecCoeffCalculator(value,rLexic).execute()
        );
    }

}