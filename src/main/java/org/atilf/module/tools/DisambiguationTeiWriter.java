package org.atilf.module.tools;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguisation.EvaluationProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Map;

import static org.atilf.models.disambiguisation.ContextResources.*;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambiguationTeiWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private final String _p;
    private final Map<String, EvaluationProfile> _evaluationLexic;
    private DocumentBuilder _dBuilder;
    private Document _doc;
    private DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
    private XPath _xpath = XPathFactory.newInstance().newXPath();

    public DisambiguationTeiWriter(String p, Map<String, EvaluationProfile> evaluationLexic) {
        _p = p;
        _evaluationLexic = evaluationLexic;
        _xpath.setNamespaceContext(NAMESPACE_CONTEXT);

        try {
            _dbFactory.setNamespaceAware(true);
            _dBuilder = _dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    public void execute() {
        XPathExpression span;
        XPathExpression ana;
        XPathExpression corresp;
        try {
            span = _xpath.compile(SPAN_T);
            ana = _xpath.compile(ANA_T);
            corresp = _xpath.compile(CORRESP_T);
            NodeList termNodes = (NodeList) span.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < termNodes.getLength(); i++){
                Node correspVal = (Node) corresp.evaluate(termNodes.item(i), XPathConstants.NODE);
                Node anaVal = (Node) ana.evaluate(termNodes.item(i), XPathConstants.NODE);
                String termId = correspVal.getNodeValue().substring(1) + "_" + anaVal.getNodeValue().substring(1);
                if (_evaluationLexic.containsKey(termId)) {
                    anaVal.setNodeValue(
                            anaVal.getNodeValue() + " #" + _evaluationLexic.get(termId).getDisambId()
                    );
                }
            }

            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
                _doc.setXmlStandalone(true);
                DOMSource source = new DOMSource(_doc);
                StreamResult result = new StreamResult(TermithIndex.getOutputPath() + "/"
                        + FilesUtils.nameNormalizer(_p) + ".xml");
                transformer.transform(source, result);
            } catch (TransformerException e) {
                LOGGER.error("error during file writing",e);
            }

        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }


    }
}
