package com.wujt.java9;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author gow 2021/06/13
 */
public class ProcessDemo {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
//        String command="Code.exe";
        String command="C:\\Users\\Administrator\\AppData\\Local\\JetBrains\\Toolbox\\apps\\IDEA-C\\ch-0\\211.7142.45\\bin\\idea64.exe";
        Process p = new ProcessBuilder(command).start();
        System.out.println(p.pid());
        ProcessHandle processHandle = p.toHandle();
        CompletableFuture<ProcessHandle> onExit = processHandle.onExit();
        onExit.get();
        onExit.thenAccept(ph_ -> System.out.printf("PID %d terminated%n", ph_.pid()));

        ProcessHandle.allProcesses()
                .filter(ph -> ph.info().command().isPresent())
                .limit(10)
                .forEach((process) -> dumpProcessInfo(process));
    }
    static void dumpProcessInfo(ProcessHandle ph)
    {
        System.out.println("PROCESS INFORMATION");
        System.out.println("===================");
        System.out.printf("Process id: %d%n", ph.pid());
        ProcessHandle.Info info = ph.info();
        System.out.printf("Command: %s%n", info.command().orElse(""));
        String[] args = info.arguments().orElse(new String[]{});
        System.out.println("Arguments:");
        for (String arg: args)
            System.out.printf("   %s%n", arg);
        System.out.printf("Command line: %s%n", info.commandLine().orElse(""));
        System.out.printf("Start time: %s%n",
                info.startInstant().orElse(Instant.now()).toString());
        System.out.printf("Run time duration: %sms%n",
                info.totalCpuDuration()
                        .orElse(Duration.ofMillis(0)).toMillis());
        System.out.printf("Owner: %s%n", info.user().orElse(""));
        System.out.println();
    }
}
