package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.AnnotationResources;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTermsTest {
    AggregateTerms _aggregateTerms;
    @BeforeClass
    public void setUp() throws Exception {
        Map<String,EvaluationProfile> terms = new HashMap<>();
        EvaluationProfile term1 = new EvaluationProfile();
        term1.setDisambiguationId(AnnotationResources.DA_OFF);
        

    }

    @Test
    public void execute() throws Exception {

    }

}