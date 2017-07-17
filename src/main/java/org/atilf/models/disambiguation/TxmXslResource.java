package org.atilf.models.disambiguation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Simon Meoni on 14/04/17.
 */
public class TxmXslResource extends DisambiguationXslResources{
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslResources.class.getName());

    public TxmXslResource(String resourcePath) {
        super(resourcePath);
    }
}
