package thread;

import models.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Simon Meoni
 * Created on 22/08/16.
 */
public class InitializerTest {
    private Initializer initializerMonoThread;
    private Initializer initializerMultiThread;
    private TermithIndex termithIndex;

    @Before
    public void setup() throws IOException {
        termithIndex = new TermithIndex.Builder().baseFolder("src/test/resources/corpus/xml").build();
        initializerMonoThread = new Initializer(
                1,
                termithIndex
        );

        initializerMultiThread = new Initializer(
                8,
               termithIndex
        );
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
}