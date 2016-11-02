package org.atilf.module.disambiguisation;

import org.atilf.models.disambiguisation.DisambXslResources;
import org.atilf.models.extractor.XslResources;
import org.atilf.module.extractor.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambXslTransformer.class.getName());
    private final File _file;
    private final DisambXslResources _xslResources;

    public DisambXslTransformer(File file, DisambXslResources xslResources) {
        _file = file;
        _xslResources = xslResources;
    }

    public StringBuilder xsltTransformation() {
        Source input = new StreamSource(_file);
        Transformer transformer;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            LOGGER.debug("apply " + _xslResources.getDisamb().toString() + "to xml file" + input.toString());
            transformer = _xslResources.getFactory().newTransformer(_xslResources.getDisamb());
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            LOGGER.error("could not apply the xslt transformation : ", e);
        }

        return new StringBuilder(stringWriter.getBuffer());    }
}
