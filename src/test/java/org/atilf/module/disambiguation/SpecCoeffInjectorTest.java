package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.RLexicon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffInjectorTest {

    private CorpusLexicon _corpusLexicon = new CorpusLexicon(new HashMap<>(),new HashMap<>());
    private RLexicon _rLexicon;
    private Map<String, LexiconProfile> _contextLexicon1 = new HashMap<>();
    private Map<String, LexiconProfile> _contextLexicon2 = new HashMap<>();
    private Map<String,float[]> _specificities = new HashMap<>();

    @Before
    public void setUp() throws Exception {

        ContextExtractor contextExtractor = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _contextLexicon1, _corpusLexicon);
        ContextExtractor contextExtractor1 = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _contextLexicon1, _corpusLexicon);
        ContextExtractor contextExtractor2 = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _contextLexicon2, _corpusLexicon);
        ContextExtractor contextExtractor3 = new ContextExtractor("src/test/resources/corpus/tei/test1.xml", _contextLexicon2, _corpusLexicon);

        _rLexicon = new RLexicon(_corpusLexicon);

        contextExtractor.execute();
        contextExtractor1.execute();
        contextExtractor2.execute();
        contextExtractor3.execute();

        _specificities.put("entry-8318_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});
        _specificities.put("entry-990_lexOff",new float[]{1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f, 1.8162f});
        _specificities.put("entry-7263_lexOn",new float[]{3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f, 3.9302f,
                3.9302f, 3.9302f, 3.9302f});
        _specificities.put("entry-13471_lexOn",new float[]{1.3425f, 1.3425f, 1.3425f, 1.3425f, 3.4531f, 1.3425f});

        _contextLexicon2.forEach((key, value) -> new SpecCoefficientInjector(value, _rLexicon, _corpusLexicon).execute());
    }

    @Test
    public void execute() throws Exception {
        _contextLexicon2.values().forEach(
                values -> values.getSpecCoefficientMap().values().forEach(
                        coef -> Assert.assertNotEquals("this coefficient must be not equals to 0", 0, coef)
                )
        );
    }

    @Test
    public void computeSpecCoeff() throws Exception {
        _contextLexicon1.forEach(
                (key,value) -> Assert.assertArrayEquals(
                        _specificities.get(key),
                        new SpecCoefficientInjector(value, _rLexicon, _corpusLexicon).computeSpecCoefficient(),
                        0
                )
        );
    }
}