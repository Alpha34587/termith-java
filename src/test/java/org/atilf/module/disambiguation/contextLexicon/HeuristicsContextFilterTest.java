package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.disambiguation.LexiconProfile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Simon Meoni on 16/05/17.
 */
public class HeuristicsContextFilterTest {
    public static Map<String,LexiconProfile> _lexiconProfileMap = new HashMap<>();
    public static HeuristicsContextFilter _heuristicsContextFilter;
    public static Set<String> _expectedKey = new HashSet<>();

    @BeforeClass
    public static void setUp() throws Exception {
        _lexiconProfileMap.put("entry1_lexOn",new LexiconProfile());
        _lexiconProfileMap.put("entry2_lexOn",new LexiconProfile());
        _lexiconProfileMap.put("entry2_lexOff",new LexiconProfile());
        _lexiconProfileMap.put("entry3_lexOn",new LexiconProfile());
        _lexiconProfileMap.put("entry4_lexOn",new LexiconProfile());
        _heuristicsContextFilter = new HeuristicsContextFilter(_lexiconProfileMap);
        _expectedKey.add("entry2_lexOn");
        _expectedKey.add("entry2_lexOff");
    }

    @Test
    public void execute() throws Exception {
        _heuristicsContextFilter.execute();
        Set<String> observedKey = _lexiconProfileMap.keySet();
        Assert.assertArrayEquals("the expected and observed key must be equals",
                observedKey.toArray(),
                _expectedKey.toArray());
    }

}
