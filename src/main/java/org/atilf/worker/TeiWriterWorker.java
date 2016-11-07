package org.atilf.worker;

import org.atilf.models.tei.exporter.StandOffResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.MorphoSyntaxOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.atilf.module.tools.TeiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path outputFile = Paths.get(TermithIndex.getOutputPath() + "/" + key + ".xml");
        LOGGER.debug("writing : " + outputFile);
        TeiWriter teiWriter;
        try {
            if (termithIndex.getTerminologyStandOff().containsKey(key)) {

                teiWriter = new TeiWriter(
                        FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                        FilesUtils.readListObject(
                                termithIndex.getMorphoSyntaxStandOff().get(key),
                                MorphoSyntaxOffsetId.class
                        ),
                        FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key),StringBuilder.class),
                        termithIndex.getTerminologyStandOff().get(key),
                        outputFile,
                        standOffResources);
            }

            else {
                teiWriter = new TeiWriter(
                        FilesUtils.readFile(termithIndex.getXmlCorpus().get(key)),
                        FilesUtils.readListObject(
                                termithIndex.getMorphoSyntaxStandOff().get(key),
                                MorphoSyntaxOffsetId.class
                        ),
                        FilesUtils.readObject(termithIndex.getTokenizeTeiBody().get(key),StringBuilder.class),
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
