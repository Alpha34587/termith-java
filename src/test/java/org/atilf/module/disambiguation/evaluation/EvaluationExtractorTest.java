package org.atilf.module.disambiguation.evaluation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class EvaluationExtractorTest {
    private static EvaluationExtractor _evaluationExtractor;
    private static Map<String,EvaluationProfile> expectedMap = new HashMap<>();
    private static Map<String,EvaluationProfile> _observedMap;
    @BeforeClass
    public static void setUp() throws IOException {
        TermithIndex termithIndex = new TermithIndex.Builder().build();
        termithIndex.getContextLexicon().put("entry-13471_lexOn",new LexiconProfile());
        termithIndex.getContextLexicon().put("entry-13471_lexOff",new LexiconProfile());

        _evaluationExtractor = new EvaluationExtractor(
                "src/test/resources/module/disambiguation/evaluation/evaluationExtractor/test1.xml",
                termithIndex);

        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("ce PRO:DEM");
        entry1.add("article NOM");
        entry1.add("présenter VER:pres");
        entry1.add("un DET:ART");
        entry1.add("étude NOM");
        entry1.add("comparer VER:pper");
        entry1.add("du PRP:det");
        entry1.add("donnée NOM");
        entry1.add("archéo-ichtyofauniques ADJ");
        entry1.add("livrer VER:pper");
        entry1.add("par PRP");
        entry1.add("deux NUM");
        entry1.add("du PRP:det");
        entry1.add("site NOM");
        entry1.add("de PRP");
        entry1.add("le DET:ART");
        entry1.add("âge NOM");
        expectedMap.put("entry-13471_noDM",new EvaluationProfile(entry1));
        _observedMap = termithIndex.getEvaluationLexicon().get("test1");
    }

    @Test
    public void extractEvaluationLexicon() throws Exception {
        _evaluationExtractor.execute();
        expectedMap.forEach(
                (key,value) -> {
                    Multiset observed = _observedMap.get(key).getLexicalTable();
                    value.forEach(
                            el -> {
                                int count = observed.count(el);
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.count(el),observed.count(el)
                                );
                            }
                    );
                }
        );
    }

}
