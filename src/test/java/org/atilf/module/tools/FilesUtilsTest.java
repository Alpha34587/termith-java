package org.atilf.module.tools;

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
public class FilesUtilsTest {
    String path;
    Map<String, StringBuilder> testMap;

    @Before
    public void setUp() throws Exception {
        path = FilesUtils.createTemporaryFolder("test");
        testMap = new HashMap<>();
        testMap.put("test1", new StringBuilder("test2"));
        testMap.put("test3", new StringBuilder("test4"));
        FilesUtils.createFiles(path,testMap,"txt");
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