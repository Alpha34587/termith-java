package org.atilf.models.disambiguation;

import com.google.common.collect.Multiset;

import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ScoreTerm {
    Map<String, Multiset<String>> _context;
    int _totalOccurences;
    int _correctOccurences;
    float _recall;
    float _precision;
    float _f1Score;
}
