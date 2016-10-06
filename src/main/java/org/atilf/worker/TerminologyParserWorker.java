package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.termsuite.terminology.TerminologyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TerminologyParserWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminologyParserWorker.class.getName());
    private Path terminologyPath;
    private TermithIndex termithIndex;

    public TerminologyParserWorker(TermithIndex termithIndex) {
        terminologyPath = termithIndex.getJsonTerminology();
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        TerminologyParser terminologyParser = new TerminologyParser(terminologyPath);
        try {
            terminologyParser.execute();
            termithIndex.setTerminologyStandOff(terminologyParser.getStandOffTerminology());
            LOGGER.info("parsing terminology ended");
        } catch (IOException e) {
            LOGGER.error("error during terminology parsing", e);
        }
    }
}
