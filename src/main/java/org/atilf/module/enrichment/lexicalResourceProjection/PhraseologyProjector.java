package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.LexicalResourceProjectionResources;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.PhraseoOffsetId;
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
    private final List<PhraseoOffsetId> _phraseoOffsetIds;
    private LexicalResourceProjectionResources _lexicalResourceProjectionResources;
    private String _id;

    public PhraseologyProjector(String id, TermithIndex termithIndex, LexicalResourceProjectionResources lexicalResourceProjectionResources) {
        this(id,FilesUtils.readListObject(termithIndex.getMorphologyStandOff().get(id)),termithIndex
                .getPhraseoOffetId().get(id), lexicalResourceProjectionResources);
        _id = id;
    }

    PhraseologyProjector(String id, List<MorphologyOffsetId> morpho, List<PhraseoOffsetId> phraseoOffsetIds,
                         LexicalResourceProjectionResources lexicalResourceProjectionResources){
        _id = id;
        _morpho = morpho;
        _phraseoOffsetIds = phraseoOffsetIds;
        _lexicalResourceProjectionResources = lexicalResourceProjectionResources;
    }

    @Override
    protected void execute() {
        _logger.info("projection of phraseologie for file : {} is started",_id);
        detectwords(2,5);
        _logger.info("projection of phraseologie for file : {} is finished",_id);
    }

    void detectwords(int wordsize, int wordSizeThreshlod) {
        int memWordSize = wordsize;
        for (MorphologyOffsetId mOffsetId : _morpho) {
            wordsize = Integer.valueOf(memWordSize);
            String currentLemma = mOffsetId.getLemma() + " ";
            List<MorphologyOffsetId> currentMorphoOffsetId = new ArrayList<>();
            currentMorphoOffsetId.add(mOffsetId);
            while(wordsize <= wordSizeThreshlod){

                int wordAfter = _morpho.indexOf(mOffsetId) + wordsize - 1;
                if (wordAfter < _morpho.size()) {
                    if (wordsize > 1) {
                        currentLemma += _morpho.get(wordAfter).getLemma() + " ";
                        currentMorphoOffsetId.add(_morpho.get(wordAfter));
                    }
                    if(_lexicalResourceProjectionResources.getResourceMap().containsKey(currentLemma.trim())){
                        _logger.debug("detection of expression : {}",currentLemma);
                        addToProjectedData(currentMorphoOffsetId,
                                _lexicalResourceProjectionResources.getResourceMap().get(currentLemma.trim()));
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
        for (Integer entryId : new HashSet<>(EntryIds)) {
            addNewEntry(listMorphologyOffsetId, lemma, new ArrayList<>(ids), entryId);
        }
    }

    protected void addNewEntry(List<MorphologyOffsetId> listMorphologyOffsetId, String lemma, List<Integer> ids, Integer entryId) {
        _phraseoOffsetIds.add(new PhraseoOffsetId(listMorphologyOffsetId.get(0).getBegin(),
                listMorphologyOffsetId.get(listMorphologyOffsetId.size() - 1).getEnd(), entryId, lemma,ids));
    }
}
