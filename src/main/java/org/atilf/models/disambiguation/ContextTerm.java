package org.atilf.models.disambiguation;

import java.util.Arrays;
import java.util.List;

/**
 * @author Simon Meoni Created on 02/12/16.
 */
public class ContextTerm {
    private String _corresp;
    private String _ana;
    private int _beginTag;
    private int _endTag;

    public ContextTerm(String corresp, String ana, String target) {
        _corresp = corresp;
        _ana = ana;
        List<String> tags = Arrays.asList(target.replace("#t","").split(" "));
        _beginTag = Integer.parseInt(tags.get(0));
        _endTag = Integer.parseInt(tags.get(tags.size()-1));
    }


    public ContextTerm(String corresp, String target) {
        _corresp = corresp;
        List<String> tags = Arrays.asList(target.replace("#t","").split(" "));
        _beginTag = Integer.parseInt(tags.get(0));
        _endTag = Integer.parseInt(tags.get(tags.size()-1));
    }

    public String getCorresp() {
        return _corresp;
    }

    public String getAna() {
        return _ana;
    }

    public int getBeginTag() {
        return _beginTag;
    }

    public int getEndTag() {
        return _endTag;
    }

    public boolean inTerm(int target) {
        return _beginTag <= target && target <= _endTag;

    }
}
