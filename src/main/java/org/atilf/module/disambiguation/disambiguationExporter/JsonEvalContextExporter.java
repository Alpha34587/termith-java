package org.atilf.module.disambiguation.disambiguationExporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonEvalContextExporter extends Module{
    private final String _file;
    private final Map<String, EvaluationProfile> _eval;
    private final String _path;

    public JsonEvalContextExporter(String file, Map<String, EvaluationProfile> eval, String outputPath) {
        _file = file;
        _eval = eval;
        _path = outputPath;
    }

    @Override
    protected void execute() {
        JsonFactory jsonFactory = new JsonFactory();
        File file = new File(_path + "/" + _file + ".json");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            _logger.debug("Writing " + file.getPath());
            JsonGenerator jg = jsonFactory.createGenerator(writer);
            jg.useDefaultPrettyPrinter();
            jg.writeStartArray();
            _eval.forEach((id, context) -> {
                        try {
                            jg.writeStartObject();
                            jg.writeFieldName("id");
                            jg.writeString(id);
                            jg.writeObjectFieldStart("context");
                            context.getSpecCoefficientMap().forEach(
                                    (k,v) -> {
                                        try {
                                            jg.writeFieldName(k);
                                            jg.writeNumber(v);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            );
                            jg.writeEndObject();
                            jg.writeEndObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            jg.writeEndArray();
            jg.flush();
            writer.close();
        } catch (IOException e) {
            _logger.error("Failure while serializing " + file + "\nCaused by"
                    + e.getClass().getCanonicalName() + ":" + e.getMessage(), e);
        }
    }
}
