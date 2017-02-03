package org.atilf.module.disambiguation.lexiconProfile;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RConnectionPool;
import org.atilf.models.disambiguation.RLexicon;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rosuda.REngine.Rserve.RConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoefficientInjectorTest {

    private static CorpusLexicon _corpusLexicon = new CorpusLexicon(new HashMap<>(),new HashMap<>());
    private static Multiset<String> _lexiconMultiset = HashMultiset.create();
    private static RLexicon _rLexicon;
    private static Map<String, LexiconProfile> _contextLexicon = new HashMap<>();
    private static Map<String,float[]> _specificities = new HashMap<>();

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {
    new TermithIndex.Builder().export(temporaryFolder.getRoot().toString()).build();
    /*
    context initialization
     */
    _lexiconMultiset.add("Cet");
    _lexiconMultiset.add("article");
    _lexiconMultiset.add("présente");
    _lexiconMultiset.add("une");
    _lexiconMultiset.add("étude");
    _lexiconMultiset.add("comparée");
    _lexiconMultiset.add("des");
    _lexiconMultiset.add("données");
    _lexiconMultiset.add("livrées");
    _lexiconMultiset.add("par");
    _lexiconMultiset.add("deux");
    _lexiconMultiset.add("sites");
    _lexiconMultiset.add("de");
    _lexiconMultiset.add("l'");
    _lexiconMultiset.add("âge");
    _lexiconMultiset.add("du");
    _contextLexicon.put("archéo-ichtyofauniques",new LexiconProfile(_lexiconMultiset));

    /*
    corpus lexicon initialization
     */
    _corpusLexicon.addOccurrence("Cet");
    _corpusLexicon.addOccurrence("article");
    _corpusLexicon.addOccurrence("présente");
    _corpusLexicon.addOccurrence("une");
    _corpusLexicon.addOccurrence("étude");
    _corpusLexicon.addOccurrence("comparée");
    _corpusLexicon.addOccurrence("des");
    _corpusLexicon.addOccurrence("données");
    _corpusLexicon.addOccurrence("archéo-ichtyofauniques");
    _corpusLexicon.addOccurrence("livrées");
    _corpusLexicon.addOccurrence("par");
    _corpusLexicon.addOccurrence("deux");
    _corpusLexicon.addOccurrence("sites");
    _corpusLexicon.addOccurrence("de");
    _corpusLexicon.addOccurrence("l'");
    _corpusLexicon.addOccurrence("âge");
    _corpusLexicon.addOccurrence("du");
    _corpusLexicon.addOccurrence("Cet");
    _corpusLexicon.addOccurrence("article");
    _corpusLexicon.addOccurrence("présente");
    _corpusLexicon.addOccurrence("une");
    _corpusLexicon.addOccurrence("étude");
    _corpusLexicon.addOccurrence("comparée");
    _corpusLexicon.addOccurrence("des");
    _corpusLexicon.addOccurrence("données");
    _corpusLexicon.addOccurrence("archéo-ichtyofauniques");
    _corpusLexicon.addOccurrence("livrées");
    _corpusLexicon.addOccurrence("par");
    _corpusLexicon.addOccurrence("deux");
    _corpusLexicon.addOccurrence("sites");
    _corpusLexicon.addOccurrence("de");
    _corpusLexicon.addOccurrence("l'");
    _corpusLexicon.addOccurrence("âge");
    _corpusLexicon.addOccurrence("du");

    _rLexicon = new RLexicon(_corpusLexicon);
    }

    @Test
    public void computeSpecCoeff() throws Exception {
        RConnectionPool rConnectionPool = new RConnectionPool(1,_rLexicon);
        RConnection rConnection = rConnectionPool.getRConnection(Thread.currentThread());
        List<Float> observedResult = new SpecCoefficientInjector(
                new LexiconProfile(_lexiconMultiset),
                _rLexicon,
                _corpusLexicon,rConnection)
                .computeSpecCoefficient();
        List<Float> expectedResult = new LinkedList<>();
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);

        Assert.assertEquals("this two arrays must be equals",expectedResult,observedResult);
    }


}