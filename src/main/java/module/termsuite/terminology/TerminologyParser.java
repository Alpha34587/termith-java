package module.termsuite.terminology;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import models.TerminologyOffetId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParser {
    private Path path;
    private Map<String,List<TerminologyOffetId>> standOffTerminology;
    private Map<String,String> idSource;
    private final JsonFactory factory = new JsonFactory();

    public TerminologyParser(Path path) {
        this.path = path;
        this.standOffTerminology = new ConcurrentHashMap<>();
        this.idSource = new HashMap<>();
    }

    public Map<String, List<TerminologyOffetId>> getStandOffTerminology() {
        return standOffTerminology;
    }

    public void execute() throws IOException {

        JsonParser parser = factory.createParser(new File(path.toString()));
        JsonToken jsonToken;
        boolean inTerms = false;
        boolean inSource = false;
        while ((jsonToken = parser.nextToken()) != null) {

            if (inSource) {
                if (jsonToken == JsonToken.END_OBJECT) {
                    inSource = false;
                }
                extractInputSource(jsonToken, parser);
            } else if (inTerms) {
                if (jsonToken == JsonToken.END_ARRAY && Objects.equals(parser.getCurrentName(), "terms")) {
                    inTerms = false;
                }
                extractTerm();

            } else if ("input_sources".equals(parser.getParsingContext().getCurrentName())) {
                inSource = true;
            } else if ("terms".equals(parser.getParsingContext().getCurrentName())) {
                inTerms = true;
            }
        }
    }

    private void extractTerm() {

    }

    private void extractInputSource(JsonToken jsonToken, JsonParser parser) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)) {
            idSource.put(parser.getCurrentName(), parser.nextTextValue());
        }
    }
}
