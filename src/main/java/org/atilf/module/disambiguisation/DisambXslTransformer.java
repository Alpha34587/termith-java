package org.atilf.module.disambiguisation;

import org.atilf.models.extractor.XslResources;
import org.atilf.module.extractor.TextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambXslTransformer extends TextExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambXslTransformer.class.getName());

    public DisambXslTransformer(File file, XslResources xslResources) {
        super(file,xslResources);}
}
