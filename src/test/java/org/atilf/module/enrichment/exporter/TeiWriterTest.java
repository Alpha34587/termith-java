package org.atilf.module.enrichment.exporter;
import org.atilf.models.enrichment.MorphologyOffsetId;
import org.atilf.models.enrichment.MultiWordsOffsetId;
import org.atilf.resources.enrichment.StandOffResources;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * Created by Simon Meoni on 21/07/17.
 */
public class TeiWriterTest {
    private TeiWriter _teiWriter;
    private  String observedPath;
    private List<MultiWordsOffsetId> _transdisciplinaryOffsetIds = new ArrayList<>();
    private List<MultiWordsOffsetId> _phraseologyOffsetIds = new ArrayList<>();
    private List<MultiWordsOffsetId> _termsOffsetIds = new ArrayList<>();
    private List<MorphologyOffsetId> _morphologyOffsetIds = new ArrayList<>();

    @ClassRule
    public static TemporaryFolder _temporaryFolder = new TemporaryFolder();
    @Before
    public void setUp() throws Exception {
        observedPath = _temporaryFolder.getRoot().getAbsolutePath() + "/test.xml";
        StandOffResources.init("termith-resources/all/xml_standoff_fragments/");
        StringBuilder xmlCorpus = new StringBuilder(
                "<TEI xmlns=\"http://tei-c.org\">" +
                        "<teiHeader/>" +
                        "<text>au moins, le chat rouge dort</text>" +
                        "</TEI>"
        );
        StringBuilder tokenizeBody = new StringBuilder(
                "<text><w xml:id=\"t1\">au</w> " +
                        "<w xml:id=\"t2\">moins</w><w xml:id=\"t3\">,</w> " +
                        "<w xml:id=\"t4\">le</w> " +
                        "<w xml:id=\"t5\">chat</w> " +
                        "<w xml:id=\"t6\">dort</w></text>"
        );
        _morphologyOffsetIds.add(new MorphologyOffsetId(0,2,"au","PRP",1));
        _morphologyOffsetIds.add(new MorphologyOffsetId(3,8,"moins","PRP",2));
        _morphologyOffsetIds.add(new MorphologyOffsetId(8,9,",","PUNC",3));
        _morphologyOffsetIds.add(new MorphologyOffsetId(10,12,"le","DET",4));
        _morphologyOffsetIds.add(new MorphologyOffsetId(13,17,"chat","NOM",5));
        _morphologyOffsetIds.add(new MorphologyOffsetId(18,22,"dort","VERB",6));

        _termsOffsetIds.add(new MultiWordsOffsetId(13,17,1,"chat", Collections.singletonList(1)));

        _phraseologyOffsetIds.add(new MultiWordsOffsetId(13,22,1,"chat dort", Arrays.asList(5,6)));
        _transdisciplinaryOffsetIds.add(new MultiWordsOffsetId(0,8,1,"au moins",Arrays.asList(1,2)));

        _teiWriter = new TeiWriter(
                xmlCorpus,
                _morphologyOffsetIds,
                tokenizeBody,
                _termsOffsetIds,
                _phraseologyOffsetIds,
                _transdisciplinaryOffsetIds,
                Paths.get(observedPath)
        );
    }

    @Test
    public void insertStandoffNs() throws Exception {
        _teiWriter.insertStandoffNs();
        Assert.assertEquals(
                "these two strings must be equals",
                "<TEI xmlns:ns=\"http://standoff.proposal\" xmlns=\"http://tei-c.org\">" +
                        "<teiHeader/>" +
                        "<text>au moins, le chat rouge dort</text>" +
                        "</TEI>",
                _teiWriter._xmlCorpus.toString()
                );
    }

    @Test
    public void insertBody() throws Exception {
        _teiWriter.insertBody();
        Assert.assertEquals(
                "these two strings must be equals",
                "<text><w xml:id=\"t1\">au</w> <w xml:id=\"t2\">moins</w>" +
                        "<w xml:id=\"t3\">,</w> <w xml:id=\"t4\">le</w> <w xml:id=\"t5\">chat</w> " +
                        "<w xml:id=\"t6\">dort</w></text>\n" +
                        "</TEI>",
                getBufferWriter()
                );
    }

    @Test
    public void searchStart() throws Exception {
        Assert.assertEquals(
                "these two index must be equals",
                42,
                _teiWriter.searchStart()
        );
    }
    @Test
    public void serializeTransdisciplinary() throws Exception {
        _teiWriter.serializeTransdisciplinary(_transdisciplinaryOffsetIds);
        Assert.assertEquals(
                "these two strings must be equals",
                String.join("\n",Files.readAllLines(
                        Paths.get("src/test/resources/module/enrichment/exporter/serializeTransdisciplinary.xml")
                        )
                ),
                getBufferWriter()
        );
    }

    @Test
    public void serializePhraseology() throws Exception {
        _teiWriter.serializePhraseology(_phraseologyOffsetIds);

        Assert.assertEquals(
                "these two strings must be equals",
                String.join("\n",Files.readAllLines(
                        Paths.get("src/test/resources/module/enrichment/exporter/serializePhraseology.xml")
                        )
                ),
                getBufferWriter()
        );
    }

    @Test
    public void serializeTerminology() throws Exception {
        _teiWriter.serializeTerminology(_termsOffsetIds);
        Assert.assertEquals(
                "these two strings must be equals",
                String.join("\n",Files.readAllLines(
                        Paths.get("src/test/resources/module/enrichment/exporter/serializeTerminology.xml")
                        )
                ),
                getBufferWriter()
        );
    }

    @Test
    public void cut() throws Exception {
        Assert.assertEquals(
                "these two strings must be equals",
                _teiWriter.cut(new StringBuilder("<p>\n</p>"),true).toString(),
                "</p>"
        );
        Assert.assertEquals(
                "these two strings must be equals",
                _teiWriter.cut(new StringBuilder("<p>\n</p>"),false).toString(),
                "<p>\n"
        );
    }

    @Test
    public void serializeMorphosyntax() throws Exception {
        _teiWriter.serializeMorphosyntax(_morphologyOffsetIds);
        Assert.assertEquals(
                "these two strings must be equals",
                String.join("\n", Files.readAllLines(
                        Paths.get("src/test/resources/module/enrichment/exporter/morphologySerializer.xml"))
                ),
                getBufferWriter()
        );
    }

    @Test
    public void serializeId() throws Exception {
        Assert.assertEquals(
                "these two strings must be equals",
                "#t1 #t2 #t3",
                _teiWriter.serializeId(Arrays.asList(new Integer[]{1,2,3}))
        );

    }

    public String getBufferWriter() throws IOException {
        if(_teiWriter._bufferedWriter != null) {
            _teiWriter._bufferedWriter.close();
        }
        return String.join("\n", Files.readAllLines(Paths.get(observedPath)));
    }

}
