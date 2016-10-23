package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.GlobalLexic;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class GlobalCorpusTest {
    GlobalCorpus corpus1;
    GlobalCorpus corpus2;
    GlobalLexic observedCorpus = new GlobalLexic(new HashMap<>(), new HashMap<>());
    GlobalLexic expectedCorpus = new GlobalLexic(new HashMap<>(), new HashMap<>());


    @Before
    public void setUp() throws Exception {
        corpus1 = new GlobalCorpus("src/test/resources/corpus/tei/test3.xml",observedCorpus);
        corpus2 = new GlobalCorpus("src/test/resources/corpus/tei/test4.xml",observedCorpus);

        expectedCorpus.addEntry("ce PRO:DEM");
        expectedCorpus.addEntry("article NOM");
        expectedCorpus.addEntry("présenter VER:pres");
        expectedCorpus.addEntry("un DET:ART");
        expectedCorpus.addEntry("étude NOM");
        expectedCorpus.addEntry("comparer VER:pper");
        expectedCorpus.addEntry("du PRP:det");
        expectedCorpus.addEntry("donnée NOM");
        expectedCorpus.addEntry("archéo-ichtyofauniques ADJ");
        expectedCorpus.addEntry("livrer VER:pper");
        expectedCorpus.addEntry("par PRP");
        expectedCorpus.addEntry("deux NUM");
        expectedCorpus.addEntry("ce PRO:DEM");
        expectedCorpus.addEntry("article NOM");
        expectedCorpus.addEntry("présenter VER:pres");
        expectedCorpus.addEntry("un DET:ART");
        expectedCorpus.addEntry("étude NOM");
        expectedCorpus.addEntry("comparer VER:pper");
        expectedCorpus.addEntry("du PRP:det");
        expectedCorpus.addEntry("donnée NOM");
        expectedCorpus.addEntry("archéo-ichtyofauniques ADJ");
        expectedCorpus.addEntry("rouge VER:pper");
        expectedCorpus.addEntry("chien PRP");
        expectedCorpus.addEntry("deux NUM");
    }

    @Test
    public void execute() throws Exception {
        corpus1.execute();
        corpus2.execute();
        Assert.assertEquals("this two multisets must be equals",expectedCorpus.getGlobalLexic().toString(),
                observedCorpus.getGlobalLexic().toString());
    }

}