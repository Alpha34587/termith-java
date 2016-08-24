package module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TextExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextExtractor.class.getName());
    private File file;

    public TextExtractor(File file) {
        this.file = file;
    }

    public StringBuffer xsltTransformation() throws IOException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/main/resources/xsl/ExtractText.xsl"));
        Source input = new StreamSource(file);
        Transformer transformer;
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);

        try {
            LOGGER.info("apply " + xslt.toString() + "to xml file" + input.toString());
            transformer = factory.newTransformer(xslt);
            transformer.transform(input, streamResult);

        } catch (TransformerException e) {
            LOGGER.error("could not apply the xslt transformation : ", e);
        }

        return new StringBuffer(stringWriter.getBuffer());
    }
}
