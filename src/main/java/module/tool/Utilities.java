package module.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
/**
 * Created by Simon Meoni on 16/08/16.
 */
public class Utilities {

        public static void deleteFolder(String path){
            try {
                File f = new File(path);
                if (f.exists())
                    FileUtils.forceDelete(f); //delete directory
                FileUtils.forceMkdir(f); //create directory
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void writeToFile(String filename, StringBuffer data) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(data.toString());
        out.flush();
        out.close();
    }
}
