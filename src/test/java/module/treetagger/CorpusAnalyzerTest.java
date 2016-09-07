package module.treetagger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import thread.Initializer;

/**
 * @author Simon Meoni
 *         Created on 07/09/16.
 */
public class CorpusAnalyzerTest {
    private Initializer initializerOddSize;
    private Initializer initializerEvenSize;

    @Before
    public void setUp() throws Exception {
        initializerOddSize = new Initializer();
        initializerOddSize.addText("1", new StringBuffer("le petit chat mange un gant"));
        initializerOddSize.addText("2", new StringBuffer("le \t\tpetit chat mange un gantelet"));
        initializerOddSize.addText("3", new StringBuffer("le petit\n  chat mange une pizza\n"));
        initializerOddSize.addText("4", new StringBuffer("le petit chat mange un gant;.&&&"));

        initializerEvenSize = new Initializer();
        initializerEvenSize.addText("1", new StringBuffer("le petit chat mange un gant;.&&&\n"));
        initializerEvenSize.addText("2", new StringBuffer("le \t\tpetit chat mange un gantele"));

    }

    @Test
    public void totalSize() throws Exception {

        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer();
        Assert.assertEquals("the integer must be equals to :",127, corpusAnalyzer.totalSize(initializerOddSize));
        Assert.assertEquals("the integer must be equals to :",66, corpusAnalyzer.totalSize(initializerEvenSize));

    }

    @Test
    public void documentSize() throws Exception {

    }

    @Test
    public void nbOfDocs() throws Exception {

    }

    @Test
    public void documentOffset() throws Exception {

    }

    @Test
    public void docIndex() throws Exception {

    }

    @Test
    public void isLastDoc() throws Exception {

    }

    @Test
    public void cumulSize() throws Exception {

    }

}