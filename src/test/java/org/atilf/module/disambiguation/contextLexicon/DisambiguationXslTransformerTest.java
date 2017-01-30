package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslTransformerTest {
    private static DisambiguationXslTransformer _disambiguationXslTransformer;

    @BeforeClass
    public static void setUp(){
        _disambiguationXslTransformer = new DisambiguationXslTransformer(
                new File("src/test/resources/corpus/disambiguation/tei/test1.xml"),
                new DisambiguationXslResources()
        );
    }

    @Test
    public void testXsltTransformation() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        _disambiguationXslTransformer.execute();
        assertXMLEqual("this two xml must be equals :" +  _disambiguationXslTransformer.getTransformedContent(),
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/corpus/disambiguation/transform-tei/test1.xml"))),
                _disambiguationXslTransformer.getTransformedContent().toString()
        );
    }

}