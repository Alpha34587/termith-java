import org.atilf.delegate.enrichment.lexical.resource.projection.PhraseologyProjectorDelegate;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.atilf.runner.TermithResourceManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.List;

public class PhraseologyProjectorDelegateTest extends IntegrationTasks {

    private PhraseologyProjectorDelegate _p = new PhraseologyProjectorDelegate();
    private static List<MorphologyOffsetId> morphologyOffsetIds = new ArrayList<>();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        TermithResourceManager.addToClasspath("src/main/resources/termith-resources");
        TermithResourceManager.TermithResource.setLang("fr");

        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"le","",1));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"chat","",2));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"mange","",3));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"une","",4));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"pizza","",5));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"à","",6));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"défaut","",7));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"de","",8));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"tomates","",9));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"à","",10));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"tout","",11));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"le","",12));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"moins","",13));
        for(int i = 0; i < 20; i++){
            _termithIndex.getMorphologyStandOff().put(
                    "test"+i,
                    FilesUtils.writeObject(morphologyOffsetIds, _temporaryFolder.getRoot().toPath())
            );
        }
    }

    @Test
    public void execute() throws Exception {
        executeTasksTest(_p);

        String expectedId = "[15303, 31180]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[6, 7, 8, 10, 11, 12, 13]";
        List<Integer> observedMorphoId = new ArrayList<>();
        _termithIndex.getPhraseoOffsetId().get("test10").forEach(
                el -> {
                    observedId.add(el.getTermId());
                    observedMorphoId.addAll(el.getIds());
                }

        );

        Assert.assertEquals("this two list of ids must be equals", observedId.toString(), expectedId);
        Assert.assertEquals("this two list of morphologies ids  must be  equals",
                observedMorphoId.toString(),
                expectedMorphoId
        );

    }
}
