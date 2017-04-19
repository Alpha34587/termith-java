package org.atilf.cli.disambiguation;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.atilf.module.tools.CLIUtils;
import org.atilf.runner.Runner;
import org.atilf.runner.RunnerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by smeoni on 19/04/17.
 */
public class TxmSerializerCLI {
    private static final Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationCLI.class.getName());

    private TxmSerializerCLI() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * @param args shell args
     * @throws IOException
     * @throws InterruptedException
     * @throws TransformerException
     */
    public static void main(String[] args) throws IOException, InterruptedException, TransformerException {
        CommandLineParser parser = new PosixParser();

        Option learning = new Option("i","input",true,"path of the learning corpus in tei format");
        learning.setRequired(true);
        Option out = new Option("o","output",true,"output folder");
        out.setRequired(true);
        Option debug = new Option("d","debug",true,"show debug log");
        debug.setRequired(false);
        debug.setArgs(0);
        Option annotation = new Option("a","_annotation",true,"which auto/manual annotation should be extracted ?");
        annotation.setRequired(true);

        options.addOption(learning);
        options.addOption(out);
        options.addOption(debug);
        options.addOption(annotation);

        try {
            CommandLine line = parser.parse( options, args );
            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }

            RunnerBuilder runnerBuilder = new RunnerBuilder()
                    .setTxmInputPath(line.getOptionValue("i"))
                    .setBpmnDiagram("runner/txmSerializer.bpmn20.xml")
                    .setAnnotation(line.getOptionValue("a"))
                    .setOut(line.getOptionValue("o"));

            Runner runner = runnerBuilder.createRunner();
            runner.execute();
        } catch (ParseException e) {
            LOGGER.error("There are some problems during running : ",e);
        } catch (Exception e) {
            LOGGER.error("There are some problems during the creation of runner : ",e);
        }
    }
}
