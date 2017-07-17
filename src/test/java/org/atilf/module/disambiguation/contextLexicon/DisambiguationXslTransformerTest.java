package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.disambiguation.TxmXslResource;
import org.atilf.runner.TermithResourceManager;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.atilf.runner.TermithResourceManager.*;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslTransformerTest {
    private static DisambiguationXslTransformer _disambiguationXslTransformer;
    private static DisambiguationXslTransformer _txmXslTransformer;

    @BeforeClass
    public static void setUp(){
        _disambiguationXslTransformer = new DisambiguationXslTransformer(
                new File("src/test/resources/module/disambiguation/contextLexicon/" +
                        "disambiguationXslTransformer/test1.xml"),
                new DisambiguationXslResources("termith-resources/all/xsl_stylesheet/disambiguation.xsl")
        );

        _txmXslTransformer = new DisambiguationXslTransformer(
                new File("src/test/resources/module/disambiguation/contextLexicon/" +
                        "disambiguationXslTransformer/test1.xml"),
                new TxmXslResource("termith-resources/all/xsl_stylesheet/txm.xsl")
        );
    }

    @Test
    public void testXsltTransformation() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        _disambiguationXslTransformer.execute();
        assertXMLEqual("this two xml must be equals :" +  _disambiguationXslTransformer.getTransformedContent(),
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/module/disambiguation/contextLexicon/" +
                                "disambiguationXslTransformer/test2.xml"))),
                _disambiguationXslTransformer.getTransformedContent().toString()
        );
    }

    @Test
    public void testTxmXsltTransformation() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        _txmXslTransformer.execute();
        assertXMLEqual("this two xml must be equals :" +  _txmXslTransformer.getTransformedContent(),
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/module/disambiguation/contextLexicon/" +
                                "disambiguationXslTransformer/test3.xml"))),
                _txmXslTransformer.getTransformedContent().toString()
        );
    }
}
