package me.preciouso.spoofnick;

import me.preciouso.spoofnick.service.SpoofNickService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

// TODO /spoof <unNick>
public class CommandSpoof extends CommandBase {
    public CommandSpoof() {}

    public ArrayList<String> getOnlinePlayerNames() {
        List onlinePlayerInfo = Minecraft.getMinecraft().thePlayer.sendQueue.playerInfoList; //Minecraft.getMinecraft().getNetHandler().playerInfoList;

        ArrayList<String> onlinePlayers = new ArrayList<>();
        for (Object playerInfo: onlinePlayerInfo) {
            if (playerInfo instanceof GuiPlayerInfo) {
                // char 167 = '§'
                String unformat = new ChatComponentText(((GuiPlayerInfo) playerInfo).name).getUnformattedTextForChat();
                if (unformat.startsWith((String.valueOf((char) 167)))) {
                    unformat = unformat.substring(2);
                }
                onlinePlayers.add(unformat);
            }
        }
        return onlinePlayers;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "spoof";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/spoof <name> <change> OR /spoof <name-to-unspoof>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            ArrayList<String> onlinePlayers = getOnlinePlayerNames();
            return getListOfStringsMatchingLastWord(args, onlinePlayers.toArray(new String[0]));

           //  return getListOfStringsMatchingLastWord(args, String.valueOf(onlinePlayers));
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // System.out.println("SpoofNick: " + Arrays.toString(args));
        if (args.length == 2) {
            try {
                SpoofNickService.initSpoof(args[0], args[1]);
            } catch (ExecutionException | InterruptedException e) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Some error occurred (it probably couldn't find player named " + EnumChatFormatting.DARK_RED + args[1] + "for some reason). Check your logs." + EnumChatFormatting.RESET));
                e.printStackTrace();
                return;
            }
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Spoofing " + args[0] + " as " + args[1] + "..." + EnumChatFormatting.RESET));
        }  else if (args.length == 1) {
            SpoofNickService.removeSpoof(args[0]);
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + "Removing spoof for " + args[0] + EnumChatFormatting.RESET));
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + this.getCommandUsage(sender) + EnumChatFormatting.RESET));
        }
    }


}
