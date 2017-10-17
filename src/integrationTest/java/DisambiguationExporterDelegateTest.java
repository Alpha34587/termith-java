import org.atilf.delegate.disambiguation.disambiguationExporter.DisambiguationExporterDelegate;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.resources.disambiguation.AnnotationResources;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

public class DisambiguationExporterDelegateTest extends IntegrationTasks {
    private DisambiguationExporterDelegate _d = new DisambiguationExporterDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        _termithIndex.getEvaluationLexicon().put("eval1", new HashMap<>());
        _termithIndex.getEvaluationLexicon().put("eval2", new HashMap<>());
        _termithIndex.getEvaluationLexicon().put("eval3", new HashMap<>());

        _termithIndex.getEvaluationLexicon().get("eval1").put("entry-13471_DM1", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval1").put("entry-7263_DM3", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval1").put("entry-990_noDM", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval2").put("entry-13471_DM1", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval2").put("entry-7263_DM3", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval2").put("entry-990_noDM", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval3").put("entry-13471_DM1", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval3").put("entry-7263_DM3", new EvaluationProfile());
        _termithIndex.getEvaluationLexicon().get("eval3").put("entry-990_noDM", new EvaluationProfile());

        _termithIndex.getEvaluationLexicon().get("eval1").get("entry-13471_DM1").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval1").get("entry-7263_DM3").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval1").get("entry-990_noDM").setAutomaticAnnotation(AnnotationResources.DA_OFF);
        _termithIndex.getEvaluationLexicon().get("eval2").get("entry-13471_DM1").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval2").get("entry-7263_DM3").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval2").get("entry-990_noDM").setAutomaticAnnotation(AnnotationResources.DA_OFF);
        _termithIndex.getEvaluationLexicon().get("eval3").get("entry-13471_DM1").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval3").get("entry-7263_DM3").setAutomaticAnnotation(AnnotationResources.DA_ON);
        _termithIndex.getEvaluationLexicon().get("eval3").get("entry-990_noDM").setAutomaticAnnotation(AnnotationResources.DA_OFF);

        _d.setEvaluationPath(Paths.get("src/integrationTest/resources/evaluation"));
        _d.setOutputPath(_temporaryFolder.getRoot().toPath());

    }

    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_d);
        XMLUnit.setIgnoreWhitespace(true);
        String eval1 = String.join("\n", Files.readAllLines(
                Paths.get(_temporaryFolder.getRoot() + "/eval1.xml")
        ));

        String eval2 = String.join("\n", Files.readAllLines(
                Paths.get(_temporaryFolder.getRoot() + "/eval2.xml")
        ));

        String eval3 = String.join("\n", Files.readAllLines(
                Paths.get(_temporaryFolder.getRoot() + "/eval3.xml")
        ));

        String ExpectedEval = String.join("\n", Files.readAllLines(
                Paths.get("src/integrationTest/resources/output/testEval.xml")
        ));

        assertXMLEqual("these files must be equals", eval1, ExpectedEval);
        assertXMLEqual("these files must be equals", eval2, ExpectedEval);
        assertXMLEqual("these files must be equals", eval3, ExpectedEval);
    }
}
