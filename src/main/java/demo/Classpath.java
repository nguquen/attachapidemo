package demo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Created by thien.ld on 4/6/16.
 */
public class Classpath {
        private static Method addURL = getAddURL();

        // http://stackoverflow.com/questions/8222976/why-urlclassloader-addurl-protected-in-java
        private static Method getAddURL() {
            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static void addPath(URLClassLoader cl, String path) throws Exception {
            URL url  = new File(path).toURI().toURL();
            URL[] urls = cl.getURLs();
            if (!Arrays.asList(urls).contains(url)) addURL.invoke(cl, url);
        }

    public static void removePath(URLClassLoader cl, String path) throws Exception {
        URL url  = new File(path).toURI().toURL();
        URL[] urls = cl.getURLs();
        if (!Arrays.asList(urls).contains(url)) addURL.invoke(cl, url);
    }

}
