package models;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 13/09/16.
 */
public class TermithIndex {

    private String outputPath;
    private String lang;
    private String treeTaggerHome;
    private Path base;
    private boolean trace;
    private List<Path> terminologies;
    private Map<String, StringBuffer> morphoSyntaxStandOff;
    private Map<String, StringBuffer> tokenizeTeiBody;
    private Path corpus;
    private Map<String, StringBuffer> extractedText;
    private Map<String, StringBuffer> xmlCorpus;


    public String getOutputPath() {
        return outputPath;
    }

    public String getLang() {
        return lang;
    }

    public String getTreeTaggerHome() {
        return treeTaggerHome;
    }

    public Path getBase() {
        return base;
    }

    public boolean isTrace() {
        return trace;
    }

    public List<Path> getTerminologies() {
        return terminologies;
    }

    public Map<String, StringBuffer> getMorphoSyntaxStandOff() {
        return morphoSyntaxStandOff;
    }

    public Map<String, StringBuffer> getTokenizeTeiBody() {
        return tokenizeTeiBody;
    }

    public Path getCorpus() {
        return corpus;
    }

    public Map<String, StringBuffer> getExtractedText() {
        return extractedText;
    }

    public Map<String, StringBuffer> getXmlCorpus() {
        return xmlCorpus;
    }

    /**
     * This class is used to instance a termITH object
     */
    public static class Builder
    {

    }
}
