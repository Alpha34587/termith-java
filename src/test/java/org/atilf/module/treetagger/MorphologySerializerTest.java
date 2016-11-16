package org.atilf.module.treetagger;

import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.CorpusAnalyzer;
import org.atilf.models.treetagger.TagNormalizer;
import org.atilf.module.termsuite.morphology.MorphologySerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class MorphologySerializerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private MorphologySerializer _morphologySerializerLemma;
    private File _jsonResFile;
    private TermithIndex _termithIndex;

    @Before
    public void setUp() throws IOException {
        _termithIndex = new TermithIndex.Builder().export(temporaryFolder.getRoot().getPath()).build();
        TagNormalizer.initTag("en");
        _termithIndex.addText("1",
                new StringBuilder("\n \n \nJournal of Gerontology: PSYCHOLOGICAL patient (1998@)"));
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(CorpusAnalyzerTest.convertExtractedText(_termithIndex.getExtractedText()));

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
        String observe = String.join("\n",Files.readAllLines(_jsonResFile.toPath()));
        String expected = String.join("\n", Files.readAllLines(Paths.get("src/test/resources/serialize/file1.json")));
        expected = expected.replace("test1.json", _jsonResFile.getAbsolutePath());
        Assert.assertEquals("files content must be equals : ",expected,observe);
    }

}