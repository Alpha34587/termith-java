package org.atilf.tools;

import org.junit.Assert;
import org.junit.BeforeClass;
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

    private static String _path;
    private static Map<String, StringBuilder> _expectedMap = new HashMap<>();

    @BeforeClass
    public static void setUp() throws Exception {
        _path = FilesUtils.createTemporaryFolder("test");
        _expectedMap.put("test1", new StringBuilder("test2"));
        _expectedMap.put("test3", new StringBuilder("test4"));
        FilesUtils.createFiles(_path, _expectedMap,"txt");
    }

    @Test
    public void testIfFilesExists() {
        Path path1 = Paths.get(_path + "/test1.txt");
        Path path2 = Paths.get(_path + "/test3.txt");
        Assert.assertTrue("the file " + path1.toString() + " must be exists ", Files.exists(path1));
        Assert.assertTrue("the file " + path2.toString() + " must be exists ", Files.exists(path2));
    }

    @Test
    public void testFilesContent() {
        _expectedMap.forEach((name, content) -> {
            try {
                System.out.println(content);
                Assert.assertEquals("this file must contains",
                        Files.readAllLines(Paths.get( _path + "/"+ name + ".txt")).get(0),content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}