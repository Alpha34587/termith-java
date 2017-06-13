package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.LexicalResourceProjectionResources;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.TransdisciplinaryOffsetId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexicsProjector extends PhraseologyProjector {

    List<TransdisciplinaryOffsetId> _transdisciplinaryOffsetIds = new ArrayList<>();

    public TransdisciplinaryLexicsProjector(String id, TermithIndex termithIndex, LexicalResourceProjectionResources transdisciplinaryResource) {
        super(id, termithIndex, transdisciplinaryResource);
        termithIndex.getTransOffsetId().put(id,_transdisciplinaryOffsetIds);
    }

    protected TransdisciplinaryLexicsProjector(String id, List<MorphologyOffsetId> morpho, List<TransdisciplinaryOffsetId> transdisciplinaryOffsetIds,
                                               LexicalResourceProjectionResources lexicalResourceProjectionResources) {
        super(id, morpho, lexicalResourceProjectionResources);
        _transdisciplinaryOffsetIds = transdisciplinaryOffsetIds;
    }

    @Override
    protected void execute() {
        detectwords(1,5);
    }

    @Override
    protected void addNewEntry(List<MorphologyOffsetId> listMorphologyOffsetId, String lemma, List<Integer> ids, Integer entryId) {
        _transdisciplinaryOffsetIds.add(new TransdisciplinaryOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId,
                _lexicalResourceProjectionResources.getPhraseologyWordsMap().get(lemma),ids));
    }
}



