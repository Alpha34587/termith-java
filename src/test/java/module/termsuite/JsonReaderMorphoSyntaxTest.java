package module.termsuite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Simon Meoni
 *         Created on 25/08/16.
 */
public class JsonReaderMorphoSyntaxTest {

    JsonReaderMorphoSyntax jsonReader = new JsonReaderMorphoSyntax();
    Stack<JsonReaderMorphoSyntax.Token> tokenStack = new Stack<>();
    Logger logger = LoggerFactory.getLogger(JsonReaderMorphoSyntaxTest.class);

    @Before
    public void setUp(){
        JsonReaderMorphoSyntax.Token token1 = new JsonReaderMorphoSyntax.Token("NN", "hearing", 22, 29);
        JsonReaderMorphoSyntax.Token token2 = new JsonReaderMorphoSyntax.Token("N", "research", 30, 38);
        JsonReaderMorphoSyntax.Token token3 = new JsonReaderMorphoSyntax.Token("CD", "125", 39, 42);
        tokenStack.add(token1);
        tokenStack.add(token2);
        tokenStack.add(token3);
        jsonReader.parsing(new File("src/test/resources/file.reader.json/file1.json"));
    }
    @Test
    public void parsingTest(){
        while (!tokenStack.isEmpty() || !jsonReader.getTokenStack().isEmpty()) {
            try {
                Assert.assertSame("tokenStack must be equals :", tokenStack.peek(), jsonReader.getTokenStack().peek());
            }
            catch (EmptyStackException e){
                Assert.fail("stacks have not the same size");
                logger.error("stacks have not the same size :", e);
            }
        }
    }

}