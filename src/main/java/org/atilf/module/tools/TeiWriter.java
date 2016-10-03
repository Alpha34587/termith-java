package org.atilf.module.tools;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.models.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.atilf.models.SpecialChXmlEscape.replaceXmlChar;
import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private final BufferedWriter bufferedWriter;
    private List<MorphoSyntaxOffsetId> morphoStandoff;
    private StringBuilder tokenizeBody;
    private String key;
    private StringBuilder value;
    private TermithIndex termithIndex;
    private StandOffResources stdfRes;
    private final String SEPARATOR = "(?<=\n)";


    public TeiWriter(String key, StringBuilder value, TermithIndex termithIndex, StandOffResources stdfRes) throws IOException {
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
        this.stdfRes = stdfRes;
        this.morphoStandoff = (List<MorphoSyntaxOffsetId>) FilesUtilities.readObject(termithIndex.getMorphoSyntaxStandOff().get(key));
        this.tokenizeBody = (StringBuilder) FilesUtilities.readObject(termithIndex.getTokenizeTeiBody().get(key));
        this.bufferedWriter = Files.newBufferedWriter(Paths.get(outputPath + "/" + key + ".xml"));

        Files.delete(termithIndex.getMorphoSyntaxStandOff().get(key));
        Files.delete(termithIndex.getTokenizeTeiBody().get(key));
    }

    public void execute() throws IOException {
        try {
            LOGGER.debug("writing : " + outputPath + "/" + key + ".xml");
            insertStandoffNs();
            insertStandOff();
            insertBody();
            termithIndex.getOutputFile().add(Paths.get(outputPath + "/" + key + ".xml"));
            bufferedWriter.close();
        }
        catch (Exception e){
            LOGGER.error("could not write file",e);
        }
    }

    private void insertStandoffNs() throws IOException {
        int teiTag = value.indexOf("<TEI ") + 5;
        value.insert(teiTag, stdfRes.NS.substring(0, stdfRes.NS.length() - 1) + " ");
    }

    private void insertBody() throws IOException {
        bufferedWriter.append(tokenizeBody.append("\n"));
        bufferedWriter.append(value.subSequence(value.indexOf("</text>") + 7  , value.length()));
        bufferedWriter.flush();
    }

    private int searchStart() {
        int index = value.indexOf("<text>");
        if (index == -1){
            index = value.indexOf("<text ");
        }
        return index;
    }

    private void insertStandOff() throws IOException {
        int startText = searchStart();
        bufferedWriter.append(value.subSequence(0,startText));
        if (termithIndex.getTerminologyStandOff().containsKey(key) &&
                !termithIndex.getTerminologyStandOff().get(key).isEmpty()){
            serializeTerminology(termithIndex.getTerminologyStandOff().get(key));
        }
        if (termithIndex.getMorphoSyntaxStandOff().containsKey(key)){
            serializeMorphosyntax(morphoStandoff);
        }
    }

    private void serializeTerminology(List<TermsOffsetId> termsOffsetIds) throws IOException {
        termsOffsetIds.sort((o1, o2) -> {
            int comp = o1.getIds().get(0).compareTo(o2.getIds().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.getIds().size()).compareTo(o2.getIds().size()) * -1;
            }
            return comp;
        });
        bufferedWriter.append(replaceTemplate(cut(new StringBuilder(stdfRes.STANDOFF),false),"@type","candidatsTermes"));
        bufferedWriter.append(stdfRes.T_TEI_HEADER);
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,false));
        for (TermsOffsetId token : termsOffsetIds) {
            StringBuilder entry = new StringBuilder(stdfRes.T_SPAN);
            replaceTemplate(entry,"@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@corresp", String.valueOf(token.getTermId()));
            replaceTemplate(entry, "@string", replaceXmlChar(token.getWord()));
            bufferedWriter.append(entry);
        }
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,true));
        bufferedWriter.append(cut(stdfRes.STANDOFF,true));
    }

    private StringBuilder cut(StringBuilder template,boolean closedTag) {
        if (closedTag)
            return new StringBuilder(template.toString().split(SEPARATOR)[1]);
        else
            return new StringBuilder(template.toString().split(SEPARATOR)[0]);
    }

    private StringBuilder replaceTemplate(StringBuilder template, String model, String occurence) {
        int index = template.indexOf(model);
        template.replace(index, index + model.length(), occurence);
        return template;
    }

    private void serializeMorphosyntax(List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds) throws IOException {
        StringBuilder standoff = new StringBuilder();

        bufferedWriter.append(replaceTemplate(cut(stdfRes.STANDOFF,false),"@type","wordForms"));
        bufferedWriter.append(stdfRes.MS_TEI_HEADER);
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,false));
        for (MorphoSyntaxOffsetId token : morphoSyntaxOffsetIds) {
            StringBuilder entry = new StringBuilder(stdfRes.MS_SPAN);
            replaceTemplate(entry, "@target", serializeId(token.getIds()));
            replaceTemplate(entry, "@lemma", replaceXmlChar(token.getLemma().replace("<unknown>", "@unknown")));
            replaceTemplate(entry, "@pos", token.getTag());
            bufferedWriter.append(entry);
        }
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,true));
        bufferedWriter.append(cut(stdfRes.STANDOFF,true));
    }

    private String serializeId(List<Integer> ids) {
        String target = "";
        for (int id : ids) {
            target += "#t" + id + " ";
        }
        return target.substring(0,target.length() - 1);
    }
}
