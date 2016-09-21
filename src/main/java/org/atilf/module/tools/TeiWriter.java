package org.atilf.module.tools;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.models.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.worker.TeiWriterWorker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter {

    private String key;
    private StringBuffer value;
    private TermithIndex termithIndex;
    private StandOffResources stdfRes;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());
    private final String SEPARATOR = "(?<=\n)";


    public TeiWriter(String key, StringBuffer value, TermithIndex termithIndex, StandOffResources stdfRes) {
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
        this.stdfRes = stdfRes;
    }

    public void execute() throws IOException {

        LOGGER.debug("writing : " + outputPath + "/" + key + ".xml");
        insertStandoffNs();
        insertStandOff();
        insertBody();
        writeFile();
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
            bufferedWriter.write(String.valueOf(value));
            termithIndex.getOutputFile().add(Paths.get(outputPath + "/" + key + ".xml"));
        } catch (IOException e) {
            LOGGER.error("Some errors during files writing",e);
        }
        finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    private void insertBody() {
        if (termithIndex.getTokenizeTeiBody().containsKey(key)){
            int startText = value.indexOf("<text>");
            int endText = value.indexOf("</TEI>");
            value.delete(startText,endText);
            value.insert(startText,termithIndex.getTokenizeTeiBody().get(key).append("\n"));
        }
    }

    private void insertStandOff() {
        int startText = value.indexOf("<text>");
        if (termithIndex.getTerminologyStandOff().containsKey(key))
            value.insert(startText, serializeTerminology(termithIndex.getTerminologyStandOff().get(key)));
        if (termithIndex.getMorphoSyntaxStandOff().containsKey(key))
            value.insert(startText,serializeMorphosyntax(termithIndex.getMorphoSyntaxStandOff().get(key)));
    }

    private StringBuffer serializeTerminology(List<TermsOffsetId> termsOffsetIds) {
        StringBuffer standoff = new StringBuffer();
        termsOffsetIds.sort((o1, o2) -> {
            int comp = o1.getIds().get(0).compareTo(o2.getIds().get(0));
            if (comp == 0) {
                comp = ((Integer) o1.getIds().size()).compareTo(o2.getIds().size()) * -1;
            }
            return comp;
        });
        Deque<TermsOffsetId> termDeque = new ArrayDeque<>(termsOffsetIds);

        standoff.append(stdfRes.STANDOFF.split(SEPARATOR)[0].replace("@type", "candidatsTermes"));
        standoff.append(stdfRes.T_TEI_HEADER);
        standoff.append(stdfRes.LIST_ANNOTATION.split(SEPARATOR)[0]);
        while (!termDeque.isEmpty()){
            TermsOffsetId token = termDeque.poll();
            standoff.append(
                    stdfRes.T_SPAN.replace("@target",serializeId(token.getIds()))
                            .replace("@corresp",String.valueOf(token.getTermId()))
                            .replace("@string", token.getWord())
            );
        }
        standoff.append(stdfRes.LIST_ANNOTATION.split(SEPARATOR)[1]);
        standoff.append(stdfRes.STANDOFF.split(SEPARATOR)[1]);

        return standoff;
    }

    private StringBuffer serializeMorphosyntax(List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds) {
        StringBuffer standoff = new StringBuffer();
        Deque<MorphoSyntaxOffsetId> morphoDeque = new ArrayDeque<>(morphoSyntaxOffsetIds);

        standoff.append(stdfRes.STANDOFF.split(SEPARATOR)[0]
                .replace("@type", "wordForms"));
        standoff.append(stdfRes.MS_TEI_HEADER);
        standoff.append(stdfRes.LIST_ANNOTATION.split(SEPARATOR)[0]);
        while (!morphoDeque.isEmpty()){
            MorphoSyntaxOffsetId token = morphoDeque.poll();
            standoff.append(
                    stdfRes.MS_SPAN.replace("@target",serializeId(token.getIds()))
                            .replace("@lemma", token.getLemma().replace("<unknown>", "@unknown"))
                            .replace("@pos", token.getTag())
            );
        }
        standoff.append(stdfRes.LIST_ANNOTATION.split(SEPARATOR)[1]);
        standoff.append(stdfRes.STANDOFF.split(SEPARATOR)[1]);

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
