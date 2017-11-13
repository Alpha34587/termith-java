import org.apache.commons.io.FileUtils;
import org.atilf.delegate.enrichment.initializer.TextExtractorDelegate;
import org.atilf.module.tools.FilesUtils;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TextExtractorDelegateTest extends IntegrationTasks {
    private TextExtractorDelegate _e = new TextExtractorDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _e.setBase(_temporaryFolder.getRoot().toPath());
        _e.setOutput(_temporaryFolder.getRoot().toPath());
        for (int i = 0; i < 100; i++ ){
            Files.copy(
                    Paths.get("/home/smeoni/IdeaProjects/termITH/src/integrationTest/resources/textExtractor/test1.xml"),
                    Paths.get(_temporaryFolder.getRoot() + "/test" + i + ".xml")
            );
        }
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_e);
        Assert.assertEquals("these extracted texts must be equals",
                "\n" +
                        "\t\t\n" +
                        "\t\t\tle chien mange des saucisses\n" +
                        "\n" +
                        "\t\t\n" +
                        "\t",
                FilesUtils.readObject(_termithIndex.getExtractedText().get("test80"),StringBuilder.class).toString()
                );
    }
}
