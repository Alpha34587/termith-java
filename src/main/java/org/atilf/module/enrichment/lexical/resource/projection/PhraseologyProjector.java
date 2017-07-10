package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.LexicalResourceProjectionResources;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjector extends Module{

    private List<MorphologyOffsetId> _morphologyOffset;
    private List<MultiWordsOffsetId> _resourceProjectorOffsetIds;
    LexicalResourceProjectionResources _lexicalResourceProjectionResources;
    private String _id;

    public PhraseologyProjector(String id, TermithIndex termithIndex, LexicalResourceProjectionResources lexicalResourceProjectionResources) {
        this(id,FilesUtils.readListObject(termithIndex.getMorphologyStandOff().get(id)),termithIndex.getPhraseoOffsetId().get(id),
                lexicalResourceProjectionResources);
    }

    PhraseologyProjector(String id, List<MorphologyOffsetId> morphologyOffset, List<MultiWordsOffsetId> resourceProjectorOffsetIds,
                         LexicalResourceProjectionResources lexicalResourceProjectionResources){
        _id = id;
        _morphologyOffset = morphologyOffset;
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
        for (MorphologyOffsetId mOffsetId : _morphologyOffset) {
            int currentWordSize = memWordSize;
            StringBuilder currentLemma = new StringBuilder(mOffsetId.getLemma() + " ");
            List<MorphologyOffsetId> currentMorphologyOffsetId = new ArrayList<>();
            currentMorphologyOffsetId.add(mOffsetId);
            while(currentWordSize <= wordSizeThreshold){

                int wordAfter = _morphologyOffset.indexOf(mOffsetId) + currentWordSize - 1;
                if (wordAfter < _morphologyOffset.size()) {
                    if (currentWordSize > 1) {
                        currentLemma.append(_morphologyOffset.get(wordAfter).getLemma()).append(" ");
                        currentMorphologyOffsetId.add(_morphologyOffset.get(wordAfter));
                    }
                    String currentTrimmedLemma = currentLemma.toString().trim();
                    if(_lexicalResourceProjectionResources.getResourceMap().containsKey(currentTrimmedLemma)){
                        _logger.debug("detection of expression : {}",currentLemma);
                        addToProjectedData(currentMorphologyOffsetId,
                                _lexicalResourceProjectionResources.getResourceMap().get(currentTrimmedLemma));
                    }
                    currentWordSize++;
                }
                else {
                    break;
                }

            }
        }
    }

    private void addToProjectedData(List<MorphologyOffsetId> listMorphologyOffsetId, List<Integer> entryIds) {
        StringBuilder lemma = new StringBuilder("");
        List<Integer> ids = new ArrayList<>();
        for (MorphologyOffsetId morphologyOffsetId : listMorphologyOffsetId) {
            lemma.append(morphologyOffsetId.getLemma()).append(" ");
            ids.addAll(morphologyOffsetId.getIds());
        }
        String trimmedLemma = lemma.toString().trim();
        for (Integer entryId : new HashSet<>(entryIds)) {
            addNewEntry(listMorphologyOffsetId, trimmedLemma, new ArrayList<>(ids), entryId);
        }
    }

    protected void addNewEntry(List<MorphologyOffsetId> listMorphologyOffsetId, String lemma, List<Integer> ids, Integer entryId) {
        _resourceProjectorOffsetIds.add(new MultiWordsOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId,
                _lexicalResourceProjectionResources.getMultiWordsMap().get(lemma),ids));
    }
}
