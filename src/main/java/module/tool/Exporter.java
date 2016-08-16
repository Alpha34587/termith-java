package module.tool;

import runner.Termith;

import java.io.IOException;

/**
 * Created by Simon Meoni on 16/08/16.
 */
public class Exporter {

    public static void export(String path, boolean trace, Termith termith){
        if (trace){
            String textPath = path + "/text";
            Utilities.deleteFolder(textPath);
            termith.getInitializedFiles().forEach((file, content) -> {
                try {
                    Utilities.writeToFile(textPath + "/" + file.replace(".xml",".txt"), content.getPlaintText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
