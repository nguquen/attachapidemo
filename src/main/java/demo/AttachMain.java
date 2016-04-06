package demo;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by thien.ld on 4/5/16.
 */
public class AttachMain {

    private static void addToolJarToClasspath() throws Exception {
        String path = System.getProperty("java.home") + "/../lib/tools.jar";
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Classpath.addPath(classLoader, path);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java -jar <jarfile> <pid> <command>");
            System.exit(-1);
        }
        if (args[1].split(":").length != 2) {
            System.err.println("Usage: java -jar <jarfile> <pid> <command>\n" +
                    "\tcommand:\n" +
                    "\t\tkill:<thread name>\n" +
                    "\t\talter:<value>");
            System.exit(-1);
        }
        // add tool.jar to classpath
        addToolJarToClasspath();
        // get current jar filepath
        File self = new File(AttachMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String pid = args[0];
        String command = args[1];
        VirtualMachine vm = VirtualMachine.attach(pid);
        try {
            vm.loadAgent(self.getAbsolutePath(), command);
        } finally {
            vm.detach();
        }
    }
}
