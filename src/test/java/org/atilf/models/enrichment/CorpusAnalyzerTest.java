package org.atilf.models.enrichment;

import org.atilf.module.tools.FilesUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzerTest {

    private static  Map<String,StringBuilder> _extractedText = new HashMap<>();
    private static  CorpusAnalyzer _corpusAnalyzer;

    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {
        _extractedText.put("1", FilesUtils.readFile(Paths.get("src/test/resources/models/enrichment/" +
                "corpusAnalyzer/file1.txt")));
        _extractedText.put("2", FilesUtils.readFile(Paths.get("src/test/resources/models/enrichment/" +
                "corpusAnalyzer/file2.txt")));
        _extractedText.put("3", FilesUtils.readFile(Paths.get("src/test/resources/models/enrichment/" +
                "corpusAnalyzer/file3.txt")));
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
    }

    @Test
    public void totalSize() throws Exception {
        Assert.assertEquals("the total size must be equals to :",132,
                _corpusAnalyzer.totalSize(_extractedText));
    }

    @Test
    public void documentSize() throws Exception {
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
        Assert.assertEquals("the size of the document must be equals to :",
                24, _corpusAnalyzer.documentSize(_extractedText,"1"));
        Assert.assertEquals("the size of the document must be equals to :",
                76, _corpusAnalyzer.documentSize(_extractedText,"2"));
        Assert.assertEquals("the size of the document must be equals to :",
                32, _corpusAnalyzer.documentSize(_extractedText,"3"));
    }

    @Test
    public void nbOfDocs() throws Exception {
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
        Assert.assertEquals("the number of the document must be equals to :",
                3, _corpusAnalyzer.nbOfDocs(_extractedText));
    }

    @Test
    public void documentOffset() throws Exception {
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
        Assert.assertEquals("the end must be equals to :", 23,
                _corpusAnalyzer.end(_extractedText,"1")
                );
        Assert.assertEquals("the end must be equals to :", 75,
                _corpusAnalyzer.end(_extractedText,"2")
        );
        Assert.assertEquals("the end must be equals to :", 31,
                _corpusAnalyzer.end(_extractedText,"3")
        );
    }

    @Test
    public void cumulSize() throws Exception {
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
        Assert.assertEquals("the cumul size must be equals to :", 24,
                _corpusAnalyzer.getAnalyzedTexts().get("1").getCumulatedSize()
        );
        Assert.assertEquals("the cumul size must be equals to :", 100,
                _corpusAnalyzer.getAnalyzedTexts().get("2").getCumulatedSize()
        );
        Assert.assertEquals("the cumul size  must be equals to :", 132,
                _corpusAnalyzer.getAnalyzedTexts().get("3").getCumulatedSize()
        );
    }
}
