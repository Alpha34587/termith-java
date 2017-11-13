package delegate;

import org.atilf.delegate.disambiguation.txm.TxmExporterDelegate;
import org.atilf.models.disambiguation.TxmContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

public class TxmExporterDelegateTest extends IntegrationTasks {
    private TxmExporterDelegate _t = new TxmExporterDelegate();
    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        List<TxmContext> _txmContextList1 = new ArrayList<>();
        List<TxmContext> _txmContextList2 = new ArrayList<>();
        List<TxmContext> _txmContextList3 = new ArrayList<>();
        List<TxmContext> _txmContextList4 = new ArrayList<>();
        List<TxmContext> _txmContextList5 = new ArrayList<>();
        TxmContext context1 = new TxmContext();
        context1.addElements("1","le","DET","le");
        context1.addElements("2","chat","NOM","chat");
        context1.addElements("3","manger","VER","mange");
        context1.addElements("4","une","DET","une");
        context1.addElements("5","pomme","NOM","pommes");
        context1.setFilename("1");
        TxmContext context2 = new TxmContext();
        context2.addElements("10","le","DET","le");
        context2.addElements("11","chien","NOM","chien");
        context2.setFilename("2");
        _txmContextList1.add(context1);
        _txmContextList1.add(context2);

        TxmContext context3 = new TxmContext();
        context3.addElements("1","le","DET","le");
        context3.addElements("2","chat","NOM","chat");
        context3.addElements("3","manger","VER","mange");
        context3.addElements("4","une","DET","une");
        context3.addElements("5","pomme","NOM","pommes");
        context3.setFilename("1");
        TxmContext context4 = new TxmContext();
        context4.addElements("10","le","DET","le");
        context4.addElements("11","chien","NOM","chien");
        context4.setFilename("2");
        _txmContextList2.add(context3);
        _txmContextList2.add(context4);

        TxmContext context5 = new TxmContext();
        context5.addElements("1","le","DET","le");
        context5.addElements("2","chat","NOM","chat");
        context5.addElements("3","manger","VER","mange");
        context5.addElements("4","une","DET","une");
        context5.addElements("5","pomme","NOM","pommes");
        context5.setFilename("1");
        TxmContext context6 = new TxmContext();
        context6.addElements("10","le","DET","le");
        context6.addElements("11","chien","NOM","chien");
        context6.setFilename("2");
        _txmContextList3.add(context5);
        _txmContextList3.add(context6);


        _t.setOutputPath(temporaryFolder.getRoot().toPath());
        _termithIndex.getTermsTxmContext().put("txm1",_txmContextList1);
        _termithIndex.getTermsTxmContext().put("txm2",new ArrayList<>(_txmContextList2));
        _termithIndex.getTermsTxmContext().put("txm3",new ArrayList<>(_txmContextList3));

    }

    @Test
    public void execute() throws Exception {
        executeTasksTest(_t);
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(
                "these files must be equals",
                String.join("\n", Files.readAllLines(
                        Paths.get("src/integrationTest/resources/txmExporter/txm3.xml"))
                ),
                String.join("\n",Files.readAllLines(Paths.get(temporaryFolder.getRoot().toString() + "/txm3.xml")))
        );
    }

}
