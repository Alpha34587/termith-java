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
    private String filePath;
    private StringBuffer txt;
    private String txtPath;
    private TextAnalyzer textAnalyzer;

    public Serialize(StringBuilder tokenDeque, String txtPath,String filePath, StringBuffer txt
            , TextAnalyzer textAnalyzer) {
        this.tokenDeque = new ArrayDeque();
        populateTokenDeque(tokenDeque);
        this.filePath = filePath;
        this.txt = txt;
        this.txtPath = txtPath;
        this.textAnalyzer = textAnalyzer;
    }

    private void populateTokenDeque(StringBuilder tokenDeque) {
        for (String token : tokenDeque.toString().split("\n")) {
            this.tokenDeque.add(token);
        }
    }

    public void execute() throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
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
        jg.writeString(txtPath);
        jg.writeEndObject();
    }

    public void writeTag(JsonGenerator jg) throws IOException {
        Integer[] offset = new Integer[]{0,1};
        jg.writeFieldName("word_annotations");
        jg.writeStartArray();
        while (!tokenDeque.isEmpty()){
            String[] line = tokenDeque.poll().toString().split("\t");

            jg.writeStartObject();
            addTagCat(line[1],jg);
            addLemma(line[2],jg);
            offset = addOffsets(offset,line[0],jg);
            jg.writeEndObject();
        }
        jg.writeEndArray();

    }

    public void addLemma(String token, JsonGenerator jg) throws IOException {
        jg.writeFieldName("lemma");
        jg.writeString(token);
    }

    public void addTagCat(String tag, JsonGenerator jg) throws IOException {
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
