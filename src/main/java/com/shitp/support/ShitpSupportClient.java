package com.shitp.support;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShitpSupportClient implements ClientModInitializer {
    public static final String MOD_ID = "shitp_support_library";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Shitp Support Library initialized.");
    }
}
