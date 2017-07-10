package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.LexicalResourceProjectionResources;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.ResourceProjectorOffsetId;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjector extends Module{

    private List<MorphologyOffsetId> _morpho;
    private List<ResourceProjectorOffsetId> _resourceProjectorOffsetIds;
    protected LexicalResourceProjectionResources _lexicalResourceProjectionResources;
    private String _id;

    public PhraseologyProjector(String id, TermithIndex termithIndex, LexicalResourceProjectionResources lexicalResourceProjectionResources) {
        this(id,FilesUtils.readListObject(termithIndex.getMorphologyStandOff().get(id)),termithIndex.getPhraseoOffsetId().get(id),
                lexicalResourceProjectionResources);
    }

    PhraseologyProjector(String id, List<MorphologyOffsetId> morpho, List<ResourceProjectorOffsetId> resourceProjectorOffsetIds,
                         LexicalResourceProjectionResources lexicalResourceProjectionResources){
        _id = id;
        _morpho = morpho;
        _lexicalResourceProjectionResources = lexicalResourceProjectionResources;
        _resourceProjectorOffsetIds = resourceProjectorOffsetIds;
    }

    @Override
    protected void execute() {
        _logger.info("projection of phraseology for file : {} is started",_id);
        detectWords(2,5);
        _logger.info("projection of phraseology for file : {} is finished",_id);
    }

    void detectWords(int wordSize, int wordSizeThreshold) {
        int memWordSize = wordSize;
        for (MorphologyOffsetId mOffsetId : _morpho) {
            wordSize = Integer.valueOf(memWordSize);
            String currentLemma = mOffsetId.getLemma() + " ";
            List<MorphologyOffsetId> currentMorphoOffsetId = new ArrayList<>();
            currentMorphoOffsetId.add(mOffsetId);
            while(wordSize <= wordSizeThreshold){

                int wordAfter = _morpho.indexOf(mOffsetId) + wordSize - 1;
                if (wordAfter < _morpho.size()) {
                    if (wordSize > 1) {
                        currentLemma += _morpho.get(wordAfter).getLemma() + " ";
                        currentMorphoOffsetId.add(_morpho.get(wordAfter));
                    }
                    if(_lexicalResourceProjectionResources.getResourceMap().containsKey(currentLemma.trim())){
                        _logger.debug("detection of expression : {}",currentLemma);
                        addToProjectedData(currentMorphoOffsetId,
                                _lexicalResourceProjectionResources.getResourceMap().get(currentLemma.trim()));
                    }
                    wordSize++;
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
        for (Integer entryId : new HashSet<>(EntryIds)) {
            addNewEntry(listMorphologyOffsetId, lemma, new ArrayList<>(ids), entryId);
        }
    }

    protected void addNewEntry(List<MorphologyOffsetId> listMorphologyOffsetId, String lemma, List<Integer> ids, Integer entryId) {
        _resourceProjectorOffsetIds.add(new ResourceProjectorOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId,
                _lexicalResourceProjectionResources.getPhraseologyWordsMap().get(lemma),ids));
    }
}
