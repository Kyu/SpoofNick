package me.preciouso.spoofnick.service;

import me.preciouso.spoofnick.SpoofNick;
import me.preciouso.spoofnick.SpoofPlayer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class SpoofNickService {

    public static void initSpoof(String realUsername, String fakeUsername) throws ExecutionException, InterruptedException {
        removeSpoof(realUsername);
        SpoofPlayer spoofPlayer = Executors.newSingleThreadExecutor().submit(new PullInfoService(fakeUsername, realUsername)).get();
        SpoofNick.spoofedNames.put(realUsername, spoofPlayer);
    }

    public static void removeSpoof(String realUsername) {
        // Map map = ReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getMinecraft().getTextureManager(),"mapTextureObjects");
        SpoofPlayer torem = SpoofNick.spoofedNames.get(realUsername);
        if (torem != null) {
            SpoofNick.toRemove.add(torem.getRealUsername());
        }
        // map.remove();

    }

    public static void totallyRemove(String realUsername) {
        SpoofNick.spoofedNames.remove(realUsername);
        SpoofNick.toRemove.remove(realUsername);
    }
}
