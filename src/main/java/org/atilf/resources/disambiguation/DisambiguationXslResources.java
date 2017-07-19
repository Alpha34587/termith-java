package org.atilf.resources.disambiguation;

import org.atilf.resources.enrichment.XslResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the xsl resource used during the module.disambiguation. the xsl stylesheet converts tei file into working file format
 * @author Simon Meoni
 *         Created on 02/11/16.
 */
public class DisambiguationXslResources extends XslResources{

        private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationXslResources.class.getName());

        public DisambiguationXslResources(String resourcePath) {
            super(resourcePath);
        }
}
