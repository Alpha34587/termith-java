package org.atilf.module.disambiguation;

import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.extractor.TextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 *         the DisambiguationXslTransformer convert tei file into xml working file format.
 *         There are some example on the folder : src/test/resources/corpus/disambiguation/transform-tei.
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslTransformer extends TextExtractor {
    private CountDownLatch _transformCounter;
    private Map<String, Path> _xmlTransformedMap;
    private Path _outputPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslTransformer.class.getName());

    /**
     * constructor of DisambiguationXslTransformer module
     * @param file input tei file
     * @param xslResources the xslResource object who have as field the parse xsl stylesheet
     *                     used to convert the input file
     */
    public DisambiguationXslTransformer(File file, XslResources xslResources) {
        super(file,xslResources);}

    /**
     * constructor of DisambiguationXslTransformer module
     * @param transformCounter the transformCounter in an object of java.util.concurrent. When the transformation
     *                         is performed the counter is decreased. This counter is used in the ContextLexiconThread
     *                         in order to wait that the transformation is performed for all the corpus
     * @param file input tei file
     * @param xslResources the xslResource object who have as field the parse xsl stylesheet
     *                     used to convert the input file
     */
    public DisambiguationXslTransformer(File file, CountDownLatch transformCounter,
                                        TermithIndex termithIndex, XslResources xslResources){
        super(file,xslResources);
        _transformCounter = transformCounter;
        _xmlTransformedMap = termithIndex.getLearningTransformedFile();
        _outputPath = TermithIndex.getOutputPath();
    }

    public DisambiguationXslTransformer(File file, CountDownLatch transformCounter,
                                        Map<String, Path> xmlTransformedMap, XslResources xslResources){
        this(file,transformCounter,xmlTransformedMap,xslResources,TermithIndex.getOutputPath());
    }


    public DisambiguationXslTransformer(File file, CountDownLatch transformCounter,
                                        Map<String,Path> xmlTransformedMap, XslResources xslResources, Path outputPath){
        super(file,xslResources);
        _transformCounter = transformCounter;
        _xmlTransformedMap = xmlTransformedMap;
        _outputPath = outputPath;
    }

    /**
     * this method call the inherited method execute from textExtractor. the method execute transform the file
     * and return a StringBuffer with the result. this result is wrote in a file and the path is retained on a map in a
     * termithIndexObject
     */
    @Override
    public void run() {
        super.run();
        try {
            LOGGER.info("convert xml file: " + _file.getAbsolutePath());
            _xmlTransformedMap.put(
                    FilesUtils.nameNormalizer(
                    /*
                    key of the entry
                     */
                            _file.getName()),
                    /*
                    transform and write file
                     */
                    FilesUtils.writeFile(
                            execute(),
                            _outputPath,
                            _file.getName())
            );
            /*
            decrease the counter
             */
            _transformCounter.countDown();
            LOGGER.info("Extraction done for file: " + _file);
        } catch (IOException e) {
            LOGGER.error("File Exception: ",e);
        }
    }
}
