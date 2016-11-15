package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class CorpusLexiconExtractorTest {
    private CorpusLexiconExtractor _corpus1;
    private CorpusLexicon _observedCorpus = new CorpusLexicon(new HashMap<>(), new HashMap<>());
    private CorpusLexicon _expectedCorpus = new CorpusLexicon(new HashMap<>(), new HashMap<>());


    @Before
    public void setUp() throws Exception {
        _corpus1 = new CorpusLexiconExtractor(
                "src/test/resources/corpus/disambiguation/transform-tei/test1.xml",
                _observedCorpus
        );

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
        _expectedCorpus.addEntry("site NOM");
        _expectedCorpus.addEntry("de PRP");
        _expectedCorpus.addEntry("le DET:ART");
        _expectedCorpus.addEntry("âge NOM");
        _expectedCorpus.addEntry("du PRP:det");

    }

    @Test
    public void execute() throws Exception {
        _corpus1.execute();
        Assert.assertEquals("this two multisets must be equals", _expectedCorpus.getMultisetLexicon().toString(),
                _observedCorpus.getMultisetLexicon().toString());
    }

}