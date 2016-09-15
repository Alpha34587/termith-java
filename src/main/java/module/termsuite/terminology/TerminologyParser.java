package module.termsuite.terminology;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import models.TermsOffsetId;
import module.tools.FilesUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static eu.project.ttc.readers.JsonCasConstants.F_BEGIN;
import static eu.project.ttc.readers.JsonCasConstants.F_END;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParser {
    private Path path;
    private Map<String,List<TermsOffsetId>> standOffTerminology;
    private Map<String,String> idSource;
    private final JsonFactory factory = new JsonFactory();
    private String currentFile;

    public TerminologyParser(Path path) {
        this.path = path;
        standOffTerminology = new ConcurrentHashMap<>();
        idSource = new HashMap<>();
        currentFile = "";
    }

    public Map<String, List<TermsOffsetId>> getStandOffTerminology() {
        return standOffTerminology;
    }

    public void execute() throws IOException {

        JsonParser parser = factory.createParser(new File(path.toString()));
        JsonToken jsonToken;
        boolean inTerms = false;
        boolean inSource = false;
        boolean inOcc = false;
        TermsOffsetId offsetId = new TermsOffsetId();
        while ((jsonToken = parser.nextToken()) != null) {

            if (inSource) {
                if (jsonToken == JsonToken.END_OBJECT) {
                    inSource = false;
                }
                extractInputSource(jsonToken, parser);
            }

            else if (inTerms) {
                if (jsonToken == JsonToken.END_ARRAY && Objects.equals(parser.getCurrentName(), "terms")) {

                    inTerms = false;
                }

                else if (jsonToken == JsonToken.END_ARRAY && inOcc){
                    inOcc = false;
                }


                else if (jsonToken == JsonToken.END_OBJECT && inOcc) {
                    fillTerminology(offsetId);
                }

                else if (Objects.equals(parser.getCurrentName(), "occurrences")){
                    inOcc = true;
                }

                extractTerm(jsonToken, parser, offsetId);
            }
            else if ("input_sources".equals(parser.getParsingContext().getCurrentName())) {
                inSource = true;
            }

            else if ("terms".equals(parser.getParsingContext().getCurrentName())) {
                inTerms = true;
            }
        }
    }

    private void fillTerminology(TermsOffsetId offsetId) {
        String realId =
                FilesUtilities.nameNormalizer(idSource.get(currentFile));
        if (standOffTerminology.containsKey(realId)){
            standOffTerminology.get(realId).add(new TermsOffsetId(offsetId));
        }

        else {
            standOffTerminology.put(realId,new ArrayList<>(
                    Collections.singletonList(new TermsOffsetId(offsetId))));
        }
    }

    private void extractTerm(JsonToken jsonToken, JsonParser parser,
                             TermsOffsetId offetId) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)){


            switch (parser.getCurrentName()){
                case "id" :
                    offetId.setTermId(parser.nextIntValue(0));
                    break;
                case "text" :
                    offetId.setWord(parser.nextTextValue());
                    break;
                case F_BEGIN :
                    offetId.setBegin(parser.nextIntValue(0));
                    break;
                case F_END :
                    offetId.setEnd(parser.nextIntValue(0));
                    break;
                case "file" :
                    currentFile = String.valueOf(parser.nextIntValue(0));
                    break;
                default:
                    break;
            }
        }

    }

    private void extractInputSource(JsonToken jsonToken, JsonParser parser) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)) {
            idSource.put(parser.getCurrentName(),
                    FilesUtilities.nameNormalizer(parser.nextTextValue()));
        }
    }

}
