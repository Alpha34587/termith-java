package module.treetagger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayDeque;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class Serialize {

    private ArrayDeque tokenDeque;
    private StringBuffer txt;
    private String jsonPath;
    private TextAnalyzer textAnalyzer;

    public Serialize(StringBuilder tokenDeque, String jsonPath, StringBuffer txt
            , TextAnalyzer textAnalyzer) {
        this.tokenDeque = new ArrayDeque();
        populateTokenDeque(tokenDeque);
        this.txt = txt;
        this.jsonPath = jsonPath;
        this.textAnalyzer = textAnalyzer;
    }

    private void populateTokenDeque(StringBuilder tokenDeque) {
        for (String token : tokenDeque.toString().split("\n")) {
            this.tokenDeque.add(token);
        }
    }

    public void execute() throws IOException {
        FileOutputStream fos = new FileOutputStream(jsonPath);
        Writer writer = new OutputStreamWriter(fos, "UTF-8");
        JsonFactory jfactory = new JsonFactory();
        JsonGenerator jg = jfactory.createGenerator(writer);
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
    }

    private void writeTermOcc(JsonGenerator jg) throws IOException {
        jg.writeFieldName("term_occ_annotations");
        jg.writeStartArray();
        jg.writeEndArray();
    }

    private void writeFe(JsonGenerator jg) throws IOException {
        jg.writeFieldName("fixed_expressions");
        jg.writeStartArray();
        jg.writeEndArray();
    }

    private void writeText(JsonGenerator jg) throws IOException {
        jg.writeFieldName("covered_text");
        jg.writeString(txt.toString());

    }

    private void writeSdi(JsonGenerator jg) throws IOException {
        jg.writeFieldName("sdi");
        jg.writeStartObject();
        jg.writeFieldName("uri");
        jg.writeString("file:/"+ jsonPath);
        jg.writeFieldName("off_in_s");
        jg.writeNumber(0);
        jg.writeFieldName("document_index");
        jg.writeNumber(textAnalyzer.getDocIndex());
        jg.writeFieldName("nb_documents");
        jg.writeNumber(textAnalyzer.getNbOfDocs());
        jg.writeFieldName("document_size");
        jg.writeNumber(textAnalyzer.getDocumentSize());
        jg.writeFieldName("cumul_doc_size");
        jg.writeNumber(textAnalyzer.getCumulSize());
        jg.writeFieldName("corpus_size");
        jg.writeNumber(textAnalyzer.getTotalSize());
        jg.writeFieldName("last_segment");
        jg.writeBoolean(textAnalyzer.getIsLastDoc());
        jg.writeFieldName("begin");
        jg.writeNumber(textAnalyzer.getBegin());
        jg.writeFieldName("end");
        jg.writeNumber(textAnalyzer.getEnd());
        jg.writeEndObject();
    }

    public void writeTag(JsonGenerator jg) throws IOException {
        Integer[] offset = new Integer[]{0,1};
        jg.writeFieldName("word_annotations");
        jg.writeStartArray();
        while (!tokenDeque.isEmpty()){
            String[] line = tokenDeque.poll().toString().split("\t");

            jg.writeStartObject();
            addTag(line[1], jg);
            addCat(line[1], jg);
            addLemma(line[2],jg);
            offset = addOffsets(offset,line[0],jg);
            jg.writeEndObject();
        }
        jg.writeEndArray();

    }

    private void addCat(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("cat");
        jg.writeString(TagNormalizer.normalize(token));
    }

    public void addLemma(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("lemma");
        jg.writeString(token);
    }

    public void addTag(String tag, JsonGenerator jg) throws IOException {
        jg.writeFieldName("tag");
        jg.writeString(tag);
    }

    public Integer[] addOffsets(Integer[] offset, String token, JsonGenerator jGenerator) throws IOException {
        char[] letterCharArray = token.toCharArray();
        boolean findBegin = false;
        int begin = -1;
        int end = -1;
        int cpt = 0;
        while (end == -1){
            char ch = letterCharArray[cpt];

            if (!findBegin && txt.charAt(offset[0]) == ch) {
                findBegin = true;
                begin = offset[0];
            }

            if (cpt == letterCharArray.length -1  && txt.charAt(offset[0]) == ch)
                end = offset[1];

            if (findBegin)
                cpt++;
            offset[0]++;
            offset[1]++;
        }
        jGenerator.writeFieldName("begin");
        jGenerator.writeNumber(begin);
        jGenerator.writeFieldName("end");
        jGenerator.writeNumber(end);
        return new Integer[]{end ,end + 1};
    }

}
