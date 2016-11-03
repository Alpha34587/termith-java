package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class SubLexicExtractorTest {
    private Deque<String> _expectedTarget = new ArrayDeque<>();
    private Deque<String> _expectedCorresp = new ArrayDeque<>();

    private Deque<String> _expectedLexAna = new ArrayDeque<>();
    private Map<String,LexicalProfile> _expectedMap = new HashMap<>();
    private Map<String,LexicalProfile> _multiSub = new HashMap<>();

    private SubLexicExtractor _subCorpus = new SubLexicExtractor(
            "src/test/resources/corpus/tei/test2.xml",
            _multiSub
    );

    @Before
    public void setUp(){
        _expectedTarget.add("#t13 #t14 #t15 #t16");
        _expectedTarget.add("#t16 #t17 #t18");
        _expectedTarget.add("#t30");

        _expectedCorresp.add("#entry-13471");
        _expectedCorresp.add("#entry-13471");
        _expectedCorresp.add("#entry-7263");

        _expectedLexAna.add("#DM1");
        _expectedLexAna.add("#DM1");
        _expectedLexAna.add("#DM3");

        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("du PRP:det");
        entry1.add("le DET:ART");
        entry1.add(". SENT");
        entry1.add("sur PRP");
        entry1.add("le DET:ART");
        entry1.add("deux NUM");
        entry1.add("site NOM");

        _expectedMap.put("entry-13471_lexOn",new LexicalProfile(entry1));
        Multiset<String> entry2 = HashMultiset.create();
        entry2.add("pêche NOM");
        entry2.add(", PUN");
        entry2.add("limiter VER:pper");
        entry2.add("à PRP");
        entry2.add("quelque PRO:IND");
        entry2.add("espèce NOM");
        entry2.add("commun ADJ");
        entry2.add(". SENT");
        entry2.add("il PRO:PER");
        _expectedMap.put("entry-7263_lexOn",new LexicalProfile(entry2));
    }

    @Test
    public void extractTerms() throws Exception {
        _subCorpus.extractTerms();
        Assert.assertEquals(
                "these queues must have the same size",
                _expectedTarget.size(),
                _subCorpus.getTarget().size()
        );
        _subCorpus.getTarget().forEach(
                el -> Assert.assertEquals("target must be equals", _expectedTarget.poll(),el)
        );

        Assert.assertEquals(
                "these queues must have the same size",
                _expectedCorresp.size(),
                _subCorpus.getCorresp().size()
        );
        _subCorpus.getCorresp().forEach(
                el -> Assert.assertEquals("terms id must be equals", _expectedCorresp.poll(),el)
        );

        Assert.assertEquals(
                "these queues must have the same size",
                _expectedLexAna.size(),
                _subCorpus.getLexAna().size()
        );
        _subCorpus.getLexAna().forEach(
                el -> Assert.assertEquals("ana id must be equals", _expectedLexAna.poll(),el)
        );

    }

    @Test
    public void extractSubCorpus() throws Exception {
        _subCorpus = new SubLexicExtractor(
                "src/test/resources/corpus/tei/test2.xml",
                _multiSub
        );
        _subCorpus.execute();
        Assert.assertEquals(
                "this two map must have the same size",
                _multiSub.size(),
                _expectedMap.size()
        );

        _expectedMap.forEach(
                (key, value) -> {
                    Multiset observed = _multiSub.get(key).getLexicalTable();
                    value.getLexicalTable().forEach(
                            el -> {
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.getLexicalTable().count(el), observed.count(el)
                                );
                            }
                    );
                }
        );
    }
}