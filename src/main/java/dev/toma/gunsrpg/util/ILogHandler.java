package dev.toma.gunsrpg.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public interface ILogHandler {

    void debug(String debugMsg, Object... data);

    void info(String infoMsg, Object... data);

    void warning(String warningMsg, Object... data);

    void err(String errMsg, Object... data);

    void fatal(String fatalMsg, Object... data);

    static ILogHandler wrapLoggerWithMarker(Logger logger, Marker marker) {
        return new ILogHandler() {
            @Override
            public void debug(String debugMsg, Object... data) {
                logger.debug(marker, debugMsg, data);
            }

            @Override
            public void info(String infoMsg, Object... data) {
                logger.info(marker, infoMsg, data);
            }

            @Override
            public void warning(String warningMsg, Object... data) {
                logger.warn(marker, warningMsg, data);
            }

            @Override
            public void err(String errMsg, Object... data) {
                logger.error(marker, errMsg, data);
            }

            @Override
            public void fatal(String fatalMsg, Object... data) {
                logger.fatal(marker, fatalMsg, data);
            }
        };
    }
}
