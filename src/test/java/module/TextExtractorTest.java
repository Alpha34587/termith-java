package module;

import module.extractor.TextExtractor;
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
    TextExtractor textExtractor;
    @Before
    public void setUp(){
        textExtractor = new TextExtractor(new File("src/test/resources/corpus/xml/file1.xml"));
    }

    @Test
    public void testXsltTransformation() throws Exception {
        Assert.assertEquals("the extracted text must be equal",
                String.join("\n", Files.readAllLines(Paths.get("src/test/resources/corpus/txt/file1.txt"))),
                textExtractor.xsltTransformation().toString()
        );
    }

}