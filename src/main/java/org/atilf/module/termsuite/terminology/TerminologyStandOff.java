package org.atilf.module.termsuite.terminology;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.TermsOffsetId;

import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyStandOff {
    private final String id;
    private final List<MorphoSyntaxOffsetId> morpho;
    private final List<TermsOffsetId> termino;
    private NavigableMap<Integer,List<Integer>> beginMap;
    private NavigableMap<Integer,List<Integer>> endMap;

    public TerminologyStandOff(String id, List<MorphoSyntaxOffsetId> morpho, List<TermsOffsetId> termino) {

        this.id = id;
        this.morpho = morpho;
        this.termino = termino;
    }

    public List<TermsOffsetId> getTermino() {
        return termino;
    }

    public void execute() {
        createNavigablesMap();

        termino.forEach(
                el -> {
                    el.setIds(retrieveMorphoIds(el.getBegin(),el.getEnd()));
                }
        );
    }

    private List<Integer> retrieveMorphoIds(int begin, int end) {

        List<Integer> beginSubMap = castToIntList(beginMap.subMap(begin, end));
        List<Integer> endSubMap = castToIntList(endMap.subMap(begin, false, end, true));

        beginSubMap.retainAll(endSubMap);
        return beginSubMap;
    }

    private List<Integer> castToIntList(SortedMap<Integer, List<Integer>> integerListSortedMap) {
        return integerListSortedMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private void createNavigablesMap() {
        beginMap = new TreeMap<>();
        endMap = new TreeMap<>();
        morpho.forEach(
                el -> {
                    beginMap.put(el.getBegin(),el.getIds());
                    endMap.put(el.getEnd(),el.getIds());
                }
        );
    }

}
