package org.atilf.module.tools;

import org.atilf.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.atilf.models.SpecialChXmlEscape.*;
import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter {

    private List<MorphoSyntaxOffsetId> morphoStandoff;
    private StringBuilder tokenizeBody;
    private String key;
    private StringBuilder value;
    private TermithIndex termithIndex;
    private StandOffResources stdfRes;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private final String SEPARATOR = "(?<=\n)";


    public TeiWriter(String key, StringBuilder value, TermithIndex termithIndex, StandOffResources stdfRes) {
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
        this.stdfRes = stdfRes;
        this.morphoStandoff = (List<MorphoSyntaxOffsetId>) FilesUtilities.readObject(termithIndex.getMorphoSyntaxStandOff().get(key));
        this.tokenizeBody = (StringBuilder) FilesUtilities.readObject(termithIndex.getTokenizeTeiBody().get(key));
    }

    public void execute() throws IOException {
        try {
            LOGGER.debug("writing : " + outputPath + "/" + key + ".xml");
            insertStandoffNs();
            insertStandOff();
            insertBody();
            writeFile();
        }
        catch (Exception e){
            LOGGER.error("cannot write file: ",e);
        }
    }

    private void insertStandoffNs() {
        int teiTag = value.indexOf("<TEI ") + 5;
        value.insert(teiTag, stdfRes.NS.substring(0,
                stdfRes.NS.length() - 1) + " ");
    }


    private void writeFile() throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter =
                    Files.newBufferedWriter(Paths.get(outputPath + "/" + key + ".xml"));
            bufferedWriter.append(value);
            termithIndex.getOutputFile().add(Paths.get(outputPath + "/" + key + ".xml"));
        } catch (IOException e) {
            LOGGER.error("Some errors during files writing",e);
        }
        finally {
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }
    }

    private void insertBody() {
        if (termithIndex.getTokenizeTeiBody().containsKey(key)){
            int startText = searchStart();
            int endText = value.indexOf("</TEI>");
            value.delete(startText,endText);
            value.insert(startText,tokenizeBody.append("\n"));
        }
    }

    private int searchStart() {
        int index = value.indexOf("<text>");
        if (index == -1){
            index = value.indexOf("<text ");
        }
        return index;
    }

    private void insertStandOff() {
        int startText = searchStart();
        if (termithIndex.getTerminologyStandOff().containsKey(key))
            value.insert(startText, serializeTerminology(termithIndex.getTerminologyStandOff().get(key)));
        if (termithIndex.getMorphoSyntaxStandOff().containsKey(key))
            value.insert(startText,serializeMorphosyntax(morphoStandoff));
    }

    private StringBuilder serializeTerminology(List<TermsOffsetId> termsOffsetIds) {
        StringBuilder standoff = new StringBuilder();
        termsOffsetIds.sort((o1, o2) -> {
            int comp = o1.getIds().get(0).compareTo(o2.getIds().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.getIds().size()).compareTo(o2.getIds().size()) * -1;
            }
            return comp;
        });
        standoff.append(replaceTemplate(cut(stdfRes.STANDOFF,false),"@type","candidatsTermes"));
        standoff.append(stdfRes.T_TEI_HEADER);
        standoff.append(cut(stdfRes.LIST_ANNOTATION,false));
        for (TermsOffsetId token : termsOffsetIds) {
            StringBuilder entry;
            entry = replaceTemplate(stdfRes.T_SPAN, "@target", serializeId(token.getIds()));
            entry = replaceTemplate(entry, "@corresp", String.valueOf(token.getTermId()));
            entry = replaceTemplate(entry, "@string", replaceXmlChar(token.getWord()));
            standoff.append(entry);
        }
        standoff.append(cut(stdfRes.LIST_ANNOTATION,true));
        standoff.append(cut(stdfRes.STANDOFF,true));
        termsOffsetIds.clear();
        return standoff;
    }

    private StringBuilder cut(StringBuilder template,boolean closedTag) {
        if (closedTag)
            return new StringBuilder(template.toString().split(SEPARATOR)[1]);
        else
            return new StringBuilder(template.toString().split(SEPARATOR)[0]);
    }

    private StringBuilder replaceTemplate(StringBuilder template, String model, String occurence) {
        int index = template.indexOf(model);
        return new StringBuilder(new StringBuilder(template).replace(index, index + model.length(), occurence));
    }

    private StringBuilder serializeMorphosyntax(List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds) {
        StringBuilder standoff = new StringBuilder();

        standoff.append(replaceTemplate(cut(stdfRes.STANDOFF,false),"@type","wordForms"));
        standoff.append(stdfRes.MS_TEI_HEADER);
        standoff.append(cut(stdfRes.LIST_ANNOTATION,false));
        for (MorphoSyntaxOffsetId token : morphoSyntaxOffsetIds) {
            StringBuilder entry;
            entry = replaceTemplate(stdfRes.MS_SPAN, "@target", serializeId(token.getIds()));
            entry = replaceTemplate(entry, "@lemma",
                    replaceXmlChar(token.getLemma().replace("<unknown>", "@unknown")));
            entry = replaceTemplate(entry, "@pos", token.getTag());
            standoff.append(entry);
        }
        standoff.append(cut(stdfRes.LIST_ANNOTATION,true));
        standoff.append(cut(stdfRes.STANDOFF,true));
        morphoStandoff.clear();
        return standoff;
    }

    private String serializeId(List<Integer> ids) {
        String target = "";
        for (int id : ids) {
            target += "#t" + id + " ";
        }
        return target.substring(0,target.length() - 1);
    }
}
