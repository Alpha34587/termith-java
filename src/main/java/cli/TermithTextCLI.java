package cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.TermithText;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * @author Simon Meoni
 * Created on 25/07/16.
 */
class TermithTextCLI {
    private static Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithTextCLI.class.getName());

    private TermithTextCLI() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * @param args shell args
     * @throws IOException
     * @throws InterruptedException
     * @throws TransformerException
     */
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
            TermithText termithText;
            if (options.hasOption("trace")) {

                termithText = new TermithText.Builder()
                        .lang(line.getOptionValue("l"))
                        .baseFolder(line.getOptionValue("i"))
                        .treeTaggerHome(line.getOptionValue("tt"))
                        .trace(true)
                        .export(line.getOptionValue("o"))
                        .build();
                termithText.execute();
            }
            else {

                termithText = new TermithText.Builder()
                        .lang(line.getOptionValue("l"))
                        .baseFolder(line.getOptionValue("i"))
                        .treeTaggerHome(line.getOptionValue("tt"))
                        .export(line.getOptionValue("o"))
                        .build();
                termithText.execute();
            }
        } catch (ParseException e) {
            LOGGER.info("There are some problems during parsing arguments : ",e);
        }
    }
}