package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class GlobalCorpusTest {
    GlobalCorpus corpus1;
    GlobalCorpus corpus2;
    Multiset observedCorpus = HashMultiset.create();
    Multiset expectedCorpus = HashMultiset.create();


    @Before
    public void setUp() throws Exception {
        corpus1 = new GlobalCorpus("src/test/resources/corpus/tei/test1.xml",observedCorpus);
        corpus2 = new GlobalCorpus("src/test/resources/corpus/tei/test1.xml",observedCorpus);


    }

    @Test
    public void execute() throws Exception {
        Assert.assertEquals("this two multisets must be equals",expectedCorpus.toString(),observedCorpus.toString());
    }

}