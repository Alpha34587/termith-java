package delegate;

import org.atilf.delegate.enrichment.analyzer.TermsuitePipelineBuilderDelegate;
import org.atilf.runner.TermithResourceManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
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
        Files.list(Paths.get("src/integrationTest/resources/termsuiteJson")).forEach(
                el -> {
                    try {
                        Files.copy(el, Paths.get(_temporaryFolder.getRoot() + "/json/" + el.getFileName().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
    }
}
