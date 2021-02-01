package me.preciouso.spoofnick;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class SpoofPlayer {
    private final SkinManager sm;
    private final String fakeUsername;
    private final String realUsername;
    private final String skin;
    private final String cape;
    private MinecraftProfileTexture texSkin = null;
    private MinecraftProfileTexture texCape = null;

    public SpoofPlayer(String fakeUsername, String realUsername, String skin, String cape, boolean OFCape) throws IOException {
        // Fake username
        String cape1 = null;
        this.fakeUsername = fakeUsername;
        this.realUsername = realUsername;
        this.skin = skin;
        if (skin != null) {
            this.texSkin = this.getTexture(skin);
        }

        if (cape != null) {
            if (OFCape && OFCapeExists(new URL(cape))) {
                this.texCape = this.getTexture(cape);
                cape1 = cape;
            } else if (! OFCape) {
                this.texCape = this.getTexture(cape);
                cape1 = cape;
            }
        }
        
        this.cape = cape1;

        this.sm = Minecraft.getMinecraft().func_152342_ad();
    }

    public String getRealUsername() {
        return this.realUsername;
    }

    public String getFakeUsername() {
        return fakeUsername;
    }

    public ResourceLocation getSkin() {
        return this.skin == null ? null : this.sm.func_152792_a(this.texSkin, MinecraftProfileTexture.Type.SKIN);
    }

    public ResourceLocation getCape() {
        // TODO Thread peek
        return this.cape == null ? null : this.sm.func_152792_a(this.texCape, MinecraftProfileTexture.Type.CAPE);
    }

    private MinecraftProfileTexture getTexture(String url) {
        Class<MinecraftProfileTexture> clazz = MinecraftProfileTexture.class;

        try {
            Constructor<MinecraftProfileTexture> c = clazz.getConstructor(String.class, Map.class);
            return c.newInstance(url, null);
        } catch (Exception e) {

            System.err.println("Ignore this!: " + e.getLocalizedMessage());
            return new MinecraftProfileTexture(url);
        }
    }

    private boolean OFCapeExists(URL url) throws IOException {
        // https://stackoverflow.com/a/45036771/3875151
        // We want to check the current URL
        HttpURLConnection.setFollowRedirects(false);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // We don't need to get data
        try {
            httpURLConnection.setRequestMethod("HEAD");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        // Some websites don't like programmatic access so pretend to be a browser
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        int responseCode = httpURLConnection.getResponseCode();

        // We only accept response code 200
        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
