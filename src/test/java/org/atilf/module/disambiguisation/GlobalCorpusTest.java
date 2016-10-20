package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class GlobalCorpusTest {
    GlobalCorpus corpus1;
    GlobalCorpus corpus2;
    Multiset observedCorpus = HashMultiset.create();
    Multiset expectedCorpus = HashMultiset.create();


    @Before
    public void setUp() throws Exception {
        corpus1 = new GlobalCorpus("src/test/resources/corpus/tei/test3.xml",observedCorpus);
        corpus2 = new GlobalCorpus("src/test/resources/corpus/tei/test4.xml",observedCorpus);

        expectedCorpus.add("ce PRO:DEM");
        expectedCorpus.add("article NOM");
        expectedCorpus.add("présenter VER:pres");
        expectedCorpus.add("un DET:ART");
        expectedCorpus.add("étude NOM");
        expectedCorpus.add("comparer VER:pper");
        expectedCorpus.add("du PRP:det");
        expectedCorpus.add("donnée NOM");
        expectedCorpus.add("archéo-ichtyofauniques ADJ");
        expectedCorpus.add("livrer VER:pper");
        expectedCorpus.add("par PRP");
        expectedCorpus.add("deux NUM");
        expectedCorpus.add("ce PRO:DEM");
        expectedCorpus.add("article NOM");
        expectedCorpus.add("présenter VER:pres");
        expectedCorpus.add("un DET:ART");
        expectedCorpus.add("étude NOM");
        expectedCorpus.add("comparer VER:pper");
        expectedCorpus.add("du PRP:det");
        expectedCorpus.add("donnée NOM");
        expectedCorpus.add("archéo-ichtyofauniques ADJ");
        expectedCorpus.add("rouge VER:pper");
        expectedCorpus.add("chien PRP");
        expectedCorpus.add("deux NUM");
    }

    @Test
    public void execute() throws Exception {
        corpus1.execute();
        corpus2.execute();
        Assert.assertEquals("this two multisets must be equals",expectedCorpus.toString(),observedCorpus.toString());
    }

}