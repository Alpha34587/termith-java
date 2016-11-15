package org.atilf.module.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class EvaluationTest {
    private Map<String,EvaluationProfile> _evaluationProfileMap  = new HashMap<>();
    private List<String> _disambIdObserved;

    @Before
    public void setUp() throws Exception {
        Map<String, LexiconProfile> lexicalProfileMap = new HashMap<>();
        Evaluation evaluation = new Evaluation(_evaluationProfileMap, lexicalProfileMap);

        Multiset<String> entry1 = HashMultiset.create();
        entry1.add("du PRP:det");
        entry1.add("le DET:ART");
        entry1.add(". SENT");
        entry1.add("sur PRP");
        entry1.add("le DET:ART");
        entry1.add("deux NUM");
        entry1.add("site NOM");
        _evaluationProfileMap.put("entry-13471_DM1",new EvaluationProfile(entry1));


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
        _evaluationProfileMap.put("entry-7263_DM3",new EvaluationProfile(entry2));

        Multiset<String> entry3 = HashMultiset.create();
        entry3.add("du PRP:det");
        entry3.add("le DET:ART");
        entry3.add(". SENT");
        entry3.add("sur PRP");
        entry3.add("le DET:ART");
        entry3.add("deux NUM");

        lexicalProfileMap.put("entry-13471_lexOn",new LexiconProfile(entry3));
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec("du PRP:det", 1f);
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec("le DET:ART", 1f);
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec(". SENT", 1f);
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec("sur PRP", 1f);
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec("le DET:ART", 1f);
        lexicalProfileMap.get("entry-13471_lexOn").addCoefficientSpec("deux NUM", 1f);

        Multiset<String> entry4 = HashMultiset.create();
        entry4.add("pêche NOM");
        entry4.add(", PUN");
        entry4.add("limiter VER:pper");
        entry4.add("à PRP");
        entry4.add("quelque PRO:IND");
        entry4.add("espèce NOM");
        entry4.add("il PRO:PER");
        lexicalProfileMap.put("entry-13471_lexOff",new LexiconProfile(entry4));
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("pêche NOM", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec(", PUN", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("limiter VER:pper", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("à PRP", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("quelque PRO:IND", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("espèce NOM", 1f);
        lexicalProfileMap.get("entry-13471_lexOff").addCoefficientSpec("il PRO:PER", 1f);

        Multiset<String> entry5 = HashMultiset.create();
        entry5.add("pêche NOM");
        entry5.add(", PUN");
        entry5.add("limiter VER:pper");
        entry5.add("à PRP");
        entry5.add("quelque PRO:IND");
        entry5.add("espèce NOM");
        entry5.add("commun ADJ");
        entry5.add(". SENT");
        entry5.add("il PRO:PER");
        lexicalProfileMap.put("entry-7263_lexOn",new LexiconProfile(entry5));
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("pêche NOM", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec(", PUN", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("limiter VER:pper", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("à PRP", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("quelque PRO:IND", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("espèce NOM", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("commun ADJ", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec(". SENT", 1f);
        lexicalProfileMap.get("entry-7263_lexOn").addCoefficientSpec("il PRO:PER", 1f);

        Multiset<String> entry6 = HashMultiset.create();
        entry6.add("pêche NOM");
        entry6.add("pêche NOM");
        entry6.add("pêche NOM");
        entry6.add("pêche NOM");
        entry6.add(", PUN");
        entry6.add("limiter VER:pper");
        entry6.add("à PRP");
        entry6.add("quelque PRO:IND");
        entry6.add("espèce NOM");
        entry6.add("commun ADJ");
        entry6.add(". SENT");
        entry6.add("il PRO:PER");
        lexicalProfileMap.put("entry-7263_lexOff",new LexiconProfile(entry6));
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("pêche NOM", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec(", PUN", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("limiter VER:pper", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("à PRP", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("quelque PRO:IND", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("espèce NOM", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("commun ADJ", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec(". SENT", 1f);
        lexicalProfileMap.get("entry-7263_lexOff").addCoefficientSpec("il PRO:PER", 1f);
        evaluation.execute();

        _disambIdObserved = new ArrayList<>();
        _disambIdObserved.add("DaOn");
        _disambIdObserved.add("DaOn");
    }

    @Test
    public void execute() throws Exception {
        int cnt = 0;
        for (EvaluationProfile evaluationProfile : _evaluationProfileMap.values()) {
            Assert.assertEquals("this value must be equals", _disambIdObserved.get(cnt),evaluationProfile.getDisambiguationId());
            cnt++;
        }
    }

}