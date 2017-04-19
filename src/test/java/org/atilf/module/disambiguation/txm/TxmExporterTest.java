package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.TxmContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmExporterTest {
    private static List<TxmContext> _txmContextList = new ArrayList<>();
    private static TxmExporter _txmExporter;

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {
        TxmContext context1 = new TxmContext();
        context1.addElements("5","pomme","NOM","pommes");
        context1.addElements("4","une","DET","une");
        context1.addElements("3","manger","VER","mange");
        context1.addElements("2","chat","NOM","chat");
        context1.addElements("1","le","DET","le");
        context1.setFilename("1");
        TxmContext context2 = new TxmContext();
        context2.addElements("11","chien","NOM","chien");
        context2.addElements("10","le","DET","le");
        context2.setFilename("2");
        _txmContextList.add(context1);
        _txmContextList.add(context2);
        _txmExporter = new TxmExporter("test1.xml",_txmContextList,temporaryFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void execute() throws Exception {
        _txmExporter.execute();
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(
                "these files must be equals",
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/module/disambiguation/txm/txmExporter/test1.xml"))
                ),
                String.join("\n",Files.readAllLines(Paths.get(temporaryFolder.getRoot().toString() + "/test1.xml")))
        );
    }

}
