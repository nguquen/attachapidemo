package demo;

import java.net.URLClassLoader;

/**
 * Created by thien.ld on 4/6/16.
 */
public class KillThreadAgent {
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

    public static void agentmain(String tname) {
        System.out.println(">>> Current thread: " + Thread.currentThread().getName());
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
}
