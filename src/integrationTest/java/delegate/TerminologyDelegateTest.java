package delegate;

import org.atilf.delegate.enrichment.analyzer.TerminologyParserDelegate;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TerminologyDelegateTest extends IntegrationTasks {

    private TerminologyParserDelegate _t = new TerminologyParserDelegate();
    private List<String> _expectedFile = new ArrayList<>();
    HashMap<String, List<String>> _expected = new HashMap<>();

    @Before
    public void setUp(){
       _termithIndex.getTerminologies()
               .add(Paths.get("src/integrationTest/resources/terminologyParser/terminology.json"));
       _termithIndex.getTerminologies()
               .add(Paths.get("src/integrationTest/resources/terminologyParser/terminology.json"));
        _expectedFile.add("file1");
        _expectedFile.add("file2");
        List<String> file1 = new ArrayList<>();
        List<String> file2 = new ArrayList<>();

        file1.add("0 14 cuillière en bois 1");
        file1.add("9 15 pomme de terre 2");
        file2.add("10 14 cuillière de bois 1");

        _expected.put("file1",file1);
        _expected.put("file2",file2);
    }

    @Test
    public void execute() throws Exception {
        executeTasksTest(_t);
        _expectedFile.forEach(
                key -> _termithIndex.getTerminologyStandOff().get(key).forEach(
                        offset -> Assert.assertEquals("this object must be equals",
                                _expected.get(key)
                                        .get(_termithIndex.getTerminologyStandOff()
                                                .get(key).indexOf(offset)),parse(offset))
                )
        );

    }

    private String parse(MultiWordsOffsetId offset) {
        return offset.getBegin() + " " + offset.getEnd() + " " + offset.getWord() + " " + offset.getTermId();
    }
}
