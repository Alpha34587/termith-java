package module.treetagger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class SerializeTest {

    private StringBuilder tokenLemma;
    private StringBuffer lemma;
    private StringBuilder tokenMultex;
    private StringBuffer multex;
    private StringBuilder tokenOffset;
    private StringBuffer offset;

    Serialize serializeLemma;
    Serialize serializeTag;
    Serialize serializeOffset;


    @Before
    public void setUp(){

        tokenLemma = new StringBuilder(
                "Journal\tNP\tJournal\n" +
                "of\tIN\tof\n" +
                "Gerontology\tNP\t<unknown>\n" +
                ":\t:\t:\n" +
                "PSYCHOLOGICAL\tJJ\tpsychological\n");
        lemma = new StringBuffer("\n" +
                "    \n" +
                "      \n" +
                "        \n" +
                "Journal of Gerontology: PSYCHOLOGICAL");

        tokenOffset = new StringBuilder("" +
                "Hearing\tVVG\thear\n" +
                "Research\tNP\tResearch\n" +
                "151\tCD\t@card@\n" +
                "(\t(\t(\n" +
                "2001\tCD\t@card@\n");
        offset = new StringBuffer(
                "\n    \n      \n        \nHearing Research 151 (2001"
        );
        serializeLemma = new Serialize(tokenLemma,lemma,0);
        serializeTag = new Serialize(tokenLemma,lemma,0);
        serializeOffset = new Serialize(tokenOffset,offset,0);

    }

    @Test
    public void addLemmaTest() throws Exception {
        serializeLemma.execute();

        List<String> lemmaList = new ArrayList<>();
        lemmaList.add("Journal");
        lemmaList.add("of");
        lemmaList.add("<unknown>");
        lemmaList.add(":");
        lemmaList.add("psychological");
        int cpt = 0;

        for (JsonTag jsonTag : serializeLemma.getTtJsonFile().jsonTagList) {
            Assert.assertEquals("lemma must be equals : ",lemmaList.get(cpt), jsonTag.getLemma());
            cpt++;
        }
    }

    @Test
    public void addTagCatTest() throws Exception {
        serializeTag.execute();

        List<String> lemmaList = new ArrayList<>();
        lemmaList.add("NP");
        lemmaList.add("IN");
        lemmaList.add("NP");
        lemmaList.add(":");
        lemmaList.add("JJ");
        int cpt = 0;

        for (JsonTag jsonTag : serializeTag.getTtJsonFile().jsonTagList) {
            Assert.assertEquals("tag must be equals : ",lemmaList.get(cpt), jsonTag.getTag());
            cpt++;
        }
    }

    @Test
    public void findOffsetTest() throws Exception {
        serializeOffset.execute();
        List<Integer[]> lemmaList = new ArrayList<>();
        lemmaList.add(new Integer[]{22,29});
        lemmaList.add(new Integer[]{30,38});
        lemmaList.add(new Integer[]{39,42});
        lemmaList.add(new Integer[]{43,44});
        lemmaList.add(new Integer[]{44,48});
        int cpt = 0;

        for (JsonTag jsonTag : serializeOffset.getTtJsonFile().jsonTagList) {
            Assert.assertEquals("begin offset must be equals",lemmaList.get(cpt)[0].intValue(), jsonTag.getBegin());
            Assert.assertEquals("end offset must be equals",lemmaList.get(cpt)[1].intValue(), jsonTag.getEnd());
            cpt++;
        }
    }

}