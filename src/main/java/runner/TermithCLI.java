package runner;

import tool.TeiImporter;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TermithCLI {
    static TeiImporter teiImporter;
    static Termith termith;
    public static void main(String[] args) throws IOException, InterruptedException, TransformerException {
        teiImporter = new TeiImporter(args[0]);
        termith = new Termith(teiImporter);
        termith.run();
    }
}
