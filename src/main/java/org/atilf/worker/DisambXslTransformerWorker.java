package org.atilf.worker;

import org.atilf.models.disambiguisation.DisambXslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguisation.DisambXslTransformer;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslTransformerWorker implements Runnable {
    private final DisambXslResources _xslResources = new DisambXslResources();
    private final Path _p;
    private final TermithIndex _termithIndex;
    private CountDownLatch _transformCounter;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambXslTransformerWorker.class.getName());

    public DisambXslTransformerWorker(Path p, TermithIndex termithIndex, CountDownLatch transformCounter) {
        _p = p;
        _termithIndex = termithIndex;
        _transformCounter = transformCounter;
    }

    public DisambXslTransformerWorker(Path p, TermithIndex termithIndex) {
        this(p, termithIndex, null);
    }

    @Override
    public void run() {
        try {
            LOGGER.info("convert xml file: " + _p);
            DisambXslTransformer disambXslTransformer = new DisambXslTransformer(_p.toFile(),_xslResources);
            _termithIndex.getDisambTranformedFile().put(
                    FilesUtils.nameNormalizer(_p.getFileName().toString()),
                    FilesUtils.writeXml(
                            disambXslTransformer.xsltTransformation(),
                            TermithIndex.getOutputPath(),
                            _p.getFileName())
            );
            _transformCounter.countDown();
            LOGGER.info("Extraction done for file: " + _p);
        } catch (IOException e) {
            LOGGER.error("File Exception",e);
        }
    }
}
