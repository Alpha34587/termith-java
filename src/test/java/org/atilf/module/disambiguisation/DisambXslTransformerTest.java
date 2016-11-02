package org.atilf.module.disambiguisation;

import org.atilf.models.disambiguisation.DisambXslResources;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslTransformerTest extends XMLTestCase {
    DisambXslTransformer _disambXslTransformer;
    @Before
    public void setUp(){
        _disambXslTransformer = new DisambXslTransformer(
                new File("src/test/resources/corpus/disambiguisation/tei/test2.xml"),
                new DisambXslResources()
        );
    }

    @Test
    public void testXsltTransformation() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual("this two xml must be equals",
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/corpus/disambiguisation/transform-tei/test2.xml"))),
                _disambXslTransformer.xsltTransformation().toString()
        );
    }

}