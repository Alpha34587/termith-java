import org.atilf.delegate.enrichment.analyzer.TermsuitePipelineBuilderDelegate;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TermsuitePipelineDelegateTest extends IntegrationTasks {
    private TermsuitePipelineBuilderDelegate _t = new TermsuitePipelineBuilderDelegate();
    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _t.setLang("fr");
        _t.setOutputPath(_temporaryFolder.getRoot().toPath().toString());
        _temporaryFolder.newFolder("json");
        for (int i = 1; i < 5; i++) {
            Files.copy(
                    Paths.get("src/integrationTest/resources/termsuiteJson/file"+ i +".json"),
                    Paths.get(_temporaryFolder.getRoot().toString() + "/json/file" + i + ".json")
            );
        }
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        Assert.assertFalse(
                "the terminology must be not empty",
                Files.readAllLines(_termithIndex.getTerminologies().get(0)).isEmpty()
        );
    }
}
