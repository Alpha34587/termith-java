package org.atilf.runner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Simon Meoni on 11/07/17.
 */
public class TermithResourceManager {
    private static String _prefixUrl;
    public static void addToClasspath(String prefixUrl) throws Exception {
        File f = new File(prefixUrl);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u.toURL()});
        _prefixUrl = prefixUrl;
    }


}
