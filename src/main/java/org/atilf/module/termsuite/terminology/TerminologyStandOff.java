package org.atilf.module.termsuite.terminology;

import org.atilf.models.termsuite.MorphoSyntaxOffsetId;
import org.atilf.models.termsuite.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyStandOff implements Runnable{
    private final List<MorphoSyntaxOffsetId> _morpho;
    private final List<TermsOffsetId> _terminology;
    private NavigableMap<Integer,List<Integer>> _beginMap;
    private NavigableMap<Integer,List<Integer>> _endMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminologyStandOff.class.getName());
    private String _id;

    public TerminologyStandOff(List<MorphoSyntaxOffsetId> morpho, List<TermsOffsetId> terminology) {
        _morpho = morpho;
        _terminology = terminology;
    }

    public List<TermsOffsetId> getTerminology() {
        return _terminology;
    }

    public void execute() {
        createNavigablesMap();
        _terminology.forEach(
                el -> {
                    el.setIds(retrieveMorphoIds(el.getBegin(),el.getEnd()));
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
                    _beginMap.put(el.getBegin(),el.getIds());
                    _endMap.put(el.getEnd(),el.getIds());
                }
        );
    }

    @Override
    public void run() {
        LOGGER.debug("retrieve morphosyntax id for file :" + _id);
        this.execute();
        LOGGER.debug("retrieve id task finished");
    }

}
