import org.atilf.delegate.disambiguation.contextLexicon.HeuristicsContextFilterDelegate;
import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class HeuristicsDelegateTest extends  IntegrationTasks {
    private HeuristicsContextFilterDelegate _h = new HeuristicsContextFilterDelegate();

    @Before
    public void setUp() throws Exception {
        _termithIndex.getContextLexicon().put("entry_lexOff",new LexiconProfile());
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_h);
        Assert.assertTrue("the context lexicon must be empty",_termithIndex.getContextLexicon().isEmpty());
    }
}