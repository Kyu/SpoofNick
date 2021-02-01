package me.preciouso.spoofnick.render;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.preciouso.spoofnick.SpoofNick;
import me.preciouso.spoofnick.SpoofPlayer;
import me.preciouso.spoofnick.service.SpoofNickService;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerRender extends RenderPlayer {

    @Override
    protected void bindEntityTexture(Entity entity) {
        if (entity instanceof EntityPlayer && SpoofNick.spoofedNames.containsKey(entity.getCommandSenderName())) {
            SpoofPlayer player = SpoofNick.spoofedNames.get(entity.getCommandSenderName());
            this.bindTexture(player.getSkin());
            ((EntityPlayer) entity).func_152111_bt();
            // this.bindTexture(player.getCape());
        } else {
            super.bindEntityTexture(entity);
        }
    }

    @Override
    protected void renderEquippedItems(AbstractClientPlayer player, float f1) {
        super.renderEquippedItems(player, f1);
        if (SpoofNick.spoofedNames.containsKey(player.getCommandSenderName())) {
            SpoofPlayer spoofPlayer = SpoofNick.spoofedNames.get(player.getCommandSenderName());
            if (spoofPlayer.getCape() != null) {
                player.func_152121_a(MinecraftProfileTexture.Type.CAPE, spoofPlayer.getCape());
                // Minecraft.getMinecraft().renderEngine.loadTexture(spoofPlayer.getCape(), Minecraft.getMinecraft().getTextureManager().getTexture(spoofPlayer.getCape()));
            }
        }

        if (SpoofNick.toRemove.contains(player.getCommandSenderName())) {
            player.func_152121_a(MinecraftProfileTexture.Type.CAPE, null);// new ResourceLocation("doesnotexistloljpeg"));
            SpoofNickService.totallyRemove(player.getCommandSenderName());
        }
        /*
        net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre event = new net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre(player, this, f1);
        if (SpoofNick.spoofedNames.containsKey(player.getCommandSenderName())) {
            SpoofPlayer spoofPlayer = SpoofNick.spoofedNames.get(player.getCommandSenderName());
            if (spoofPlayer.getCape() != null) {
                boolean flag = player.func_152122_n();
                flag = event.renderCape && flag;
                float f4;
                if (flag && !player.isInvisible() && !player.getHideCape())
                {
                    this.bindTexture(spoofPlayer.getCape());
                    GL11.glPushMatrix();
                    GL11.glTranslatef(0.0F, 0.0F, 0.125F);
                    double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * (double)f1 - (player.prevPosX + (player.posX - player.prevPosX) * (double)f1);
                    double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * (double)f1 - (player.prevPosY + (player.posY - player.prevPosY) * (double)f1);
                    double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * (double)f1 - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f1);
                    f4 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f1;
                    double d1 = (double) MathHelper.sin(f4 * (float)Math.PI / 180.0F);
                    double d2 = (double)(-MathHelper.cos(f4 * (float)Math.PI / 180.0F));
                    float f5 = (float)d4 * 10.0F;

                    if (f5 < -6.0F)
                    {
                        f5 = -6.0F;
                    }

                    if (f5 > 32.0F)
                    {
                        f5 = 32.0F;
                    }

                    float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
                    float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;

                    if (f6 < 0.0F)
                    {
                        f6 = 0.0F;
                    }

                    float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * f1;
                    f5 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * f1) * 6.0F) * 32.0F * f8;

                    if (player.isSneaking())
                    {
                        f5 += 25.0F;
                    }

                    GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                    this.modelBipedMain.renderCloak(0.0625F);
                    GL11.glPopMatrix();
                }
            }
        }


         */
        



    }

    @Override
    protected void renderLivingAt(EntityLivingBase entityLivingBase, double x, double y, double z) {
        super.renderLivingAt(entityLivingBase, x, y, z);

        if (entityLivingBase instanceof EntityPlayer && SpoofNick.spoofedNames.containsKey((entityLivingBase).getCommandSenderName())) {
            EntityPlayer entityPlayer = (EntityPlayer) entityLivingBase;
            entityPlayer.refreshDisplayName();
        }
    }
}