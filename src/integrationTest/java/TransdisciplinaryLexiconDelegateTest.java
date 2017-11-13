import org.atilf.delegate.enrichment.lexical.resource.projection.PhraseologyProjectorDelegate;
import org.atilf.delegate.enrichment.lexical.resource.projection.TransdisciplinaryLexiconsProjectorDelegate;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.List;

public class TransdisciplinaryLexiconDelegateTest extends IntegrationTasks{

    private TransdisciplinaryLexiconsProjectorDelegate _p = new TransdisciplinaryLexiconsProjectorDelegate();
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
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"de","",6));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"plus","",7));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"des","",8));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"tomates","",9));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"par","",10));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"conséquent","",11));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"il","",12));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"est","",13));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"absent","",14));
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

        String expectedId = "[1, 39, 428]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[6, 7, 10, 11, 14]";
        List<Integer> observedMorphoId = new ArrayList<>();
        _termithIndex.getTransdisciplinaryOffsetId().get("test17").forEach(
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
