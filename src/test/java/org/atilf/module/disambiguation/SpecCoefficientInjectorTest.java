package org.atilf.module.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.module.disambiguation.lexiconprofile.SpecCoefficientInjector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
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


    @BeforeClass
    public static void setUp() throws Exception {

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
        float[] observedResult = new SpecCoefficientInjector(
                new LexiconProfile(_lexiconMultiset),
                _rLexicon,
                _corpusLexicon)
                .computeSpecCoefficient();
        float[] expectedResult = new float[]
                {
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f,
                0.1383f
        };
        Assert.assertArrayEquals("this two arrays must be equals",expectedResult,observedResult,0);
    }


}