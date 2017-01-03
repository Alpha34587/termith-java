package org.atilf.module.disambiguation;

import com.google.common.io.Files;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.atilf.models.termith.TermithIndex;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToCsvTest {
    private static File _expectedFile = new File("src/test/resources/corpus/disambiguation/score/test1.csv");
    private static Map<String,ScoreTerm> _scoreTerm = new HashMap<>();
    private static TotalTermScore _totalScoreTerm = new TotalTermScore();
    private static ExportScoreToJson _exportScoreToJson = new ExportScoreToJson(_scoreTerm,_totalScoreTerm);

    @Rule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public void setUp() throws IOException {
        ContextWord word1 = new ContextWord("t1");
        word1.setPosLemma("NOM");
        ScoreTerm scoreTerm1 = new ScoreTerm();
        scoreTerm1.setFlexionsWords("test1-id");
        scoreTerm1.addTermWords(Collections.singletonList(word1));
        scoreTerm1.setRecall(0.5f);
        scoreTerm1.setPrecision(0.4f);
        scoreTerm1.setF1Score(0.3f);
        scoreTerm1.setTerminologyTrend(0.6f);
        scoreTerm1.setAmbiguityRate(0.4f);

        ContextWord word2 = new ContextWord("t1");
        word2.setPosLemma("ADJ");
        ScoreTerm scoreTerm2 = new ScoreTerm();
        scoreTerm2.setFlexionsWords("test2-id");
        scoreTerm2.addTermWords(Collections.singletonList(word2));
        scoreTerm2.setRecall(0.6f);
        scoreTerm2.setPrecision(0.2f);
        scoreTerm2.setF1Score(0.4f);
        scoreTerm2.setTerminologyTrend(0.6f);
        scoreTerm2.setAmbiguityRate(0.4f);

        _scoreTerm.put("test1", scoreTerm1);
        _scoreTerm.put("test2", scoreTerm2);

        _totalScoreTerm.setRecall(0.5f);
        _totalScoreTerm.setRecall(0.4f);
        _totalScoreTerm.setF1score(0.3f);
        new TermithIndex.Builder().export(temporaryFolder.getRoot().getAbsolutePath());
    }
    @Test
    public void execute() throws Exception {
        _exportScoreToJson.execute();
        Assert.assertTrue(Files.equal(_expectedFile,
                new File(TermithIndex.getOutputPath().toString() + "/termith-score.csv")));
    }

}