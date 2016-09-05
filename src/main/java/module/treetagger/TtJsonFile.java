package module.treetagger;

import java.util.ArrayList;

/**
 * @author Simon Meoni
 *         Created on 05/09/16.
 */
public class TtJsonFile {

    ArrayList<JsonTag> jsonTagList;

    public TtJsonFile() {
        this.jsonTagList = new ArrayList<>();
    }

    public void addJsonTag(JsonTag jsonTag){
        jsonTagList.add(jsonTag);
    }
}
