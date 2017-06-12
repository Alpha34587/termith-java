package org.atilf.module.enrichment.lexicalResourceProjection;

import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.PhraseoOffsetId;
import org.atilf.models.enrichment.PhraseologyResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjectorTest {


    private static PhraseologyResources phraseologyResources = new PhraseologyResources("fr");
    private static List<MorphologyOffsetId> morphologyOffsetIds = new ArrayList<>();
    private static List<PhraseoOffsetId> expectedPhraseoOffsetIds = new ArrayList<>();
    private static List<PhraseoOffsetId> observedPhraseoOffsetIds = new ArrayList<>();


    @BeforeClass
    public static void setUp(){
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"le","",1));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"chat","",2));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"mange","",3));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"une","",4));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"pizza","",5));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"en","",6));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"outre","",7));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"il","",8));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"aime","",9));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"ça","",10));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"de","",11));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"plus","",12));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"elle","",13));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"est","",14));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"grande","",15));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"par","",16));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"conséquent","",17));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"il","",18));
        morphologyOffsetIds.add(new MorphologyOffsetId(0,0,"meurt","",19));

        expectedPhraseoOffsetIds.add(new PhraseoOffsetId(0,0,3,"en outre"));
        expectedPhraseoOffsetIds.add(new PhraseoOffsetId(0,0,4,"de plus"));
        expectedPhraseoOffsetIds.add(new PhraseoOffsetId(0,0,39,"par conséquent"));

    }

    @Test
    public void execute() throws Exception {
        new PhraseologyProjector("", morphologyOffsetIds, observedPhraseoOffsetIds).execute();
        String expectedId = "[ 3, 4, 39 ]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[ 6, 7, 11, 12, 16, 17 ]";
        List<Integer> observedMorphoId = new ArrayList<>();
        observedPhraseoOffsetIds.forEach(
                el -> {
                    observedId.add(el.getTermId());
                    observedMorphoId.addAll(el.getIds());
                }

        );

        Assert.assertEquals(observedId.toString(), expectedId, "this two list of ids must be equals");
        Assert.assertEquals(observedMorphoId.toString(), expectedMorphoId, "this two list of morphologies ids  must be" +
                " equals");

    }
}
