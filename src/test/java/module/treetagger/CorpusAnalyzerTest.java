package module.treetagger;

import Utils.File;
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

    @Before
    public void setUp() throws Exception {
        initializerCorpus = new Initializer();
        initializerCorpus.addText("1", File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file1.txt")));
        initializerCorpus.addText("2",File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file2.txt")));
        initializerCorpus.addText("3",File.ReadFile(Paths.get("src/test/resources/corpus.analyzer/txt/file3.txt")));
    }

    @Test
    public void totalSize() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();
    }

    @Test
    public void documentSize() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

    @Test
    public void nbOfDocs() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

    @Test
    public void documentOffset() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

    @Test
    public void docIndex() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

    @Test
    public void isLastDoc() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

    @Test
    public void cumulSize() throws Exception {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();

    }

}