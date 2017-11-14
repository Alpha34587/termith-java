package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.resources.enrichment.ResourceProjection;
import org.atilf.resources.enrichment.TransdisciplinaryResourceProjection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.atilf.runner.TermithResourceManager.TermithResource;
import static org.atilf.runner.TermithResourceManager.addToClasspath;

/**
 * Created by Simon Meoni on 13/06/17.
 */
public class TransdisciplinaryLexiconsProjectorTest {

    private static ResourceProjection resourceProjection;
    private static List<MorphologyOffsetId> morphologyOffsetIds = new ArrayList<>();
    private static List<MultiWordsOffsetId> transdisciplinaryOffsetIds = new ArrayList<>();


    @BeforeClass
    public static void setUp() throws Exception {
        TermithResource.setLang("fr");
        addToClasspath("src/main/resources/termith-resources");
        resourceProjection = new TransdisciplinaryResourceProjection(TermithResource.LST.getPath());

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
    }

    @Test
    public void execute() throws Exception {
        new TransdisciplinaryLexiconsProjector("", morphologyOffsetIds, transdisciplinaryOffsetIds, resourceProjection)
                .execute();
        String expectedId = "[1, 39, 428]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[6, 7, 10, 11, 14]";
        List<Integer> observedMorphoId = new ArrayList<>();
        transdisciplinaryOffsetIds.forEach(
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
