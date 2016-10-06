package org.atilf.worker;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.atilf.module.tools.TeiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriterWorker implements Runnable {

    private String key;
    private TermithIndex termithIndex;
    private StandOffResources standOffResources;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());


    public TeiWriterWorker(String key, TermithIndex termithIndex, StandOffResources standOffResources){
        this.key = key;
        this.termithIndex = termithIndex;
        this.standOffResources = standOffResources;
    }
    @Override
    public void run() {
        Path outputFile = Paths.get(outputPath + "/" + key + ".xml");
        LOGGER.debug("writing : " + outputFile);
        TeiWriter teiWriter = null;
        try {
            if (termithIndex.getTerminologyStandOff().containsKey(key)) {
                teiWriter = new TeiWriter(
                        (StringBuilder) FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                        (List<MorphoSyntaxOffsetId>) FilesUtils.readObject(termithIndex.getMorphoSyntaxStandOff().get(key)),
                        (StringBuilder) FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key)),
                        termithIndex.getTerminologyStandOff().get(key),
                        outputFile,
                        standOffResources);
            }

            else {
                teiWriter = new TeiWriter(
                        (StringBuilder) FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                        (List<MorphoSyntaxOffsetId>) FilesUtils.readObject(termithIndex.getMorphoSyntaxStandOff().get(key)),
                        (StringBuilder) FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key)),
                        null,
                        outputFile,
                        standOffResources);
            }

            Files.delete(termithIndex.getMorphoSyntaxStandOff().get(key));
            Files.delete(termithIndex.getTokenizeTeiBody().get(key));
            teiWriter.execute();
            termithIndex.getOutputFile().add(outputFile);
        } catch (IOException e) {
            LOGGER.error("errors during writing xml/tei file",e);
        }
    }


}
