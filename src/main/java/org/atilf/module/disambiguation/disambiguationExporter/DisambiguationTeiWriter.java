package org.atilf.module.disambiguation.disambiguationExporter;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.AnnotationResources;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.Module;
import org.atilf.tools.FilesUtils;
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

import static org.atilf.models.disambiguation.ContextResources.NAMESPACE_CONTEXT;
import static org.atilf.models.disambiguation.ContextResources.TEI_SPAN;

/**
 * Write the result of module.disambiguation in tei file format
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambiguationTeiWriter extends Module {
    private final String _p;
    private final Map<String, EvaluationProfile> _evaluationLexicon;
    private DocumentBuilder _dBuilder;
    private Document _doc;
    private DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
    private XPath _xpath = XPathFactory.newInstance().newXPath();

    /**
     * constructor for DisambiguationTeiWriter
     * @param p the file name
     * @param evaluationLexicon the evaluation lexicon that contains the result for module.disambiguation for one file
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
            _logger.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            _logger.error("error during the execute of document",e);
        }
    }

    public DisambiguationTeiWriter(String p, TermithIndex termithIndex) {
        super(termithIndex);
        /*
        prepare dom parser
         */
        _p = p;
        _evaluationLexicon = _termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));

        _xpath.setNamespaceContext(NAMESPACE_CONTEXT);

        try {
            _dbFactory.setNamespaceAware(true);
            _dBuilder = _dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            _logger.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            _logger.error("error during the execute of document",e);
        }
    }

    public void execute() {
        _logger.debug("write tei module.disambiguation for :" + _p);
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
                write result of module.disambiguation in ana attribute
                 */
                String termId = correspVal.substring(1) + "_" + anaVal.substring(1);
                if (_evaluationLexicon.containsKey(termId)) {
                    anaNode.setNodeValue(
                            anaVal + " " + _evaluationLexicon.get(termId).getDisambiguationId()
                    );
                    _logger.debug("write DaOn or DaOff value");
                }

                else {
                    anaNode.setNodeValue(
                            anaVal + " " + AnnotationResources.NO_DA.getValue()
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
                _logger.error("error during file writing",e);
            }

        } catch (XPathExpressionException e) {
            _logger.error("error during the execute of document",e);
        }
        finally {
            _logger.debug("tei module.disambiguation is written for :" + _p);
        }
    }
}
