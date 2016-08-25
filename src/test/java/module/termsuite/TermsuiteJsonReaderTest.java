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
public class TermsuiteJsonReaderTest {
    TermsuiteJsonReader termsuiteJsonReader;
    TermsuiteJsonReader cleanTermsuiteJsonReader;

    Queue<TermsuiteJsonReader.Token> tokenStack = new LinkedList<>();
    Queue<TermsuiteJsonReader.Token> tokenStack2 = new LinkedList<>();
    Queue<Pair<Integer, Integer>> offsets = new LinkedList<>();


    Logger logger = LoggerFactory.getLogger(TermsuiteJsonReaderTest.class);

    @Before
    public void setUp(){
        //Parsing Test
        cleanTermsuiteJsonReader = new TermsuiteJsonReader();
        termsuiteJsonReader = new TermsuiteJsonReader(new File("src/test/resources/file.reader.json/file1.json"));

        TermsuiteJsonReader.Token token1 = new TermsuiteJsonReader.Token("NN", "hearing", 22, 29);
        TermsuiteJsonReader.Token token2 = new TermsuiteJsonReader.Token("N", "research", 30, 38);
        TermsuiteJsonReader.Token token3 = new TermsuiteJsonReader.Token("CD", "125", 39, 42);
        tokenStack.add(token1);
        tokenStack.add(token2);
        tokenStack.add(token3);
        termsuiteJsonReader.parsing();

        //Clean Test
        offsets.add(new Pair<>(0,5));
        offsets.add(new Pair<>(6,7));
        offsets.add(new Pair<>(8,8));
        offsets.add(new Pair<>(9,10));
        offsets.add(new Pair<>(11,15));
        offsets.add(new Pair<>(16,18));

        TermsuiteJsonReader.Token token4 = new TermsuiteJsonReader.Token("", "", 0, 5);
        TermsuiteJsonReader.Token token5 = new TermsuiteJsonReader.Token("", "", 6, 7);
        TermsuiteJsonReader.Token token6 = new TermsuiteJsonReader.Token("", "", 6, 9);
        TermsuiteJsonReader.Token token7 = new TermsuiteJsonReader.Token("", "", 9, 10);
        TermsuiteJsonReader.Token token8 = new TermsuiteJsonReader.Token("", "", 11, 16);
        TermsuiteJsonReader.Token token9 = new TermsuiteJsonReader.Token("", "", 16, 18);
        tokenStack2.add(token4);
        tokenStack2.add(token5);
        tokenStack2.add(token6);
        tokenStack2.add(token7);
        tokenStack2.add(token8);
        tokenStack2.add(token9);
        cleanTermsuiteJsonReader.setTokenQueue(tokenStack2);
        cleanTermsuiteJsonReader.clean();
    }

    @Test
    public void parsingTest(){
        while (!tokenStack.isEmpty() || !termsuiteJsonReader.getTokenQueue().isEmpty()) {
            try {

                TermsuiteJsonReader.Token expected = tokenStack.poll();
                TermsuiteJsonReader.Token current = termsuiteJsonReader.getTokenQueue().poll();
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
        while (!offsets.isEmpty() || !cleanTermsuiteJsonReader.getTokenQueue().isEmpty()) {
            try {

                Pair<Integer, Integer> expected = offsets.poll();
                TermsuiteJsonReader.Token current = cleanTermsuiteJsonReader.getTokenQueue().poll();
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