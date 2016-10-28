package org.atilf.worker;

import org.atilf.models.termsuite.MorphoSyntaxOffsetId;
import org.atilf.models.tei.exporter.StandOffResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.atilf.module.tools.TeiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        Path outputFile = Paths.get(TermithIndex.get_outputPath() + "/" + key + ".xml");
        LOGGER.debug("writing : " + outputFile);
        TeiWriter teiWriter = null;
        try {
            if (termithIndex.get_terminologyStandOff().containsKey(key)) {
                teiWriter = new TeiWriter(
                        (StringBuilder) FilesUtils.readFile(termithIndex.get_xmlCorpus().get(key)),
                        (List<MorphoSyntaxOffsetId>) FilesUtils.readObject(termithIndex.get_morphoSyntaxStandOff().get(key)),
                        (StringBuilder) FilesUtils.readObject(termithIndex.get_tokenizeTeiBody().get(key)),
                        termithIndex.get_terminologyStandOff().get(key),
                        outputFile,
                        standOffResources);
            }

            else {
                teiWriter = new TeiWriter(
                        (StringBuilder) FilesUtils.readFile(termithIndex.get_xmlCorpus().get(key)),
                        (List<MorphoSyntaxOffsetId>) FilesUtils.readObject(termithIndex.get_morphoSyntaxStandOff().get(key)),
                        (StringBuilder) FilesUtils.readObject(termithIndex.get_tokenizeTeiBody().get(key)),
                        null,
                        outputFile,
                        standOffResources);
            }

            Files.delete(termithIndex.get_morphoSyntaxStandOff().get(key));
            Files.delete(termithIndex.get_tokenizeTeiBody().get(key));
            teiWriter.execute();
            termithIndex.get_outputFile().add(outputFile);
        } catch (IOException e) {
            LOGGER.error("errors during writing xml/tei file",e);
        }
    }


}
