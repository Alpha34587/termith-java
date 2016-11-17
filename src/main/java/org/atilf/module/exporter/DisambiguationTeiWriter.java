package org.atilf.module.exporter;

import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
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

import static org.atilf.models.disambiguation.ContextResources.*;

/**
 * Write the result of disambiguation in tei file format
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambiguationTeiWriter implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeiWriter.class.getName());
    private final String _p;
    private final Map<String, EvaluationProfile> _evaluationLexicon;
    private DocumentBuilder _dBuilder;
    private Document _doc;
    private DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
    private XPath _xpath = XPathFactory.newInstance().newXPath();

    /**
     * constructor for DisambiguationTeiWriter
     * @param p the file name
     * @param evaluationLexicon the evaluation lexicon that contains the result for disambiguation for one file
     */
    public DisambiguationTeiWriter(String p, Map<String, EvaluationProfile> evaluationLexicon) {
        /*
        prepare dom parser
         */
        _p = p;
        _evaluationLexicon = evaluationLexicon;

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
            LOGGER.error("error during the execute of document",e);
        }
    }

    public void execute() {
        XPathExpression span;
        try {
            /*
            compile needed xpath expression
             */
            span = _xpath.compile(TEI_SPAN);
            /*
            get all the span of corresp element
             */
            NodeList termNodes = (NodeList) span.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < termNodes.getLength(); i++){
                /*
                get corresp attribute of span element
                 */
                String correspVal = termNodes.item(i).getAttributes().getNamedItem("corresp").getNodeValue();
                /*
                get ana attribute of span element
                 */
                Node anaNode = termNodes.item(i).getAttributes().getNamedItem("ana");
                String anaVal = anaNode.getNodeValue();

                /*
                write result of disambiguation in ana attribute
                 */
                String termId = correspVal.substring(1) + "_" + anaVal.substring(1);
                if (_evaluationLexicon.containsKey(termId)) {
                    anaNode.setNodeValue(
                            anaVal + " #" + _evaluationLexicon.get(termId).getDisambiguationId()
                    );
                    LOGGER.debug("write DaOn or DaOff value");
                }

                else {
                    anaNode.setNodeValue(
                            anaVal + " #noDa"
                    );
                }
            }
            try {
                /*
                write result
                 */
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
            LOGGER.error("error during the execute of document",e);
        }
    }

    /**
     * call execute method
     */
    @Override
    public void run() {
        LOGGER.debug("write tei disambiguation for :" + _p);
        execute();
        LOGGER.debug("tei disambiguation is written for :" + _p);
    }

}
