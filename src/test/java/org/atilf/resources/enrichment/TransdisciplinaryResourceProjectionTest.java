package org.atilf.resources.enrichment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.atilf.runner.TermithResourceManager.TermithResource;
import static org.atilf.runner.TermithResourceManager.addToClasspath;

public class TransdisciplinaryResourceProjectionTest {
    private TransdisciplinaryResourceProjection trp;

    @Before
    public void setUp() throws Exception {
        TermithResource.setLang("fr");
        addToClasspath("src/main/resources/termith-resources");
        trp = new TransdisciplinaryResourceProjection(
                TermithResource.LST.getPath()
        );
    }

    @Test
    public void parseResource() throws Exception {
        Assert.assertEquals(
                "the lst resource must have this size : ",
                1926,
                trp.getResourceMap().values().stream().mapToInt(List::size).sum()
        );
    }

}