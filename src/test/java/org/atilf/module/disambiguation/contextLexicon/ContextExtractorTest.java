package org.atilf.module.disambiguation.contextLexicon;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 17/10/16.
 */
public class ContextExtractorTest {
    private static Map<String,LexiconProfile> _expectedLexicon = new HashMap<>();
    private static Map<String,LexiconProfile> _observedLexicon = new HashMap<>();
    private static Map<String, LexiconProfile> _observedTagLexicon = new HashMap<>();
    private static Map<String,LexiconProfile> _expectedTagLexicon = new HashMap<>();
    private static Map<String,LexiconProfile> _thresholdObservedLexicon = new HashMap<>();
    private static Map<String,LexiconProfile> _thresholdExpectedLexicon = new HashMap<>();
    private static Map<String,LexiconProfile> _thresholdObservedLexicon2 = new HashMap<>();
    private static CorpusLexicon _observedCorpus = new CorpusLexicon(new HashMap<>(),new HashMap<>());
    private static Multiset<String> _expectedCorpus;

    @BeforeClass
    public static void setUp(){
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
        entry1.add("donnée NOM",2);
        entry1.add("archéo-ichtyofauniques ADJ",2);
        entry1.add("livrer VER:pper",2);
        entry1.add("par PRP",2);
        entry1.add("deux NUM",2);
        entry1.add("site NOM",2);
        entry1.add("de PRP",2);
        entry1.add("le DET:ART",2);
        entry1.add("âge NOM",2);
        _expectedLexicon.put("entry-13471_lexOff",new LexiconProfile(entry1));
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
        entry2.add("côtier ADJ");
        _expectedLexicon.put("entry-7263_lexOff",new LexiconProfile(entry2));
        Multiset<String> entry2Tag = HashMultiset.create();
        entry2Tag.add("quelque PRO:IND");
        _expectedTagLexicon.put("entry-7263_lexOff",new LexiconProfile(entry2Tag));
        
        /*
        simple multi case : w w w T1 T2 w w w
         */
        Multiset<String> entry3 = HashMultiset.create();
        entry3.add("ce PRO:DEM");
        entry3.add("présenter VER:pres");
        entry3.add("archéo-ichtyofauniques ADJ");
        entry3.add("du PRP:det");
        entry3.add("un DET:ART");
        entry3.add("article NOM");
        entry3.add("donnée NOM");
        entry3.add("livrer VER:pper");
        entry3.add("deux NUM");
        entry3.add("étude NOM");
        entry3.add("par PRP");
        entry3.add("comparer VER:pper");
        _expectedLexicon.put("entry-575_lexOff",new LexiconProfile(entry3));

        Multiset<String> thresholdEntry3 = HashMultiset.create();
        thresholdEntry3.add("un DET:ART");
        thresholdEntry3.add("étude NOM");
        thresholdEntry3.add("comparer VER:pper");
        thresholdEntry3.add("archéo-ichtyofauniques ADJ");
        thresholdEntry3.add("livrer VER:pper");
        thresholdEntry3.add("par PRP");
        thresholdEntry3.add("du PRP:det");
        thresholdEntry3.add("donnée NOM");



        _thresholdExpectedLexicon.put("entry-575_lexOff",new LexiconProfile(thresholdEntry3));

        /*
        embedded multi case :  < w w T1 > T2 T3 w w w
         */
        Multiset<String> entry4 = HashMultiset.create();
        entry4.add("type NOM");
        entry4.add("de PRP");
        entry4.add("rejet NOM");
        entry4.add("précéder VER:ppre");
        entry4.add("leur DET:POS");
        entry4.add("enfouissement NOM");
        entry4.add(". SENT");
        _expectedLexicon.put("entry-5750_lexOff",new LexiconProfile(entry4));
        /*
        case : T1 <T2> w w w
         */
        Multiset<String> entry5 = HashMultiset.create();
        entry5.add("sur PRP");
        entry5.add("le DET:ART");
        entry5.add("deux NUM");
        entry5.add("site NOM");
        entry5.add("bronze NOM");
        entry5.add(". SENT");
        _expectedLexicon.put("entry-35_lexOff",new LexiconProfile(entry5));

        /*
        case : w T < w w w >
         */
        Multiset<String> entry6 = HashMultiset.create();
        entry6.add(", PUN");
        entry6.add("pêche NOM");
        entry6.add("être VER:pres");
        entry6.add("un DET:ART");
        entry6.add("le DET:ART");
        _expectedLexicon.put("entry-450_lexOff",new LexiconProfile(entry6));

        _expectedCorpus = HashMultiset.create();
        _expectedCorpus.addAll(entry1);
        _expectedCorpus.addAll(entry2);
        _expectedCorpus.addAll(entry3);
        _expectedCorpus.addAll(entry4);
        _expectedCorpus.addAll(entry5);
        _expectedCorpus.addAll(entry6);
        List<String> includeElements = new ArrayList<>();
        includeElements.add("p");
        includeElements.add("note");
        includeElements.add("head");
        includeElements.add("cit");
        includeElements.add("q");
        new ContextExtractor("src/test/resources/module/disambiguation/contextLexicon/contextExtractor/test1.xml",
                _observedLexicon,
                _observedCorpus).execute();

        List<String> authorizedTags = new ArrayList<>();
        authorizedTags.add("PRO:IND");
        new ContextExtractor("src/test/resources/module/disambiguation/contextLexicon/contextExtractor/test1.xml",
                _observedTagLexicon, authorizedTags,
                new CorpusLexicon(new HashMap<>(),new HashMap<>())).execute();

        new ContextExtractor("src/test/resources/module/disambiguation/contextLexicon/contextExtractor/test1.xml",
                _thresholdObservedLexicon,
                new CorpusLexicon(new HashMap<>(),new HashMap<>()),3).execute();

        new ContextExtractor("src/test/resources/module/disambiguation/contextLexicon/contextExtractor/test1.xml",
                _thresholdObservedLexicon2,
                new CorpusLexicon(new HashMap<>(),new HashMap<>()),60).execute();
    }

    @Test
    public void extractLexiconProfile() throws Exception {
        _expectedLexicon.forEach(
                (key, value) -> {
                    Multiset observed = _observedLexicon.get(key).getLexicalTable();
                    Multiset expected = value.getLexicalTable();
                    Assert.assertTrue(
                            showLexicon(observed,expected,key),
                            expected.equals(observed)
                    );
                }
        );
    }

    @Test
    public void extractTagLexiconProfile() throws Exception {

        _expectedTagLexicon.forEach(
                (key, value) -> {
                    Multiset observed = _observedTagLexicon.get(key).getLexicalTable();
                    Multiset expected = value.getLexicalTable();
                    Assert.assertTrue(showLexicon(observed,expected,key),
                            observed.equals(expected));
                });
    }

    @Test
    public void extractThresholdLexiconProfile() throws Exception {

        _thresholdExpectedLexicon.forEach(
                (key, value) -> {
                    Multiset observed = _thresholdObservedLexicon.get(key).getLexicalTable();
                    Multiset expected = value.getLexicalTable();
                    Assert.assertTrue(showLexicon(observed,expected,key),
                            observed.equals(expected));
                }
        );
    }

    @Test
    public void extractCorpus() {
        Assert.assertTrue(showLexicon(_observedCorpus.getLexicalTable(),_expectedCorpus,"corpus"),
                _expectedCorpus.equals(_observedCorpus.getLexicalTable()));
    }

    private String showLexicon(Multiset observedLexicon, Multiset expectedLexicon, String term){
        return "this two lexicons must be equals (" + term + ") : \n observed :" +
                observedLexicon.toString() +
                "\n expected :" + expectedLexicon.toString();
    }

}
