package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;

import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjector extends Module{

    private final List<MorphologyOffsetId> _morpho;
    private String _id;

    public PhraseologyProjector(String id, TermithIndex termithIndex) {
        super(termithIndex);
        _id = id;
        _morpho = FilesUtils.readListObject(_termithIndex.getMorphologyStandOff().get(_id));
    }

    @Override
    protected void execute() {

    }
}
