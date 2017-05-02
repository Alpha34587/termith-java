package org.atilf.module.enrichment.analyzer.treeTaggerWorker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.enrichment.TagNormalizer;
import org.atilf.models.enrichment.TextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Collections;

/**
 * Serialize TreeTagger result to Termsuite json format
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class MorphologySerializer {

    private ArrayDeque<String> _tokenDeque = new ArrayDeque<>();
    private StringBuilder _txt;
    private String _jsonPath;
    private TextAnalyzer _textAnalyzer;
    private final Logger LOGGER = LoggerFactory.getLogger(MorphologySerializer.class.getName());


    /**
     * constructor for MorphologySerializer
     * @param treeTaggerOutput treetagger output
     * @param jsonPath the output of json file
     * @param txt the plain text of the file
     * @param textAnalyzer the termsuite json metadata
     */
    public MorphologySerializer(StringBuilder treeTaggerOutput, String jsonPath, StringBuilder txt,
                                TextAnalyzer textAnalyzer) {
        Collections.addAll(_tokenDeque, treeTaggerOutput.toString().split("\n"));
        _txt = txt;
        _jsonPath = jsonPath;
        _textAnalyzer = textAnalyzer;
    }

    /**
     * serialize result into a json
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    public void execute() throws IOException {
        FileOutputStream fos = new FileOutputStream(_jsonPath);
        Writer writer = new OutputStreamWriter(fos, "UTF-8");
        JsonFactory jFactory = new JsonFactory();
        JsonGenerator jg = jFactory.createGenerator(writer);
        jg.useDefaultPrettyPrinter();
        jg.writeStartObject();
        writeSdi(jg);
        writeTag(jg);
        writeTermOcc(jg);
        writeFe(jg);
        writeText(jg);
        jg.writeEndObject();
        jg.flush();
        writer.close();
        LOGGER.debug("write file {}",_jsonPath);
    }

    /**
     * write empty term_occ_annotations element
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void writeTermOcc(JsonGenerator jg) throws IOException {
        jg.writeFieldName("term_occ_annotations");
        jg.writeStartArray();
        jg.writeEndArray();
    }

    /**
     * write empty fixed_expressions element
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void writeFe(JsonGenerator jg) throws IOException {
        jg.writeFieldName("fixed_expressions");
        jg.writeStartArray();
        jg.writeEndArray();
    }

    /**
     * write plain text on covered_text element
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void writeText(JsonGenerator jg) throws IOException {
        jg.writeFieldName("covered_text");
        jg.writeString(_txt.toString());

    }

    /**
     * write termsuite json metadata
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void writeSdi(JsonGenerator jg) throws IOException {
        jg.writeFieldName("sdi");
        jg.writeStartObject();
        jg.writeFieldName("uri");
        jg.writeString("file:/"+ _jsonPath);
        jg.writeFieldName("off_in_s");
        jg.writeNumber(0);
        jg.writeFieldName("document_index");
        jg.writeNumber(_textAnalyzer.getDocIndex());
        jg.writeFieldName("nb_documents");
        jg.writeNumber(_textAnalyzer.getNbOfDocs());
        jg.writeFieldName("document_size");
        jg.writeNumber(_textAnalyzer.getDocumentSize());
        jg.writeFieldName("cumul_doc_size");
        jg.writeNumber(_textAnalyzer.getCumulatedSize());
        jg.writeFieldName("corpus_size");
        jg.writeNumber(_textAnalyzer.getTotalSize());
        jg.writeFieldName("last_segment");
        jg.writeBoolean(_textAnalyzer.getIsLastDoc());
        jg.writeFieldName("begin");
        jg.writeNumber(_textAnalyzer.getBegin());
        jg.writeFieldName("end");
        jg.writeNumber(_textAnalyzer.getEnd());
        jg.writeEndObject();
    }

    /**
     * write tag element
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void writeTag(JsonGenerator jg) throws IOException {
        Integer[] offset = new Integer[]{0,1};
        jg.writeFieldName("word_annotations");
        jg.writeStartArray();
        while (!_tokenDeque.isEmpty()){
            String[] line = _tokenDeque.poll().split("\t");

            jg.writeStartObject();
            addTag(line[1], jg);
            addCat(line[1], jg);
            addLemma(line[2],jg);
            offset = addOffsets(offset,line[0],jg);
            jg.writeEndObject();
        }
        jg.writeEndArray();

    }

    /**
     * write cat field into tag element
     * @param token the category value
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void addCat(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("cat");
        jg.writeString(TagNormalizer.normalize(token));
    }

    /**
     * write lemma field into tag element
     * @param token the lemma value
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void addLemma(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("lemma");
        jg.writeString(token);
    }

    /**
     * write tag field into tag element
     * @param token the tag value
     * @param jg the current jsonGenerator
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private void addTag(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("tag");
        jg.writeString(token);
    }

    /**
     * write the offset of the of the tag into a tag element
     * @param offset the current offset
     * @param word the current words
     * @param jGenerator the current json Generator
     * @return the new character offset
     * @throws IOException thrown an exception if the jsonGenerator meet a problem during the writing of the file
     */
    private Integer[] addOffsets(Integer[] offset, String word, JsonGenerator jGenerator) throws IOException {
        char[] letterCharArray = word.toCharArray();
        boolean findBegin = false;
        int begin = -1;
        int end = -1;
        int cpt = 0;
        try {
            while (end == -1) {
                char ch = letterCharArray[cpt];

                if (!findBegin && _txt.charAt(offset[0]) == ch) {
                    findBegin = true;
                    begin = offset[0];
                }

                if (cpt == letterCharArray.length - 1 && _txt.charAt(offset[0]) == ch)
                    end = offset[1];

                if (findBegin)
                    cpt++;
                offset[0]++;
                offset[1]++;
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            LOGGER.error("problem : " + word, e);
        }
        jGenerator.writeFieldName("begin");
        jGenerator.writeNumber(begin);
        jGenerator.writeFieldName("end");
        jGenerator.writeNumber(end);
        return new Integer[]{end ,end + 1};
    }

}
