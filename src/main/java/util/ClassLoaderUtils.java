package util;

import org.apache.catalina.loader.WebappClassLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderUtils {

    public static void loadClass(String path) throws Exception {
        try {
            JarFile jarFile = new JarFile(new File(path));
            URL url = new URL("file:" + path);
            WebappClassLoader webappClassLoader = ((WebappClassLoader) Thread.currentThread().getContextClassLoader());
            Method addUrl = webappClassLoader.getClass().getSuperclass().getDeclaredMethod("addURL", URL.class);
            addUrl.setAccessible(true);
            addUrl.invoke(webappClassLoader, url);
            Enumeration<JarEntry> es = jarFile.entries();
            while (es.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) es.nextElement();
                String name = jarEntry.getName();
                if (name != null && name.endsWith(".class")) {
                    String className = name.replace("/", ".").substring(0, name.length() - 6);
                    webappClassLoader.loadClass(className);
                }
            }
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }
}
