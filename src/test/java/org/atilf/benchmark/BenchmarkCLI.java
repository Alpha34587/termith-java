package org.atilf.benchmark;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Simon Meoni Created on 16/01/17.
 */
public class BenchmarkCLI {
    private static final Options options = new Options();
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkCLI.class.getName());

    public static void main(String[] args) throws IOException, NoSuchMethodException {
        CommandLineParser parser = new PosixParser();

        Option learning = new Option("le","learning",true,"path of the learning corpus in tei format");
        learning.setRequired(true);
        Option evaluation = new Option("e","evaluation",true,"path of the evaluation corpus in tei format");
        evaluation.setRequired(true);
        Option out = new Option("o","output",true,"output folder");
        out.setRequired(true);

        options.addOption(learning);
        options.addOption(evaluation);
        options.addOption(out);

        try {
            CommandLine line = parser.parse( options, args );
            Benchmark benchmark = new Benchmark(
                    line.getOptionValue("le"),
                    line.getOptionValue("e"),
                    line.getOptionValue("o"));
            benchmark.execute();
            FileUtils.deleteDirectory(new File(line.getOptionValue("o")));
        } catch (ParseException e) {
            LOGGER.error("There are some problems during execute arguments : ",e);
        }
    }
}
