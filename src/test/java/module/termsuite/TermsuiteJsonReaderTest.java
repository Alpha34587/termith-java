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
    private TermsuiteJsonReader termsuiteJsonReader;
    private TermsuiteJsonReader cleanTermsuiteJsonReader;
    private TermsuiteJsonReader expectedJsonReader;

    private Queue<Pair<Integer, Integer>> offsets = new LinkedList<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(TermsuiteJsonReaderTest.class);

    @Before
    public void setUp(){
        //Parsing Test
        cleanTermsuiteJsonReader = new TermsuiteJsonReader();
        termsuiteJsonReader = new TermsuiteJsonReader(new File("src/test/resources/file/reader/json/file1.json"));
        expectedJsonReader = new TermsuiteJsonReader();
        expectedJsonReader.createToken("NN", "hearing", 22, 29);
        expectedJsonReader.createToken("N", "research", 30, 38);
        expectedJsonReader.createToken("CD", "125", 39, 42);
        termsuiteJsonReader.parsing();

        //Clean Test
        offsets.add(new Pair<>(0,5));
        offsets.add(new Pair<>(6,7));
        offsets.add(new Pair<>(8,8));
        offsets.add(new Pair<>(9,10));
        offsets.add(new Pair<>(11,15));
        offsets.add(new Pair<>(16,18));
    }

    @Test
    public void parsingTest(){
        while (!expectedJsonReader.getTokenQueue().isEmpty() || !termsuiteJsonReader.getTokenQueue().isEmpty()) {
            try {

                TermsuiteJsonReader.Token expected = expectedJsonReader.getTokenQueue().poll();
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
                LOGGER.error("stacks have not the same size :", e);
            }
        }
    }

}