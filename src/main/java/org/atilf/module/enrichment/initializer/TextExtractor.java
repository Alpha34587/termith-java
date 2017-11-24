package org.atilf.module.enrichment.initializer;

import org.atilf.models.TermithIndex;
import org.atilf.module.Module;
import org.atilf.module.tools.FilesUtils;
import org.atilf.resources.enrichment.XslResources;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

/**
 * The textExtractor class is used extract the plain text of a tei file
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractor extends Module {
    private String _fileName;
    private File _file;
    private Path _out;
    private XslResources _xslResources;
    private StringBuilder _extractedText;

    /**
     * constructor for textExtractor
     * @param fileName Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     * @param termithIndex the termithIndex of a process
     */
    public TextExtractor(String fileName, TermithIndex termithIndex, Path out, XslResources xslResources) {
        super(termithIndex);
        _file = termithIndex.getXmlCorpus().get(fileName).toFile();
        _out = out;
        _fileName = fileName;
        _xslResources = xslResources;
    }

    /**
     * constructor for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources contains the parsed xsl stylesheet
     */
    TextExtractor(File file, XslResources xslResources) {
        _file = file;
        _xslResources = xslResources;
    }

    StringBuilder getExtractedText() {
        return _extractedText;
    }

    /**
     * this method apply an xsl stylesheet to a file given in the _file field. it extracts the plain text of the tei file
     */
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
        if (_logger.isDebugEnabled()) {
            _logger.debug(
                    "apply {} to xml file {}",
                    _xslResources._stylesheet.toString(),
                    input.toString()
            );
        }
        try {

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

        _extractedText =  new StringBuilder(stringWriter.getBuffer());
    }

    /*
    this method call execute method and write the result into the working directory and keep the path
    into the _termithIndex field
     */
    @Override
    public void run() {
        super.run();
        try {
            _logger.debug("Extracting text of file: {}",_file);
            /*
            check if the result of the extraction is not empty
             */
            if (_extractedText.length() != 0) {
                /*
                put the result into the extractedText Map
                 */
                _termithIndex.getExtractedText().put(
                        _fileName,
                        FilesUtils.writeObject(_extractedText, _out));
            }

            else {
                _logger.info("{} has empty body",_file);
                _termithIndex.getXmlCorpus().remove(_fileName);
            }
            _logger.debug("Extraction done for file: {}",_file);
        } catch (IOException e) {
            _logger.error("File Exception : ",e);
        }
    }
}
