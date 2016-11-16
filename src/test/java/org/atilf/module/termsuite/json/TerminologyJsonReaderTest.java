package org.atilf.module.termsuite.json;

import org.antlr.v4.runtime.misc.Pair;
import org.atilf.module.termsuite.terminology.TerminologyJsonReader;
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
public class TerminologyJsonReaderTest {
    private TerminologyJsonReader _terminologyJsonReader;
    private TerminologyJsonReader _cleanTerminologyJsonReader;
    private TerminologyJsonReader expectedJsonReader;

    private Queue<Pair<Integer, Integer>> offsets = new LinkedList<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(TerminologyJsonReaderTest.class);

    @Before
    public void setUp(){
        //Parsing Test
        _cleanTerminologyJsonReader = new TerminologyJsonReader();
        _terminologyJsonReader = new TerminologyJsonReader(new File("src/test/resources/file/reader/json/file1.json"));
        expectedJsonReader = new TerminologyJsonReader();
        expectedJsonReader.createToken("NN", "hearing", 22, 29);
        expectedJsonReader.createToken("N", "research", 30, 38);
        expectedJsonReader.createToken("CD", "125", 39, 42);
        _terminologyJsonReader.parsing();

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
        while (!expectedJsonReader.getTokenQueue().isEmpty() || !_terminologyJsonReader.getTokenQueue().isEmpty()) {
            try {

                TerminologyJsonReader.Token expected = expectedJsonReader.getTokenQueue().poll();
                TerminologyJsonReader.Token current = _terminologyJsonReader.getTokenQueue().poll();
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