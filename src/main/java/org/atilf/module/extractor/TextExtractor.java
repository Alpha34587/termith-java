package org.atilf.module.extractor;

import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
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

/**
 * The textExtractor class is used extract the plain text of a tei file
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractor implements Runnable{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
    protected File _file;
    protected TermithIndex _termithIndex;
    protected XslResources _xslResources;

    /**
     * constructor for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     * @param termithIndex the termithIndex of a process
     */
    public TextExtractor(File file, TermithIndex termithIndex, XslResources xslResources) {
        _file = file;
        _termithIndex = termithIndex;
        _xslResources = xslResources;
    }

    /**
     * constructor for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     */
    public TextExtractor(File file, XslResources xslResources) {
        _file = file;
        _xslResources = xslResources;
    }

    /**
     * this method apply an xsl stylesheet to a file given in the _file field. it extracts the plain text of the tei file
     * @return the extracted text
     * @throws IOException throw IO exception of StreamResult variable
     */
    public StringBuilder execute() throws IOException {
        /*
        instantiate needed variables for transformation. The StringWriter variable is used to return
        the result as a StringBuilder variable
         */
        Source input = new StreamSource(_file);
        Transformer transformer;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            LOGGER.debug("apply " + _xslResources._stylesheet.toString() + "to xml file" + input.toString());
            /*
            get new transformer
             */
            transformer = _xslResources._factory.newTransformer();
            /*
            apply the transformation
             */
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            LOGGER.error("could not apply the xslt transformation to the file : " + _file.getAbsolutePath() + " ", e);
        }

        return new StringBuilder(stringWriter.getBuffer());
    }

    /*
    this method call execute method and write the result into the working directory and keep the path
    into the _termithIndex field
     */
    @Override
    public void run() {
        try {
            LOGGER.debug("Extracting text of file: " + _file);
            /*
            call execute method
             */
            StringBuilder extractedText = execute();
            /*
            check if the result of the extraction is not empty
             */
            if (extractedText.length() != 0) {
                /*
                put the result into the extractedText Map
                 */
                _termithIndex.getExtractedText().put(FilesUtils.nameNormalizer(_file.toString()),
                        FilesUtils.writeObject(extractedText, TermithIndex.getOutputPath()));
            }
            /*
            otherwise a log is thrown and the corpusSize is decremented by 1
             */
            else {
                LOGGER.info(_file + " has empty body");
                _termithIndex.setCorpusSize(_termithIndex.getCorpusSize() - 1);
            }
            LOGGER.debug("Extraction done for file: " + _file);
        } catch (IOException e) {
            LOGGER.error("File Exception",e);
        }
    }
}
