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
    SubLexicExtractor subCorpus;
    SubLexicExtractor subCorpus2;
    SubLexicExtractor subLexicExtractor = new SubLexicExtractor(
            "src/test/resources/corpus/tei/test1.xml",
            new HashMap<>()
    );
    Deque<String> expectedTarget = new ArrayDeque<>();
    Deque<String> expectedCorresp = new ArrayDeque<>();
    Deque<String> expectedLexAna = new ArrayDeque<>();
    Map<String,LexicalProfile> expectedMap = new HashMap<>();
    Map<String,LexicalProfile> expectedMap2 = new HashMap<>();
    Map<String,LexicalProfile> multiSub = new HashMap<>();
    Map<String, LexicalProfile> multiSub2 = new HashMap<>();

    @Before
    public void setUp(){
        expectedTarget.add("#t16 #t17 #t18");
        expectedTarget.add("#t30");
        expectedTarget.add("#t49");

        expectedCorresp.add("#entry-8318");
        expectedCorresp.add("#entry-7263");
        expectedCorresp.add("#entry-990");

        expectedLexAna.add("#DM4");
        expectedLexAna.add("#DM3");
        expectedLexAna.add("#DM0");

        subCorpus = new SubLexicExtractor("src/test/resources/corpus/tei/test2.xml", multiSub);
        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("du PRP:det");
        entry1.add("le DET:ART");
        entry1.add(". SENT");
        entry1.add("sur PRP");
        entry1.add("le DET:ART");
        entry1.add("deux NUM");
        entry1.add("site NOM");

        expectedMap.put("entry-13471_lexOn",new LexicalProfile(entry1));
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
        expectedMap.put("entry-7263_lexOn",new LexicalProfile(entry2));

        subCorpus2 = new SubLexicExtractor("src/test/resources/corpus/tei/test5.xml", multiSub2);
        expectedMap2.put("entry-7263_lexOn",new LexicalProfile(entry2));
    }

    @Test
    public void extractTerms() throws Exception {
        subLexicExtractor.extractTerms();
        subLexicExtractor.get_target().forEach(
                el -> Assert.assertEquals("target must be equals",expectedTarget.poll(),el)
        );
        subLexicExtractor.get_corresp().forEach(
                el -> Assert.assertEquals("terms id must be equals",expectedCorresp.poll(),el)
        );
        subLexicExtractor.get_lexAna().forEach(
                el -> Assert.assertEquals("ana id must be equals",expectedLexAna.poll(),el)
        );

    }

    @Test
    public void extractSubCorpusSimple() throws Exception {
        subCorpus.extractTerms();
        subCorpus.extractSubCorpus();
        expectedMap.forEach(
                (key, value) -> {
                    Multiset observed = multiSub.get(key).get_lexicalTable();
                    value.get_lexicalTable().forEach(
                            el -> {
                                int count = observed.count(el);
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.get_lexicalTable().count(el), observed.count(el)
                                );
                            }
                    );
                }
        );
    }

    @Test
    public void extractSubCorpusOneTerm() throws Exception {

        subCorpus2.extractTerms();
        subCorpus2.extractSubCorpus();
        expectedMap2.forEach(
                (key, value) -> {
                    Multiset observed = multiSub2.get(key).get_lexicalTable();
                    value.get_lexicalTable().forEach(
                            el -> {
                                int count = observed.count(el);
                                Assert.assertEquals("the occurence of element must be equals at " + key +
                                                " for the word : " + el,
                                        value.get_lexicalTable().count(el), observed.count(el)
                                );
                            }
                    );
                }
        );

    }
}