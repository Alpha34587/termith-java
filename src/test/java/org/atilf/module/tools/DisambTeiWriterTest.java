package org.atilf.module.tools;

import org.atilf.module.disambiguisation.EvaluationProfile;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambTeiWriterTest {

    DisambTeiWriter teiWriter;
    Map<String,EvaluationProfile> evaluationProfile;
    @Before
    public void setUp() throws Exception {
        teiWriter = new DisambTeiWriter(
                Paths.get("src/test/corpus/tei/test2.xml"),
                evaluationProfile
        );
    }

    @Test
    public void execute() throws Exception {

    }

}