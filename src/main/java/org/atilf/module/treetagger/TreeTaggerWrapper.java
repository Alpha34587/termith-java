package org.atilf.module.treetagger;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
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

    public void execute() throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(new String[]{"bash","-c", treeTaggerParameter.parse() + " "
                + writeFile(parsingText())});

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        ttOut  =
                bufferedReader.lines().map(String::toString).collect(Collector.of(
                        StringBuilder::new,
                        (stringBuilder, str) -> stringBuilder.append(str).append("\n"),
                        StringBuilder::append)
                );
    }

    private String writeFile(String parsingText) throws IOException {
        File temp = File.createTempFile(UUID.randomUUID().toString(), ".tt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(parsingText);
        bw.flush();
        bw.close();
        return temp.getAbsolutePath();
    }

    public String parsingText(){
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

        String parseTxt = txt.toString().trim();
        parseTxt = parseTxt.replace(" ", "\n");
        while (!oldPuncts.isEmpty()) {
                    parseTxt = parseTxt.replace(oldPuncts.poll(), newPuncts.poll());
        }

        return parseTxt;
    }
}
