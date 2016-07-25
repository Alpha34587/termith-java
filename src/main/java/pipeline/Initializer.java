package pipeline;

import module.TextExtractor;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Initializer implements Callable<StringBuffer>{
    File file;

    public Initializer(File file) {
        this.file = file;
    }

    @Override
    public StringBuffer call() throws Exception {
        System.out.println("[" + Thread.currentThread().getName() + "]" +" Initialize file : " + file.getName());
        TextExtractor textExtractor = new TextExtractor(file);
        return textExtractor.XsltTransformation();

    }
}
