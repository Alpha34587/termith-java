package org.atilf.module.disambiguation;

import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 *         the DisambiguationXslTransformer convert tei file into xml working file format.
 *         There are some example on the folder : src/test/resources/corpus/disambiguation/transform-tei.
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslTransformer extends Module{
    private CountDownLatch _transformCounter;
    private Map<String, Path> _xmlTransformedMap;
    private TermithIndex _termithIndex;
    private Path _outputPath;
    private File _file;
    private XslResources _xslResources;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslTransformer.class.getName());
    private StringBuilder _transformedContent;

    /**
     * constructor for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     * @param termithIndex the termithIndex of a process
     */
    public DisambiguationXslTransformer(File file, TermithIndex termithIndex, XslResources xslResources) {
        super(termithIndex);
        _file = file;
        _xslResources = xslResources;
    }

    /**
     * constructor for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     */
    public DisambiguationXslTransformer(File file, XslResources xslResources) {
        _file = file;
        _xslResources = xslResources;
    }

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
        _file = file;
        _transformCounter = transformCounter;
        _xmlTransformedMap = termithIndex.getLearningTransformedFile();
        _termithIndex = termithIndex;
        _xslResources = xslResources;
        _outputPath = TermithIndex.getOutputPath();
    }

    public DisambiguationXslTransformer(File file, TermithIndex termithIndex, CountDownLatch transformCounter,
                                        Map<String, Path> xmlTransformedMap, XslResources xslResources){
        this(file,termithIndex,transformCounter,xmlTransformedMap,xslResources,TermithIndex.getOutputPath());
    }


    public DisambiguationXslTransformer(File file,TermithIndex termithIndex, CountDownLatch transformCounter,
                                        Map<String,Path> xmlTransformedMap, XslResources xslResources, Path outputPath){
        _file = file;
        _termithIndex = termithIndex;
        _transformCounter = transformCounter;
        _xmlTransformedMap = xmlTransformedMap;
        _xslResources = xslResources;
        _outputPath = outputPath;
    }

    public StringBuilder getTransformedContent() {
        return _transformedContent;
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
                            _transformedContent,
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

    @Override
    public void execute() {
        /*
        instantiate needed variables for transformation. The StringWriter variable is used to return
        the result as a StringBuilder variable
         */
        Source input = new StreamSource(_file);
        Transformer transformer;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            _logger.debug("apply " + _xslResources._stylesheet.toString() + "to xml file" + input.toString());
            /*
            get new transformer
             */
            transformer = _xslResources._factory.newTransformer();
            /*
            apply the transformation
             */
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            _logger.error("could not apply the xslt transformation to the file : " + _file.getAbsolutePath() + " ", e);
        }
        _transformedContent =  new StringBuilder(stringWriter.getBuffer());
    }
}
