package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class SubLexicTest {
    SubLexic subLexic;
    @Before
    public void setUp(){
        subLexic = new SubLexic("/home/smeoni/IdeaProjects/termITH/src/test/resources/corpus/tei/test1.xml",
                new HashMap<>());
    }

    @Test
    public void extractTerms() throws Exception {
        subLexic.extractTerms();
    }

    @Test
    public void extractSubCorpus() throws Exception {
        subLexic.extractSubCorpus();
    }
}