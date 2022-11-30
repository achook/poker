package pl.edu.agh.kis.pz1;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {

    public static class LogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            // This example will print date/time, class, and log level in yellow,
            // followed by the log message and it's parameters in white .
            StringBuilder builder = new StringBuilder();

            builder.append(record.getMessage());

            Object[] params = record.getParameters();

            if (params != null) {
                builder.append("\t");
                for (int i = 0; i < params.length; i++) {
                    builder.append(params[i]);
                    if (i < params.length - 1)
                        builder.append(", ");
                }
            }

            return builder.toString();
        }
    }
    protected Logger logger;

    public Log() {
        logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();

        Formatter formatter = new LogFormatter();
        handler.setFormatter(formatter);

        logger.addHandler(handler);
    }

    protected void log(String message) {
        logger.info(message);
    }

    protected void logln(String message) {
        log(message + "\n");
    }
}
