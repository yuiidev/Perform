package net.demozo.perform;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class MidiToKeyTranslator {
    private HashMap<Integer, String> indexToName;
    private HashMap<String, String> nameToKey;

    public MidiToKeyTranslator() {
        var objectMapper = new ObjectMapper();

        try {
            indexToName = objectMapper.readValue(Util.getResourceFileAsString("index_to_name.json"), new TypeReference<HashMap<Integer, String>>() {});
            nameToKey = objectMapper.readValue(Util.getResourceFileAsString("default.json"), new TypeReference<HashMap<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int translate(int note) {
        var name = indexToName.get(note);
        var key = nameToKey.get(name);

        if(name == null) {
            Perform.LOGGER.warn("No mapping for MIDI note {}", note);
            return -1;
        }

        var keyChar = key.charAt(0);

        if(Character.isAlphabetic(keyChar)) {
            keyChar = Character.toUpperCase(keyChar);
        }

        return keyChar;
    }
}
