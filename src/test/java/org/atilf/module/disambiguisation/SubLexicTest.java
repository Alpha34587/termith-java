package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class SubLexicTest {
    SubLexic subLexic;
    Deque<String> expectedTarget = new ArrayDeque<>();
    Deque<String> expectedCorresp = new ArrayDeque<>();
    Deque<String> expectedLexAna = new ArrayDeque<>();
    @Before
    public void setUp(){
        subLexic = new SubLexic("src/test/resources/corpus/tei/test1.xml",
                new HashMap<>());
        expectedTarget.add("#t13 #t14 #t15 #t16");
        expectedTarget.add("#t16 #t17 #t18");
        expectedTarget.add("#t30");
        expectedTarget.add("#t49");
        expectedTarget.add("#t62");
        expectedTarget.add("#t68 #t69 #t70");
        expectedCorresp.add("#entry-13471");
        expectedCorresp.add("#entry-8318");
        expectedCorresp.add("#entry-7263");
        expectedCorresp.add("#entry-990");
        expectedCorresp.add("#entry-39775");
        expectedCorresp.add("#entry-151826");
        expectedLexAna.add("#noDM");
        expectedLexAna.add("#DM4");
        expectedLexAna.add("#DM3");
        expectedLexAna.add("#DM0");
        expectedLexAna.add("#noDM");
        expectedLexAna.add("#noDM");
    }

    @Test
    public void extractTerms() throws Exception {
        subLexic.extractTerms();
        subLexic.getTarget().forEach(
                el -> Assert.assertEquals("target must be equals",expectedTarget.poll(),el)
        );
        subLexic.getCorresp().forEach(
                el -> Assert.assertEquals("terms id must be equals",expectedCorresp.poll(),el)
        );
        subLexic.getLexAna().forEach(
                el -> Assert.assertEquals("ana id must be equals",expectedLexAna.poll(),el)
        );

    }

    @Test
    public void extractSubCorpus() throws Exception {
        subLexic.extractSubCorpus();
    }
}