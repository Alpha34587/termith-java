import org.atilf.delegate.enrichment.analyzer.TermsuitePipelineBuilderDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;

public class TermsuitePipelineDelegateTest extends IntegrationTasks {
    private TermsuitePipelineBuilderDelegate _t = new TermsuitePipelineBuilderDelegate();
    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        _t.setLang("fr");
        _t.setOutputPath(_temporaryFolder.getRoot().toPath().toString());
        _temporaryFolder.newFolder("json");
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
    }
}
