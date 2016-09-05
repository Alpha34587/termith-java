package module.treetagger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collector;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerWrapper {

    private final StringBuffer txt;
    private TreeTaggerParameter treeTaggerParameter;
    private String treeTaggerHome;

    private StringBuilder ttOut;

    public TreeTaggerWrapper(StringBuffer txt, String treeTaggerHome, TreeTaggerParameter treeTaggerParameter) {

        this.txt = txt;
        this.treeTaggerHome = treeTaggerHome;
        this.ttOut = new StringBuilder();
        this.treeTaggerParameter = treeTaggerParameter;
    }

    public StringBuilder getTtOut() {
        return ttOut;
    }

    public void execute() throws IOException {
        Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","echo \"" + puntuationParsing()
                + "\" | " + treeTaggerParameter.parse()});
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        ttOut  =
                bufferedReader.lines().map(String::toString).collect(Collector.of(
                        StringBuilder::new,
                        (stringBuilder, str) -> stringBuilder.append(str).append("\n"),
                        StringBuilder::append)
                );


    }

    public String puntuationParsing(){
        Deque<String> oldPuncts = new ArrayDeque<>();
        Deque<String> newPuncts = new ArrayDeque<>();
        oldPuncts.add(".");
        oldPuncts.add("?");
        oldPuncts.add("!");
        oldPuncts.add(";");
        oldPuncts.add(",");
        oldPuncts.add(",");
        oldPuncts.add(":");
        oldPuncts.add("(");
        oldPuncts.add(")");
        oldPuncts.add("[");
        oldPuncts.add("]");
        oldPuncts.add("{");
        oldPuncts.add("}");
        oldPuncts.add("\"");
        oldPuncts.add("\'");

        newPuncts.add("\n.\n");
        newPuncts.add("\n?\n");
        newPuncts.add("\n!\n");
        newPuncts.add("\n;\n");
        newPuncts.add("\n,\n");
        newPuncts.add("\n,\n");
        newPuncts.add("\n:\n");
        newPuncts.add("\n(\n");
        newPuncts.add("\n)\n");
        newPuncts.add("\n[\n");
        newPuncts.add("\n]\n");
        newPuncts.add("\n{\n");
        newPuncts.add("\n}\n");
        newPuncts.add("\n\"\n");
        newPuncts.add("\n\'\n");

        String parseTxt = txt.toString().replace(" ", "\n");

        while (!oldPuncts.isEmpty()) {
                    parseTxt = parseTxt.replace(oldPuncts.poll(), newPuncts.poll());
        }

        return parseTxt;
    }
}
