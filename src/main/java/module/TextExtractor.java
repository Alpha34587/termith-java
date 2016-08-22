package module;

import thread.TermithXmlInjector;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TextExtractor {
    private static Logger LOGGER = Logger.getLogger(TextExtractor.class.getName());
    private File file;

    public TextExtractor(File file) {
        this.file = file;
    }

    public StringBuffer XsltTransformation() throws TransformerException, IOException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/main/resources/xsl/ExtractText.xsl"));
        Source input = new StreamSource(file);
        Transformer transformer = factory.newTransformer(xslt);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(input, streamResult);
        return new StringBuffer(stringWriter.getBuffer());

    }
}
