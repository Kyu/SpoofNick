package me.preciouso.spoofnick.service;

import me.preciouso.spoofnick.SpoofPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class PullInfoService implements Callable<SpoofPlayer> {
    /*
    https://api.mojang.com/users/profiles/minecraft/BluGroot
    {"name":"BluGroot","id":"11c4795212a246f7ad81bfb5fa15a9a9"}

    https://sessionserver.mojang.com/session/minecraft/profile/11c4795212a246f7ad81bfb5fa15a9a9
    {
      "id" : "11c4795212a246f7ad81bfb5fa15a9a9",
      "name" : "BluGroot",
      "properties" : [ {
        "name" : "textures",
        "value" : "ewogICJ0aW1lc3RhbXAiIDogMTYxMTQ4NTM5NzA0NSwKICAicHJvZmlsZUlkIiA6ICIxMWM0Nzk1MjEyYTI0NmY3YWQ4MWJmYjVmYTE1YTlhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCbHVHcm9vdCIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MzNhOTFmZTI2ZDZjYTE0MWVmOGFlNmY4ZjIzNjU5NzJhNGY0ZjEwYjk0Y2Q5MjNiMzY3NmZkMWViODBjNTBhIgogICAgfQogIH0KfQ=="
      } ]
    }
     */
    String fakeUsername;
    String realUsername;

    public PullInfoService(String fakeUsername, String realUsername) {
        this.realUsername = realUsername;
        this.fakeUsername = fakeUsername;
    }

    @Override
    public SpoofPlayer call() {
        boolean OFCape = false;
        JSONObject json;
        try {
            json = this.readJSON("https://api.mojang.com/users/profiles/minecraft/" + this.fakeUsername);
            String id = json.getString("id");

            json = this.readJSON("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
            json = json.getJSONArray("properties").getJSONObject(0);

            json = new JSONObject(this.decodeBase64(json.getString("value")));
            json = json.getJSONObject("textures");

            String urlSkin = null;
            String urlCape;

            if (json.has("SKIN")) {
                urlSkin = json.getJSONObject("SKIN").getString("url");
            }

            if (json.has("CAPE")) {
                urlCape = json.getJSONObject("CAPE").getString("url");
            } else {
                urlCape = getOFCape(this.fakeUsername);
                OFCape = true;
            }

            return new SpoofPlayer(this.fakeUsername, this.realUsername, urlSkin, urlCape, OFCape);
        } catch (IOException e) {
            e.printStackTrace();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + e.getMessage() + EnumChatFormatting.RESET));
            return null;
        }
    }

    private String getOFCape(String username) {
        return "http://s.optifine.net/capes/" + username + ".png";
    }

    private JSONObject readJSON(String url) throws IOException {
        InputStream is = (new URL(url)).openStream();
        StringBuilder sb = new StringBuilder();

        int b;
        while((b = is.read()) != -1) {
            sb.append((char)b);
        }

        return new JSONObject(sb.toString());
    }

    private String decodeBase64(String string) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(string), StandardCharsets.UTF_8);
    }
}
