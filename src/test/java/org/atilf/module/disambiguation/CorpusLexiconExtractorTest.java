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

        _expectedCorpus.addOccurrence("ce PRO:DEM");
        _expectedCorpus.addOccurrence("article NOM");
        _expectedCorpus.addOccurrence("présenter VER:pres");
        _expectedCorpus.addOccurrence("un DET:ART");
        _expectedCorpus.addOccurrence("étude NOM");
        _expectedCorpus.addOccurrence("comparer VER:pper");
        _expectedCorpus.addOccurrence("du PRP:det");
        _expectedCorpus.addOccurrence("donnée NOM");
        _expectedCorpus.addOccurrence("archéo-ichtyofauniques ADJ");
        _expectedCorpus.addOccurrence("livrer VER:pper");
        _expectedCorpus.addOccurrence("par PRP");
        _expectedCorpus.addOccurrence("deux NUM");
        _expectedCorpus.addOccurrence("site NOM");
        _expectedCorpus.addOccurrence("de PRP");
        _expectedCorpus.addOccurrence("le DET:ART");
        _expectedCorpus.addOccurrence("âge NOM");
        _expectedCorpus.addOccurrence("du PRP:det");

    }

    @Test
    public void execute() throws Exception {
        _corpus1.execute();
        Assert.assertEquals("this two multisets must be equals", _expectedCorpus.getLexicalTable().toString(),
                _observedCorpus.getLexicalTable().toString());
    }

}