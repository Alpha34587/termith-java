package org.atilf.module.enrichment.analyzer.treeTaggerWorker;

import org.atilf.models.enrichment.TreeTaggerParameter;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * Created by Simon Meoni on 20/07/17.
 */
public class TreeTaggerWrapperTest {
    private static TreeTaggerWrapper _treeTaggerWrapper;
    private static TreeTaggerParameter _treeTaggerParameter;
    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();
    @BeforeClass
    public static void setUp() throws Exception {
        _treeTaggerParameter = new TreeTaggerParameter(false,"fr","/opt/treetagger");
        _treeTaggerWrapper = new TreeTaggerWrapper(
                new StringBuilder("le chat"),
                _treeTaggerParameter,
                _temporaryFolder.getRoot().getPath()
        );
    }

    @Test
    public void execute() throws Exception {
        _treeTaggerWrapper.execute();
        Assert.assertEquals("this two String must be equals","le\tDET:ART\tle\n" +
                "chat\tNOM\tchat\n",_treeTaggerWrapper.getTtOut().toString());
    }

}
