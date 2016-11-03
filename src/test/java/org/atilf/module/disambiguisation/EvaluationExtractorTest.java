package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class EvaluationExtractorTest {
    EvaluationExtractor evalExtractor1;
    EvaluationExtractor evalExtractor2;
    Deque<String> expectedTarget = new ArrayDeque<>();
    Deque<String> expectedCorresp = new ArrayDeque<>();
    Deque<String> expectedLexAna = new ArrayDeque<>();
    //TODO rename this variable
    Map<String,EvaluationProfile> expectedMap = new HashMap<>();
    Map<String,EvaluationProfile> multiSub = new HashMap<>();
    @Before
    public void setUp(){
        evalExtractor1 = new EvaluationExtractor("src/test/resources/corpus/tei/test1.xml",
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

        evalExtractor2 = new EvaluationExtractor("src/test/resources/corpus/tei/test2.xml", multiSub);
        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("du PRP:det");
        entry1.add("le DET:ART");
        entry1.add(". SENT");
        entry1.add("sur PRP");
        entry1.add("le DET:ART");
        entry1.add("deux NUM");
        entry1.add("site NOM");
        expectedMap.put("entry-13471_DM1",new EvaluationProfile(entry1));

        Multiset<String> entry2 = HashMultiset.create();
        entry2.add("pêche NOM");
        entry2.add(", PUN");
        entry2.add("limiter VER:pper");
        entry2.add("à PRP");
        entry2.add("quelque PRO:IND");
        entry2.add("espèce NOM");
        entry2.add("commun ADJ");
        entry2.add(". SENT");
        entry2.add("il PRO:PER");
        expectedMap.put("entry-7263_DM3",new EvaluationProfile(entry2));

        Multiset<String> entry3 = HashMultiset.create();
        entry3.add("type NOM");
        entry3.add("de PRP");
        entry3.add("rejet NOM");
        entry3.add("précéder VER:ppre");
        entry3.add("leur DET:POS");
        entry3.add(". SENT");
        expectedMap.put("entry-990_noDM",new EvaluationProfile(entry3));

    }

    @Test
    public void extractTerms() throws Exception {
        evalExtractor1.extractTerms();
        evalExtractor1.getTarget().forEach(
                el -> Assert.assertEquals("target must be equals",expectedTarget.poll(),el)
        );
        evalExtractor1.getCorresp().forEach(
                el -> Assert.assertEquals("terms id must be equals",expectedCorresp.poll(),el)
        );
        evalExtractor1.getLexAna().forEach(
                el -> Assert.assertEquals("ana id must be equals",expectedLexAna.poll(),el)
        );

    }

    @Test
    public void extractSubCorpusSimple() throws Exception {
        evalExtractor2.extractTerms();
        evalExtractor2.extractSubCorpus();
        expectedMap.forEach(
                (key,value) -> {
                    Multiset observed = multiSub.get(key).get_lexicalTable();
                    value.forEach(
                            el -> {
                                int count = observed.count(el);
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.countOccurence(el),observed.count(el)
                                );
                            }
                    );
                }
        );
    }

}