package org.atilf.resources.enrichment;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private String _tokenizePath;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TreeTaggerParameter.class.getName());

    /**
     * constructor for TreeTaggerParameter
     *
     * @param sgml           activate/deactivate sgml option for treetagger (ignore or not the sgml tag)
     * @param lang           specify the lang used to tag files
     * @param treeTaggerHome the path of the application
     */
    public TreeTaggerParameter(boolean sgml, String lang, String treeTaggerHome, String outputPath) {
        _sgml = sgml;
        _lang = lang;
        _treeTaggerHome = treeTaggerHome;
        _tokenizePath = copyTokenizeScript(outputPath);
    }

    private String copyTokenizeScript(String outputPath) {
        String perlPath =  outputPath + "/utf8-tokenize-custom.perl";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
                "module/enrichment/analyze/treeTaggerWorker/utf8-tokenize-custom.perl"
        );
        try {
            Files.write(Paths.get(perlPath), ByteStreams.toByteArray(resourceAsStream));
            boolean tryExecutable = new File(perlPath).setExecutable(true);
            if (!tryExecutable){
                throw new IOException("copy of utf8-tokenize-custom.perl failed, the file cannot be executable");
            }
        } catch (IOException e) {
            LOGGER.error("cannot copy termsuite-resources.jar : ",e);
        }
        return perlPath;
    }

    public String getTokenizePath() {
        return _tokenizePath;
    }

    public String getLang() {
        return _lang;
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


