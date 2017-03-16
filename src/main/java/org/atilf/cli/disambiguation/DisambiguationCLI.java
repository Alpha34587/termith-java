package org.atilf.cli.disambiguation;

import ch.qos.logback.classic.Level;
import org.apache.commons.cli.*;
import org.atilf.models.TermithIndex;
import org.atilf.tools.BenchmarkFactory;
import org.atilf.tools.CLIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Paths;

//import org.atilf.runner.Disambiguation;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class DisambiguationCLI {
    private static final Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationCLI.class.getName());

    private DisambiguationCLI() {
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

        Option learning = new Option("le","learning",true,"path of the learning corpus in tei format");
        learning.setRequired(true);
        Option evaluation = new Option("e","evaluation",true,"path of the evaluation corpus in tei format");
        evaluation.setRequired(true);
        Option out = new Option("o","output",true,"output folder");
        out.setRequired(true);
        Option lang = new Option("l","lang",true,"specify the language of the corpus");
        lang.setRequired(false);
        Option terminology = new Option("t","terminology",true,"set terminology path");
        terminology.setRequired(false);
        Option annotation = new Option("a","annotation",true,"set annotation json path");
        annotation.setRequired(false);
        Option debug = new Option("d","debug",true,"show debug log");
        debug.setRequired(false);
        debug.setArgs(0);
        Option score = new Option("s","score",true,"evaluation of module.disambiguation");
        score.setRequired(false);
        Option benchmark = new Option("b","benchmark",true,"export benchmark");
        benchmark.setRequired(false);
        Option threshold = new Option("t","threshold",true,"context threshold");
        threshold.setRequired(false);

        options.addOption(learning);
        options.addOption(evaluation);
        options.addOption(out);
        options.addOption(lang);
        options.addOption(terminology);
        options.addOption(debug);
        options.addOption(annotation);
        options.addOption(score);
        options.addOption(benchmark);
        options.addOption(threshold);

        try {
            CommandLine line = parser.parse( options, args );

            TermithIndex.Builder termithBuilder = new TermithIndex.Builder()
                    .lang(line.getOptionValue("l"))
                    .learningFolder(line.getOptionValue("le"))
                    .evaluationFolder(line.getOptionValue("e"))
                    .terminology(line.getOptionValue("t"))
                    .export(line.getOptionValue("o"));
            CLIUtils.setGlobalLogLevel(Level.INFO);

            if (line.hasOption("debug")){
                CLIUtils.setGlobalLogLevel(Level.DEBUG);
            }
            if (line.hasOption("score")){
                termithBuilder.score(line.getOptionValue("s"));
            }
            if (line.hasOption("threshold")){
                termithBuilder.threshold(Integer.parseInt(line.getOptionValue("t")));
            }

            if (line.hasOption("benchmark")) {
                BenchmarkFactory._exportBenchmark = true;
                BenchmarkFactory._performancePath = Paths.get(line.getOptionValue("benchmark"));
            }
//            new Disambiguation(termithBuilder.build()).executeTasks();

        } catch (ParseException e) {
            LOGGER.error("There are some problems during executeTasks arguments : ",e);
        }
    }
}
