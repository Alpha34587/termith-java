package org.atilf.module.enrichment.lexical.resource.projection;

import org.atilf.resources.enrichment.LexicalResourceProjectionResources;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.runner.TermithResourceManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.atilf.resources.enrichment.LexicalResourceProjectionResources.PH_TYPE;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjectorTest {
    private static LexicalResourceProjectionResources lexicalResourceProjectionResources;
    private static List<MorphologyOffsetId> morphologyOffsetIds = new ArrayList<>();
    private static List<MultiWordsOffsetId> observedMultiWordsOffsetIds = new ArrayList<>();


    @BeforeClass
    public static void setUp(){
        TermithResourceManager.TermithResource.setLang("fr");
        lexicalResourceProjectionResources = new LexicalResourceProjectionResources("termith-resources/fr/lexical_resources/PhraseologyResource.json", PH_TYPE);
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
    }

    @Test
    public void execute() throws Exception {
        new PhraseologyProjector("", morphologyOffsetIds, observedMultiWordsOffsetIds, lexicalResourceProjectionResources).execute();
        String expectedId = "[15303, 31180]";
        List<Integer> observedId = new ArrayList<>();
        String expectedMorphoId = "[6, 7, 8, 10, 11, 12, 13]";
        List<Integer> observedMorphoId = new ArrayList<>();
        observedMultiWordsOffsetIds.forEach(
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
