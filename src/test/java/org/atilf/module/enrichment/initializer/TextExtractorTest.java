package org.atilf.module.enrichment.initializer;

import org.atilf.resources.enrichment.XslResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 * Created on 22/08/16.
 */
public class TextExtractorTest {
    private static TextExtractor _textExtractor;

    @BeforeClass
    public static void setUp(){
        _textExtractor = new TextExtractor(new File("src/test/resources/module/enrichment/" +
                "initializer/textExtractor/file1.xml"), new XslResources("termith-resources/all/xsl_stylesheet/extractText.xsl"));
    }

    @Test
    public void testXsltTransformation() throws Exception {
        _textExtractor.execute();
        Assert.assertEquals("the extracted text must be equal",
                String.join("\n", Files.readAllLines(Paths.get("src/test/resources/module/enrichment/" +
                        "initializer/textExtractor/file1.txt"))),
                _textExtractor.getExtractedText().toString()
        );
    }

}
