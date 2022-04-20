package com.wujt.java9;

/**
 * @author gow 2021/06/12
 */
public class InterfaceJava8Demo {

    public static void main(String[] args) {
        LogOracle log = new LogOracle();
        log.logDebug("");
        log.logInfo("");
        log.logWarn("");
        log.logError("");
        log.logFatal("");

        LogMySql log1 = new LogMySql();
        log1.logDebug("");
        log1.logInfo("");
        log1.logWarn("");
        log1.logError("");
        log1.logFatal("");
    }

}

final class LogOracle implements Logging {
    @Override
    public void logDebug(String message) {
        Logging.getConnection();
        System.out.println("Log Message : " + ORACLE + " debug");
        Logging.closeConnection();
    }
}

final class LogMySql implements Logging {

    @Override
    public void logDebug(String message) {
        Logging.getConnection();
        System.out.println("Log Message : " + MYSQL + " debug");
        Logging.closeConnection();
    }
}

interface Logging {
    String ORACLE = "Oracle_Database";
    String MYSQL = "MySql_Database";

    void logDebug(String message);

    default void logInfo(String message) {
        getConnection();
        System.out.println("Log Message : " + "INFO");
        closeConnection();
    }

    default void logWarn(String message) {
        getConnection();
        System.out.println("Log Message : " + "WARN");
        closeConnection();
    }

    default void logError(String message) {
        getConnection();
        System.out.println("Log Message : " + "ERROR");
        closeConnection();
    }

    default void logFatal(String message) {
        getConnection();
        System.out.println("Log Message : " + "FATAL");
        closeConnection();
    }

    static void getConnection() {
        System.out.println("Open Database connection");
    }

    static void closeConnection() {
        System.out.println("Close Database connection");
    }

}
