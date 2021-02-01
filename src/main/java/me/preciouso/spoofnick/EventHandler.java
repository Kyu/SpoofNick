package me.preciouso.spoofnick;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.preciouso.spoofnick.render.GuiRender;
import me.preciouso.spoofnick.render.PlayerRender;
import me.preciouso.spoofnick.service.SpoofStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler {
    GuiRender guiRender;
    PlayerRender playerRender;

    public EventHandler(GuiRender guiRender, PlayerRender playerRender) {
        this.guiRender = guiRender;
        this.playerRender = playerRender;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getMinecraft().ingameGUI.equals(this.guiRender)) {
            Minecraft.getMinecraft().ingameGUI = guiRender;
        }

        if (! RenderManager.instance.entityRenderMap.get(EntityPlayer.class).equals(this.playerRender)) {
            System.out.println("Replaced");
            RenderManager.instance.entityRenderMap.put(EntityPlayer.class, this.playerRender);
        }


    }

    @SubscribeEvent
    public void onPlayerNameCheck(PlayerEvent.NameFormat event) {
        if (SpoofNick.spoofedNames.containsKey(event.entityPlayer.getCommandSenderName())) {
            event.displayname = SpoofNick.spoofedNames.get(event.entityPlayer.getCommandSenderName()).getFakeUsername();
        }
    }

    @SubscribeEvent
    public void onChatRecieved(ClientChatReceivedEvent event) {
        if (! SpoofNick.spoofedNames.isEmpty()) {
            event.message = this.spoofMessage(event.message);
        }
    }

    private ChatComponentText spoofMessage(IChatComponent component) {
        String chatText = component.getFormattedText();
        chatText = SpoofStringHelper.spoofString(chatText);

        return new ChatComponentText(chatText);
    }
}
