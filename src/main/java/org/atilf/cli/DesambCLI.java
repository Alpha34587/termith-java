package org.atilf.cli;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.atilf.models.TermithIndex;
import org.atilf.module.tools.CLIUtils;
import org.atilf.runner.Disambiguisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class DesambCLI {
    private static final Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithTreeTaggerCLI.class.getName());

    private DesambCLI() {
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
        Option terminology = new Option("t","terminology",true,"set terminology path");
        terminology.setRequired(true);
        Option annotation = new Option("a","annotation",true,"set annotation json path");
        annotation.setRequired(true);


        options.addOption(in);
        options.addOption(out);
        options.addOption(lang);
        options.addOption(terminology);
        options.addOption(annotation);

        try {
            CommandLine line = parser.parse( options, args );
            TermithIndex termithIndex;

            termithIndex = new TermithIndex.Builder()
                    .lang(line.getOptionValue("l"))
                    .baseFolder(line.getOptionValue("i"))
                    .terminology(line.getOptionValue("t"))
                    .annotation(line.getOptionValue("a"))
                    .export(line.getOptionValue("o"))
                    .build();
            CLIUtils.setGlobalLogLevel(Level.INFO);

            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }


            new Disambiguisation(termithIndex).execute();

        } catch (ParseException e) {
            LOGGER.error("There are some problems during parsing arguments : ",e);
        }
    }
}
