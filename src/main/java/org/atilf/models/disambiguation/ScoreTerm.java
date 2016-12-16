package org.atilf.models.disambiguation;

import com.google.common.collect.Multiset;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ScoreTerm {
    Map<String, Multiset<String>> _context = new ConcurrentHashMap<>();
    List<List<ContextWord>> _termWords = new CopyOnWriteArrayList<>();
    int _totalOccurences = 0;
    int _correctOccurences = 0;
    int _missingOccurence = 0;
    float _recall = 0;
    float _precision = 0;
    float _f1Score = 0;

    public void addTermWords(List<ContextWord> term){
        _termWords.add(term);
    }

    public void addContext(String filename, Multiset<String> context){
        _context.put(filename,context);
    }

    public void setTotalOccurences(int totalOccurences) {
        _totalOccurences = totalOccurences;
    }

    public void setCorrectOccurences(int correctOccurences) {
        _correctOccurences = correctOccurences;
    }

    public void setMissingOccurence(int missingOccurence) {_missingOccurence = missingOccurence;}

    public void setRecall(float recall) {
        _recall = recall;
    }

    public void setPrecision(float precision) {
        _precision = precision;
    }

    public void setF1Score(float f1Score) {
        _f1Score = f1Score;
    }

    public List<List<ContextWord>> getTermWords() {
        return _termWords;
    }

    public int getTotalOccurences() {
        return _totalOccurences;
    }

    public int getCorrectOccurences() {
        return _correctOccurences;
    }

    public int getMissingOccurence() {
        return _missingOccurence;
    }
}
