package org.atilf.module.tools;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.EvaluationProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Map;

import static org.atilf.models.SubLexicResource.*;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambTeiWriter {
    private final String p;
    private final Map<String, EvaluationProfile> evaluationLexic;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private DocumentBuilder dBuilder;
    private Document doc;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private XPath xpath = XPathFactory.newInstance().newXPath();

    public DisambTeiWriter(String p, Map<String, EvaluationProfile> evaluationLexic) {
        this.p = p;
        this.evaluationLexic = evaluationLexic;
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);

        try {
            dbFactory.setNamespaceAware(true);
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            doc = dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    public void execute() {
        XPathExpression span;
        XPathExpression ana;
        XPathExpression corresp;
        try {
            span = xpath.compile(SPAN);
            ana = xpath.compile(ANA);
            corresp = xpath.compile(CORRESP);
            NodeList termNodes = (NodeList) span.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < termNodes.getLength(); i++){
                Node correspVal = (Node) corresp.evaluate(termNodes.item(i), XPathConstants.NODE);
                Node anaVal = (Node) ana.evaluate(termNodes.item(i), XPathConstants.NODE);
                String termId = correspVal.getNodeValue().substring(1) + "_" + anaVal.getNodeValue().substring(1);
                if (evaluationLexic.containsKey(termId)) {
                    anaVal.setNodeValue(
                            anaVal.getNodeValue() + " #" + evaluationLexic.get(termId).get_disambId()
                    );
                }
            }

            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
                doc.setXmlStandalone(true);
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(TermithIndex._outputPath + "/"
                        + FilesUtils.nameNormalizer(p) + ".xml");
                transformer.transform(source, result);
            } catch (TransformerException e) {
                LOGGER.error("error during file writing",e);
            }

        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }


    }
}
