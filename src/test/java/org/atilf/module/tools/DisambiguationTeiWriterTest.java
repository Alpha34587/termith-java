package org.atilf.module.tools;

import com.google.common.collect.HashMultiset;
import org.atilf.models.termith.TermithIndex;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambiguationTeiWriterTest {

    private Map<String,EvaluationProfile> _evaluationProfile = new HashMap<>();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Before
    public void setUp() throws Exception {
        new TermithIndex.Builder().export(temporaryFolder.getRoot().getPath()).build();
        _evaluationProfile.put("entry-13471_DM1", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.put("entry-7263_DM3", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.put("entry-990_noDM", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.get("entry-13471_DM1").setDisambiguationId("DaOn");
        _evaluationProfile.get("entry-7263_DM3").setDisambiguationId("DaOn");
        _evaluationProfile.get("entry-990_noDM").setDisambiguationId("DaOff");

        DisambiguationTeiWriter teiWriter = new DisambiguationTeiWriter(
                "src/test/resources/corpus/disambiguation/tei/test1.xml",
                _evaluationProfile
        );
        teiWriter.execute();
    }

    @Test
    public void execute() throws Exception {
        String join = String.join("\n", Files.readAllLines(Paths.get(TermithIndex.getOutputPath() + "/test1.xml")));
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(
                "these files must be equals",
                String.join("\n",Files.readAllLines(Paths.get("src/test/resources/corpus/disambiguation/disamb-id-tei/test1.xml"))),
                String.join("\n",Files.readAllLines(Paths.get(TermithIndex.getOutputPath() + "/test1.xml")))
                );
    }

}