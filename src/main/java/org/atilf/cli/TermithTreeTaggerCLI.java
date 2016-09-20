package org.atilf.cli;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.atilf.models.TermithIndex;
import org.atilf.module.tools.CLIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.runner.Exporter;
import org.atilf.runner.TermithTreeTagger;

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
        Option trace = new Option("d","debug",true,"activate debug log mode");
        trace.setRequired(false);
        Option treetagger = new Option("tt","treetagger",true,"set TreeTagger path");
        treetagger.setRequired(true);

        options.addOption(in);
        options.addOption(out);
        options.addOption(trace);
        options.addOption(treetagger);
        options.addOption(lang);

        try {
            CommandLine line = parser.parse( options, args );
            TermithIndex termithIndex;

            termithIndex = new TermithIndex.Builder()
                    .lang(line.getOptionValue("l"))
                    .baseFolder(line.getOptionValue("i"))
                    .treeTaggerHome(line.getOptionValue("tt"))
                    .export(line.getOptionValue("o"))
                    .build();
            CLIUtils.setGlobalLogLevel(Level.INFO);

            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }


            new TermithTreeTagger(termithIndex).execute();
            Exporter exporter = new Exporter(termithIndex);
            exporter.execute();
        } catch (ParseException e) {
            LOGGER.error("There are some problems during parsing arguments : ",e);
        }
    }
}
