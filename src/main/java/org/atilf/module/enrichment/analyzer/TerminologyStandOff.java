package org.atilf.module.enrichment.analyzer;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.TermOffsetId;
import org.atilf.module.Module;
import org.atilf.tools.FilesUtils;

import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * this class retrieve xml:id from w elements of morphology and links to the terms occurrences.
 *
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyStandOff extends Module {
    private final List<MorphologyOffsetId> _morpho;
    private final List<TermOffsetId> _terminology;
    private NavigableMap<Integer,List<Integer>> _beginMap = new TreeMap<>();
    private NavigableMap<Integer,List<Integer>> _endMap = new TreeMap<>();
    private String _id;

    public TerminologyStandOff(String id, TermithIndex termithIndex) {
        super(termithIndex);
        _id = id;
        _morpho = FilesUtils.readListObject(_termithIndex.getMorphologyStandOff().get(_id),MorphologyOffsetId.class);
        _terminology = termithIndex.getTerminologyStandOff().get(_id);
    }


    /**
     * constructor for TerminologyStandOffThread class
     * @param morpho the morphology entities analyzes by treetagger
     * @param terminology the terminologies occurrences compute by termsuite
     */
    TerminologyStandOff(List<MorphologyOffsetId> morpho, List<TermOffsetId> terminology) {
        _morpho = morpho;
        _terminology = terminology;
    }

    /**
     * getter for _terminology field
     * @return return List<TermOffsetId>
     */
    public List<TermOffsetId> getTerminology() {
        return _terminology;
    }

    /**
     * the execute method calls fillNavigableMaps and retrieves for each term occurrences, words associated with them
     */
    public void execute() {
        _logger.debug("retrieve morphosyntax id for file :" + _id);
        fillNavigableMaps();
        _terminology.forEach(
                el -> el.setIds(retrieveMorphologyIds(el.getBegin(),el.getEnd()))
        );
        _logger.debug("retrieve id task finished");
    }

    /**
     * make intersection between the _beginMap and the _endMap
     * @param begin the character offset of the beginning of the term
     * @param end the character offset of the end of the term
     * @return the xml:id of the w elements belongs to the term occurrences
     */
    private List<Integer> retrieveMorphologyIds(int begin, int end) {

        List<Integer> beginSubMap = castToIntList(_beginMap.subMap(begin, end));
        List<Integer> endSubMap = castToIntList(_endMap.subMap(begin, false, end, true));

        beginSubMap.retainAll(endSubMap);
        return beginSubMap;
    }

    /**
     * cast TreeMap to List<Integer>
     * @param integerListSortedMap the treeMap to cast
     * @return the int list of id
     */
    private List<Integer> castToIntList(SortedMap<Integer, List<Integer>> integerListSortedMap) {
        return integerListSortedMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * - fill _beginMap with the beginning character offset of words and the associated id
     * - fill _endMap with the ending character offset of words and the associated id
     */
    private void fillNavigableMaps() {
        _morpho.forEach(
                el -> {
                    _beginMap.put(el.getBegin(),el.getIds());
                    _endMap.put(el.getEnd(),el.getIds());
                }
        );
    }
}
