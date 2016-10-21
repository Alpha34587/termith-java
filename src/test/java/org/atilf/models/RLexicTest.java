package org.atilf.models;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.module.disambiguisation.GlobalCorpus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static eu.project.ttc.utils.TermSuiteConstants.ADJ;
import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class RLexicTest {

    Multiset<String> corpus;
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

        corpus = HashMultiset.create();
        corpus.add("ce PRO:DEM");
        corpus.add("article NOM");
        corpus.add("présenter VER:pres");
        corpus.add("un DET:ART");
        corpus.add("un DET:ART");
        corpus.add("comparer VER:pper");
        corpus.add("du PRP:det");
        corpus.add("donnée NOM");
        corpus.add("archéo-ichtyofauniques ADJ");
        corpus.add("livrer VER:pper");
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