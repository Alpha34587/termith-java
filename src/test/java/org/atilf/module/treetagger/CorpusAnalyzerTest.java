package org.atilf.module.treetagger;

import org.atilf.models.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.atilf.thread.InitializerThread;
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

    private TermithIndex termithIndex;
    private InitializerThread initializerThreadCorpus;
    private CorpusAnalyzer corpusAnalyzer;
    private Map<String,StringBuilder> extractedText;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {

        termithIndex = new TermithIndex.Builder().export(temporaryFolder.getRoot().getPath()).build();
        termithIndex.addText("1", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        termithIndex.addText("2", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        termithIndex.addText("3", FilesUtils.readFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
        extractedText = convertExtractedText(termithIndex.get_extractedText());
    }

    public static Map<String,StringBuilder> convertExtractedText(Map<String,Path> extractedText){
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
        corpusAnalyzer = new CorpusAnalyzer(extractedText);
        Assert.assertEquals("the total size must be equals to :",132,
                corpusAnalyzer.totalSize(extractedText));
    }

    @Test
    public void documentSize() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer(extractedText);
        Assert.assertEquals("the size of the document must be equals to :",
                24, corpusAnalyzer.documentSize(extractedText,"1"));
        Assert.assertEquals("the size of the document must be equals to :",
                76, corpusAnalyzer.documentSize(extractedText,"2"));
        Assert.assertEquals("the size of the document must be equals to :",
                32, corpusAnalyzer.documentSize(extractedText,"3"));
    }

    @Test
    public void nbOfDocs() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer(extractedText);
        Assert.assertEquals("the number of the document must be equals to :",
                3, corpusAnalyzer.nbOfDocs(extractedText));
    }

    @Test
    public void documentOffset() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer(extractedText);
        Assert.assertEquals("the end must be equals to :", 23,
                corpusAnalyzer.end(extractedText,"1")
                );
        Assert.assertEquals("the end must be equals to :", 75,
                corpusAnalyzer.end(extractedText,"2")
        );
        Assert.assertEquals("the end must be equals to :", 31,
                corpusAnalyzer.end(extractedText,"3")
        );
    }

    @Test
    public void cumulSize() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(extractedText);
        Assert.assertEquals("the cumul size must be equals to :", 24,
                corpusAnalyzer.get_analyzedTexts().get("1").get_cumulSize()
        );
        Assert.assertEquals("the cumul size must be equals to :", 100,
                corpusAnalyzer.get_analyzedTexts().get("2").get_cumulSize()
        );
        Assert.assertEquals("the cumul size  must be equals to :", 132,
                corpusAnalyzer.get_analyzedTexts().get("3").get_cumulSize()
        );
    }
}