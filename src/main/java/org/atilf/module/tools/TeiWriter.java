package org.atilf.module.tools;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.StandOffResources;
import org.atilf.models.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.atilf.models.SpecialChXmlEscape.replaceXmlChar;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private BufferedWriter bufferedWriter = null;
    private final StringBuilder xmlCorpus;
    private final List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds;
    private StringBuilder tokenizeBody;
    private final List<TermsOffsetId> termsOffsetIds;
    private StandOffResources stdfRes;

    public TeiWriter(StringBuilder xmlCorpus,
                     List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds,
                     StringBuilder tokenizeBody,List<TermsOffsetId> termsOffsetIds,
                     Path outputPath,
                     StandOffResources standOffResources) {

        this.xmlCorpus = xmlCorpus;
        this.morphoSyntaxOffsetIds = morphoSyntaxOffsetIds;
        this.tokenizeBody = tokenizeBody;
        this.termsOffsetIds = termsOffsetIds;
        this.stdfRes = standOffResources;
        try {
            this.bufferedWriter = Files.newBufferedWriter(outputPath);
        } catch (IOException e) {
            LOGGER.error("cannot initialize buffered writer object",e);
        }

    }


    public void execute() throws IOException {
        try {
            insertStandoffNs();
            insertStandOff();
            insertBody();
            bufferedWriter.close();
        }
        catch (Exception e){
            LOGGER.error("could not write file",e);
        }
    }

    private void insertStandoffNs() throws IOException {
        int teiTag = xmlCorpus.indexOf("<TEI ") + 5;
        xmlCorpus.insert(teiTag, stdfRes.NS.substring(0, stdfRes.NS.length() - 1) + " ");
    }

    private void insertBody() throws IOException {
        bufferedWriter.append(tokenizeBody.append("\n"));
        bufferedWriter.append(xmlCorpus.subSequence(xmlCorpus.indexOf("</text>") + 7  , xmlCorpus.length()));
        bufferedWriter.flush();
    }

    private int searchStart() {
        int index = xmlCorpus.indexOf("<text>");
        if (index == -1){
            index = xmlCorpus.indexOf("<text ");
        }
        return index;
    }

    private void insertStandOff() throws IOException {
        int startText = searchStart();
        bufferedWriter.append(xmlCorpus.subSequence(0,startText));
        if (morphoSyntaxOffsetIds != null){
            serializeMorphosyntax(morphoSyntaxOffsetIds);
        }

        if (termsOffsetIds != null &&
                !termsOffsetIds.isEmpty()){
            serializeTerminology(termsOffsetIds);
        }

    }

    private void serializeTerminology(List<TermsOffsetId> termsOffsetIds) throws IOException {
        termsOffsetIds.sort((o1, o2) -> {
            int comp = o1.get_ids().get(0).compareTo(o2.get_ids().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.get_ids().size()).compareTo(o2.get_ids().size()) * -1;
            }
            return comp;
        });
        bufferedWriter.append(replaceTemplate(cut(new StringBuilder(stdfRes.STANDOFF),false),"@type","candidatsTermes"));
        bufferedWriter.append(stdfRes.T_TEI_HEADER);
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,false));
        for (TermsOffsetId token : termsOffsetIds) {
            StringBuilder entry = new StringBuilder(stdfRes.T_SPAN);
            replaceTemplate(entry,"@target", serializeId(token.get_ids()));
            replaceTemplate(entry, "@corresp", String.valueOf(token.getTermId()));
            replaceTemplate(entry, "@string", replaceXmlChar(token.getWord()));
            bufferedWriter.append(entry);
        }
        bufferedWriter.append(cut(stdfRes.LIST_ANNOTATION,true));
        bufferedWriter.append(cut(stdfRes.STANDOFF,true));
    }

    private StringBuilder cut(StringBuilder template,boolean closedTag) {
        String separator = "(?<=\n)";
        if (closedTag)
            return new StringBuilder(template.toString().split(separator)[1]);
        else
            return new StringBuilder(template.toString().split(separator)[0]);
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
            replaceTemplate(entry, "@target", serializeId(token.get_ids()));
            replaceTemplate(entry, "@lemma", replaceXmlChar(token.get_lemma().replace("<unknown>", "@unknown")));
            replaceTemplate(entry, "@pos", token.get_tag());
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
