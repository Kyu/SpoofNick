package me.preciouso.spoofnick;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import me.preciouso.spoofnick.render.GuiRender;
import me.preciouso.spoofnick.render.PlayerRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid=SpoofNick.MODID)
public class SpoofNick {
    public static final String MODID = "spoofnick";
    public static ArrayList<String> toRemove;
    public static HashMap<String, SpoofPlayer> spoofedNames;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        spoofedNames = new HashMap<>();
        toRemove = new ArrayList<>();

        GuiRender guiRender = new GuiRender(Minecraft.getMinecraft());
        PlayerRender playerRender = new PlayerRender();

        playerRender.setRenderManager(RenderManager.instance);
        RenderManager.instance.entityRenderMap.put(EntityPlayer.class, playerRender);

        ClientCommandHandler.instance.registerCommand(new CommandSpoof());

        EventHandler handler = new EventHandler(guiRender, playerRender);
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }
}
