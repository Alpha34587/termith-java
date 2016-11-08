package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.GlobalLexicon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class CorpusLexicTest {
    private LexiconExtractor _corpus1;
    private LexiconExtractor _corpus2;
    private GlobalLexicon _observedCorpus = new GlobalLexicon(new HashMap<>(), new HashMap<>());
    private GlobalLexicon _expectedCorpus = new GlobalLexicon(new HashMap<>(), new HashMap<>());


    @Before
    public void setUp() throws Exception {
        _corpus1 = new LexiconExtractor("src/test/resources/corpus/tei/test3.xml", _observedCorpus);
        _corpus2 = new LexiconExtractor("src/test/resources/corpus/tei/test4.xml", _observedCorpus);

        _expectedCorpus.addEntry("ce PRO:DEM");
        _expectedCorpus.addEntry("article NOM");
        _expectedCorpus.addEntry("présenter VER:pres");
        _expectedCorpus.addEntry("un DET:ART");
        _expectedCorpus.addEntry("étude NOM");
        _expectedCorpus.addEntry("comparer VER:pper");
        _expectedCorpus.addEntry("du PRP:det");
        _expectedCorpus.addEntry("donnée NOM");
        _expectedCorpus.addEntry("archéo-ichtyofauniques ADJ");
        _expectedCorpus.addEntry("livrer VER:pper");
        _expectedCorpus.addEntry("par PRP");
        _expectedCorpus.addEntry("deux NUM");
        _expectedCorpus.addEntry("ce PRO:DEM");
        _expectedCorpus.addEntry("article NOM");
        _expectedCorpus.addEntry("présenter VER:pres");
        _expectedCorpus.addEntry("un DET:ART");
        _expectedCorpus.addEntry("étude NOM");
        _expectedCorpus.addEntry("comparer VER:pper");
        _expectedCorpus.addEntry("du PRP:det");
        _expectedCorpus.addEntry("donnée NOM");
        _expectedCorpus.addEntry("archéo-ichtyofauniques ADJ");
        _expectedCorpus.addEntry("rouge VER:pper");
        _expectedCorpus.addEntry("chien PRP");
        _expectedCorpus.addEntry("deux NUM");
    }

    @Test
    public void execute() throws Exception {
        _corpus1.execute();
        _corpus2.execute();
        Assert.assertEquals("this two multisets must be equals", _expectedCorpus.getMultisetLexicon().toString(),
                _observedCorpus.getMultisetLexicon().toString());
    }

}