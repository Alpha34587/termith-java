package delegate;

import org.atilf.delegate.disambiguation.contextLexicon.ThresholdLexiconCleanerDelegate;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ThresholdDelegateTest extends IntegrationTasks {
    private ThresholdLexiconCleanerDelegate _t = new ThresholdLexiconCleanerDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        LexiconProfile lexiconProfile = new LexiconProfile();
        lexiconProfile.getSpecCoefficientMap().put("chat", -3f);
        lexiconProfile.getSpecCoefficientMap().put("chaussure", -13f);
        lexiconProfile.getSpecCoefficientMap().put("fromage", 3f);
        lexiconProfile.getSpecCoefficientMap().put("carré", 13f);
        lexiconProfile.getSpecCoefficientMap().put("cercle",10f);

        LexiconProfile lexiconProfile2 = new LexiconProfile();
        lexiconProfile.getSpecCoefficientMap().put("chat", -3f);
        lexiconProfile.getSpecCoefficientMap().put("chaussure", -13f);
        lexiconProfile.getSpecCoefficientMap().put("fromage", 3f);
        lexiconProfile.getSpecCoefficientMap().put("carré", 13f);
        lexiconProfile.getSpecCoefficientMap().put("cercle",10f);

        LexiconProfile lexiconProfile1 = new LexiconProfile();
        lexiconProfile.getSpecCoefficientMap().put("chat", -3f);
        lexiconProfile.getSpecCoefficientMap().put("chaussure", -13f);
        lexiconProfile.getSpecCoefficientMap().put("fromage", 3f);
        lexiconProfile.getSpecCoefficientMap().put("carré", 13f);
        lexiconProfile.getSpecCoefficientMap().put("cercle",10f);

        _termithIndex.getContextLexicon().put("test",lexiconProfile);
        _termithIndex.getContextLexicon().put("test1",lexiconProfile1);
        _termithIndex.getContextLexicon().put("test2",lexiconProfile2);

        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _t.setThresholdMin(3);
        _t.setThresholdMax(13);
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        Assert.assertEquals("the size of the map must be equals",
                1,
                _termithIndex.getContextLexicon().get("test").getSpecCoefficientMap().size()
        );
        String word = "cercle";
        Assert.assertTrue("the coefficient must contains only this entry : " + word,
                _termithIndex.getContextLexicon().get("test").getSpecCoefficientMap().containsKey(word));

    }
}
