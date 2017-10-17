package org.atilf.resources.disambiguation;

import org.apache.commons.io.IOUtils;
import org.atilf.module.disambiguation.lexiconProfile.SpecCoefficientInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Simon Meoni Created on 28/11/16.
 */
public class RResources {
    private static StringBuilder script;
    private static final Logger LOGGER = LoggerFactory.getLogger(RResources.class.getName());

    private RResources() {
        throw new IllegalAccessError("Utility class");
    }

    public static void init(String resourcePath){
        String script = null;
        InputStream resourceAsStream = SpecCoefficientInjector.class.getClassLoader().getResourceAsStream(resourcePath);
        try {
            script = IOUtils.toString(resourceAsStream, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("cannot read R resource",e);
        }
        assert script != null;
        RResources.script = new StringBuilder(script);
    }

    public static StringBuilder getScript() {
        return script;
    }
}
