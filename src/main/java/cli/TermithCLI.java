package cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.Termith;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Simon Meoni on 25/07/16.
 */
class TermithCLI {
    private static Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithCLI.class.getName());

    private TermithCLI() {
        throw new IllegalAccessError("Utility class");
    }

    public static void main(String[] args) throws IOException, InterruptedException, TransformerException{
        CommandLineParser parser = new DefaultParser();

        Option in = Option.builder("i")
                .longOpt("input")
                .argName("input folder")
                .hasArg()
                .desc("path of the corpus in tei format")
                .required(true)
                .build();

        Option out = Option.builder("o")
                .longOpt("output")
                .argName("output folder")
                .hasArg()
                .desc("path of the output folder")
                .required(true)
                .build();

        Option lang = Option.builder("l")
                .longOpt("lang")
                .argName("language")
                .hasArg(true)
                .desc("specify the language of the corpus")
                .required(true)
                .build();

        Option trace = Option.builder("t")
                .longOpt("trace")
                .hasArg(false)
                .desc("write all the phase in the output folder")
                .build();

        Option treetagger = Option.builder("tt")
                .longOpt("treetagger")
                .argName("TreeTagger path")
                .hasArg(true)
                .required(true)
                .desc("set TreeTagger path")
                .build();

        options.addOption(in);
        options.addOption(out);
        options.addOption(trace);
        options.addOption(treetagger);
        options.addOption(lang);

        try {
            CommandLine line = parser.parse( options, args );
            Termith termith;
            if (options.hasOption("trace")) {

                termith = new Termith.Builder()
                        .lang(line.getOptionValue("l"))
                        .baseFolder(line.getOptionValue("i"))
                        .treeTaggerHome(line.getOptionValue("tt"))
                        .trace(true)
                        .export(line.getOptionValue("o"))
                        .build();
                termith.execute();
            }
            else {

                termith = new Termith.Builder()
                        .lang(line.getOptionValue("l"))
                        .baseFolder(line.getOptionValue("i"))
                        .treeTaggerHome(line.getOptionValue("tt"))
                        .export(line.getOptionValue("o"))
                        .build();
                termith.execute();
            }
        } catch (ParseException e) {
            LOGGER.info("There are some problems during parsing arguments : ",e);
        }
    }
}
