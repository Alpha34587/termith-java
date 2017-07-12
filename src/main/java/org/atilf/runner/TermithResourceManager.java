package org.atilf.runner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Simon Meoni on 11/07/17.
 */
public class TermithResourceManager {

    TermithResourceManager() {
        throw new IllegalStateException("Static resource manager class !");
    }
    public static void addToClasspath(String path) throws Exception {
        URLClassLoader urlClassLoader = null;
        try {
            File f = new File(path);
            URI u = f.toURI();
            urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class<URLClassLoader> urlClass = URLClassLoader.class;
            Method method;
            method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(urlClassLoader, new Object[]{u.toURL()});
        } catch (Exception e) {
            throw new RuntimeException("Could not add "+ path +" to classpath", e);
        }
    }

    public enum TermithResourcePath {
        TERMITH_TREETAGGER_BPMN_DIAGRAM(""),
        DISAMBIGUATION_BPMN_DIAGRAM(""),
        CUSTOM_BPMN_DIAGRAM(""),
        DISAMBIGUATION_XSL(""),
        TXM_XSL(""),
        LIST_ANNOTATION(""),
        LST_SPAN(""),
        LST_TEI_HEADER(""),
        MS_SPAN(""),
        MS_TEI_HEADER(""),
        NS(""),
        PH_SPAN(""),
        PH_TEI_HEADER(""),
        STANDOFF(""),
        T_INTERP_GRP(""),
        T_SPAN(""),
        T_TEI_HEADER(""),
        XML_DECLARATION(""),
        TREE_TAGGER_MULTEX_EN(""),
        TREE_TAGGER_MULTEX_FR(""),
        TEXT_XSL("");
        private String path;

        TermithResourcePath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
