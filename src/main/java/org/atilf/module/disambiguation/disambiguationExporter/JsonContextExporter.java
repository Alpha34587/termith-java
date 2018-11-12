package org.atilf.module.disambiguation.disambiguationExporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonContextExporter extends Module{
    private final String _id;
    private final LexiconProfile _lexiconProfile;
    private final String _path;

    public JsonContextExporter(String id, LexiconProfile lexiconProfile, String path) {
        _id = id;
        _lexiconProfile = lexiconProfile;
        _path = path;
    }

    @Override
    protected void execute() {
        JsonFactory jsonFactory = new JsonFactory();
        File file = new File(_path + "/" + _id + ".json");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            _logger.debug("Writing " + file.getPath());
            JsonGenerator jg = jsonFactory.createGenerator(writer);
            jg.useDefaultPrettyPrinter();
            jg.writeStartObject();
            _lexiconProfile.getSpecCoefficientMap().forEach(
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
            jg.flush();
            writer.close();
        } catch (IOException e) {
            _logger.error("Failure while serializing " + file + "\nCaused by"
                    + e.getClass().getCanonicalName() + ":" + e.getMessage(), e);
        }
    }
}

