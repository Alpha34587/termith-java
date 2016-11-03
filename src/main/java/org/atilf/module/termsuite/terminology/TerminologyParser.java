package org.atilf.module.termsuite.terminology;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.atilf.models.termsuite.TermsOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static eu.project.ttc.readers.JsonCasConstants.F_BEGIN;
import static eu.project.ttc.readers.JsonCasConstants.F_END;
import static org.atilf.models.termsuite.JsonTermResources.*;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParser {
    private Path _path;
    private Map<String,List<TermsOffsetId>> _standOffTerminology = new ConcurrentHashMap<>();
    private Map<String,String> _idSource = new HashMap<>();
    private String _currentFile;
    private final JsonFactory _factory = new JsonFactory();
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminologyParser.class.getName());

    public TerminologyParser(Path path) {
        _path = path;
        _currentFile = "";
    }

    public Map<String, List<TermsOffsetId>> get_standOffTerminology() {
        return _standOffTerminology;
    }

    public void execute() throws IOException {
        try {
            JsonParser parser = _factory.createParser(new File(_path.toString()));
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
                        break;
                    } else if (jsonToken == JsonToken.END_ARRAY && inOcc) {
                        inOcc = false;
                    } else if (jsonToken == JsonToken.END_OBJECT && inOcc) {
                        fillTerminology(offsetId);
                    } else if (Objects.equals(parser.getCurrentName(), "occurrences")) {
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
        catch (Exception e){
            LOGGER.error("cannot parse file",e);
        }
    }

    private void fillTerminology(TermsOffsetId offsetId) {
        String realId =
                FilesUtils.nameNormalizer(_idSource.get(_currentFile));
        if (_standOffTerminology.containsKey(realId)){
            _standOffTerminology.get(realId).add(new TermsOffsetId(offsetId));
        }

        else {
            _standOffTerminology.put(realId,new ArrayList<>(
                    Collections.singletonList(new TermsOffsetId(offsetId))));
        }
    }

    private void extractTerm(JsonToken jsonToken, JsonParser parser,
                             TermsOffsetId offsetId) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)){


            switch (parser.getCurrentName()){
                case T_ID :
                    offsetId.setTermId(parser.nextIntValue(0));
                    break;
                case T_TEXT :
                    offsetId.setWord(parser.nextTextValue());
                    break;
                case F_BEGIN :
                    offsetId.setBegin(parser.nextIntValue(0));
                    break;
                case F_END :
                    offsetId.setEnd(parser.nextIntValue(0));
                    break;
                case T_FILE :
                    _currentFile = String.valueOf(parser.nextIntValue(0));
                    break;
                default:
                    break;
            }
        }

    }

    private void extractInputSource(JsonToken jsonToken, JsonParser parser) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)) {
            _idSource.put(parser.getCurrentName(),
                    FilesUtils.nameNormalizer(parser.nextTextValue()));
        }
    }

}
