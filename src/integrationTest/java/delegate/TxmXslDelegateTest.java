package delegate;

import org.atilf.delegate.disambiguation.txm.TxmXslTransformerDelegate;
import org.atilf.runner.TermithResourceManager;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TxmXslDelegateTest extends IntegrationTasks {
    private TxmXslTransformerDelegate _t = new TxmXslTransformerDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        _t.setOutputPath(_temporaryFolder.getRoot().toPath());
        _t.setTxmInputPath(Paths.get("src/integrationTest/resources/evaluation"));
    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_t);
        String salut = String.join("\n", Files.readAllLines(Paths.get(_temporaryFolder.getRoot().toString() + "/eval2.xml")));
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(
                "the content of file must be equals",
                String.join("\n", Files.readAllLines(Paths.get(_temporaryFolder.getRoot().toString() + "/eval2.xml"))),
                String.join("\n",Files.readAllLines(Paths.get("src/integrationTest/resources/txmTransformer/txm2.xml")))
        );
    }
}
