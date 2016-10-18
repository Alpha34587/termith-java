package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class SubLexicTest {
    SubLexic subLexic;
    SubLexic subCorpus;
    Deque<String> expectedTarget = new ArrayDeque<>();
    Deque<String> expectedCorresp = new ArrayDeque<>();
    Deque<String> expectedLexAna = new ArrayDeque<>();
    Map<String,Multiset> expectedMap = new HashMap<>();
    Map<String,Multiset> multiSub = new HashMap<>();
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

        subCorpus = new SubLexic("src/test/resources/corpus/tei/test2.xml", multiSub);
        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("du");
        entry1.add("l'");
        entry1.add(".");
        entry1.add("sur");
        entry1.add("les");
        entry1.add("deux");
        entry1.add("sites");

        expectedMap.put("entry-13471_lexOff",entry1);
        Multiset<String> entry2 = HashMultiset.create();
        entry2.add("pêche");
        entry2.add(",");
        entry2.add("limitée");
        entry2.add("à");
        entry2.add("quelques");
        entry2.add("espèces");
        entry2.add("communes");
        entry2.add(".");
        entry2.add("ils");
        expectedMap.put("entry-7263_lexOff",entry2);

        Multiset<String> entry3 = HashMultiset.create();
        entry3.add("type");
        entry3.add("de");
        entry3.add("rejet");
        entry3.add("précédant");
        entry3.add("leur");
        entry3.add(".");
        expectedMap.put("entry-990_noLex",entry3);
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
    public void extractSubCorpusSimple() throws Exception {
        subLexic.extractSubCorpus();
        expectedMap.forEach(
                (key,value) -> {
                    Multiset observed = multiSub.get(key);
                    value.forEach(
                            el -> {
                                Assert.assertTrue(
                                        "this element must be contains on this multiset : ",
                                        observed.contains(el));
                                Assert.assertEquals("the occurence of element must be equals",
                                        value.count(el),observed.contains(el)
                                );
                            }
                    );
                }
        );
    }
}