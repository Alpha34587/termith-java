import org.atilf.delegate.enrichment.analyzer.TerminologyStandOffDelegate;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.module.tools.FilesUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TerminologyStandOffDelegateTest extends IntegrationTasks {

    private List<MorphologyOffsetId> _morphology = new ArrayList<>();
    private List<MultiWordsOffsetId> _terminology = new ArrayList<>();
    private TerminologyStandOffDelegate _t = new TerminologyStandOffDelegate();

    @Rule
    public TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        _morphology.add(new MorphologyOffsetId(0,10,"cuillière","N",0));
        _morphology.add(new MorphologyOffsetId(11,13,"en","DET",1));
        _morphology.add(new MorphologyOffsetId(14,18,"bois","N",2));
        _morphology.add(new MorphologyOffsetId(15,19,"rien","N",3));
        _morphology.add(new MorphologyOffsetId(20,26,"pierre","N",4));

        _terminology.add(new MultiWordsOffsetId(0,10,0,"cuillière"));
        _terminology.add(new MultiWordsOffsetId(0,18,1,"cuillière en bois"));
        _terminology.add(new MultiWordsOffsetId(20,26,2,"pierre"));

        for (int i = 0; i < 1000; i++){
            _termithIndex.getTerminologyStandOff().put(String.valueOf(i),
                    _terminology.stream().map(MultiWordsOffsetId::new).collect(Collectors.toList()));
            _termithIndex.getMorphologyStandOff().put(
                    String.valueOf(i),
                    FilesUtils.writeObject(_morphology, Paths.get(_temporaryFolder.getRoot().toString()))
            );
        }
    }
    @Test
    public void simpleExecute() throws Exception {
        executeTasksTest(_t);

        List<String> expected = new ArrayList<>();
        expected.add("[0]");
        expected.add("[0, 1, 2]");
        expected.add("[4]");

        Assert.assertFalse("the list of index must be not empty",
                _termithIndex.getTerminologyStandOff().isEmpty());

        _termithIndex.getTerminologyStandOff().forEach(
                (k,v) -> v.forEach(
                        el -> Assert.assertEquals("morphology ids must be equals",
                                expected.get(v.indexOf(el)),
                                el.getIds().toString()
                        )
                )
        );

    }
}
