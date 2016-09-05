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
    private StringBuffer txt;

    public Serialize(StringBuilder tokenDeque, StringBuffer txt, int totalSize) {
        this.tokenDeque = new ArrayDeque();
        populateTokenDeque(tokenDeque);
        this.txt = txt;
        this.totalSize = totalSize;
        this.ttJsonFile = new TtJsonFile();
    }

    public TtJsonFile getTtJsonFile() {
        return ttJsonFile;
    }

    private void populateTokenDeque(StringBuilder tokenDeque) {
        for (String token : tokenDeque.toString().split("\n")) {
            this.tokenDeque.add(token);
        }
    }

    public void execute() throws IOException {
        Integer[] offset = new Integer[]{0,1};

        while (!tokenDeque.isEmpty()){
            String[] line = tokenDeque.poll().split("\t");
            JsonTag jsonTag = new JsonTag();
            offset = findOffset(offset,line[0]);
            jsonTag.setOffset(offset[0],offset[1]);
            jsonTag.setTag(addTagCat(line[1]));
            jsonTag.setLemma(addLemma(line[2]));
            ttJsonFile.addJsonTag(jsonTag);
            offset[1] = offset[0] + 1;

        }
    }

    public String addLemma(String token) {
        return token;
    }

    public String addTagCat(String tag) {
        return tag;
    }

    public Integer[] findOffset(Integer[] offset, String token) {
        char[] letterCharArray = token.toCharArray();
        boolean findBegin = false;
        int begin = -1;
        int end = -1;
        int cpt = 0;
        while (end == -1){
            char ch = letterCharArray[cpt];

            if (!findBegin && txt.charAt(offset[0]) == ch) {
                findBegin = true;
                begin = offset[0];
            }

            if (cpt == letterCharArray.length -1  && txt.charAt(offset[0]) == ch)
                end = offset[1];

            if (findBegin)
                cpt++;
            offset[0]++;
            offset[1]++;
        }

        return new Integer[]{begin,end};
    }

}
