package org.atilf.models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.module.disambiguisation.GlobalCorpus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static eu.project.ttc.utils.TermSuiteConstants.ADJ;
import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexicTest {

    GlobalLexic corpus;
    RLexic rLexic;
    String rName;
    String rOcc;
    @Before
    public void setUp() throws Exception {
        rName = "c(\"donnée NOM\"," +
                "\"ce PRO:DEM\"," +
                "\"présenter VER:pres\"," +
                "\"archéo-ichtyofauniques ADJ\"," +
                "\"livrer VER:pper\"," +
                "\"du PRP:det\"," +
                "\"un DET:ART\"," +
                "\"article NOM\"," +
                "\"comparer VER:pper\")";
        rOcc = "c(1,1,1,1,1,1,2,1,1)";

        corpus = new GlobalLexic(new HashMap<>(),new HashMap<>());
        corpus.addEntry("ce PRO:DEM");
        corpus.addEntry("article NOM");
        corpus.addEntry("présenter VER:pres");
        corpus.addEntry("un DET:ART");
        corpus.addEntry("un DET:ART");
        corpus.addEntry("comparer VER:pper");
        corpus.addEntry("du PRP:det");
        corpus.addEntry("donnée NOM");
        corpus.addEntry("archéo-ichtyofauniques ADJ");
        corpus.addEntry("livrer VER:pper");
        rLexic = new RLexic(corpus);
    }

    @Test
    public void getrName() throws Exception {
        Assert.assertEquals("rName variable must be equals ",rName,rLexic.getrName().toString());
    }

    @Test
    public void getrOcc() throws Exception {
        Assert.assertEquals("rOcc variable must be equals ",rOcc,rLexic.getrOcc().toString());

    }

}