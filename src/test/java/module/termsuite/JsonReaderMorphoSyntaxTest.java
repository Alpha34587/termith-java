package module.termsuite;

import org.antlr.v4.runtime.misc.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Simon Meoni
 *         Created on 25/08/16.
 */
public class JsonReaderMorphoSyntaxTest {
    JsonReaderMorphoSyntax jsonReader = new JsonReaderMorphoSyntax();
    JsonReaderMorphoSyntax cleanJsonReader = new JsonReaderMorphoSyntax();

    Queue<JsonReaderMorphoSyntax.Token> tokenStack = new LinkedList<>();
    Queue<JsonReaderMorphoSyntax.Token> tokenStack2 = new LinkedList<>();
    Queue<Pair<Integer, Integer>> offsets = new LinkedList<>();


    Logger logger = LoggerFactory.getLogger(JsonReaderMorphoSyntaxTest.class);

    @Before
    public void setUp(){
        //Parsing Test
        JsonReaderMorphoSyntax.Token token1 = new JsonReaderMorphoSyntax.Token("NN", "hearing", 22, 29);
        JsonReaderMorphoSyntax.Token token2 = new JsonReaderMorphoSyntax.Token("N", "research", 30, 38);
        JsonReaderMorphoSyntax.Token token3 = new JsonReaderMorphoSyntax.Token("CD", "125", 39, 42);
        tokenStack.add(token1);
        tokenStack.add(token2);
        tokenStack.add(token3);
        jsonReader.parsing(new File("src/test/resources/file.reader.json/file1.json"));

        //Clean Test
        offsets.add(new Pair<>(0,5));
        offsets.add(new Pair<>(6,7));
        offsets.add(new Pair<>(8,8));
        offsets.add(new Pair<>(9,10));
        offsets.add(new Pair<>(11,15));
        offsets.add(new Pair<>(16,18));

        JsonReaderMorphoSyntax.Token token4 = new JsonReaderMorphoSyntax.Token("", "", 0, 5);
        JsonReaderMorphoSyntax.Token token5 = new JsonReaderMorphoSyntax.Token("", "", 6, 7);
        JsonReaderMorphoSyntax.Token token6 = new JsonReaderMorphoSyntax.Token("", "", 6, 9);
        JsonReaderMorphoSyntax.Token token7 = new JsonReaderMorphoSyntax.Token("", "", 9, 10);
        JsonReaderMorphoSyntax.Token token8 = new JsonReaderMorphoSyntax.Token("", "", 11, 16);
        JsonReaderMorphoSyntax.Token token9 = new JsonReaderMorphoSyntax.Token("", "", 16, 18);
        tokenStack2.add(token4);
        tokenStack2.add(token5);
        tokenStack2.add(token6);
        tokenStack2.add(token7);
        tokenStack2.add(token8);
        tokenStack2.add(token9);
        cleanJsonReader.setTokenQueue(tokenStack2);
        cleanJsonReader.clean();
    }

    @Test
    public void parsingTest(){
        while (!tokenStack.isEmpty() || !jsonReader.getTokenQueue().isEmpty()) {
            try {

                JsonReaderMorphoSyntax.Token expected = tokenStack.poll();
                JsonReaderMorphoSyntax.Token current = jsonReader.getTokenQueue().poll();
                Assert.assertEquals("tokenStack must be equals :", expected.getBegin(),
                        current.getBegin());
                Assert.assertEquals("tokenStack must be equals :", expected.getEnd(),
                        current.getEnd());
                Assert.assertEquals("tokenStack must be equals :", expected.getLemma(),
                        current.getLemma());
                Assert.assertEquals("tokenStack must be equals :", expected.getPos(),
                        current.getPos());

            }
            catch (EmptyStackException e){
                Assert.fail("stacks have not the same size");
                logger.error("stacks have not the same size :", e);
            }
        }
    }

    @Test
    public void cleanTest() throws Exception {
        while (!offsets.isEmpty() || !cleanJsonReader.getTokenQueue().isEmpty()) {
            try {

                Pair<Integer, Integer> expected = offsets.poll();
                JsonReaderMorphoSyntax.Token current = cleanJsonReader.getTokenQueue().poll();
                Assert.assertEquals("begin value must be equal :", expected.a.intValue(),
                        current.getBegin());
                Assert.assertEquals("end value must be equal :", expected.b.intValue(),
                        current.getEnd());
            }
            catch (EmptyStackException e){
                Assert.fail("stacks have not the same size");
                logger.error("stacks have not the same size :", e);
            }
        }
    }


}