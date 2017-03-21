package org.atilf.module.enrichment.analyzer.treeTaggerWorker;

import org.atilf.models.enrichment.CorpusAnalyzer;
import org.atilf.models.enrichment.TagNormalizer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class MorphologySerializerTest {
    
    private static  MorphologySerializer _morphologySerializerLemma;
    private static  File _jsonResFile;

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    @BeforeClass
    public static void setUp() throws IOException {
        TagNormalizer.initTag("en");
        Map<String,StringBuilder> textExtracted = new HashMap<>();
        textExtracted.put("1",new StringBuilder("\n \n \nJournal of Gerontology: " +
                "PSYCHOLOGICAL patient (1998@)"));
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(textExtracted);

        StringBuilder tokenLemma = new StringBuilder(
                "Journal\tNP\tJournal\n" +
                        "of\tIN\tof\n" +
                        "Gerontology\tNP\tgerontology\n" +
                        ":\t:\t:\n" +
                        "PSYCHOLOGICAL\tJJ\tpsychological\n" +
                        "patient\tJJ\tpatient\n" +
                        "(\tJJ\t(\n" +
                        "1998@\tJJ\t1998@\n" +
                        ")\tJJ\t)");
        StringBuilder lemma = new StringBuilder(
                "\n \n \nJournal of Gerontology: PSYCHOLOGICAL patient (1998@)");

        _jsonResFile = temporaryFolder.newFile("test1.json");

        _morphologySerializerLemma = new MorphologySerializer(tokenLemma, _jsonResFile.getAbsolutePath(),
                lemma, corpusAnalyzer.getAnalyzedTexts().get("1"));
    }

    @Test
    public void executeTest() throws Exception {
        _morphologySerializerLemma.execute();
        String observed = String.join("\n",Files.readAllLines(_jsonResFile.toPath()));
        String expected = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/module/enrichment/analyze/treeTaggerWorker/file1.json")));
        expected = expected.replace("test1.json", _jsonResFile.getAbsolutePath());
        Assert.assertEquals("files content must be equals : ",expected,observed);
    }

}
