package pl.edu.agh.kis.pz1;

import java.util.logging.Logger;

public class Log {
    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected void log(String message) {
        logger.info(message);
    }

    protected void logln(String message) {
        log(message + "\n");
    }
}
