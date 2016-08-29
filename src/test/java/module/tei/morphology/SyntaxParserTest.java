package module.tei.morphology;

import module.termsuite.TermsuiteJsonReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

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
    SyntaxParser symbolTokenInjector;
    SyntaxParser symbolTokenInjector2;
    SyntaxParser symbolTokenInjector3;
    SyntaxParser alignmentTokenInjector;
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

        //insideTokenInjector
        TermsuiteJsonReader insideTermsuiteJsonReader = new TermsuiteJsonReader();
        insideTermsuiteJsonReader.createToken("N","le",0,2);
        insideTermsuiteJsonReader.createToken("N","chien",3,8);
        insideTermsuiteJsonReader.createToken("N","mange",9,14);
        insideTermsuiteJsonReader.createToken("N","des",15,18);
        insideTermsuiteJsonReader.createToken("N","pommes",19,25);
        insideTokenInjector = new SyntaxParser(
                new StringBuffer("le chien mange des pommes"),
                new StringBuffer("<text>le <hi>chi</hi><hi>en</hi> mange de<s>s</s> <hi>pommes</hi></text>"),
                insideTermsuiteJsonReader
        );

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

        //symbolTokenInjector
        TermsuiteJsonReader symbolTermsuiteJsonReader = new TermsuiteJsonReader();

        symbolTokenInjector = new SyntaxParser(
                new StringBuffer("le &amp; &amp; chi&eacute;ien ma&diams;nge des pommes&amp;"),
                new StringBuffer("le &amp; &amp; chi&eacute;ien ma&diams;nge des pommes&amp;"),
                symbolTermsuiteJsonReader
        );

        TermsuiteJsonReader symbolTermsuiteJsonReader2 = new TermsuiteJsonReader();
        symbolTermsuiteJsonReader2.createToken("N", "le", 0, 2);
        symbolTermsuiteJsonReader2.createToken("N", "&amp;", 3, 4);
        symbolTermsuiteJsonReader2.createToken("N", "&amp;", 5, 6);
        symbolTermsuiteJsonReader2.createToken("N", "chien", 7, 14);
        symbolTermsuiteJsonReader2.createToken("N", "mange", 15, 21);
        symbolTermsuiteJsonReader2.createToken("N", "des", 22, 26);
        symbolTermsuiteJsonReader2.createToken("N", "pommes", 27, 34);
        symbolTokenInjector2 = new SyntaxParser(
                new StringBuffer("le &amp; &amp; chi&eacute;ien ma&diams;nge &diams;des pommes&amp;"),
                new StringBuffer("<text>le &amp; &amp; chi&eacute;ien ma&diams;nge &diams;des pommes&amp;</text>"),
                symbolTermsuiteJsonReader2
        );

        TermsuiteJsonReader symbolTermsuiteJsonReader3 = new TermsuiteJsonReader();
        symbolTermsuiteJsonReader3.createToken("N", "le", 0, 2);
        symbolTermsuiteJsonReader3.createToken("N", "&amp;", 3, 4);
        symbolTermsuiteJsonReader3.createToken("N", "&amp;", 5, 6);
        symbolTermsuiteJsonReader3.createToken("N", "chien", 7, 14);
        symbolTermsuiteJsonReader3.createToken("N", "mange", 15, 22);
        symbolTermsuiteJsonReader3.createToken("N", "des", 23, 27);
        symbolTermsuiteJsonReader3.createToken("N", "pommes", 28, 35);
        symbolTokenInjector3 = new SyntaxParser(
                new StringBuffer("le &amp; &amp; chi&eacute;ien ma&diams;&diams;nge &diams;des pommes&amp;"),
                new StringBuffer("<text><hi>le</hi> &amp; &amp; <hi>chi</hi>&eacute;ien" +
                        " <hi>ma</hi><sub>&diams;&diams;</sub><sub>nge</sub> " +
                        "<sub>&diams;d</sub>es " +
                        "<hi>pommes&amp;</hi>" +
                        "</text>"),
                symbolTermsuiteJsonReader3
        );


        TermsuiteJsonReader alignmentTermsuiteJsonReader = new TermsuiteJsonReader();
        alignmentTermsuiteJsonReader.createToken("N", "le", 0, 2);
        alignmentTermsuiteJsonReader.createToken("N", "chien", 3 , 8);
        alignmentTermsuiteJsonReader.createToken("N", "mange", 9, 14);
        alignmentTermsuiteJsonReader.createToken("N", "un", 15, 17);
        alignmentTermsuiteJsonReader.createToken("N", "fromage", 18, 25);
        alignmentTermsuiteJsonReader.createToken("N", "assez", 26, 31);
        alignmentTermsuiteJsonReader.createToken("N", "délicieux", 33, 42);
        alignmentTermsuiteJsonReader.createToken("N", "&", 46, 47);
        alignmentTokenInjector = new SyntaxParser(
                new StringBuffer("le chien\nmange un fromage assez\n\ndélicieux\n\n\n\n&"),
                new StringBuffer("<text><head>le chien</head><p>mange " +
                        "<div>un froma<sup>ge</sup> assez" +
                        "</div></p><p>d&eacute;licieux</p>\n\n\n&amp;</text>"),
                alignmentTermsuiteJsonReader
        );
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
        basicTokenInjector.teiWordTokenizer();

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
        insideTokenInjector.teiWordTokenizer();
        Assert.assertEquals("tokenizeInjector inside test fail :",
                "<text>" +
                        "<w xml:id=\"t1\">le</w> " +
                        "<hi><w xml:id=\"t2\">chi</w></hi>" +
                        "<hi><w xml:id=\"t3\">en</w></hi> " +
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
        commentTokenInjector.teiWordTokenizer();
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
    public void checkIfSymbolTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        symbolTokenInjector.teiWordTokenizer();
        symbolTokenInjector2.teiWordTokenizer();
        symbolTokenInjector3.teiWordTokenizer();

        Assert.assertEquals("the offset must have this value :",33 ,symbolTokenInjector.getOffset());
        Assert.assertEquals("symbol parsing test fails :",
                "<text>" +
                        "<w xml:id=\"t1\">le</w> " +
                        "<w xml:id=\"t2\">&amp;</w> " +
                        "<w xml:id=\"t3\">&amp;</w> " +
                        "<w xml:id=\"t4\">chi&eacute;ien</w> " +
                        "<w xml:id=\"t5\">ma&diams;nge</w> " +
                        "<w xml:id=\"t6\">&diams;des</w> " +
                        "<w xml:id=\"t7\">pommes&amp;</w>" +
                        "</text>",
                symbolTokenInjector2.getTokenizeBuffer().toString());

        Assert.assertEquals("complex symbol parsing test fails :",
                "<text>" +
                        "<hi><w xml:id=\"t1\">le</w></hi> " +
                        "<w xml:id=\"t2\">&amp;</w> " +
                        "<w xml:id=\"t3\">&amp;</w> " +
                        "<hi><w xml:id=\"t4\">chi</w></hi>" +
                        "<w xml:id=\"t5\">&eacute;ien</w> " +
                        "<hi><w xml:id=\"t6\">ma</w></hi>" +
                        "<sub><w xml:id=\"t7\">&diams;&diams;</w></sub>" +
                        "<sub><w xml:id=\"t8\">nge</w></sub> " +
                        "<sub><w xml:id=\"t9\">&diams;d</w></sub>" +
                        "<w xml:id=\"t10\">es</w> " +
                        "<hi><w xml:id=\"t11\">pommes&amp;</w></hi>" +
                        "</text>",
                symbolTokenInjector3.getTokenizeBuffer().toString());

    }

    @Test
    public void checkTextAlignmentTest(){
        alignmentTokenInjector.teiWordTokenizer();
        Assert.assertEquals("text alignment test fails :",
                "<text>" +
                        "<head><w xml:id=\"t1\">le</w> " +
                        "<w xml:id=\"t2\">chien</w></head>" +
                        "<p><w xml:id=\"t3\">mange</w> " +
                        "<div><w xml:id=\"t4\">un</w> " +
                        "<w xml:id=\"t5\">froma</w>" +
                        "<sup><w xml:id=\"t6\">ge</w></sup> " +
                        "<w xml:id=\"t7\">assez</w></div></p>" +
                        "<p><w xml:id=\"t8\">d&eacute;licieux</w></p>\n\n\n" +
                        "<w xml:id=\"t9\">&amp;</w>" +
                        "</text>",
                alignmentTokenInjector.getTokenizeBuffer().toString());
    }

}