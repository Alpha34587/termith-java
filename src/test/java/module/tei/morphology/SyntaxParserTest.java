package module.tei.morphology;

import module.termsuite.TermsuiteJsonReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Simon Meoni
 *         Created on 26/08/16.
 */
public class SyntaxParserTest {
    SyntaxParser syntaxBody;
    SyntaxParser syntaxBody2;
    SyntaxParser syntaxBody3;
    SyntaxParser basicTokenInjector;
    SyntaxParser insideTokenInjector;
    SyntaxParser commentTokenInjector;
    String expectedStringBuffer;
    String expectedStringBuffer2;
    @Before
    public void setUp(){

        //bodySplitter test
        expectedStringBuffer =
                "<text>\n" +
                        "    <body>\n" +
                        "      <div>\n" +
                        "        <p>\n" +
                        "Le chien mange des chips\n" +
                        "  </text>";

        expectedStringBuffer2 =
                "<text xml:id=\"pas fraiche du tout\">\n" +
                        "    <body>\n" +
                        "      <div>\n" +
                        "        <p>\n" +
                        "Le chien mange des chips pas fraiche\n" +
                        "  </text>";

        StringBuffer stringBuffer = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!--Version 1.2 générée le 5-4-2016-->\n" +
                        "<TEI xmlns:tei=\"http://www.tei-c.org/ns/1.0\" " +
                        "xmlns=\"http://www.tei-c.org/ns/1.0\" xml:lang=\"en\">\n" +
                        "  <teiHeader>\n" +
                        "\t\t<fileDesc>\n" +
                        "\t\t\t<titleStmt>\n" +
                        "\t\t\t\t<title/>\n" +
                        "\t\t\t</titleStmt>\n" +
                        "\t\t\t<publicationStmt>\n" +
                        "\t\t\t\t<publisher/>\n" +
                        "\t\t\t</publicationStmt>\n" +
                        "\t\t\t<sourceDesc>\n" +
                        "\t\t\t\t<p/>\n" +
                        "\t\t\t</sourceDesc>\n" +
                        "\t\t</fileDesc>\n" +
                        "\t</teiHeader>\n" +
                        "  <text>\n" +
                        "    <body>\n" +
                        "      <div>\n" +
                        "        <p>\n" +
                        "Le chien mange des chips\n" +
                        "  </text>");

        StringBuffer stringBuffer2 = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!--Version 1.2 générée le 5-4-2016-->\n" +
                        "<TEI xmlns:tei=\"http://www.tei-c.org/ns/1.0\" " +
                        "xmlns=\"http://www.tei-c.org/ns/1.0\" xml:lang=\"en\">\n" +
                        "  <teiHeader>\n" +
                        "\t\t<fileDesc>\n" +
                        "\t\t\t<titleStmt>\n" +
                        "\t\t\t\t<title/>\n" +
                        "\t\t\t</titleStmt>\n" +
                        "\t\t\t<publicationStmt>\n" +
                        "\t\t\t\t<publisher/>\n" +
                        "\t\t\t</publicationStmt>\n" +
                        "\t\t\t<sourceDesc>\n" +
                        "\t\t\t\t<p/>\n" +
                        "\t\t\t</sourceDesc>\n" +
                        "\t\t</fileDesc>\n" +
                        "\t</teiHeader>\n" +
                        "  <text xml:id=\"pas fraiche du tout\">\n" +
                        "    <body>\n" +
                        "      <div>\n" +
                        "        <p>\n" +
                        "Le chien mange des chips pas fraiche\n" +
                        "  </text>");

        StringBuffer stringBuffer3 = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!--Version 1.2 générée le 5-4-2016-->\n" +
                        "<TEI xmlns:tei=\"http://www.tei-c.org/ns/1.0\" " +
                        "xmlns=\"http://www.tei-c.org/ns/1.0\" xml:lang=\"en\">\n" +
                        "  <teiHeader>\n" +
                        "\t\t<fileDesc>\n" +
                        "\t\t\t<titleStmt>\n" +
                        "\t\t\t\t<title/>\n" +
                        "\t\t\t</titleStmt>\n" +
                        "\t\t\t<publicationStmt>\n" +
                        "\t\t\t\t<publisher/>\n" +
                        "\t\t\t</publicationStmt>\n" +
                        "\t\t\t<sourceDesc>\n" +
                        "\t\t\t\t<p/>\n" +
                        "\t\t\t</sourceDesc>\n" +
                        "\t\t</fileDesc>\n" +
                        "\t</teiHeader><\n" +
                        "  <text xml:id=\"pas fraiche du tout\">\n" +
                        "    <body>\n" +
                        "      <div>\n" +
                        "        <p>\n" +
                        "Le chien mange des chips pas fraiche\n" +
                        "<!--lalalalal--></text>");
        syntaxBody = new SyntaxParser(stringBuffer);
        syntaxBody2 = new SyntaxParser(stringBuffer2);
        syntaxBody3 = new SyntaxParser(stringBuffer3);
        syntaxBody.teiBodyspliter();
        syntaxBody2.teiBodyspliter();
        syntaxBody3.teiBodyspliter();

        //basicTokenInjector
        TermsuiteJsonReader basicTermsuiteJsonReader = new TermsuiteJsonReader();
        basicTermsuiteJsonReader.createToken("N", "le", 0, 2);
        basicTermsuiteJsonReader.createToken("N", "chien", 3, 8);
        basicTermsuiteJsonReader.createToken("N", "mange", 9, 14);
        basicTermsuiteJsonReader.createToken("N", "des", 15, 18);
        basicTermsuiteJsonReader.createToken("N", "pommes", 19, 25);
        basicTokenInjector = new SyntaxParser(
                new StringBuffer("le chien mange des pommes"),
                new StringBuffer("<text>le chien mange des pommes</text>"),
                basicTermsuiteJsonReader
        );
        basicTokenInjector.teiWordTokenizer();

        //insideTokenInjector
        TermsuiteJsonReader insideTermsuiteJsonReader = new TermsuiteJsonReader();
        insideTermsuiteJsonReader.createToken("N","le",0,2);
        insideTermsuiteJsonReader.createToken("N","chien",3,8);
        insideTermsuiteJsonReader.createToken("N","mange",9,14);
        insideTermsuiteJsonReader.createToken("N","des",15,18);
        insideTermsuiteJsonReader.createToken("N","pommes",19,25);
        insideTokenInjector = new SyntaxParser(
                new StringBuffer("le chien mange des pommes"),
                new StringBuffer("<text>le <hi>chi</hi>en mange de<s>s</s> <hi>pommes</hi></text>"),
                insideTermsuiteJsonReader
        );
        insideTokenInjector.teiWordTokenizer();

        //commentTokenInjector
        TermsuiteJsonReader commentTermsuiteJsonReader = new TermsuiteJsonReader();
        commentTermsuiteJsonReader.createToken("N", "le", 0, 2);
        commentTermsuiteJsonReader.createToken("N", "chien", 3, 8);
        commentTermsuiteJsonReader.createToken("N", "mange", 9, 14);
        commentTermsuiteJsonReader.createToken("N", "des", 15, 18);
        commentTermsuiteJsonReader.createToken("N", "pommes", 19, 25);
        commentTokenInjector = new SyntaxParser(
                new StringBuffer("le chien mange des pommes"),
                new StringBuffer("<text>le<!--testtest--> <hi>chi</hi>en" +
                        " <!--test-->mange de<s>s</s><!--lalalal--><!--test--> " +
                        "<hi>pommes</hi><!--lalala--></text>"),
                commentTermsuiteJsonReader
        );
        commentTokenInjector.teiWordTokenizer();
    }
    @Test
    public void teiBodyspliterTest() throws Exception {
        Assert.assertEquals("this stringBuffer must be equals to :",
                syntaxBody.getXml().toString(), expectedStringBuffer);
        Assert.assertEquals("this stringBuffer must be equals to :",
                syntaxBody2.getXml().toString(), expectedStringBuffer2);
    }

    @Test
    public void executeTest() throws Exception {

    }

    @Test
    public void basictokenInjectorTest() {

        Assert.assertEquals("tokenizeInjector basic test fail :",
                "<text>" +
                        "<w xml:id=\"t1\">le</w> " +
                        "<w xml:id=\"t2\">chien</w> " +
                        "<w xml:id=\"t3\">mange</w> " +
                        "<w xml:id=\"t4\">des</w> " +
                        "<w xml:id=\"t5\">pommes</w>" +
                        "</text>",
                basicTokenInjector.getTokenizeBuffer().toString()
        );
    }

    @Test
    public void insideTokenInjectorTest() {

        Assert.assertEquals("tokenizeInjector inside test fail :",
                "<text>" +
                        "<w xml:id=\"t1\">le</w> " +
                        "<hi><w xml:id=\"t2\">chi</w></hi>" +
                        "<w xml:id=\"t3\">en</w> " +
                        "<w xml:id=\"t4\">mange</w> " +
                        "<w xml:id=\"t5\">de</w>" +
                        "<s><w xml:id=\"t6\">s</w></s> " +
                        "<hi><w xml:id=\"t7\">pommes</w></hi>" +
                        "</text>",
                insideTokenInjector.getTokenizeBuffer().toString()
        );
    }

    @Test
    public void commentTokenInjector() {
        Assert.assertEquals("tokenizeInjector comment test fail :",
                "<text>" +
                        "<w xml:id=\"t1\">le</w><!--testtest--> " +
                        "<hi><w xml:id=\"t2\">chi</w></hi>" +
                        "<w xml:id=\"t3\">en</w> " +
                        "<!--test--><w xml:id=\"t4\">mange</w> " +
                        "<w xml:id=\"t5\">de</w>" +
                        "<s><w xml:id=\"t6\">s</w></s><!--lalalal--><!--test--> " +
                        "<hi><w xml:id=\"t7\">pommes</w></hi><!--lalala-->" +
                        "</text>",
                commentTokenInjector.getTokenizeBuffer().toString()
        );
    }

    @Test
    public void checkIfSymbolTest() {

    }

    @Test
    public void checkTextAlignmentTest(){

    }
}