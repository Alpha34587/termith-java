package org.atilf.module.disambiguation.evaluationScore;

import org.apache.commons.io.FileUtils;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToJsonTest {
    private static File _expectedFile = new File("src/test/resources/module/disambiguation/" +
            "evaluationScore/exportToJson/test1.json");
    private static Map<String,ScoreTerm> _scoreTerm = new HashMap<>();
    private static TotalTermScore _totalScoreTerm = new TotalTermScore();
    private static ExportScoreToJson _exportScoreToJson;

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        ContextWord word1 = new ContextWord("t1");
        word1.setPosLemma("test1 NOM");
        ScoreTerm scoreTerm1 = new ScoreTerm();
        scoreTerm1.setFlexingWords("test1");
        scoreTerm1.addTermWords(Collections.singletonList(word1));
        scoreTerm1.setRecall(0.5f);
        scoreTerm1.setPrecision(0.4f);
        scoreTerm1.setF1Score(0.3f);
        scoreTerm1.setTerminologyTrend(0.6f);
        scoreTerm1.setAmbiguityRate(0.4f);

        List<ContextWord> contextWords = new ArrayList<>();
        ContextWord word2 = new ContextWord("t1");
        word2.setPosLemma("test2 ADJ");
        contextWords.add(word1);
        contextWords.add(word2);
        ScoreTerm scoreTerm2 = new ScoreTerm();
        scoreTerm2.setFlexingWords("test2");
        scoreTerm2.addTermWords(contextWords);
        scoreTerm2.setRecall(0.6f);
        scoreTerm2.setPrecision(0.2f);
        scoreTerm2.setF1Score(0.4f);
        scoreTerm2.setTerminologyTrend(0.6f);
        scoreTerm2.setAmbiguityRate(0.4f);

        _scoreTerm.put("test1-id", scoreTerm1);
        _scoreTerm.put("test2-id", scoreTerm2);

        _totalScoreTerm.setRecall(0.5f);
        _totalScoreTerm.setPrecision(0.4f);
        _totalScoreTerm.setF1score(0.3f);
        _exportScoreToJson =
                new ExportScoreToJson(_scoreTerm,_totalScoreTerm, temporaryFolder.getRoot().toPath(),false);
    }
    @Test
    public void execute() throws Exception {
        _exportScoreToJson.execute();
        Assert.assertEquals(
        FileUtils.readFileToString(
                _expectedFile, "utf-8"),
                FileUtils.readFileToString(
                        new File(temporaryFolder.getRoot().toString() + "/termith-score.json"), "utf-8")
        );
    }
}
