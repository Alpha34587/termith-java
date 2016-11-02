package org.atilf.worker;

import org.atilf.models.disambiguisation.DisambXslResources;
import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguisation.DisambXslTransformer;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslTransformerWorker implements Runnable {
    private final DisambXslResources _xslResources = new DisambXslResources();
    private final Path _p;
    private final TermithIndex _termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambXslTransformerWorker.class.getName());

    public DisambXslTransformerWorker(Path p, TermithIndex termithIndex) {
        _p = p;
        _termithIndex = termithIndex;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("convert xml file: " + _p);
            DisambXslTransformer disambXslTransformer = new DisambXslTransformer(_p.toFile(),_xslResources);
            _termithIndex.get_disambTranformedFile().put(
                    FilesUtils.nameNormalizer(_p.getFileName().toString()),
                    FilesUtils.writeXml(disambXslTransformer.xsltTransformation(),_p.toAbsolutePath())
            );
            LOGGER.debug("Extraction done for file: " + _p);
        } catch (IOException e) {
            LOGGER.error("File Exception",e);
        }
    }
}
