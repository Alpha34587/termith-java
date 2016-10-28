package org.atilf.module.disambiguisation;

import org.atilf.models.disambiguisation.GlobalLexic;
import org.atilf.models.disambiguisation.RLexic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffInjectorTest {

    private LexicExtractor corpus1;
    private LexicExtractor corpus2;
    private GlobalLexic globalLexic;
    private RLexic rLexic;
    private Map<String, LexicalProfile> subLexic;
    private Map<String, LexicalProfile> executeSubLexic;
    private SubLexicExtractor subCorpus1;
    private SubLexicExtractor subCorpus2;
    private Map<String,float[]> specificities;
    private SubLexicExtractor executeSubCorpus2;
    private SubLexicExtractor executeSubCorpus1;

    @Before
    public void setUp() throws Exception {
        globalLexic = new GlobalLexic(new HashMap<>(),new HashMap<>());
        subLexic = new HashMap<>();
        executeSubLexic = new HashMap<>();
        corpus1 = new LexicExtractor("src/test/resources/corpus/tei/test1.xml", globalLexic);
        corpus2 = new LexicExtractor("src/test/resources/corpus/tei/test2.xml", globalLexic);
        subCorpus1 = new SubLexicExtractor("src/test/resources/corpus/tei/test1.xml", subLexic);
        subCorpus2 = new SubLexicExtractor("src/test/resources/corpus/tei/test2.xml", subLexic);
        executeSubCorpus1 = new SubLexicExtractor("src/test/resources/corpus/tei/test1.xml", executeSubLexic);
        executeSubCorpus2 = new SubLexicExtractor("src/test/resources/corpus/tei/test2.xml", executeSubLexic);

        corpus1.execute();
        corpus2.execute();

        rLexic = new RLexic(globalLexic);

        subCorpus1.execute();
        subCorpus2.execute();
        executeSubCorpus1.execute();
        executeSubCorpus2.execute();

        specificities = new HashMap<>();
        specificities.put("entry-8318_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});
        specificities.put("entry-990_lexOff",new float[]{1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f});
        specificities.put("entry-7263_lexOn",new float[]{3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f,
                3.9302f, 3.9302f, 3.9302f});
        specificities.put("entry-13471_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});

        executeSubLexic.forEach((key,value) -> new SpecCoefficientInjector(value, rLexic, globalLexic).execute());
    }

    @Test
    public void execute() throws Exception {
        executeSubLexic.values().forEach(
                values -> {
                    values.get_specCoefficientMap().values().forEach(
                            coef -> Assert.assertNotEquals("this coefficient must be not equals to 0", 0, coef)
                    );
                }
        );
    }

    @Test
    public void computeSpecCoeff() throws Exception {
        subLexic.forEach(
                (key,value) -> {
                    Assert.assertArrayEquals(
                            specificities.get(key),
                            new SpecCoefficientInjector(value, rLexic, globalLexic).computeSpecCoefficient(),
                            0);}
        );
    }
}