package org.atilf.module.treetagger;

import org.atilf.models.TagNormalizer;
import org.atilf.models.TermithIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.atilf.thread.InitializerThread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class SerializeTest {

    private StringBuilder tokenLemma;
    private StringBuilder lemma;
    private InitializerThread initializerThread;
    private Serialize serializeLemma;
    private Serialize serializeTag;
    private File jsonResFile;
    private TermithIndex termithIndex;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        termithIndex = new TermithIndex.Builder().export(temporaryFolder.getRoot().getPath()).build();
        TagNormalizer.initTag("en");
        termithIndex.addText("1",
                new StringBuilder("\n \n \nJournal of Gerontology: PSYCHOLOGICAL patient (1998@)"));
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(CorpusAnalyzerTest.convertExtractedText(termithIndex.get_extractedText()));

        tokenLemma = new StringBuilder(
                "Journal\tNP\tJournal\n" +
                        "of\tIN\tof\n" +
                        "Gerontology\tNP\tgerontology\n" +
                        ":\t:\t:\n" +
                        "PSYCHOLOGICAL\tJJ\tpsychological\n" +
                        "patient\tJJ\tpatient\n" +
                        "(\tJJ\t(\n" +
                        "1998@\tJJ\t1998@\n" +
                        ")\tJJ\t)");
        lemma = new StringBuilder(
                "\n \n \nJournal of Gerontology: PSYCHOLOGICAL patient (1998@)");

        jsonResFile = temporaryFolder.newFile("test1.json");

        serializeLemma = new Serialize(tokenLemma, jsonResFile.getAbsolutePath(),
                lemma, corpusAnalyzer.getAnalyzedTexts().get("1"));
    }

    @Test
    public void executeTest() throws Exception {
        serializeLemma.execute();
        String observe = String.join("\n",Files.readAllLines(jsonResFile.toPath()));
        String expected = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/serialize/file1.json")));
        expected = expected.replace("test1.json",jsonResFile.getAbsolutePath());
        Assert.assertEquals("files content must be equals : ",expected,observe);
    }

}