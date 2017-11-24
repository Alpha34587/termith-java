package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.models.TermithIndex;
import org.atilf.resources.enrichment.ResourceProjection;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.tools.FilesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexiconsProjector extends PhraseologyProjector {

    private List<MultiWordsOffsetId> _transdisciplinaryOffsetIds = new ArrayList<>();

    public TransdisciplinaryLexiconsProjector(String id, TermithIndex termithIndex,
                                              ResourceProjection transdisciplinaryResource) {
        this(
                id,
                FilesUtils.readListObject(termithIndex.getMorphologyStandOff().get(id)),
                termithIndex.getTransdisciplinaryOffsetId().get(id),
                transdisciplinaryResource
        );
    }

    TransdisciplinaryLexiconsProjector(String id, List<MorphologyOffsetId> morpho,
                                       List<MultiWordsOffsetId> transdisciplinaryOffsetIds,
                                       ResourceProjection resourceProjection) {
        super(id, morpho, null, resourceProjection);
        _transdisciplinaryOffsetIds = transdisciplinaryOffsetIds;
    }

    @Override
    protected void execute() {
        detectWords(1,5);
    }

    @Override
    protected void addNewEntry(List<MorphologyOffsetId> listMorphologyOffsetId, String lemma,
                               List<Integer> ids, Integer entryId) {
        _transdisciplinaryOffsetIds.add(new MultiWordsOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId,
                _resourceProjection.getMultiWordsMap().get(lemma),ids));
    }
}



