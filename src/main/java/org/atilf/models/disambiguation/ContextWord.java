package org.atilf.models.disambiguation;

/**
 * @author Simon Meoni Created on 02/12/16.
 */
public class ContextWord {
    private String _target;
    private String _posLemma;

    public ContextWord(String target) {
        _target = target;
    }

    public void setPosLemma(String posLemma) {
        _posLemma = posLemma;
    }

    public String getTarget() {
        return _target;
    }

    public String getPosLemma() {
        return _posLemma;
    }
}
