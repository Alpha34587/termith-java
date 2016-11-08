package org.atilf.module.extractor;

import org.atilf.models.extractor.XslResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 * Created on 22/08/16.
 */
public class TextExtractorTest {
    private TextExtractor _textExtractor;
    @Before
    public void setUp(){
        _textExtractor = new TextExtractor(new File("src/test/resources/corpus/xml/file1.xml"), new XslResources());
    }

    @Test
    public void testXsltTransformation() throws Exception {
        Assert.assertEquals("the extracted text must be equal",
                String.join("\n", Files.readAllLines(Paths.get("src/test/resources/corpus/txt/file1.txt"))),
                _textExtractor.execute().toString()
        );
    }

}