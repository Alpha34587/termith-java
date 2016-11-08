package org.atilf.models;

import org.atilf.models.disambiguation.GlobalLexicon;
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

    private GlobalLexicon _corpus;
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

        _corpus = new GlobalLexicon(new HashMap<>(),new HashMap<>());
        _corpus.addEntry("ce PRO:DEM");
        _corpus.addEntry("article NOM");
        _corpus.addEntry("présenter VER:pres");
        _corpus.addEntry("un DET:ART");
        _corpus.addEntry("un DET:ART");
        _corpus.addEntry("comparer VER:pper");
        _corpus.addEntry("du PRP:det");
        _corpus.addEntry("donnée NOM");
        _corpus.addEntry("archéo-ichtyofauniques ADJ");
        _corpus.addEntry("livrer VER:pper");
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