package com.shitp.support.util;

import com.shitp.support.ShitpSupportClient;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileHandler {

    public static int processDroppedFile(Path sourceFile, MinecraftClient client) {
        if (client == null || client.runDirectory == null) {
            return 0;
        }

        String fileName = sourceFile.getFileName().toString().toLowerCase();
        Path gameDir = client.runDirectory.toPath();
        Path targetDir;
        int returnCode = 1;

        if (fileName.endsWith(".jar")) {
            targetDir = gameDir.resolve("mods");
            returnCode = 2;
        } else if (fileName.endsWith(".zip")) {
            targetDir = gameDir.resolve("resourcepacks");
        } else {
            return 0;
        }

        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path targetFile = targetDir.resolve(sourceFile.getFileName());
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            return returnCode;
        } catch (IOException e) {
            ShitpSupportClient.LOGGER.error("Error moving file", e);
            return 0;
        }
    }
}
