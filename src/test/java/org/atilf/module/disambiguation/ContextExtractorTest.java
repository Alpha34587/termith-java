package org.atilf.module.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class ContextExtractorTest {;
    private Map<String,LexiconProfile> _expectedMap = new HashMap<>();
    private Map<String,LexiconProfile> _multiSub = new HashMap<>();

    @Before
    public void setUp(){
        /*
        case with embedded term part w w w T1 T2 < T3 T4 w >
         */
        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("ce PRO:DEM",2);
        entry1.add("article NOM",2);
        entry1.add("présenter VER:pres",2);
        entry1.add("un DET:ART",2);
        entry1.add("étude NOM",2);
        entry1.add("comparer VER:pper",2);
        entry1.add("du PRP:det",4);
        entry1.setCount("donnée NOM",2);
        entry1.add("archéo-ichtyofauniques ADJ",2);
        entry1.add("livrer VER:pper",2);
        entry1.add("par PRP",2);
        entry1.add("deux NUM",2);
        _expectedMap.put("entry-13471_lexOff",new LexiconProfile(entry1));

        /*
        simple case : w w w w T w w w w
         */
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
        _expectedMap.put("entry-7263_lexOff",new LexiconProfile(entry2));

        /*
        simple multi case : w w w T1 T2 w w w
         */
        Multiset<String> entry3 = HashMultiset.create();
        entry3.add("ce PRO:DEM");
        entry3.add("article NOM");
        entry3.add("présenter VER:pres");
        entry3.add("un DET:ART");
        entry3.add("étude NOM");
        entry3.add("comparer VER:pper");
        entry3.add("archéo-ichtyofauniques ADJ");
        entry3.add("livrer VER:pper");
        entry3.add("par PRP");
        entry3.add("deux NUM");
        _expectedMap.put("entry-575_lexOff",new LexiconProfile(entry3));
        
        /*
        embedded multi case :  < w w T1 > T2 T3 w w w
         */
        Multiset<String> entry4 = HashMultiset.create();
        entry4.add("type NOM");
        entry4.add("de PRP");
        entry4.add("enfouissement NOM");
        entry4.add(". SENT");
        _expectedMap.put("entry-5750_lexOff",new LexiconProfile(entry4));

        /*
        case : T1 <T2> w w w
         */
        Multiset<String> entry5 = HashMultiset.create();
        entry5.add("sur PRP");
        entry5.add("le DET:ART");
        entry5.add("deux NUM");
        entry5.add("site NOM");
        _expectedMap.put("entry-35_lexOff",new LexiconProfile(entry5));

        /*
        case : w T < w w w >
         */
        Multiset<String> entry6 = HashMultiset.create();
        entry6.add(", PUN");
        entry6.add("pêche NOM");
        entry6.add("être VER:pres");
        entry6.add("un DET:ART");
        _expectedMap.put("entry-450_lexOff",new LexiconProfile(entry6));
    }

    @Test
    public void extractLexiconProfile() throws Exception {
        ContextExtractor contextCorpus = new ContextExtractor(
                "src/test/resources/corpus/disambiguation/transform-tei/test2.xml",
                _multiSub
        );
        contextCorpus.execute();
        Assert.assertEquals(
                "this two map must have the same size",
                _expectedMap.size(),
                _multiSub.size()
        );

        _expectedMap.forEach(
                (key, value) -> {
                    Multiset observed = _multiSub.get(key).getLexicalTable();
                    value.getLexicalTable().forEach(
                            el -> Assert.assertEquals("the occurrence of element must be equals at " + key +
                                            " for the word : " + el,
                                    value.getLexicalTable().count(el), observed.count(el)
                            )
                    );
                }
        );
    }
}