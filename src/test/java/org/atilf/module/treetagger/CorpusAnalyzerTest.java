package org.atilf.module.treetagger;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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

    private TermithIndex _termithIndex;
    private Map<String,StringBuilder> _extractedText;
    private CorpusAnalyzer _corpusAnalyzer;

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    public CorpusAnalyzerTest() {
    }

    @Before
    public void setUp() throws Exception {

        _termithIndex = new TermithIndex.Builder().export(_temporaryFolder.getRoot().getPath()).build();
        _termithIndex.addText("1", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        _termithIndex.addText("2", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        _termithIndex.addText("3", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
        _extractedText = convertExtractedText(_termithIndex.getExtractedText());
        _corpusAnalyzer = new CorpusAnalyzer(_extractedText);
    }

    static Map<String,StringBuilder> convertExtractedText(Map<String, Path> extractedText){
        Map<String,StringBuilder> map = new HashMap<>();
        extractedText.forEach(
                (key,value) -> {
                    map.put(key,(StringBuilder) FilesUtils.readObject(value));
                }
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
                _corpusAnalyzer.get_analyzedTexts().get("1").getCumulSize()
        );
        Assert.assertEquals("the cumul size must be equals to :", 100,
                _corpusAnalyzer.get_analyzedTexts().get("2").getCumulSize()
        );
        Assert.assertEquals("the cumul size  must be equals to :", 132,
                _corpusAnalyzer.get_analyzedTexts().get("3").getCumulSize()
        );
    }
}