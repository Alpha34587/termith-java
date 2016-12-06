package org.atilf.models.disambiguation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexiconTest {

    private static RLexicon _rCorpus;
    private static RLexicon _rLexicon;
    private static String _rName;
    private static String _rOcc;
    private static String _rLexicOcc;
    private static String _rLexicName;

    @BeforeClass
    public static void setUp() throws Exception {
        _rLexicName = "c(\"0\"," +
                "\"2\"," +
                "\"3\"," +
                "\"1\")";
        _rLexicOcc = "c(1,1,2,1)";

        _rName = "c(\"6\"," +
                "\"0\"," +
                "\"2\"," +
                "\"7\"," +
                "\"8\"," +
                "\"5\"," +
                "\"3\"," +
                "\"1\"," +
                "\"4\")";
        _rOcc = "c(1,1,1,1,1,1,2,1,1)";
        LexiconProfile lexiconProfile = new LexiconProfile();
        lexiconProfile.addOccurrence("ce PRO:DEM");
        lexiconProfile.addOccurrence("article NOM");
        lexiconProfile.addOccurrence("présenter VER:pres");
        lexiconProfile.addOccurrence("un DET:ART");
        lexiconProfile.addOccurrence("un DET:ART");
        CorpusLexicon corpus = new CorpusLexicon(new HashMap<>(), new HashMap<>());
        corpus.addOccurrence("ce PRO:DEM");
        corpus.addOccurrence("article NOM");
        corpus.addOccurrence("présenter VER:pres");
        corpus.addOccurrence("un DET:ART");
        corpus.addOccurrence("un DET:ART");
        corpus.addOccurrence("comparer VER:pper");
        corpus.addOccurrence("du PRP:det");
        corpus.addOccurrence("donnée NOM");
        corpus.addOccurrence("archéo-ichtyofauniques ADJ");
        corpus.addOccurrence("livrer VER:pper");
        _rLexicon = new RLexicon(lexiconProfile,corpus);
        _rCorpus = new RLexicon(corpus);
    }

    @Test
    public void getCorpusRName() throws Exception {
        Assert.assertEquals("_rName variable must be equals ", _rName, _rCorpus.getRName().toString());
    }

    @Test
    public void getCorpusROcc() throws Exception {
        Assert.assertEquals("_rOcc variable must be equals ", _rOcc, _rCorpus.getROcc().toString());

    }

    @Test
    public void getLexiconRName() throws Exception {
        Assert.assertEquals("_rName variable must be equals ", _rLexicName, _rLexicon.getRName().toString());
    }

    @Test
    public void getLexiconROcc() throws Exception {
        Assert.assertEquals("_rOcc variable must be equals ", _rLexicOcc, _rLexicon.getROcc().toString());

    }

}