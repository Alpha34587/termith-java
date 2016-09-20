package org.atilf.module.treetagger;

import java.util.Objects;

/**
 * @author Simon Meoni
 *         Created on 02/09/16.
 */
public class TreeTaggerParameter {

    private final String treeTaggerHome;
    private boolean sgml;
    private String lang;

    public TreeTaggerParameter(boolean sgml, String lang, String treeTaggerHome){

        this.sgml = sgml;
        this.lang = lang;
        this.treeTaggerHome = treeTaggerHome;
    }

    public String parse() {
        String parameter = treeTaggerHome + "bin/tree-tagger " + treeTaggerHome + "lib/";

        if (Objects.equals(lang, "fr")) {
            parameter += "french.par ";
        }
        else if (Objects.equals(lang,"en")) {
            parameter += "english-utf8.par ";
        }

        if (sgml) {
            parameter +=  "-sgml ";
        }
         return parameter + "-token -lemma";
    }

}


