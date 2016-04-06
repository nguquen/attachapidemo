package demo;

import com.gianty.service.DemoService;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by thien.ld on 4/6/16.
 */
public class ReflectionAgent {
    public static void agentmain(String agentArgs) throws Exception {
        // dynamic classloaders
        URL self = ReflectionAgent.class.getProtectionDomain().getCodeSource().getLocation();
        URLClassLoader classLoader = new URLClassLoader(new URL[] {
                self
        }, ClassLoader.getSystemClassLoader()) {
            public @Override Class<?> loadClass(String name) throws ClassNotFoundException {
                if (name.contains("ReflectionAgent")) {
                    return findClass(name);
                } else {
                    return super.loadClass(name);
                }
            }
        };
        Class<?> reflectionAgentClazz = classLoader.loadClass("demo.ReflectionAgent");
        reflectionAgentClazz.getMethod("doThing").invoke(null);
    }

    public static void doThing() throws Exception {
        System.out.println(">>> reflection");
        ApplicationContext ctx = com.gianty.spring.SpringContextHolder.getContext();
        Class clazz = Class.forName("com.gianty.service.DemoService");
        DemoService service = (DemoService) ctx.getBean(clazz);

        Field field = service.getClass().getDeclaredField("diamondDiff");
        field.setAccessible(true);

        Field modifiersField = field.getClass().getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(service, 1000000);
        System.out.println(">>> after value: " + field.get(service));
    }
}
