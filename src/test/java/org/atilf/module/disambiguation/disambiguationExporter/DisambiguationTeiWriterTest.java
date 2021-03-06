package org.atilf.module.disambiguation.disambiguationExporter;

import com.google.common.collect.HashMultiset;
import org.atilf.resources.disambiguation.AnnotationResources;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.ClassRule;
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

    private static Map<String,EvaluationProfile> _evaluationProfile = new HashMap<>();

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws Exception {
        _evaluationProfile.put("entry-13471_DM1", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.put("entry-7263_DM3", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.put("entry-990_noDM", new EvaluationProfile(HashMultiset.create()));
        _evaluationProfile.get("entry-13471_DM1").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _evaluationProfile.get("entry-7263_DM3").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _evaluationProfile.get("entry-990_noDM").setAutomaticAnnotation(AnnotationResources.DA_OFF);

        DisambiguationTeiWriter teiWriter = new DisambiguationTeiWriter(
                "src/test/resources/module/" +
                        "disambiguation/disambiguationExporter/disambiguationExporter/test1.xml",
                _evaluationProfile,
                temporaryFolder.getRoot().toString()
        );
        teiWriter.execute();
    }

    @Test
    public void execute() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        assertXMLEqual(
                "these files must be equals",
                String.join("\n",Files.readAllLines(
                        Paths.get("src/test/resources/module/disambiguation/" +
                                "disambiguationExporter/disambiguationExporter/test2.xml"))
                ),
                String.join("\n",Files.readAllLines(Paths.get(temporaryFolder.getRoot().toString() + "/test1.xml")))
                );
    }

}
