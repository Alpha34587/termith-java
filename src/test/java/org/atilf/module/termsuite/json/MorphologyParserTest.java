package org.atilf.module.termsuite.json;

import org.antlr.v4.runtime.misc.Pair;
import org.atilf.models.MorphologyParser;
import org.junit.Assert;
import org.junit.BeforeClass;
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
public class MorphologyParserTest {
    private static MorphologyParser _morphologyParser;
    private static MorphologyParser _expectedJsonReader;
    private Queue<Pair<Integer, Integer>> offsets = new LinkedList<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(MorphologyParserTest.class);

    @BeforeClass
    public static void setUp(){
        _morphologyParser = new MorphologyParser(new File("src/test/resources/file/reader/json/file1.json"));
        _expectedJsonReader = new MorphologyParser();
        _expectedJsonReader.createToken("NN", "hearing", 22, 29);
        _expectedJsonReader.createToken("N", "research", 30, 38);
        _expectedJsonReader.createToken("CD", "125", 39, 42);
    }

    @Test
    public void parsingTest(){
        _morphologyParser.execute();
        while (!_expectedJsonReader.getTokenQueue().isEmpty() || !_morphologyParser.getTokenQueue().isEmpty()) {
            try {

                MorphologyParser.Token expected = _expectedJsonReader.getTokenQueue().poll();
                MorphologyParser.Token current = _morphologyParser.getTokenQueue().poll();
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