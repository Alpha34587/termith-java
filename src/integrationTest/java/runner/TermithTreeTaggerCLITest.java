package runner;

import org.apache.commons.io.FileUtils;
import org.atilf.cli.enrichment.TermithTreeTaggerCLI;
import org.atilf.module.tools.FilesUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

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

        Files.list(_temporaryFolder.getRoot().toPath()).filter(el -> el.toString().contains(".xml")).forEach(file -> {
            String xml = FilesUtils.FileToString(file);
            Assert.assertTrue(
                    "the xml must contains a wordforms element ",
                    xml.contains("<ns:standOff type=\"wordForms\">")
            );
            Assert.assertTrue(
                    "the xml must contains a candidatsTermes element ",
                    xml.contains("<ns:standOff type=\"candidatsTermes\">")
            );
            Assert.assertTrue(
                    "the xml must contains a Syn element ",
                    xml.contains("<ns:standOff type=\"syntagmesDefinis\">")
            );
            Assert.assertTrue(
                    "the xml must contains a wordforms element ",
                    xml.contains("<ns:standOff type=\"lexiquesTransdisciplinaires\">")
            );

        });
    }

    @Test
    public void runTestWithExternalResources() throws Exception {
        FileUtils.copyDirectory(
                new File("src/main/resources/termith-resources"),
                new File(_temporaryFolder.getRoot().toString() + "/termith-resources")
        );
        Path resultPath = _temporaryFolder.newFolder("result").toPath();
        TermithTreeTaggerCLI.run(
                _temporaryFolder.getRoot() +"/termith-resources",
                "src/integrationTest/resources/termithTreeTagger/base",
                "fr",
                resultPath.toString()
        );

        Files.list(_temporaryFolder.getRoot().toPath()).filter(el -> el.toString().contains(".xml")).forEach(file -> {
            String xml = FilesUtils.FileToString(file);
            Assert.assertTrue(
                    "the xml must contains a wordforms element ",
                    xml.contains("<ns:standOff type=\"wordForms\">")
            );
            Assert.assertTrue(
                    "the xml must contains a candidatsTermes element ",
                    xml.contains("<ns:standOff type=\"candidatsTermes\">")
            );
            Assert.assertTrue(
                    "the xml must contains a Syn element ",
                    xml.contains("<ns:standOff type=\"syntagmesDefinis\">")
            );
            Assert.assertTrue(
                    "the xml must contains a wordforms element ",
                    xml.contains("<ns:standOff type=\"lexiquesTransdisciplinaires\">")
            );

        });
    }
}