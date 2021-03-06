package delegate;

import org.atilf.delegate.enrichment.initializer.CorpusInitializerDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Paths;

public class CorpusInitializerTest extends IntegrationTasks {

    private CorpusInitializerDelegate _c = new CorpusInitializerDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        _c.setBase(Paths.get("/home/smeoni/IdeaProjects/termITH/src/integrationTest/resources/evaluationExtractor"));
        _c.setOutputPath(_temporaryFolder.getRoot().toPath());
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_c);
        Assert.assertEquals("these corpus map must have this size",3,_termithIndex.getXmlCorpus().size());
    }
}
