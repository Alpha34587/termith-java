package delegate;

import org.atilf.delegate.enrichment.initializer.CorpusMapperDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Paths;

public class CorpusMapperTest extends IntegrationTasks {

    private CorpusMapperDelegate _c = new CorpusMapperDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        _c.setBase(Paths.get("/home/smeoni/IdeaProjects/termITH/src/integrationTest/resources/evaluationExtractor"));
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_c);
        Assert.assertEquals("these corpus map must have this size",3,_termithIndex.getXmlCorpus().size());
    }
}
