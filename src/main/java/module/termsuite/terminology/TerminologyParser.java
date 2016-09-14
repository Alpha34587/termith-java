package module.termsuite.terminology;

import models.TerminologyOffetId;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParser {
    private Path path;
    private Map<String,List<TerminologyOffetId>> standOffTerminology;
    private Map<String,String> idSource;
    public TerminologyParser(Path path) {
        this.path = path;
        this.standOffTerminology = new ConcurrentHashMap<>();
        this.idSource = new HashMap<>();
    }

    public Map<String, List<TerminologyOffetId>> getStandOffTerminology() {
        return standOffTerminology;
    }

    public void execute() {
        extractInputSource();
        extractTerm();
    }

    private void extractTerm() {

    }

    private void extractInputSource() {

    }
}
