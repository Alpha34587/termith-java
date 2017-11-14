package delegate;

import org.atilf.delegate.disambiguation.evaluation.EvaluationXslTransformerDelegate;
import org.atilf.runner.TermithResourceManager;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;

public class EvaluationXslDelegateTest extends IntegrationTasks {
    private EvaluationXslTransformerDelegate _e = new EvaluationXslTransformerDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _e.setEvaluationPath(Paths.get("src/integrationTest/resources/learning"));
        _e.setOutputPath(_temporaryFolder.getRoot().toPath());
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_e);
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(
                "the content of file must be equals",
                String.join("\n", Files.readAllLines(Paths.get(_temporaryFolder.getRoot().toString() + "/learn2.xml"))),
                String.join("\n",Files.readAllLines(Paths.get("src/integrationTest/resources/output/testLearn.xml")))
        );
    }
}
