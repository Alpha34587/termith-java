package org.atilf.module.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.termith.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.atilf.models.disambiguation.AnnotationResources.NO_DM;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class EvaluationExtractorTest {
    private EvaluationExtractor _evaluationExtractor;
    private Deque<String> _expectedTarget = new ArrayDeque<>();
    private Deque<String> _expectedCorresp = new ArrayDeque<>();
    private Deque<String> _expectedLexAna = new ArrayDeque<>();
    private Map<String,EvaluationProfile> expectedMap = new HashMap<>();
    private Map<String,EvaluationProfile> _observedMap;
    @Before
    public void setUp() throws IOException {
        TermithIndex termithIndex = new TermithIndex.Builder().build();
        termithIndex.getContextLexicon().put("entry-13471_lexOn",new LexiconProfile());

        _evaluationExtractor = new EvaluationExtractor(
                "src/test/resources/corpus/disambiguation/transform-tei/test1.xml",
                termithIndex);

        _expectedTarget.add("#t13 #t14 #t15 #t16");
        _expectedCorresp.add("#entry-13471");
        _expectedLexAna.add(NO_DM.getValue());

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

        expectedMap.put("entry-13471_noDM",new EvaluationProfile(entry1));
        _evaluationExtractor.execute();
        _observedMap = termithIndex.getEvaluationLexicon().get("test1");
    }

    @Test
    public void extractTerms() throws Exception {
        _evaluationExtractor.getTerms().forEach(
                el -> Assert.assertEquals("target must be equals", _expectedTarget.poll(),el.getTarget())
        );
        _evaluationExtractor.getTerms().forEach(
                el -> Assert.assertEquals("terms id must be equals", _expectedCorresp.poll(),el.getCorresp())
        );
        _evaluationExtractor.getTerms().forEach(
                el -> Assert.assertEquals("ana id must be equals", _expectedLexAna.poll(),el.getAna())
        );

    }

    @Test
    public void extractLexiconProfileSimple() throws Exception {
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