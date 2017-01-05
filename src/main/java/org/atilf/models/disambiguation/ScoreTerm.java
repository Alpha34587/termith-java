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
    private String _flexionsWords = "";
    private int _totalOccurrences = 0;
    private int _correctOccurrence = 0;
    private int _missingOccurrence = 0;
    private float _validatedOccurrence = 0;
    private float _recall = 0;
    private float _precision = 0;
    private float _f1Score = 0;
    private float _terminologyTrend = 0;
    private float _ambiguityRate = 0;

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
        _missingOccurrence = missingOccurrence;
    }

    public void setFlexionsWords(String flexionsWords) {
        _flexionsWords = flexionsWords;
    }

    public float getRecall() {
        return _recall;
    }

    public float getPrecision() {
        return _precision;
    }

    public float getF1Score() {
        return _f1Score;
    }

    public float getAmbiguityRate() {
        return _ambiguityRate;
    }

    public float getTerminologyTrend() {
        return _terminologyTrend;
    }

    public synchronized String getFlexionsWords() {
        return _flexionsWords;
    }

    public float getValidatedOccurrence() {
        return _validatedOccurrence;
    }

    public void setRecall(float recall) {
        _recall = recall;
    }

    public void setPrecision(float precision) {
        _precision = precision;
    }

    public void setF1Score(float f1Score) {
        _f1Score = f1Score;
    }

    public void setTerminologyTrend(float terminologyTrend) {
        _terminologyTrend = terminologyTrend;
    }

    public void setAmbiguityRate(float ambiguityRate) {
        _ambiguityRate = ambiguityRate;
    }

    public void setValidatedOccurrence(float validatedOccurrence) {
        _validatedOccurrence = validatedOccurrence;
    }

    public synchronized void incValidatedOccurrence(){
        _validatedOccurrence++;
    }

    public synchronized void incCorrectOccurrence(){_correctOccurrence++;}

    public synchronized void incTotalOccurrence(){
        _totalOccurrences++;
    }

    public synchronized void incMissingOccurrence(){
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
