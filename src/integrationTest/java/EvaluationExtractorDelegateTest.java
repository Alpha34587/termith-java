import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.delegate.disambiguation.evaluation.EvaluationExtractorDelegate;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.disambiguation.evaluation.EvaluationExtractor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EvaluationExtractorDelegateTest extends IntegrationTasks {

    private static EvaluationExtractorDelegate _e = new EvaluationExtractorDelegate();
    private static Multiset<String> _expectedLexicon = HashMultiset.create();

    @Before
    public void setUp() throws IOException {
        _termithIndex.getContextLexicon().put("entry-13471_lexOn",new LexiconProfile());
        _termithIndex.getContextLexicon().put("entry-13471_lexOff",new LexiconProfile());
        _termithIndex.getEvaluationTransformedFiles().put(
                "file1",
                Paths.get("src/integrationTest/resources/evaluationExtractor/evalExtract1.xml")
        );
        _termithIndex.getEvaluationTransformedFiles().put(
                "file2",
                Paths.get("src/integrationTest/resources/evaluationExtractor/evalExtract2.xml")
        );
        _termithIndex.getEvaluationTransformedFiles().put(
                "file3",
                Paths.get("src/integrationTest/resources/evaluationExtractor/evalExtract3.xml")
        );
        _e.setTermithIndex(_termithIndex);

        _expectedLexicon.add("ce PRO:DEM");
        _expectedLexicon.add("article NOM");
        _expectedLexicon.add("présenter VER:pres");
        _expectedLexicon.add("un DET:ART");
        _expectedLexicon.add("étude NOM");
        _expectedLexicon.add("comparer VER:pper");
        _expectedLexicon.add("du PRP:det");
        _expectedLexicon.add("donnée NOM");
        _expectedLexicon.add("archéo-ichtyofauniques ADJ");
        _expectedLexicon.add("livrer VER:pper");
        _expectedLexicon.add("par PRP");
        _expectedLexicon.add("deux NUM");
        _expectedLexicon.add("du PRP:det");
        _expectedLexicon.add("site NOM");
        _expectedLexicon.add("de PRP");
        _expectedLexicon.add("le DET:ART");
        _expectedLexicon.add("âge NOM");
    }

    @Test
    public void extractEvaluationLexicon() throws Exception {
        executeTasksTest(_e);
        String eval1 = _termithIndex.getEvaluationLexicon().get("evalExtract1").get("entry-13471_noDM").getLexicalTable().toString();
        String eval2 = _termithIndex.getEvaluationLexicon().get("evalExtract2").get("entry-13471_noDM").getLexicalTable().toString();
        String eval3 = _termithIndex.getEvaluationLexicon().get("evalExtract3").get("entry-13471_noDM").getLexicalTable().toString();
        Assert.assertEquals("these Lexicon must be equals", _expectedLexicon.toString(),eval1);
        Assert.assertEquals("these Lexicon must be equals", _expectedLexicon.toString(),eval2);
        Assert.assertEquals("these Lexicon must be equals", _expectedLexicon.toString(),eval3);
    }
}
