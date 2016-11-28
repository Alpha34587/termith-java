package org.atilf.models;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.RLexicon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexiconTest {

    private CorpusLexicon _corpus;
    private RLexicon _rLexicon;
    private String _rName;
    private String _rOcc;
    //TODO make subLexicFormat method
    @Before
    public void setUp() throws Exception {
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

        _corpus = new CorpusLexicon(new HashMap<>(),new HashMap<>());
        _corpus.addOccurrence("ce PRO:DEM");
        _corpus.addOccurrence("article NOM");
        _corpus.addOccurrence("présenter VER:pres");
        _corpus.addOccurrence("un DET:ART");
        _corpus.addOccurrence("un DET:ART");
        _corpus.addOccurrence("comparer VER:pper");
        _corpus.addOccurrence("du PRP:det");
        _corpus.addOccurrence("donnée NOM");
        _corpus.addOccurrence("archéo-ichtyofauniques ADJ");
        _corpus.addOccurrence("livrer VER:pper");
        _rLexicon = new RLexicon(_corpus);
    }

    @Test
    public void getRName() throws Exception {
        Assert.assertEquals("_rName variable must be equals ", _rName, _rLexicon.getRName().toString());
    }

    @Test
    public void getROcc() throws Exception {
        Assert.assertEquals("_rOcc variable must be equals ", _rOcc, _rLexicon.getROcc().toString());

    }

}