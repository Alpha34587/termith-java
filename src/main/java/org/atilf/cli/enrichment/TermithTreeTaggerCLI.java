package org.atilf.cli.enrichment;

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
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TermithTreeTaggerCLI {
    private static final Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithTreeTaggerCLI.class.getName());

    private TermithTreeTaggerCLI() {
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

        Option in = new Option("i","input",true,"path of the corpus in tei format");
        in.setRequired(true);
        Option out = new Option("o","output",true,"output folder");
        out.setRequired(true);
        Option lang = new Option("l","lang",true,"specify the language of the corpus");
        lang.setRequired(true);
        Option debug = new Option("d","debug",true,"activate debug log mode");
        debug.setRequired(false);
        debug.setArgs(0);
        Option resource = new Option("r","resource",true,"set Resource path");
        resource.setRequired(true);

        options.addOption(in);
        options.addOption(out);
        options.addOption(debug);
        options.addOption(lang);
        options.addOption(resource);

        try {
            CommandLine line = parser.parse( options, args );

            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }

            run(line.getOptionValue("r"), line.getOptionValue("i"), line.getOptionValue("l"),line.getOptionValue("o"));
        } catch (ParseException e) {
            LOGGER.error("There are some problems during running : ",e);
        } catch (Exception e) {
            LOGGER.error("There are some problems during the creation of runner : ",e);
        }
    }

    public static void run(String resource, String base, String language, String output) throws Exception {
        Runner runner = new RunnerBuilder()
                .setLang(language)
                .setResourceManager(resource)
                .setBase(base)
                .setBpmnDiagram("termithTreeTagger.bpmn20.xml")
                .setOut(output)
                .createRunner();

        runner.execute();
    }

}
