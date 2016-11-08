package org.atilf.module.disambiguation;

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
    private EvaluationExtractor _evaluationExtractor;
    private EvaluationExtractor _evalExtractor2;
    private Deque<String> _expectedTarget = new ArrayDeque<>();
    private Deque<String> _expectedCorresp = new ArrayDeque<>();
    private Deque<String> _expectedLexAna = new ArrayDeque<>();
    //TODO rename this variable
    //TODO change resource path &
    private Map<String,EvaluationProfile> expectedMap = new HashMap<>();
    private Map<String,EvaluationProfile> multiSub = new HashMap<>();
    @Before
    public void setUp(){
        _evaluationExtractor = new EvaluationExtractor("src/test/resources/corpus/tei/test1.xml",
                new HashMap<>());
        _expectedTarget.add("#t13 #t14 #t15 #t16");
        _expectedTarget.add("#t16 #t17 #t18");
        _expectedTarget.add("#t30");
        _expectedTarget.add("#t49");
        _expectedTarget.add("#t62");
        _expectedTarget.add("#t68 #t69 #t70");

        _expectedCorresp.add("#entry-13471");
        _expectedCorresp.add("#entry-8318");
        _expectedCorresp.add("#entry-7263");
        _expectedCorresp.add("#entry-990");
        _expectedCorresp.add("#entry-39775");
        _expectedCorresp.add("#entry-151826");

        _expectedLexAna.add("#noDM");
        _expectedLexAna.add("#DM4");
        _expectedLexAna.add("#DM3");
        _expectedLexAna.add("#DM0");
        _expectedLexAna.add("#noDM");
        _expectedLexAna.add("#noDM");

        _evalExtractor2 = new EvaluationExtractor("src/test/resources/corpus/tei/test1.xml", multiSub);
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
        _evaluationExtractor.extractTerms();
        _evaluationExtractor.getTarget().forEach(
                el -> Assert.assertEquals("target must be equals", _expectedTarget.poll(),el)
        );
        _evaluationExtractor.getCorresp().forEach(
                el -> Assert.assertEquals("terms id must be equals", _expectedCorresp.poll(),el)
        );
        _evaluationExtractor.getLexAna().forEach(
                el -> Assert.assertEquals("ana id must be equals", _expectedLexAna.poll(),el)
        );

    }

    @Test
    public void extractSubCorpusSimple() throws Exception {
        _evalExtractor2.extractTerms();
        _evalExtractor2.extractSubCorpus();
        expectedMap.forEach(
                (key,value) -> {
                    Multiset observed = multiSub.get(key).getLexicalTable();
                    value.forEach(
                            el -> {
                                int count = observed.count(el);
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.countOccurrence(el),observed.count(el)
                                );
                            }
                    );
                }
        );
    }

}