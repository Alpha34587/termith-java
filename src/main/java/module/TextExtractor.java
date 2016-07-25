package module;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TextExtractor {
    File file;

    public TextExtractor(File file) {
        this.file = file;
    }

    public StringBuffer XsltTransformation() throws TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("src/main/resources/xsl/ExtractText.xsl"));
        Source input = new StreamSource(file);
        Transformer transformer = factory.newTransformer(xslt);
        StreamResult streamResult = new StreamResult(new StringWriter());

        transformer.transform(input, streamResult);
        return new StringBuffer(streamResult.getWriter().toString());

    }
}
