package worker;

import models.TermithIndex;
import module.tools.TeiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TeiWriterWorker implements Runnable {

    private String key;
    private StringBuffer value;
    private TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriterWorker.class.getName());


    public TeiWriterWorker(String key, StringBuffer value, TermithIndex termithIndex){
        this.key = key;
        this.value = value;
        this.termithIndex = termithIndex;
    }
    @Override
    public void run() {

        LOGGER.info("writing : " + outputPath + "/" + key + ".xml");
        TeiWriter teiWriter = new TeiWriter(key,value,termithIndex);
        teiWriter.execute();
    }


}
