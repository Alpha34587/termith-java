package org.atilf.worker;

import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.module.tools.TeiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriterWorker implements Runnable {

    private String key;
    private StringBuilder value;
    private TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());


    public TeiWriterWorker(String key, StringBuilder value, TermithIndex termithIndex){
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
    }
    @Override
    public void run() {

        LOGGER.debug("writing : " + outputPath + "/" + key + ".xml");
        TeiWriter teiWriter = new TeiWriter(key,value,termithIndex, new StandOffResources());
        try {
            teiWriter.execute();
        } catch (IOException e) {
            LOGGER.error("errors during writing xml/tei file",e);
        }
    }


}