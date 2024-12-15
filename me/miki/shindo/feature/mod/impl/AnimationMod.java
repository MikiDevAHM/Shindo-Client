package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class AnimationMod extends Mod {

    public AnimationMod() {
        super(
                "Animation",
                "1.7 Animations in 1.8.",
                Type.Visual
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Block Animation", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Eat/Drink Animation", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Bow Animation", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Fishing Rod", this, true));
    }

    @EventTarget
    public void onAnimation(ClientTickEvent e) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (Shindo.getInstance().getModManager().getMod("Animation").isToggled() && heldItem != null) {
            if (Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Block Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.BLOCK) {
                attemptSwing();
            } else if (Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Eat/Drink Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.DRINK) {
                attemptSwing();
            } else if (Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Bow Animation").isCheckToggled() && heldItem.getItemUseAction() == EnumAction.BOW) {
                attemptSwing();
            }
        }
    }

    /**
     * Swings the player's arm if you're holding the attack and use item keys at the same time and looking at a block.
     */
    private void attemptSwing() {
        if (mc.thePlayer.getItemInUseCount() > 0) {
            final boolean mouseDown = mc.gameSettings.keyBindAttack.isKeyDown() &&
                    mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                forceSwingArm();
            }
        }
    }

    /**
     * Forces the player to swing their arm.
     */
    private void forceSwingArm() {
        EntityPlayerSP player = mc.thePlayer;
        int swingEnd = player.isPotionActive(Potion.digSpeed) ?
                (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (player.isPotionActive(Potion.digSlowdown) ?
                (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
            player.swingProgressInt = -1;
            player.isSwingInProgress = true;
        }
    }
}