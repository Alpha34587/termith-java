package module.treetagger;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class Serialize {

    private Deque<String> tokenDeque;
    private final int totalSize;
    private TtJsonFile ttJsonFile;

    public Serialize(StringBuilder tokenDeque, int totalSize) {
        this.tokenDeque = new ArrayDeque();
        populateTokenDeque(tokenDeque);
        this.totalSize = totalSize;
    }

    private void populateTokenDeque(StringBuilder tokenDeque) {
    }

    public void execute() throws IOException {
        Integer[] offset = new Integer[]{0,1};
        JsonTag jsonTag = new JsonTag();

        while (!tokenDeque.isEmpty()){
            String[] line = tokenDeque.poll().split("\t");
            findOffset(offset,line[0]);
            normalizeMultex(line[1]);
            addLemma(line[2]);

        }
    }

    private void addLemma(String s) {
    }

    private void normalizeMultex(String s) {
    }

    private void findOffset(Integer[] offset, String s) {
    }


}
