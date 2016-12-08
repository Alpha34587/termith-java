package org.atilf.models.disambiguation;

/**
 * @author Simon Meoni Created on 02/12/16.
 */
public class ContextWord {
    private int _target;
    private String _posLemma;

    public ContextWord(String target) {
        _target = Integer.parseInt(target.replace("t",""));
    }

    public void setPosLemma(String posLemma) {
        _posLemma = posLemma;
    }

    public int getTarget() {
        return _target;
    }

    public String getPosLemma() {
        return _posLemma;
    }
}
