package dev.qther.convenientcontainers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConvenientContainers {
    public static final String MOD_ID = "convenientcontainers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Convenient Containers is starting.");
    }
}
