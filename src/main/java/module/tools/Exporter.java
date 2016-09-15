package module.tools;

import models.MorphoSyntaxOffsetId;
import models.TerminologyOffetId;
import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.TermithText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static models.standOffResources.*;

/**
 * This class is used to execute the different output result of the TermithText class
 * @see TermithText
 * @author Simon Meoni
 * Created on 16/08/16.
 */


public class Exporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static  final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor;
    private TermithIndex termithIndex;

    public Exporter(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Exporter(TermithIndex termithIndex, int poolSize){
        executor = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
    }

    public void execute() throws InterruptedException {

        termithIndex.getXmlCorpus().forEach(
                (key,value) -> executor.submit(new FileWriterWorker(key,value))
        );
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }

    class FileWriterWorker implements Runnable {

        String key;
        StringBuffer value;

        public FileWriterWorker(String key, StringBuffer value){
            this.key = key;
            this.value = value;
        }
        @Override
        public void run() {

            LOGGER.info("writing : " + termithIndex.getOutputPath() + "/" + key + ".xml");
            insertStandOff();
            insertBody();
            writeFile();

        }

        private void writeFile() {
            try {
                BufferedWriter bufferedWriter =
                        Files.newBufferedWriter(Paths.get(termithIndex.getOutputPath() + "/" + key + ".xml"));

                bufferedWriter.write(String.valueOf(value));
                bufferedWriter.close();
            } catch (IOException e) {
                LOGGER.error("Some errors during files writing",e);
            }
        }

        private void insertBody() {
            if (termithIndex.getTokenizeTeiBody().containsKey(key)){
                int startText = value.indexOf("<text>");
                int endText = value.indexOf("</TEI>");
                value.delete(startText,endText);
                value.insert(startText,termithIndex.getTokenizeTeiBody().get(key));
            }
        }

        private void insertStandOff() {
            int startText = value.indexOf("</teiHeader>");
            if (termithIndex.getMorphoSyntaxStandOff().containsKey(key))
                value.insert(startText,serializeMorphosyntax(termithIndex.getMorphoSyntaxStandOff().get(key)));
            if (termithIndex.getTerminologyStandOff().containsKey(key))
                value.insert(startText,serializeTerminology(termithIndex.getTerminologyStandOff().get(key)));


        }

        private StringBuffer serializeTerminology(List<TerminologyOffetId> terminologyOffetIds) {
            return new StringBuffer();
        }

        private StringBuffer serializeMorphosyntax(List<MorphoSyntaxOffsetId> morphoSyntaxOffsetIds) {
            StringBuffer standoff = new StringBuffer();
            Deque<MorphoSyntaxOffsetId> morphoDeque = new ArrayDeque<>(morphoSyntaxOffsetIds);

            standoff.append(STANDOFF.split("\n")[0].replace("@type", "wordForms")).append("\n");
            standoff.append(MS_TEI_HEADER).append("\n");
            standoff.append(MS_LIST_ANNOTATION.split("\n")[0]).append("\n");
            while (!morphoDeque.isEmpty()){
                MorphoSyntaxOffsetId token = morphoDeque.poll();
                standoff.append(
                        MS_SPAN.replace("@target",serializeId(token.getIds()))
                                .replace("@lemma",token.getLemma())
                                .replace("@pos", token.getTag())
                ).append("\n");
            }
            standoff.append(MS_LIST_ANNOTATION.split("\n")[1]).append("\n");
            standoff.append(STANDOFF.split("\n")[1]).append("\n");

            return standoff;
        }

        private String serializeId(List<Integer> ids) {
            return ids.stream().map(e ->  "t" + e.toString()).reduce(" ", String::join);
        }
    }


}
