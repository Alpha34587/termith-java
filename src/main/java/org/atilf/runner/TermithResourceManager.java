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

    public enum TermithResource {
        TERMITH_TREETAGGER_BPMN_DIAGRAM("all/bpmn.20_schema/termithTreeTagger.bpmn20.xml"),
        TREETAGGER_HOME(System.getenv("TREETAGGER_HOME")),
        DISAMBIGUATION_BPMN_DIAGRAM("all/bpmn.20_schema/disambiguation.bpmn20.xml"),
        CUSTOM_BPMN_DIAGRAM("all/bpmn.20_schema/"),
        DISAMBIGUATION_XSL("all/xsl_stylesheet/disambiguation.xsl"),
        TXM_XSL("all/xsl_stylesheet/txm.xsl"),
        TEXT_XSL("all/xsl_stylesheet/extractText.xsl"),
        STANDOFF_FRAGMENTS("all/xml_standoff_fragments/"),
        TREETAGGER_PERL_SCRIPT("all/perl_script/utf8-tokenize-custom.perl"),
        DISAMBIGUATION_R_SCRIPT("all/r_script/specificities.R"),
        TERMSUITE_RESOURCE_PATH("all/termsuite_resources/termsuite-resources.jar"),
        TREE_TAGGER_MULTEX("[LANG]/treetagger_multex/treeTaggerMultexTag.json"),
        PHRASEOLOGY("[LANG]/lexical_resources/PhraseologyResource.json"),
        LST("[LANG]/lexical_resources/TransdisciplinaryResource.json");
        private String _path;
        private static String _lang = "en";

        TermithResource(String path) {
            _path = path;
        }

        public String getPath() {
            return _path.replace("[LANG]",_lang);
        }

        public static void setLang(String lang) {
            TermithResource._lang = lang;
        }
    }
}
