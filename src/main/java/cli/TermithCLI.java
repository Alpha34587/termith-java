package cli;

import org.apache.commons.cli.*;
import runner.Termith;
import tool.TeiImporter;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Simon Meoni on 25/07/16.
 */
public class TermithCLI {
    static TeiImporter teiImporter;
    static Termith termith;
    static Options options = new Options();

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

        Option trace = Option.builder("t")
                .longOpt("trace")
                .hasArg(false)
                .desc("write all the phase in the output folder")
                .build();

        options.addOption(in);
        options.addOption(out);
        options.addOption(trace);

        try {
            CommandLine line = parser.parse( options, args );
            if (options.hasOption("trace")) {
                termith = new Termith.Builder()
                        .TeiImporter(line.getOptionValue("i"))
                        .Trace(true)
                        .Build();
                termith.Run();
            }
            else {
                termith = new Termith.Builder()
                        .TeiImporter(line.getOptionValue("i"))
                        .Build();
                termith.Run();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
