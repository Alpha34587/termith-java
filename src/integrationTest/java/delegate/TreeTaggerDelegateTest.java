package delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atilf.delegate.enrichment.analyzer.TreeTaggerWorkerDelegate;
import org.atilf.module.tools.FilesUtils;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.atilf.runner.TermithResourceManager.TermithResource;

public class TreeTaggerDelegateTest extends IntegrationTasks{
    private TreeTaggerWorkerDelegate _t = new TreeTaggerWorkerDelegate();
    private StringBuilder text = new StringBuilder("\"Chez nous vivait un poète, il avait d'autres yeux.\" " +
            "Petr Bezruč (1867-1958), Chants de Silésie, cit. in Marabout Université 240.");
    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        TermithResource.setLang("fr");
        _t.setLang("fr");
        _t.setOutputPath(_temporaryFolder.getRoot().toPath());

        for (int i = 1; i < 100; i++) {

            String id = "text" + i;
            Files.copy(
                    Paths.get("src/integrationTest/resources/treetaggerWorker/text1.xml"),
                    Paths.get(_temporaryFolder.getRoot() + "/" + id +".xml")
            );
            _termithIndex.getExtractedText().put(id,
                    FilesUtils.writeObject(text, _temporaryFolder.getRoot().toPath())
            );
            _termithIndex.getXmlCorpus().put(id, Paths.get(_temporaryFolder.getRoot() + "/"+id+".xml"));
        }

    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(new File(_temporaryFolder.getRoot().toString() + "/json/text85.json"));


        Assert.assertEquals(
                "these files must be equals",
                String.join("\n",Files.readAllLines(Paths.get("src/integrationTest/resources/treetaggerWorker/text1.json"))),
                actualObj.get("word_annotations").toString()
                );
    }
}
