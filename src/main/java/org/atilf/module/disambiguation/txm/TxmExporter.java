package org.atilf.module.disambiguation.txm;

import org.atilf.models.disambiguation.TxmContext;
import org.atilf.module.Module;
import org.flowable.bpmn.converter.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by smeoni on 14/04/17.
 */
public class TxmExporter extends Module {
    private final String _p;
    private final List<TxmContext> _TxmContext;
    private String _outputPath;
    private static final  String TARGET = "target";
    private static final String TITLE = "title";

    public TxmExporter(String p, List<TxmContext> txmContext, String outputPath) {
        _p = p;
        _TxmContext = txmContext;
        _outputPath = outputPath;
    }
    @Override
    protected void execute() {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(new FileWriter
                    (_outputPath + "/" + _p + ".xml")));

            writer.writeStartDocument();
            writer.writeStartElement("TEI");
            writer.writeDefaultNamespace("http://www.tei-c.org/ns/1.0");
            writer.writeNamespace("txm","http://textometrie.org/1.0");
            writer.setDefaultNamespace("http://www.tei-c.org/ns/1.0");
            writer.setPrefix("txm","http://textometrie.org/1.0");
            writeTeiHeader(writer);
            writerText(writer);
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (IOException | XMLStreamException e) {
            _logger.error("cannot write txm file",e);
        }
    }

    private void writerText(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("text");
        writer.writeAttribute("id",_p+".xml");
        for (TxmContext txmContext : _TxmContext) {
            writer.writeStartElement("s");
            writer.writeAttribute("n",String.valueOf(_TxmContext.indexOf(txmContext)+1));
            writer.writeAttribute("id",txmContext.getFilename());
            while (!txmContext.isEmpty()){
                Map<String,String> map = txmContext.getTxmWord();
                writer.writeStartElement("w");
                writer.writeAttribute("id","w_" + _p);
                writer.writeAttribute("n",map.get(TARGET));

                writer.writeStartElement("txm:form");
                writer.writeCharacters(map.get("word"));
                writer.writeEndElement();

                writer.writeStartElement("txm:ana");
                writer.writeAttribute("resp","#txm");
                writer.writeAttribute("type","#frpos");
                writer.writeCharacters(map.get("pos"));
                writer.writeEndElement();

                writer.writeStartElement("txm:ana");
                writer.writeAttribute("resp","#txm");
                writer.writeAttribute("type","#frlemma");
                writer.writeCharacters(map.get("lemma"));
                writer.writeEndElement();

                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void writeTeiHeader(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("teiHeader");

        writer.writeStartElement("fileDesc");
        writer.writeStartElement("titleStmt");
        writer.writeStartElement(TITLE);
        writer.writeCharacters(_p+".xml");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEmptyElement("publicationStmt");
        writer.writeStartElement("sourceDesc");
        writer.writeStartElement("p");
        writer.writeCharacters("Generated by Termith-java");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeStartElement("encodingDesc");
        writer.writeEmptyElement("applicationDesc");
        writer.writeStartElement("appInfo");
        writer.writeStartElement("txm:application");
        writer.writeAttribute("ident","TermITH");
        writer.writeAttribute("version","1");
        writer.writeAttribute("resp","atilf");
        writer.writeStartElement("ab");
        writer.writeAttribute("type","annotation");
        writer.writeStartElement("list");
        writer.writeStartElement("item");
        writer.writeStartElement("ref");
        writer.writeAttribute("type","tagset");
        writer.writeAttribute(TARGET,"#frpos");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeStartElement("item");
        writer.writeStartElement("ref");
        writer.writeAttribute("type","tagset");
        writer.writeAttribute(TARGET,"#frlemma");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeStartElement("classDecl");
        writer.writeStartElement("taxonomy");
        writer.writeAttribute("id","frpos");
        writer.writeStartElement("bibl");
        writer.writeStartElement(TITLE);
        writer.writeCharacters("see " + _p+".xml");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeStartElement("taxonomy");
        writer.writeAttribute("id","frlemma");
        writer.writeStartElement("bibl");
        writer.writeStartElement(TITLE);
        writer.writeCharacters("see " + _p+".xml");
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
    }
}
