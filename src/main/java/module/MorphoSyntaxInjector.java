package module;

import thread.Initializer;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Simon Meoni on 17/08/16.
 */
public class MorphoSyntaxInjector {

    private File json;
    private File txt;
    private File xml;

    public MorphoSyntaxInjector(File json, File txt, File xml) {
        this.json = json;
        this.txt = txt;
        this.xml = xml;
    }

    public void tokenize() {

    }

    public void standOffInjection(){

    }
}
