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
        _lex1.addOccurrence("2");
        _lex1.addOccurrence("3");
        _lex1.addOccurrence("4");
        _lex1.addOccurrence("5");
        _lex2.addOccurrence("1");
        _lex2.addOccurrence("2");
        _lex2.addOccurrence("3");
        _lex2.addOccurrence("4");
        _lex2.addOccurrence("6");
        _lex1.addCoefficientSpec("1",0f);
        _lex1.addCoefficientSpec("1",0f);
        _lex1.addCoefficientSpec("2",0f);
        _lex1.addCoefficientSpec("3",0f);
        _lex1.addCoefficientSpec("4",0f);
        _lex1.addCoefficientSpec("5",0f);
        _lex2.addCoefficientSpec("1",0f);
        _lex2.addCoefficientSpec("2",0f);
        _lex2.addCoefficientSpec("3",0f);
        _lex2.addCoefficientSpec("4",0f);
        _lex2.addCoefficientSpec("6",0f);
        _commonWordsPosLemmaCleaner = new CommonWordsPosLemmaCleaner(_lex1, _lex2);
    }

    @Test
    public void execute() throws Exception {
        _commonWordsPosLemmaCleaner.execute();
        Assert.assertEquals("the lexicon \"_lex1\" must have this size", 1,
                _lex1.getSpecCoefficientMap().size());
        Assert.assertEquals("the lexicon \"_lex2\" must have this size", 1,
                _lex1.getSpecCoefficientMap().size());
        Assert.assertTrue(
                "the lexicon \"lex1\" must contains the element \"5\"",
                _lex1.getSpecCoefficientMap().containsKey("5")
        );
        Assert.assertTrue(
                "the lexicon \"lex2\" must contains the element \"6\"",
                _lex2.getSpecCoefficientMap().containsKey("6")
        );
    }

}