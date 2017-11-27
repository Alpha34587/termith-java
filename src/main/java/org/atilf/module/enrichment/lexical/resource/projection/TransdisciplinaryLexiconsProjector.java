package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.atilf.resources.enrichment.ResourceProjection;

import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexiconsProjector extends PhraseologyProjector {


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
        super(id, morpho, transdisciplinaryOffsetIds, resourceProjection);
    }

    @Override
    protected void execute() {
        detectWords(1,5);
    }
}



