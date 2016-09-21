package org.atilf.module.extractor;

import org.atilf.models.XslResources;
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
 * The textExtractor class is used extract the plain text of an XML file
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextExtractor.class.getName());
    private File file;
    private XslResources xslResources;

    /**
     * builder for textExtractor
     * @param file Treated xml/tei file
     * @param xslResources
     */
    public TextExtractor(File file, XslResources xslResources) {
        this.file = file;
        this.xslResources = xslResources;
    }

    /**
     * this method apply an xsl stylesheet to a file given in parameter. it extracts the plain text of the xml file
     * @return the extracted text
     * @throws IOException
     */
    public StringBuilder xsltTransformation() throws IOException {
        Source input = new StreamSource(file);
        Transformer transformer = null;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            LOGGER.debug("apply " + xslResources.EXTRACT_TEXT.toString() + "to xml file" + input.toString());
            transformer = xslResources.FACTORY.newTransformer(xslResources.EXTRACT_TEXT);
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            LOGGER.error("could not apply the xslt transformation : ", e);
        }

        return new StringBuilder(stringWriter.getBuffer());
    }
}
