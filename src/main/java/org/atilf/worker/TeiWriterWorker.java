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
    private StandOffResources standOffResources;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());


    public TeiWriterWorker(String key, StringBuilder value, TermithIndex termithIndex, StandOffResources standOffResources){
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
        this.standOffResources = standOffResources;
    }
    @Override
    public void run() {
        LOGGER.debug("writing : " + outputPath + "/" + key + ".xml");
        try {
            TeiWriter teiWriter = new TeiWriter(key,value,termithIndex, standOffResources);
            teiWriter.execute();
        } catch (IOException e) {
            LOGGER.error("errors during writing xml/tei file",e);
        }
    }


}
