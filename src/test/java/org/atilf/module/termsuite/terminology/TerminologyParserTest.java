package org.atilf.module.termsuite.terminology;

import org.atilf.models.TermsOffsetId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 14/09/16.
 */
public class TerminologyParserTest {

    TerminologyParser terminologyParser;
    @Before
    public void setUp(){
        terminologyParser = new TerminologyParser(Paths.get("src/test/resources/terminology-json/terminology.json"));
    }

    @Test
    public void execute() throws Exception {

        List<String> expectedFile = new ArrayList<>();
        expectedFile.add("file1");
        expectedFile.add("file2");
        List<String> file1 = new ArrayList<>();
        List<String> file2 = new ArrayList<>();

        file1.add("0 14 cuillière en bois 1");
        file1.add("9 15 pomme de terre 2");
        file2.add("10 14 cuillière de bois 1");

        HashMap<String, List<String>> expected = new HashMap<>();
        expected.put("file1",file1);
        expected.put("file2",file2);

        terminologyParser.execute();
        expectedFile.forEach(
                key -> {
                    terminologyParser.getStandOffTerminology().get(key).forEach(
                            offset -> {
                                Assert.assertEquals("this object must be equals",
                                        expected.get(key)
                                                .get(terminologyParser.getStandOffTerminology()
                                                        .get(key).indexOf(offset)),parse(offset));
                            }
                    );
                }
        );

    }

    private String parse(TermsOffsetId offset) {
        return offset.getBegin() + " " + offset.getEnd() + " " + offset.getWord() + " " + offset.getTermId();
    }

}