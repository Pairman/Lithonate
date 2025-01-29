package org.eu.pnxlr.lithonate.util;

import com.google.gson.JsonObject;
import org.eu.pnxlr.lithonate.Lithonate;

/**
 * Reference me.fallenbreath.tweakermore.util.JsonSaveAble
 */
public interface JsonSavable {
    default JsonObject dumpToJson() {
        JsonObject jsonObject = new JsonObject();
        this.dumpToJson(jsonObject);
        return jsonObject;
    }

    void dumpToJson(JsonObject jsonObject);

    void loadFromJson(JsonObject jsonObject);

    /**
     * Just like {@link #loadFromJson}, but exception proof
     */
    default void loadFromJsonSafe(JsonObject jsonObject) {
        try {
            this.loadFromJson(jsonObject);
        } catch (Exception e) {
            Lithonate.LOGGER.warn("Failed to load data of {} from json object {}: {}", this.getClass().getSimpleName(), jsonObject, e);
        }
    }
}
