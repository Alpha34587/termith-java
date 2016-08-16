package pipeline;

import module.initializer.TextExtractor;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class Initializer implements Callable<Initializer>{
    File file;
    StringBuffer plaintText;
    public Initializer(File file) {
        this.file = file;

    }

    public StringBuffer getPlaintText() {
        return plaintText;
    }

    @Override
    public Initializer call() throws Exception {
        System.out.println("[" + Thread.currentThread().getName() + "]" +" Initialize file : " + file.getName());
        TextExtractor textExtractor = new TextExtractor(file);
        this.plaintText = textExtractor.XsltTransformation();
        return this;
    }
}
