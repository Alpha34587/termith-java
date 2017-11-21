package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.resources.enrichment.ResourceProjection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.atilf.runner.TermithResourceManager.TermithResource;
import static org.atilf.runner.TermithResourceManager.addToClasspath;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjectorTest {
    private static ResourceProjection _resourceProjection;
    private static List<MorphologyOffsetId> _morphologyOffsetIds = new ArrayList<>();
    private static List<MultiWordsOffsetId> _observedMultiWordsOffsetIds = new ArrayList<>();


    @BeforeClass
    public static void setUp() throws Exception {
        TermithResource.setLang("fr");
        addToClasspath("src/main/resources/termith-resources");

        _resourceProjection = new ResourceProjection(TermithResource.PHRASEOLOGY.getPath());
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"le","",1));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"chat","",2));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"mange","",3));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"une","",4));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"pizza","",5));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"à","",6));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"défaut","",7));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"de","",8));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"tomates","",9));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"à","",10));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"tout","",11));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"le","",12));
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"moins","",13));
    }

    @Test
    public void execute() throws Exception {
        new PhraseologyProjector("", _morphologyOffsetIds, _observedMultiWordsOffsetIds, _resourceProjection).execute();
        String expectedId = "[2, 789]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[6, 7, 8, 10, 11, 12, 13]";
        List<Integer> observedMorphoId = new ArrayList<>();
        _observedMultiWordsOffsetIds.forEach(
                el -> {
                    observedId.add(el.getTermId());
                    observedMorphoId.addAll(el.getIds());
                }

        );

        Assert.assertEquals("this two list of ids must be equals", observedId.toString(), expectedId);
        Assert.assertEquals("this two list of morphologies ids  must be  equals",
                observedMorphoId.toString(),
                expectedMorphoId);

    }
}
