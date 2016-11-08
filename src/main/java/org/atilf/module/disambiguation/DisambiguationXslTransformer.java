package org.atilf.module.disambiguation;

import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.extractor.TextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslTransformer extends TextExtractor {
    private TermithIndex _termithIndex;
    private CountDownLatch _transformCounter;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslTransformer.class.getName());

    public DisambiguationXslTransformer(File file, XslResources xslResources) {
        super(file,xslResources);}

    public DisambiguationXslTransformer(File file, CountDownLatch transformCounter, TermithIndex termithIndex, XslResources xslResources){
        super(file,xslResources);
        _transformCounter = transformCounter;
        _termithIndex = termithIndex;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("convert xml file: " + _file.getAbsolutePath());
            _termithIndex.getDisambTranformedFile().put(
                    FilesUtils.nameNormalizer(_file.getName()),
                    FilesUtils.writeXml(
                            this.execute(),
                            TermithIndex.getOutputPath(),
                            _file.getName())
            );
            _transformCounter.countDown();
            LOGGER.info("Extraction done for file: " + _file);
        } catch (IOException e) {
            LOGGER.error("File Exception",e);
        }
    }
}
