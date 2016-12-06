package org.atilf.models.disambiguation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Simon Meoni Created on 06/12/16.
 */
public class CommonWordsPosLemmaCleanerTest {
    private static CommonWordsPosLemmaCleaner _commonWordsPosLemmaCleaner;
    private static LexiconProfile _lex1 = new LexiconProfile();
    private static LexiconProfile _lex2 = new LexiconProfile();


    @BeforeClass
    public static void setUp() throws Exception {
        _lex1.addOccurrence("1");
        _lex1.addOccurrence("1");
        _lex1.addOccurrence("2");
        _lex1.addOccurrence("3");
        _lex1.addOccurrence("4");
        _lex1.addOccurrence("5");
        _lex2.addOccurrence("1");
        _lex2.addOccurrence("2");
        _lex2.addOccurrence("3");
        _lex2.addOccurrence("4");
        _lex2.addOccurrence("6");
        _commonWordsPosLemmaCleaner = new CommonWordsPosLemmaCleaner(_lex1, _lex2);
    }

    @Test
    public void execute() throws Exception {
        Assert.assertEquals("the lexicon \"_lex1\" must have this size", 1, _lex1.lexicalSize());
        Assert.assertEquals("the lexicon \"_lex2\" must have this size", 1, _lex1.lexicalSize());
        Assert.assertEquals(
                "the lexicon \"lex1\" must contains the element \"5\"",
                1,
                _lex1.count("5")
        );
        Assert.assertEquals(
                "the lexicon \"lex2\" must contains the element \"6\"",
                1,
                _lex1.count("6")
        );
    }

}