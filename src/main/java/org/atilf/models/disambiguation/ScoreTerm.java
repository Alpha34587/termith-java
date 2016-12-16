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
    private Map<String, Multiset<String>> _context = new ConcurrentHashMap<>();
    private List<List<ContextWord>> _termWords = new CopyOnWriteArrayList<>();
    private int _totalOccurrences = 0;
    private int _correctOccurrence = 0;
    private int _missingOccurrence = 0;
    private float _recall = 0;
    private float _precision = 0;
    private float _f1Score = 0;

    public void addTermWords(List<ContextWord> term){
        _termWords.add(term);
    }

    public void addContext(String filename, Multiset<String> context){
        _context.put(filename,context);
    }

    public void setTotalOccurrences(int totalOccurrences) {
        _totalOccurrences = totalOccurrences;
    }

    public void setCorrectOccurrence(int correctOccurrence) {
        _correctOccurrence = correctOccurrence;
    }

    public void setMissingOccurrence(int missingOccurrence) {
        _missingOccurrence = missingOccurrence;}

    public void setRecall(float recall) {
        _recall = recall;
    }

    public void setPrecision(float precision) {
        _precision = precision;
    }

    public void setF1Score(float f1Score) {
        _f1Score = f1Score;
    }

    public synchronized void incCorrectOccurence(){
        _correctOccurrence++;
    }
    
    public synchronized void incTotalOccurence(){
        _totalOccurrences++;
    }
    
    public synchronized void incMissingOccurence(){
        _missingOccurrence++;
    }
    
    public List<List<ContextWord>> getTermWords() {
        return _termWords;
    }

    public int getTotalOccurrences() {
        return _totalOccurrences;
    }

    public int getCorrectOccurrence() {
        return _correctOccurrence;
    }

    public int getMissingOccurrence() {
        return _missingOccurrence;
    }
}
