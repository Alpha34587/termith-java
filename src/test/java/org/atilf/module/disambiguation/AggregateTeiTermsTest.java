package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.AnnotationResources;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.ScoreTerm;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTeiTermsTest {
    private static AggregateTeiTerms _aggregateTeiTerms;
    private static Map<String,ScoreTerm> _expectedScoreTerms = new ConcurrentHashMap<>();
    private static Map<String,ScoreTerm> _observedScoreTerms = new ConcurrentHashMap<>();

    @BeforeClass
    public static void setUp() throws Exception {

        //input test unit
        Map<String,EvaluationProfile> terms = new HashMap<>();

        EvaluationProfile term1 = new EvaluationProfile();
        term1.setDisambiguationId(AnnotationResources.DA_OFF);

        terms.put("entry-450_DM4",term1);
        terms.put("entry-13471_DM3",term1);

        String file = "src/test/resources/corpus/disambiguation/transform-tei/test3.xml";
        _aggregateTeiTerms = new AggregateTeiTerms(file,terms,_observedScoreTerms);

        //expected result
        ContextWord t25 = new ContextWord("t25");
        t25.setPosLemma("le DET:ART");
        ContextWord t13 = new ContextWord("t13");
        t13.setPosLemma("site NOM");
        ContextWord t14 = new ContextWord("t14");
        t14.setPosLemma("de PRP");
        ContextWord t15 = new ContextWord("t15");
        t15.setPosLemma("le DET:ART");
        ContextWord t16 = new ContextWord("t16");
        t16.setPosLemma("âge NOM");
        ContextWord t7 = new ContextWord("t7");
        t7.setPosLemma("du PRP:det");
        ContextWord t8 = new ContextWord("t8");
        t8.setPosLemma("donnée NOM");
        ContextWord t46 = new ContextWord("t46");
        t46.setPosLemma("rejet NOM");
        ContextWord t47 = new ContextWord("t47");
        t47.setPosLemma("précéder VER:ppre");
        ContextWord t48 = new ContextWord("t48");
        t48.setPosLemma("leur DET:POS");
        ContextWord t49 = new ContextWord("t49");
        t49.setPosLemma("enfouissement NOM");


        ScoreTerm score = new ScoreTerm();
        score.setCorrectOccurrence(1);
        score.setTotalOccurrences(1);
        score.setMissingOccurrence(0);
        score.addTermWords(Collections.singletonList(t25));

        _expectedScoreTerms.put("entry-450", score);

        score = new ScoreTerm();
        score.setCorrectOccurrence(1);
        score.setTotalOccurrences(2);
        score.setMissingOccurrence(1);
        List<ContextWord> words = new ArrayList<>();
        words.add(t13);
        words.add(t14);
        words.add(t15);
        words.add(t16);
        score.addTermWords(words);
        score.addTermWords(words);

        _expectedScoreTerms.put("entry-13471",score);

        score = new ScoreTerm();
        score.setCorrectOccurrence(0);
        score.setTotalOccurrences(1);
        score.setMissingOccurrence(1);
        words = new ArrayList<>();
        words.add(t7);
        words.add(t8);
        score.addTermWords(words);
        _expectedScoreTerms.put("entry-575",score);

        score = new ScoreTerm();
        score.setCorrectOccurrence(0);
        score.setTotalOccurrences(1);
        score.setMissingOccurrence(1);
        words = new ArrayList<>();
        words.add(t46);
        words.add(t47);
        words.add(t48);
        score.addTermWords(words);
        _expectedScoreTerms.put("entry-5750",score);
    }

    @Test
    public void execute() throws Exception {
        _aggregateTeiTerms.execute();
        _expectedScoreTerms.forEach(
                (key,value) -> {
                    ScoreTerm observed = _observedScoreTerms.get(key);
                    Assert.assertEquals("these two correctOccurrences must be equals",
                            value.getCorrectOccurrence(),observed.getCorrectOccurrence());
                    Assert.assertEquals("these two missingOccurrences must be equals",
                            value.getMissingOccurrence(),observed.getMissingOccurrence());
                    Assert.assertEquals("these two totalOccurrences must be equals",
                            value.getTotalOccurrences(),observed.getTotalOccurrences());
                    Assert.assertTrue("this two list of words must be equals",
                            value.getTermWords().equals(observed));
                }
        );
    }
}