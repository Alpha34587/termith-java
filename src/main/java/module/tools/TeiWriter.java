package module.tools;

import models.MorphoSyntaxOffsetId;
import models.TermithIndex;
import models.TermsOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.TeiWriterWorker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static models.TermithIndex.outputPath;
import static models.standOffResources.*;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriter {

    private String key;
    private StringBuffer value;
    private TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());
    private final String SEPARATOR = "(?<=\n)";


    public TeiWriter(String key, StringBuffer value, TermithIndex termithIndex) {
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
    }

    public void execute() throws IOException {

        LOGGER.info("writing : " + outputPath + "/" + key + ".xml");
        insertStandOff();
        insertBody();
        writeFile();
    }


    private void writeFile() throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter =
                    Files.newBufferedWriter(Paths.get(outputPath + "/" + key + ".xml"));
            bufferedWriter.write(String.valueOf(value));
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

        standoff.append(STANDOFF.split(SEPARATOR)[0].replace("@type", "candidatsTermes"));
        standoff.append(T_TEI_HEADER);
        standoff.append(LIST_ANNOTATION.split(SEPARATOR)[0]);
        while (!termDeque.isEmpty()){
            TermsOffsetId token = termDeque.poll();
            standoff.append(
                    T_SPAN.replace("@target",serializeId(token.getIds()))
                            .replace("@corresp",String.valueOf(token.getTermId()))
                            .replace("@string", token.getWord())
            );
        }
        standoff.append(LIST_ANNOTATION.split(SEPARATOR)[1]);
        standoff.append(STANDOFF.split(SEPARATOR)[1]);

        return standoff;
    }

    private StringBuffer serializeMorphosyntax(List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds) {
        StringBuffer standoff = new StringBuffer();
        Deque<MorphoSyntaxOffsetId> morphoDeque = new ArrayDeque<>(morphoSyntaxOffsetIds);

        standoff.append(STANDOFF.split(SEPARATOR)[0].replace("@type", "wordForms"));
        standoff.append(MS_TEI_HEADER);
        standoff.append(LIST_ANNOTATION.split(SEPARATOR)[0]);
        while (!morphoDeque.isEmpty()){
            MorphoSyntaxOffsetId token = morphoDeque.poll();
            standoff.append(
                    MS_SPAN.replace("@target",serializeId(token.getIds()))
                            .replace("@lemma", token.getLemma().replace("<unknown>", "@unknown"))
                            .replace("@pos", token.getTag())
            );
        }
        standoff.append(LIST_ANNOTATION.split(SEPARATOR)[1]);
        standoff.append(STANDOFF.split(SEPARATOR)[1]);

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
