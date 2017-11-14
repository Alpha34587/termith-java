package runner;

import org.atilf.cli.enrichment.TermithTreeTaggerCLI;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TermithTreeTaggerCLITest {

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Test
    public void runTest() throws Exception {
        TermithTreeTaggerCLI.run(
                "src/main/resources/termith-resources",
                "src/integrationTest/resources/termithTreeTagger/base",
                "fr",
                _temporaryFolder.getRoot().toString()
                );

        Files.list(_temporaryFolder.getRoot().toPath()).filter(el -> el.endsWith(".xml")).forEach(file -> {
            try {
                XMLAssert.assertXMLEqual("these xml files must be equals",
                        Files.readAllLines(file.toAbsolutePath()).toString(),
                        Files.readAllLines(
                                Paths.get("src/integrationTest/resources/termithTreeTagger/out/" + file.getFileName())
                        ).toString());
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}