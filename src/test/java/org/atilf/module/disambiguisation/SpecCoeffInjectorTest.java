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

    private GlobalLexic _globalLexic = new GlobalLexic(new HashMap<>(),new HashMap<>());
    private RLexic _rLexic;
    private Map<String, LexicalProfile> _subLexic = new HashMap<>();
    private Map<String, LexicalProfile> _executeSubLexic = new HashMap<>();
    private Map<String,float[]> _specificities;

    @Before
    public void setUp() throws Exception {

        LexicExtractor corpus1 = new LexicExtractor("src/test/resources/corpus/tei/test1.xml", _globalLexic);
        LexicExtractor corpus2 = new LexicExtractor("src/test/resources/corpus/tei/test2.xml", _globalLexic);
        ContextExtractor subCorpus1 = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _subLexic);
        ContextExtractor subCorpus2 = new ContextExtractor("src/test/resources/corpus/tei/test2.xml", _subLexic);
        ContextExtractor executeSubCorpus1 = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _executeSubLexic);
        ContextExtractor executeSubCorpus2 = new ContextExtractor("src/test/resources/corpus/tei/test2.xml", _executeSubLexic);

        corpus1.execute();
        corpus2.execute();

        _rLexic = new RLexic(_globalLexic);

        subCorpus1.execute();
        subCorpus2.execute();
        executeSubCorpus1.execute();
        executeSubCorpus2.execute();

        _specificities = new HashMap<>();
        _specificities.put("entry-8318_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});
        _specificities.put("entry-990_lexOff",new float[]{1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f});
        _specificities.put("entry-7263_lexOn",new float[]{3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f,
                3.9302f, 3.9302f, 3.9302f});
        _specificities.put("entry-13471_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});

        _executeSubLexic.forEach((key, value) -> new SpecCoefficientInjector(value, _rLexic, _globalLexic).execute());
    }

    @Test
    public void execute() throws Exception {
        _executeSubLexic.values().forEach(
                values -> values.getSpecCoefficientMap().values().forEach(
                        coef -> Assert.assertNotEquals("this coefficient must be not equals to 0", 0, coef)
                )
        );
    }

    @Test
    public void computeSpecCoeff() throws Exception {
        _subLexic.forEach(
                (key,value) -> Assert.assertArrayEquals(
                        _specificities.get(key),
                        new SpecCoefficientInjector(value, _rLexic, _globalLexic).computeSpecCoefficient(),
                        0
                )
        );
    }
}