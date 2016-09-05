package thread;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * @author Simon Meoni
 * Created on 22/08/16.
 */
public class InitializerTest {
    Initializer initializerMonoThread;

    Initializer initializerMultiThread;
    Initializer initializerOddSize;
    Initializer initializerEvenSize;
    @Before
    public void setup(){
        initializerMonoThread = new Initializer(
                1,
                Paths.get("src/test/resources/corpus/xml")
        );

        initializerMultiThread = new Initializer(
                8,
                Paths.get("src/test/resources/corpus/xml")
        );

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
    public void getDocumentSize() throws Exception {

    }

    @Test
    public void getNumOfDocs() throws Exception {

    }

    @Test
    public void getDocumentOffset() throws Exception {

    }

    @Test
    public void getDocIndex() throws Exception {

    }

    @Test
    public void isLastDoc() throws Exception {

    }

    @Test
    public void testMultiThreadsExecution() throws Exception {
        initializerMonoThread.execute();
        initializerMultiThread.execute();
        initializerMultiThread.getExtractedText().forEach((filename,content) ->
                Assert.assertEquals("the text file must be equal",
                        initializerMonoThread.getExtractedText().get(filename).toString(),
                        content.toString()
                )
        );

    }

    @Test
    public void getTotalSize() throws Exception {
        Assert.assertEquals("the integer must be equals to :",127, initializerOddSize.getTotalSize());
        Assert.assertEquals("the integer must be equals to :",66, initializerEvenSize.getTotalSize());
    }

}