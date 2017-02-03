package org.atilf.cli.enrichment;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.atilf.models.TermithIndex;
import org.atilf.runner.TermithTreeTagger;
import org.atilf.tools.BenchmarkFactory;
import org.atilf.tools.CLIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Paths;

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
        Option keep = new Option("k","keep-files",true,"keep working files");
        keep.setRequired(false);
        keep.setArgs(0);
        Option treetagger = new Option("tt","treetagger",true,"set TreeTagger path");
        treetagger.setRequired(true);
        Option benchmark = new Option("b","benchmark",true,"export benchmark");
        benchmark.setRequired(false);

        options.addOption(in);
        options.addOption(out);
        options.addOption(debug);
        options.addOption(treetagger);
        options.addOption(lang);
        options.addOption(keep);
        options.addOption(benchmark);

        try {
            CommandLine line = parser.parse( options, args );
            TermithIndex termithIndex;

            termithIndex = new TermithIndex.Builder()
                    .lang(line.getOptionValue("l"))
                    .baseFolder(line.getOptionValue("i"))
                    .treeTaggerHome(line.getOptionValue("tt"))
                    .keepFiles(line.hasOption("keep-files"))
                    .export(line.getOptionValue("o"))
                    .build();
            CLIUtils.setGlobalLogLevel(Level.INFO);


            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }
            if (line.hasOption("benchmark")) {
                BenchmarkFactory._exportBenchmark = true;
                BenchmarkFactory._performancePath = Paths.get(line.getOptionValue("benchmark"));
            }
            new TermithTreeTagger(termithIndex).execute();
        } catch (ParseException e) {
            LOGGER.error("There are some problems during execute arguments : ",e);
        }
    }
}
