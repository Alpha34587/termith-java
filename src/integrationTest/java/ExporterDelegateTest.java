import org.assertj.core.util.Strings;
import org.atilf.delegate.enrichment.exporter.ExporterDelegate;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExporterDelegateTest extends IntegrationTasks {

    private ExporterDelegate _e = new ExporterDelegate();
    private final List<MorphologyOffsetId> _morphologyOffsetIds = new ArrayList<>();
    private final List<MultiWordsOffsetId> _multiWordsOffsetIds = new ArrayList<>();
    private final List<MultiWordsOffsetId> _resourceProjectorOffsetIds = new ArrayList<>();
    private final List<MultiWordsOffsetId> _transdisciplinaryOffsetIds = new ArrayList<>();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,1,"\"","PUN:cit",1));
        _morphologyOffsetIds.add(new MorphologyOffsetId(1,5,"chez","PUN:cit",Arrays.asList(2,3)));
        _morphologyOffsetIds.add(new MorphologyOffsetId(6,10,"nous","PRO:PER",Arrays.asList(4,5)));
        _morphologyOffsetIds.add(new MorphologyOffsetId(11,17,"vivre","PUN:cit",6));
        _morphologyOffsetIds.add(new MorphologyOffsetId(18,20,"un","DET:ART",Arrays.asList(7,8)));
        _morphologyOffsetIds.add(new MorphologyOffsetId(21,26,"poète","NOM",9));
        _morphologyOffsetIds.add(new MorphologyOffsetId(26,27,",","PUN",10));
        _morphologyOffsetIds.add(new MorphologyOffsetId(28,30,"il","PRO:PER",11));
        _morphologyOffsetIds.add(new MorphologyOffsetId(31,36,"avoir","VER:impf",Arrays.asList(12,13)));

        _multiWordsOffsetIds.add(
                new MultiWordsOffsetId(1,17,1457,"chez nous vivre",Arrays.asList(2,3,4,5,6))
        );
        _multiWordsOffsetIds.add(
                new MultiWordsOffsetId(18,26,1400,"un poète",Arrays.asList(7,8,9))
        );

        _resourceProjectorOffsetIds.add(
                new MultiWordsOffsetId(1,17,1457,"chez nous vivre",Arrays.asList(2,3,4,5,6))
        );

        _transdisciplinaryOffsetIds.add(
                new MultiWordsOffsetId(1,17,1457,"chez nous vivre",Arrays.asList(2,3,4,5,6))
        );
        _transdisciplinaryOffsetIds.add(
                new MultiWordsOffsetId(18,26,1400,"un poète",Arrays.asList(7,8,9))
        );
        StringBuilder tokenizeBody = new StringBuilder(
                "<text><w xml:id=\"t1\">\"</w><w xml:id=\"t2\">Chez</w> <w xml:id=\"t3\">nous</w> " +
                        "<w xml:id=\"t4\">vivait</w> <w xml:id=\"t5\">un</w> <w xml:id=\"t6\">poète</w>" +
                        "<w xml:id=\"t7\">,</w> <w xml:id=\"t8\">il</w> <w xml:id=\"t9\">avait</w> " +
                        "<w xml:id=\"t10\">d'</w><w xml:id=\"t11\">autres</w> <w xml:id=\"t12\">yeux</w>" +
                        "<w xml:id=\"t13\">.</w><w xml:id=\"t14\">\"</w> <w xml:id=\"t15\">Petr</w> " +
                        "<w xml:id=\"t16\">Bezruč</w> <w xml:id=\"t17\">(</w><w xml:id=\"t18\">1867-1958</w>" +
                        "<w xml:id=\"t19\">)</w><w xml:id=\"t20\">,</w> <w xml:id=\"t21\">Chants</w> " +
                        "<w xml:id=\"t22\">de</w> <w xml:id=\"t23\">Silésie</w><w xml:id=\"t24\">,</w> " +
                        "<w xml:id=\"t25\">cit</w><w xml:id=\"t26\">.</w> <w xml:id=\"t27\">in</w> " +
                        "<w xml:id=\"t28\">Marabout</w> <w xml:id=\"t29\">Université</w> " +
                        "<w xml:id=\"t30\">240</w><w xml:id=\"t31\">.</w></text>"
        );
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        for (int i = 0; i < 30; i++) {
            String test = "test"+i;
            Path xmlPath = Paths.get(_temporaryFolder.getRoot() + "/" + test +".xml");

            Files.copy(
                    Paths.get("src/integrationTest/resources/treetaggerWorker/text1.xml"),
                    xmlPath
            );

            _termithIndex.getXmlCorpus().put(test, xmlPath);
            _termithIndex.getTokenizeTeiBody().put(test,
                    FilesUtils.writeObject(tokenizeBody,_temporaryFolder.getRoot().toPath())
            );
            _termithIndex.getMorphologyStandOff().put(
                    test,
                    FilesUtils.writeObject(_morphologyOffsetIds,_temporaryFolder.getRoot().toPath())
            );

            _termithIndex.getTerminologyStandOff().put(
                    test,
                    new ArrayList<>(_multiWordsOffsetIds)
            );
            _termithIndex.getPhraseoOffsetId().put(
                    test,
                    new ArrayList<>(_resourceProjectorOffsetIds)
            );
            _termithIndex.getTransdisciplinaryOffsetId().put(
                    test,
                    new ArrayList<>(_transdisciplinaryOffsetIds)
            );
        }
        _e.setOutputPath(_temporaryFolder.getRoot().toPath());
    }
    @Test
    public void executeTasks() throws Exception {
        executeTasksTest(_e);
        Assert.assertEquals("these files must be equals",
                String.join("\n",Files.readAllLines(
                        Paths.get("/home/smeoni/IdeaProjects/termITH/src/integrationTest/resources/exporterTei/test1.xml")
                )),
                String.join("\n",Files.readAllLines(
                        Paths.get(_temporaryFolder.getRoot().toString() + "/test15.xml")
                ))
                );
    }

}
