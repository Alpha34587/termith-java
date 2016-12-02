package org.atilf.models.disambiguation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Simon Meoni Created on 02/12/16.
 */
public class ContextTerm {
    private String _corresp;
    private String _ana;
    private List<String> _target;

    public ContextTerm(String corresp, String ana, String target) {
        _corresp = corresp;
        _ana = ana;
        _target = Arrays.asList(target.replace("#","").split(" "));
    }

    public String getCorresp() {
        return _corresp;
    }

    public String getAna() {
        return _ana;
    }

    public List<String> getTarget() {
        return _target;
    }

    public boolean inTerm(String target) { return _target.contains(target); }
}
