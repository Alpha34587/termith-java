package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.Module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvalCorpusExtractor extends Module {
    private final Map<String, Map<String, EvaluationProfile>> _evalContext;
    private CorpusLexicon _corpusLexicon;

    public EvalCorpusExtractor(TermithIndex termithIndex) {
        _corpusLexicon = termithIndex.getCorpusLexicon();
        _corpusLexicon = new CorpusLexicon(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
        _evalContext = termithIndex.getEvaluationLexicon();

    }

    @Override
    protected void execute() {
        _evalContext.values().forEach(
                map -> map.values().forEach(
                        (ep) -> ep.getLexicalTable().entrySet().forEach(
                                occ -> _corpusLexicon.addMultiOccurrences(occ.toString(),ep.getLexicalTable().count(occ)
                        )
                )
        ));
    }
}
