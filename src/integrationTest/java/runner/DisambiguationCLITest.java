package runner;

import org.atilf.cli.disambiguation.DisambiguationCLI;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.atilf.module.tools.FilesUtils.FileToString;

public class DisambiguationCLITest {
    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Test
    public void runTest() throws Exception {
        DisambiguationCLI.run(
                "src/integrationTest/resources/disambiguation/learning",
                "src/integrationTest/resources/disambiguation/evaluation",
                _temporaryFolder.getRoot().toString(),
                "src/main/resources/termith-resources");
        Files.list(_temporaryFolder.getRoot().toPath()).filter(el -> el.endsWith(".xml")).forEach(file -> {
            try {
                XMLAssert.assertXMLEqual(
                        "these xml files must be equals",
                        FileToString(file.toAbsolutePath()),
                        FileToString(Paths.get("src/integrationTest/resources/disambiguation/out/"
                                + file.getFileName()))
                );
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        });
    }

}
