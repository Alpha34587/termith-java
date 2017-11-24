package delegate;

import org.apache.commons.io.FileUtils;
import org.atilf.delegate.tools.SpecialCharacterTranslatorDelegate;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpecialCharacterDelegateTest extends IntegrationTasks {

    private SpecialCharacterTranslatorDelegate _s = new SpecialCharacterTranslatorDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        FileUtils.copyDirectory(
                new File("src/integrationTest/resources/specialCharacterTranslator"),
                _temporaryFolder.getRoot()
        );
        Files.list(Paths.get(_temporaryFolder.getRoot().toString()))
                .filter(el -> el.toString().contains(".xml")).
                forEach(el -> _termithIndex.getXmlCorpus().put(el.toString(), el.toAbsolutePath())
        );
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_s);
        XMLAssert.assertEquals("these xml must be equals",
                String.join("\n",Files.readAllLines(Paths.get("src/integrationTest/resources/specialCharacterTranslator/out/test1.xml"))),
                String.join("\n",Files.readAllLines(Paths.get(_temporaryFolder.getRoot().toString() + "/" + "test2.xml"))));
    }
}
