package org.atilf.module.enrichment.analyzer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.TermOffsetId;
import org.atilf.module.Module;
import org.atilf.tools.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static eu.project.ttc.readers.JsonCasConstants.F_BEGIN;
import static eu.project.ttc.readers.JsonCasConstants.F_END;
import static org.atilf.models.enrichment.JsonTermResources.*;

/**
 * parse termsuite json termsuite terminology
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParser extends Module {
    private Path _path;
    private Map<String,List<TermOffsetId>> _standOffTerminology = new ConcurrentHashMap<>();
    private Map<String,String> _idSource = new HashMap<>();
    private String _currentFile = "";
    private final JsonFactory _factory = new JsonFactory();

    /**
     * constructor for TerminologyParser*
     * @param path the path of the terminology
     */
    TerminologyParser(Path path) {
        _path = path;
    }

    /**
     * constructor for TerminologyParser
     * @param termithIndex the termithIndex of a process
     */
    public TerminologyParser(TermithIndex termithIndex) {
        super(termithIndex);
        _path = termithIndex.getJsonTerminology();
    }

    /**
     * getter for parsed terminology
     * @return return Map<String, List<TermOffsetId>>
     */
    Map<String, List<TermOffsetId>> getStandOffTerminology() {
        return _standOffTerminology;
    }

    /**
     * parse the terminology with streaming json parser (jackson xml).
     * This method catch elements in word_annotation .
     * each element of word_annotation are a terminology entry, his children are his occurrences in whole files.
     * These occurrences need to retrieve in order to write the terminology standoff element. each occurrences have
     * characters offsets and a file id
     * @throws IOException thrown a exception if the json file is malformed
     */
    public void execute() {
        try {

            /*
            initialize local variable
             */
            JsonParser parser = _factory.createParser(new File(_path.toString()));
            JsonToken jsonToken;
            /*
            test variables
             */
            boolean inTerms = false;
            boolean inSource = false;
            boolean inOcc = false;
            TermOffsetId offsetId = new TermOffsetId();

            while ((jsonToken = parser.nextToken()) != null) {

                if (inSource) {
                    if (jsonToken == JsonToken.END_OBJECT) {
                        inSource = false;
                    }
                    extractInputSource(jsonToken, parser);
                }
                else if (inTerms) {
                    if (jsonToken == JsonToken.END_ARRAY && Objects.equals(parser.getCurrentName(), T_TERMS)) {
                        break;
                    } else if (jsonToken == JsonToken.END_ARRAY && inOcc) {
                        inOcc = false;
                    } else if (jsonToken == JsonToken.END_OBJECT && inOcc) {
                        fillTerminology(offsetId);
                    } else if (Objects.equals(parser.getCurrentName(), T_OCC)) {
                        inOcc = true;
                    }
                    extractTerm(jsonToken, parser, offsetId);
                }

                else if (T_INPUT.equals(parser.getParsingContext().getCurrentName())) {
                    inSource = true;
                }

                else if (T_TERMS.equals(parser.getParsingContext().getCurrentName())) {
                    inTerms = true;
                }
            }
        }
        catch (Exception e){
            _logger.error("cannot parse file",e);
        }
    }

    /**
     * add a TermOffsetId object to _standoffTerminology
     * @param offsetId the TermOffsetId that we want to add
     */
    private void fillTerminology(TermOffsetId offsetId) {
        /*
        the file concern by this adding
         */
        String realId =
                FilesUtils.nameNormalizer(_idSource.get(_currentFile));
        /*
          create an new entry if the file identifier does not exist in _standoffTerminology
         */
        if (_standOffTerminology.containsKey(realId)){
            _standOffTerminology.get(realId).add(new TermOffsetId(offsetId));
        }

        else {
            _standOffTerminology.put(realId,new ArrayList<>(
                    Collections.singletonList(new TermOffsetId(offsetId))));
        }
    }

    /**
     * parse terms occurrences
     * @param jsonToken the current jsonToken
     * @param parser the current JsonParser
     * @param offsetId set value of this TermOffsetId Object
     * @throws IOException thrown an exception if parser meets some problems
     */
    private void extractTerm(JsonToken jsonToken, JsonParser parser,
                             TermOffsetId offsetId) throws IOException {
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

    /**
     * retrieve input source element and put it on _idSource. On the json, each file is associated to a identifier and
     * this information is contained by the input_source element
     * @param jsonToken the current jsonToken
     * @param parser the current parser
     * @throws IOException thrown an exception if parser meets some problems
     */
    private void extractInputSource(JsonToken jsonToken, JsonParser parser) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)) {
            _idSource.put(parser.getCurrentName(),
                    FilesUtils.nameNormalizer(parser.nextTextValue()));
        }
    }

    /**
     * call execute method
     */
    @Override
    public void run() {
        super.run();
        _termithIndex.setTerminologyStandOff(getStandOffTerminology());
        _logger.info("execute terminology ended");
    }

}
