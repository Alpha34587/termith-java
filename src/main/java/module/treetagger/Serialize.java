package module.treetagger;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class Serialize {

    private StringBuilder ttOut;
    private final int totalSize;

    public Serialize(StringBuilder ttOut, int totalSize) {
        this.ttOut = ttOut;
        this.totalSize = totalSize;
    }

    public void execute() throws IOException {

    }
}
