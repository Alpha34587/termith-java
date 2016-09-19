package module.timer;

import models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExtractTextTimer extends ProgressBarTimer{

    public ExtractTextTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, "Extracted text phase progression", logger);
    }

    @Override
    public void run() {
        update(termithIndex.getExtractedText().size());
    }
}
