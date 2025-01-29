package org.eu.pnxlr.lithonate.util;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.eu.pnxlr.lithonate.Lithonate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    private static final String CONFIG_FILE_NAME = Lithonate.MOD_ID + ".json";

    /**
     * Use deprecation API for better old fabric loader version compatibility
     */
    @SuppressWarnings("deprecation")
    public static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(CONFIG_FILE_NAME).toFile();
    }

    public static void writeJsonToFileCreate(JsonObject root, File file) {
        if (ensureFileWritable(file)) {
            JsonUtils.writeJsonToFile(root, file);
        }
    }

    public static boolean ensureFileWritable(File file) {
        File parent = file.getParentFile();
        if ((parent.exists() && parent.isDirectory()) || parent.mkdirs()) {
            try {
                if ((file.exists() && file.isFile() && file.canWrite()) || file.createNewFile()) {
                    return true;
                }
            } catch (IOException e) {
                Lithonate.LOGGER.warn("Failed to create file: " + file);
            }
        }
        return false;
    }

    public static boolean ensureFileReadable(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }

    public static void writeToFile(Path path, String text) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(text);
        }
    }
}
