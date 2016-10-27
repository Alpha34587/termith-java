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
    private final List<MorphoSyntaxOffsetId> _morpho;
    private final List<TermsOffsetId> _termino;
    private NavigableMap<Integer,List<Integer>> _beginMap;
    private NavigableMap<Integer,List<Integer>> _endMap;

    public TerminologyStandOff(List<MorphoSyntaxOffsetId> morpho, List<TermsOffsetId> termino) {
        _morpho = morpho;
        _termino = termino;
    }

    public List<TermsOffsetId> get_termino() {
        return _termino;
    }

    public void execute() {
        createNavigablesMap();
        _termino.forEach(
                el -> {
                    el.setIds(retrieveMorphoIds(el.get_begin(),el.get_end()));
                }
        );
    }

    private List<Integer> retrieveMorphoIds(int begin, int end) {

        List<Integer> beginSubMap = castToIntList(_beginMap.subMap(begin, end));
        List<Integer> endSubMap = castToIntList(_endMap.subMap(begin, false, end, true));

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
        _beginMap = new TreeMap<>();
        _endMap = new TreeMap<>();
        _morpho.forEach(
                el -> {
                    _beginMap.put(el.get_begin(),el.get_ids());
                    _endMap.put(el.get_end(),el.get_ids());
                }
        );
    }

}
