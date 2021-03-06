package org.atilf.module.enrichment.analyzer.treeTaggerWorker;

import org.atilf.models.enrichment.TreeTaggerParameter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Simon Meoni on 20/07/17.
 */
public class TreeTaggerWrapperTest {
    private static TreeTaggerWrapper _treeTaggerWrapper;
    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();
    @BeforeClass
    public static void setUp() throws Exception {
        String getenv = System.getenv("TREETAGGER_HOME");
        TreeTaggerParameter treeTaggerParameter = new TreeTaggerParameter(false, "fr", getenv);
        _treeTaggerWrapper = new TreeTaggerWrapper(
                new StringBuilder("le chat"),
                treeTaggerParameter,
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
