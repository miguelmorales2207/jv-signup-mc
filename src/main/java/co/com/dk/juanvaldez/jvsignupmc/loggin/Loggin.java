package co.com.dk.juanvaldez.jvsignupmc.loggin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Loggin {

    /* Constants */
    private static final Logger LOGGER = LogManager.getLogger(Loggin.class);

    /**
     * @param log
     */
    public void log(String log) {
        LOGGER.info(log);
    }

}
