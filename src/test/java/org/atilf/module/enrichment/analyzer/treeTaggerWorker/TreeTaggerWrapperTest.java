package org.atilf.module.enrichment.analyzer.treeTaggerWorker;

import org.atilf.models.enrichment.TreeTaggerParameter;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Simon Meoni on 20/07/17.
 */
public class TreeTaggerWrapperTest {
    private static TreeTaggerWrapper _treeTaggerWrapper;
    private final static String TREETAGGERHOME =  "/opt/treetagger";
    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();
    @BeforeClass
    public static void setUp() throws Exception {
        String getenv = System.getenv("TREETAGGER_HOME");
        TreeTaggerParameter treeTaggerParameter = new TreeTaggerParameter(false, "fr", TREETAGGERHOME);
        _treeTaggerWrapper = new TreeTaggerWrapper(
                new StringBuilder("le chat"),
                treeTaggerParameter,
                _temporaryFolder.getRoot().getPath()
        );
    }

    @Ignore
    @Test
    public void execute() throws Exception {
        _treeTaggerWrapper.execute();
        Assert.assertEquals("this two String must be equals","le\tDET:ART\tle\n" +
                "chat\tNOM\tchat\n",_treeTaggerWrapper.getTtOut().toString());
    }

}
