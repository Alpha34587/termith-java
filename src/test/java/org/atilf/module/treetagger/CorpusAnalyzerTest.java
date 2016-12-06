package org.atilf.module.treetagger;

import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.CorpusAnalyzer;
import org.atilf.module.tools.FilesUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzerTest {

    private static  Map<String,StringBuilder> _extractedText;
    private static  CorpusAnalyzer _corpusAnalyzer;

    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {

        TermithIndex termithIndex = new TermithIndex.Builder().export(_temporaryFolder.getRoot().getPath()).build();
        termithIndex.addText("1", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        termithIndex.addText("2", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        termithIndex.addText("3", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
        _extractedText = convertExtractedText(termithIndex.getExtractedText());
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
    }

    static Map<String,StringBuilder> convertExtractedText(Map<String, Path> extractedText){
        Map<String,StringBuilder> map = new HashMap<>();
        extractedText.forEach(
                (key,value) -> map.put(key, FilesUtils.readObject(value,StringBuilder.class))
        );
        return map;
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