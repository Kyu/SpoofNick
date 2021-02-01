package me.preciouso.spoofnick.service;

import me.preciouso.spoofnick.SpoofNick;
import me.preciouso.spoofnick.SpoofPlayer;

import java.util.Map;

public class SpoofStringHelper {
    public static String spoofString(String original) {
        for (Map.Entry<String, SpoofPlayer> element : SpoofNick.spoofedNames.entrySet()) {
            original = original.replaceAll(element.getKey(), element.getValue().getFakeUsername());
        }

        return original;
    }
}
