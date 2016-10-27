package org.atilf.module.tools;

import com.google.common.collect.HashMultiset;
import com.google.common.io.Files;
import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.EvaluationProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambTeiWriterTest {

    DisambTeiWriter teiWriter;
    Map<String,EvaluationProfile> evaluationProfile;
    private TermithIndex termithIndex;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Before
    public void setUp() throws Exception {
        termithIndex = new TermithIndex.Builder().export(temporaryFolder.getRoot().getPath()).build();
        evaluationProfile = new HashMap<>();
        evaluationProfile.put("entry-13471_DM1", new EvaluationProfile(HashMultiset.create()));
        evaluationProfile.put("entry-7263_DM3", new EvaluationProfile(HashMultiset.create()));
        evaluationProfile.put("entry-990_noDM", new EvaluationProfile(HashMultiset.create()));
        evaluationProfile.get("entry-13471_DM1").set_disambId("DaOn");
        evaluationProfile.get("entry-7263_DM3").set_disambId("DaOn");
        evaluationProfile.get("entry-990_noDM").set_disambId("DaOff");

        teiWriter = new DisambTeiWriter(
                "src/test/resources/corpus/tei/test2.xml",
                evaluationProfile
        );
        teiWriter.execute();
    }

    @Test
    public void execute() throws Exception {
        Assert.assertEquals("these files must be equals",
                Files.readLines(new File("src/test/resources/corpus/disamb-tei/test2-disamb.xml"),Charset.defaultCharset()),
                Files.readLines(new File(TermithIndex.outputPath + "/test2.xml"), Charset.defaultCharset())
                );
    }

}