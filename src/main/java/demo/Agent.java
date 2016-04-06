package demo;

import com.gianty.service.DemoService;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by thien.ld on 4/5/16.
 */
public class Agent {
    public static void agentmain(String command) throws Exception {
        System.out.println(">>> agent start on " + Thread.currentThread().getName());
        String[] args = command.split(":");
        if (args.length > 1) {
            String action = args[0];
            switch (action) {
                case "kill":
                    killThread(args[1]);
                    break;
                case "alter":
                    alterVar(args[1]);
                    break;
            }
        }
    }

    public static void killThread(String tname) {
        Thread[] threads = getThreads();
        boolean found = false;
        for (Thread thread : threads) {
            if (thread != null && thread.getName().equals(tname)) {
                System.out.println(">>> Killing thread: " + tname);
                found = true;
                thread.stop();
            }
        }
        if (!found) {
            System.out.println(">>> Did not find thread: " + tname);
        }
    }

    public static void alterVar(String value) throws Exception {
        ApplicationContext ctx = com.gianty.spring.SpringContextHolder.getContext();
        Class clazz = Class.forName("com.gianty.service.DemoService");
        DemoService service = (DemoService) ctx.getBean(clazz);

        Field field = service.getClass().getDeclaredField("diamondDiff");
        field.setAccessible(true);

        Field modifiersField = field.getClass().getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        System.out.println(">>> before value: " + field.get(service));
        field.set(service, Long.valueOf(value));
        System.out.println(">>> after value: " + field.get(service));
    }

    public static Thread[] getThreads() {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        return threads;
    }
}
