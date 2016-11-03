package org.atilf.module.extractor;

import org.atilf.models.extractor.XslResources;
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
 * The textExtractor class is used extract the plain text of an XML _file
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
    protected File _file;
    protected XslResources _xslResources;

    /**
     * builder for textExtractor
     * @param file Treated xml/tei _file
     * @param xslResources
     */
    public TextExtractor(File file, XslResources xslResources) {
        _file = file;
        _xslResources = xslResources;
    }

    /**
     * this method apply an xsl stylesheet to a _file given in parameter. it extracts the plain text of the xml _file
     * @return the extracted text
     * @throws IOException
     */
    public StringBuilder xsltTransformation() throws IOException {
        Source input = new StreamSource(_file);
        Transformer transformer;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            LOGGER.debug("apply " + _xslResources._stylesheet.toString() + "to xml file" + input.toString());
            transformer = _xslResources._factory.newTransformer(_xslResources._stylesheet);
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            LOGGER.error("could not apply the xslt transformation to the file : " + _file.getAbsolutePath() + " ", e);
        }

        return new StringBuilder(stringWriter.getBuffer());
    }
}
