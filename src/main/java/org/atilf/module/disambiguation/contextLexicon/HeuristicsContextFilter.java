package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.Module;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Simon Meoni on 16/05/17.
 */
public class HeuristicsContextFilter extends Module{


    private Map<String, LexiconProfile> _contextLexiconMap;

    public HeuristicsContextFilter(TermithIndex termithIndex) {
        this(termithIndex.getContextLexicon());
    }

    HeuristicsContextFilter(Map<String,LexiconProfile> ContextLexiconMap){
        _contextLexiconMap = ContextLexiconMap;
    }

    @Override
    protected void execute() {
        Set<String> entries = new HashSet<>(_contextLexiconMap.keySet());
        entries.stream()
                .filter(el -> !_contextLexiconMap.containsKey(el.replace("lexOn","lexOff"))
                || !_contextLexiconMap.containsKey(el.replace("lexOff","lexOn")))
                .forEach(el -> _contextLexiconMap.remove(el));
    }
}
