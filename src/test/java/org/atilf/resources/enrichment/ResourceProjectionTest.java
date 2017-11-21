package org.atilf.resources.enrichment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.atilf.runner.TermithResourceManager.TermithResource;
import static org.atilf.runner.TermithResourceManager.addToClasspath;

public class ResourceProjectionTest {
    private ResourceProjection rp;

    @Before
    public void setUp() throws Exception {
        TermithResource.setLang("fr");
        addToClasspath("src/main/resources/termith-resources");
        rp = new ResourceProjection(
                TermithResource.PHRASEOLOGY.getPath()
        );
    }

    @Test
    public void parseResource() throws Exception {
        Assert.assertEquals(
                "the phraseo resource must have this size : ",
                53347,
                rp.getResourceMap().values().stream().mapToInt(List::size).sum()
        );
    }

}