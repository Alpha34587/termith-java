package org.atilf.module.treetagger;

import org.atilf.models.TermithIndex;
import org.atilf.module.tools.FilesUtilities;
import org.atilf.thread.InitializerThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
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

    @Before
    public void setUp() throws Exception {

        termithIndex = new TermithIndex.Builder().build();
        termithIndex.addText("1", FilesUtilities.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        termithIndex.addText("2",FilesUtilities.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        termithIndex.addText("3",FilesUtilities.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
        extractedText = termithIndex.getExtractedText();
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
                corpusAnalyzer.getAnalyzedTexts().get("1").getCumulSize()
        );
        Assert.assertEquals("the cumul size must be equals to :", 100,
                corpusAnalyzer.getAnalyzedTexts().get("2").getCumulSize()
        );
        Assert.assertEquals("the cumul size  must be equals to :", 132,
                corpusAnalyzer.getAnalyzedTexts().get("3").getCumulSize()
        );
    }
}