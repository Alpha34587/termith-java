package org.atilf.models.disambiguation;

import org.apache.commons.io.FileUtils;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
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
    private static File _csvContext;
    private static File _csvCorpus;

    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {
        new TermithIndex.Builder().export(_temporaryFolder.getRoot().getAbsolutePath()).build();
        _csvContext = new File("src/test/resources/models.disambiguation/test-context.csv");
        _csvCorpus = new File("src/test/resources/models.disambiguation/test-corpus.csv");
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
    public void writeFile() throws Exception {
        Assert.assertTrue("these two context files must be equals",
                FileUtils.contentEquals(_csvContext,_rLexicon.getCsvPath().toFile()));
        Assert.assertTrue("these two corpus files must be equals",
                FileUtils.contentEquals(_csvCorpus,_rCorpus.getCsvPath().toFile()));
    }

}