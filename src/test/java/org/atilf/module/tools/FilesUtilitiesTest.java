package org.atilf.module.tools;

import org.atilf.module.tools.FilesUtilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 * Created on 22/08/16.
 */
public class FilesUtilitiesTest {
    String path;
    Map<String, StringBuffer> testMap;

    @Before
    public void setUp() throws Exception {
        path = FilesUtilities.createTemporaryFolder("test");
        testMap = new HashMap<>();
        testMap.put("test1", new StringBuffer("test2"));
        testMap.put("test3", new StringBuffer("test4"));
        FilesUtilities.createFiles(path,testMap,"txt");
    }

    @Test
    public void testIfFilesExists() {
        Path path1 = Paths.get(path + "/test1.txt");
        Path path2 = Paths.get(path + "/test3.txt");
        Assert.assertTrue("the file " + path1.toString() + " must be exists ", Files.exists(path1));
        Assert.assertTrue("the file " + path2.toString() + " must be exists ", Files.exists(path2));
    }

    @Test
    public void testFilesContent() {
        testMap.forEach((name, content) -> {
            try {
                System.out.println(content);
                Assert.assertEquals("this file must contains",
                        Files.readAllLines(Paths.get( path + "/"+ name + ".txt")).get(0),content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}