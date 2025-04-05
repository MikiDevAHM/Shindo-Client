package me.miki.shindo.helpers.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShindoLogger {

    public static final Logger logger = LogManager.getLogger("Shindo Client");

    public static void info(String message) {
        logger.info("[SHINDO/INFO] " + message);
    }

    public static void warn(String message) {
        logger.warn("[SHINDO/WARN] " + message);
    }

    public static void error(String message) {
        logger.error("[SHINDO/ERROR] " + message);
    }

    public static void error(String message, Exception e) {
        logger.error("[SHINDO/ERROR] " + message, e);
    }

    public static Logger getLogger() {
        return logger;
    }
}