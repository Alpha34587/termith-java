package org.atilf.models.enrichment;

import java.util.Objects;

/**
 * this class contains TreeTagger parameter used by TreeTaggerWrapper
 * @author Simon Meoni
 *         Created on 02/09/16.
 */
public class TreeTaggerParameter {

    private final String _treeTaggerHome;
    private boolean _sgml;
    private String _lang;

    /**
     * constructor for TreeTaggerParameter
     *
     * @param sgml           activate/deactivate sgml option for treetagger (ignore or not the sgml tag)
     * @param lang           specify the lang used to tag files
     * @param treeTaggerHome the path of the application
     */
    public TreeTaggerParameter(boolean sgml, String lang, String treeTaggerHome) {
        _sgml = sgml;
        _lang = lang;
        _treeTaggerHome = treeTaggerHome;
    }

    /**
     * parse the field value and return a string with the desired options for TreeTagger
     * @return return parameters
     */
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


