package delegate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.delegate.disambiguation.lexiconProfile.LexiconProfileDelegate;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LexiconProfileDelegateTest extends IntegrationTasks {

    private LexiconProfileDelegate _l = new LexiconProfileDelegate();
    private Multiset<String> _lexiconMultiset = HashMultiset.create();
    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        /*
        context initialization
         */
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _l.setOutputPath(temporaryFolder.getRoot().toPath());
        _lexiconMultiset.add("Cet");
        _lexiconMultiset.add("article");
        _lexiconMultiset.add("présente");
        _lexiconMultiset.add("une");
        _lexiconMultiset.add("étude");
        _lexiconMultiset.add("comparée");
        _lexiconMultiset.add("des");
        _lexiconMultiset.add("données");
        _lexiconMultiset.add("livrées");
        _lexiconMultiset.add("par");
        _lexiconMultiset.add("deux");
        _lexiconMultiset.add("sites");
        _lexiconMultiset.add("de");
        _lexiconMultiset.add("l'");
        _lexiconMultiset.add("âge");
        _lexiconMultiset.add("du");
        _termithIndex.getContextLexicon().put("test_lexOn",new LexiconProfile(_lexiconMultiset));
        _termithIndex.getContextLexicon().put("test_lexOff",new LexiconProfile(_lexiconMultiset));

        /*
        corpus lexicon initialization
         */
        _termithIndex.getCorpusLexicon().addOccurrence("Cet");
        _termithIndex.getCorpusLexicon().addOccurrence("article");
        _termithIndex.getCorpusLexicon().addOccurrence("présente");
        _termithIndex.getCorpusLexicon().addOccurrence("une");
        _termithIndex.getCorpusLexicon().addOccurrence("étude");
        _termithIndex.getCorpusLexicon().addOccurrence("comparée");
        _termithIndex.getCorpusLexicon().addOccurrence("des");
        _termithIndex.getCorpusLexicon().addOccurrence("données");
        _termithIndex.getCorpusLexicon().addOccurrence("archéo-ichtyofauniques");
        _termithIndex.getCorpusLexicon().addOccurrence("livrées");
        _termithIndex.getCorpusLexicon().addOccurrence("par");
        _termithIndex.getCorpusLexicon().addOccurrence("deux");
        _termithIndex.getCorpusLexicon().addOccurrence("sites");
        _termithIndex.getCorpusLexicon().addOccurrence("de");
        _termithIndex.getCorpusLexicon().addOccurrence("l'");
        _termithIndex.getCorpusLexicon().addOccurrence("âge");
        _termithIndex.getCorpusLexicon().addOccurrence("du");
        _termithIndex.getCorpusLexicon().addOccurrence("Cet");
        _termithIndex.getCorpusLexicon().addOccurrence("article");
        _termithIndex.getCorpusLexicon().addOccurrence("présente");
        _termithIndex.getCorpusLexicon().addOccurrence("une");
        _termithIndex.getCorpusLexicon().addOccurrence("étude");
        _termithIndex.getCorpusLexicon().addOccurrence("comparée");
        _termithIndex.getCorpusLexicon().addOccurrence("des");
        _termithIndex.getCorpusLexicon().addOccurrence("données");
        _termithIndex.getCorpusLexicon().addOccurrence("archéo-ichtyofauniques");
        _termithIndex.getCorpusLexicon().addOccurrence("livrées");
        _termithIndex.getCorpusLexicon().addOccurrence("par");
        _termithIndex.getCorpusLexicon().addOccurrence("deux");
        _termithIndex.getCorpusLexicon().addOccurrence("sites");
        _termithIndex.getCorpusLexicon().addOccurrence("de");
        _termithIndex.getCorpusLexicon().addOccurrence("l'");
        _termithIndex.getCorpusLexicon().addOccurrence("âge");
        _termithIndex.getCorpusLexicon().addOccurrence("du");
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_l);
        List<Float> expectedResult = new LinkedList<>();
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        expectedResult.add(0.1383f);
        Collection<Float> coefMap = _termithIndex.getContextLexicon().get("test_lexOn").getSpecCoefficientMap().values();
        Assert.assertEquals("this two arrays must be equals",expectedResult.toString(),coefMap.toString());
    }}
