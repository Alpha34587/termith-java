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
    private Initializer initializerMonoThread;

    private Initializer initializerMultiThread;

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