package me.preciouso.spoofnick.render;

import me.preciouso.spoofnick.service.SpoofStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.GuiIngameForge;

import java.util.Collection;
import java.util.Iterator;

public class GuiRender extends GuiIngameForge {
    public GuiRender(Minecraft mc) {
        super(mc);
    }

    @Override
    protected void func_96136_a(ScoreObjective scoreObjective, int p_96136_2_, int p_96136_3_, FontRenderer fontRenderer) {
        Scoreboard scoreboard = scoreObjective.getScoreboard();
        Collection<Score> collection = scoreboard.func_96534_i(scoreObjective);

        if (collection.size() <= 15) {
            int k = fontRenderer.getStringWidth(scoreObjective.getDisplayName());

            String s;
            for(Iterator<Score> iterator = collection.iterator(); iterator.hasNext(); k = Math.max(k, fontRenderer.getStringWidth(s))) {
                Score score = iterator.next();
                ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(score.getPlayerName());
                s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName() + ": " + score.getScorePoints());
                s = this.formatString(s);
            }

            int k1 = collection.size() * fontRenderer.FONT_HEIGHT;
            int l1 = p_96136_2_ / 2 + k1 / 3;
            byte b0 = 3;
            int i2 = p_96136_3_ - k - b0;
            int l = 0;

            for (Score score1 : collection) {
                ++l;
                ScorePlayerTeam scorePlayerTeam1 = scoreboard.getTeam(score1.getPlayerName());
                
                String s1 = ScorePlayerTeam.formatPlayerName(scorePlayerTeam1, score1.getPlayerName());
                s1 = this.formatString(s1);
                
                String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
                
                int i1 = l1 - l * fontRenderer.FONT_HEIGHT;
                int j1 = p_96136_3_ - b0 + 2;
                drawRect(i2 - 2, i1, j1, i1 + fontRenderer.FONT_HEIGHT, 1342177280); // func_73734_a
                fontRenderer.drawString(s1, i2, i1, 553648127); // withShadow ()
                fontRenderer.drawString(s2, j1 - fontRenderer.getStringWidth(s2), i1, 553648127);
                if (l == collection.size()) {
                    String s3 = scoreObjective.getDisplayName();
                    drawRect(i2 - 2, i1 - fontRenderer.FONT_HEIGHT - 1, j1, i1 - 1, 1610612736);
                    drawRect(i2 - 2, i1 - 1, j1, i1, 1342177280);
                    fontRenderer.drawString(s3, i2 + k / 2 - fontRenderer.getStringWidth(s3) / 2, i1 - fontRenderer.FONT_HEIGHT, 553648127);
                }
            }
        }
    }

    private String formatString(String str) {
        str = SpoofStringHelper.spoofString(str);
        str = removeUselessCodes(str);
        return str;
    }

    private String removeUselessCodes(String str) {
        String stringRemovedCodes = "";
        boolean waitingForCode = false;
        boolean placingCode = false;
        char previousColor = 'r';
        char currentColor = 'r';
        char lastColor = 'r';

        for(int i = 0; i < str.length(); ++i) {
            char thisChar = str.charAt(i);
            if (thisChar == 167) {
                waitingForCode = true;
            } else if (waitingForCode) {
                lastColor = currentColor;
                waitingForCode = false;
                placingCode = true;
                currentColor = thisChar;
            } else {
                if (placingCode && previousColor != currentColor) {
                    if (currentColor == 'l' || currentColor == 'm' || currentColor == 'n' || currentColor == 'o') {
                        stringRemovedCodes = stringRemovedCodes + "§" + lastColor;
                    }

                    previousColor = currentColor;
                    stringRemovedCodes = stringRemovedCodes + "§" + currentColor;
                    placingCode = false;
                }

                stringRemovedCodes = stringRemovedCodes + thisChar;
            }
        }

        return stringRemovedCodes;
    }
}
