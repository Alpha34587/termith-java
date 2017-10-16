import org.atilf.delegate.disambiguation.contextLexicon.ContextExtractorDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class ContextExtractorDelegateTest extends  IntegrationTasks {
    private ContextExtractorDelegate _c = new ContextExtractorDelegate();
    private String _lexicalEntryCorpus =
            "{0=article NOM, " +
                    "1=présenter VER:pres, " +
                    "2=étude NOM, " +
                    "3=comparer VER:pper, " +
                    "4=donnée NOM, " +
                    "5=archéo-ichtyofauniques ADJ, " +
                    "6=livrer VER:pper, " +
                    "7=site NOM, " +
                    "8=âge NOM, " +
                    "9=bronze NOM, " +
                    "10=pêche NOM, " +
                    "11=être VER:pres, " +
                    "12=limiter VER:pper, " +
                    "13=espèce NOM, " +
                    "14=commun ADJ, " +
                    "15=côtier ADJ, 16=enfouissement NOM, 17=type NOM, 18=rejet NOM, 19=précéder VER:ppre}";

    private String _MultisetCorpus = "[présenter VER:pres x 9, précéder VER:ppre x 3, archéo-ichtyofauniques ADJ x 9, " +
            "être VER:pres x 3, rejet NOM x 3, limiter VER:pper x 3, âge NOM x 6, " +
            "article NOM x 9, donnée NOM x 9, pêche NOM x 6, bronze NOM x 3, livrer VER:pper x 9, " +
            "commun ADJ x 3, site NOM x 9, enfouissement NOM x 3, étude NOM x 9, " +
            "espèce NOM x 3, côtier ADJ x 3, type NOM x 3, comparer VER:pper x 9]";

    private String _multisetLexicon = "[entry-5750_lexOff, entry-575_lexOff, entry-450_lexOff, " +
            "entry-13471_lexOff, entry-35_lexOff, entry-7263_lexOff]";

    String _entry5750 = "[précéder VER:ppre x 3, rejet NOM x 3, enfouissement NOM x 3, type NOM x 3]";
    String _entry575 = "[donnée NOM x 3, présenter VER:pres x 3, archéo-ichtyofauniques ADJ x 3, " +
            "livrer VER:pper x 3, étude NOM x 3, article NOM x 3, comparer VER:pper x 3]";
    String _entry450 = "[pêche NOM x 3, être VER:pres x 3]";
    String _entry13471 = "[donnée NOM x 6, présenter VER:pres x 6, archéo-ichtyofauniques ADJ x 6, livrer VER:pper x 6, " +
            "site NOM x 6, étude NOM x 6, âge NOM x 6, article NOM x 6, comparer VER:pper x 6]";
    @Before
    public void setUp(){
        _termithIndex.getLearningTransformedFile().put(
                "1",
                Paths.get("src/integrationTest/resources/contextExtractor1.xml"));
        _termithIndex.getLearningTransformedFile().put(
                "2",
                Paths.get("src/integrationTest/resources/contextExtractor2.xml"));
        _termithIndex.getLearningTransformedFile().put(
                "3",
                Paths.get("src/integrationTest/resources/contextExtractor3.xml"));
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_c);
        Assert.assertEquals("the corpus must contains this lexical entry :",
                _lexicalEntryCorpus,
                _termithIndex.getCorpusLexicon().getLexicalEntry().toString()
        );

        Assert.assertEquals("this corpus must be contains this occurrences :",
                _termithIndex.getCorpusLexicon().getLexicalTable().toString(),
                _MultisetCorpus);

        Assert.assertEquals("the lexicon must be contains this entry",
                _termithIndex.getContextLexicon().keySet().toString(),
                _multisetLexicon);

        Assert.assertEquals("the entry 5750 must contains these occurrences :",
                _termithIndex.getContextLexicon().get("entry-5750_lexOff").getLexicalTable().toString(),
                _entry5750);

        Assert.assertEquals("the entry 575 must contains these occurrences :",
                _termithIndex.getContextLexicon().get("entry-575_lexOff").getLexicalTable().toString(),
                _entry575);
        
        Assert.assertEquals("the entry 450 must contains these occurrences :",
                _termithIndex.getContextLexicon().get("entry-450_lexOff").getLexicalTable().toString(),
                _entry450);

        Assert.assertEquals("the entry 13471 must contains these occurrences :",
                _termithIndex.getContextLexicon().get("entry-13471_lexOff").getLexicalTable().toString(),
                _entry13471);
    }
}
