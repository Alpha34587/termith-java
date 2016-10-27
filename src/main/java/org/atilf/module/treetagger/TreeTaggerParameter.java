package org.atilf.module.treetagger;

import java.util.Objects;

/**
 * @author Simon Meoni
 *         Created on 02/09/16.
 */
public class TreeTaggerParameter {

    private final String _treeTaggerHome;
    private boolean _sgml;
    private String _lang;

    public TreeTaggerParameter(boolean sgml, String lang, String treeTaggerHome){

        this._sgml = sgml;
        this._lang = lang;
        this._treeTaggerHome = treeTaggerHome;
    }

    public String parse() {
        String parameter = _treeTaggerHome + "/bin/tree-tagger " + _treeTaggerHome + "/lib/";

        if (Objects.equals(_lang, "fr")) {
            parameter += "french.par ";
        }
        else if (Objects.equals(_lang,"en")) {
            parameter += "english-utf8.par ";
        }

        if (_sgml) {
            parameter +=  "-sgml ";
        }
         return parameter + "-no-unknown -token -lemma";
    }

}


