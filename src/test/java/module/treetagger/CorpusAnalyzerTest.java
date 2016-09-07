package module.treetagger;

import Utils.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import thread.Initializer;

import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzerTest {

    private Initializer initializerCorpus;
    private CorpusAnalyzer corpusAnalyzer;
    @Before
    public void setUp() throws Exception {
        initializerCorpus = new Initializer();
        initializerCorpus.addText("1", File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        initializerCorpus.addText("2",File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        initializerCorpus.addText("3",File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
    }

    @Test
    public void totalSize() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the total size must be equals to :",132,
                corpusAnalyzer.totalSize(initializerCorpus));
    }

    @Test
    public void documentSize() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the size of the document must be equals to :",
                24,corpusAnalyzer.documentSize(initializerCorpus,"1"));
        Assert.assertEquals("the size of the document must be equals to :",
                76,corpusAnalyzer.documentSize(initializerCorpus,"2"));
        Assert.assertEquals("the size of the document must be equals to :",
                32,corpusAnalyzer.documentSize(initializerCorpus,"3"));
    }

    @Test
    public void nbOfDocs() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the number of the document must be equals to :",
                3,corpusAnalyzer.nbOfDocs(initializerCorpus));
    }

    @Test
    public void documentOffset() throws Exception {
        corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the end must be equals to :", 23,
                corpusAnalyzer.end(initializerCorpus,"1")
                );
        Assert.assertEquals("the end must be equals to :", 75,
                corpusAnalyzer.end(initializerCorpus,"2")
        );
        Assert.assertEquals("the end must be equals to :", 31,
                corpusAnalyzer.end(initializerCorpus,"3")
        );
    }

    @Test
    public void isLastDoc() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();
    }

    @Test
    public void cumulSize() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the cumul size must be equals to :", 24,
                corpusAnalyzer.cumulSize(initializerCorpus,"1")
        );
        Assert.assertEquals("the cumul size must be equals to :", 100,
                corpusAnalyzer.cumulSize(initializerCorpus,"2")
        );
        Assert.assertEquals("the cumul size  must be equals to :", 132,
                corpusAnalyzer.cumulSize(initializerCorpus,"3")
        );

    }

}