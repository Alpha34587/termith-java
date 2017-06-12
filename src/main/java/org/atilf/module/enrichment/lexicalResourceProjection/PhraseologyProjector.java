package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.PhraseoOffsetId;
import org.atilf.models.enrichment.PhraseologyResources;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjector extends Module{

    private List<MorphologyOffsetId> _morpho;
    private final List<PhraseoOffsetId> _phraseoOffsetIds;
    private PhraseologyResources _phraseologyResources;
    private List<PhraseoOffsetId> _phraseo;
    private String _id;

    public PhraseologyProjector(String id, TermithIndex termithIndex, PhraseologyResources phraseologyResources) {
        this(id,FilesUtils.readListObject(termithIndex.getMorphologyStandOff().get(id)),termithIndex
                .getPhraseoOffetId().get(id),phraseologyResources);
        _id = id;
    }

    public PhraseologyProjector(String id, List<MorphologyOffsetId> morpho, List<PhraseoOffsetId> phraseoOffsetIds,
                                PhraseologyResources phraseologyResources){
        _id = id;
        _morpho = morpho;
        _phraseoOffsetIds = phraseoOffsetIds;
        _phraseologyResources = phraseologyResources;
    }

    @Override
    protected void execute() {
        detectMultiwords(2,5);
    }

    private void detectMultiwords(int wordsize,int wordSizeThreshlod) {
        int memWordSize = wordsize;
        for (MorphologyOffsetId mOffsetId : _morpho) {
            wordsize = Integer.valueOf(memWordSize);
            String currentLemma = mOffsetId.getLemma() + " ";
            List<MorphologyOffsetId> currentMorphoOffsetId = new ArrayList<>();
            currentMorphoOffsetId.add(mOffsetId);
            while(wordsize <= wordSizeThreshlod){
                int wordAfter = _morpho.indexOf(mOffsetId) + wordsize - 1;
                if (wordAfter < _morpho.size()) {
                    currentLemma += _morpho.get(wordAfter).getLemma() + " ";
                    currentMorphoOffsetId.add(_morpho.get(wordAfter));
                    if(_phraseologyResources.getPhraseologyMap().containsKey(currentLemma.trim())){
                        addToProjectedData(currentMorphoOffsetId,
                                _phraseologyResources.getPhraseologyMap().get(currentLemma.trim()));
                    }
                    wordsize++;
                }
                else {
                    break;
                }

            }
        }
    }

    private void addToProjectedData(List<MorphologyOffsetId> listMorphologyOffsetId, List<Integer> EntryIds) {
        String lemma = "";
        List<Integer> ids = new ArrayList<>();
        for (MorphologyOffsetId morphologyOffsetId : listMorphologyOffsetId) {
            lemma += morphologyOffsetId.getLemma() + " ";
            ids.addAll(morphologyOffsetId.getIds());
        }
        lemma = lemma.trim();
        for (Integer entryId : EntryIds) {
            _phraseoOffsetIds.add(new PhraseoOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                    listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId, lemma,ids));
        }
    }
}
