package me.miki.shindo.features.patcher.impl.bugfix;

import me.miki.shindo.features.patcher.Patcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class PatcherBugFixer extends Patcher {

    /*
     * @Mixin(BlockFluidRenderer.class)
     *
     * @ModifyConstant( method = "renderFluid", constant = @Constant(floatValue = 0.001F))
     */
    public static float fixFluidStitching() {
        return  0.0F;
    }

    /*
     * @Mixin(targets = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace")
     *
     * @Redirect( method = "updateVertexBrightness", at = @At( value = "INVOKE", target = "Lnet/minecraft/block/Block;isTranslucent()Z"
     */
    public static boolean betterSmoothLighting(Block block) {
        return !block.isVisuallyOpaque() || block.getLightOpacity() == 0;
    }

    /*
     * @Mixin(CommandHandler.class)
     *
     * @ModifyArg( method = { "executeCommand", "getTabCompleteOptions"}, at @At(value "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
     */
    public static Object makeLowerCaseForGet(Object s) {
        if (s instanceof String) {
            return ((String) s).toLowerCase(Locale.ENGLISH);
        }
        return s;
    }

    /*
     * @ModifyArg(method = "registerCommand", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false), index = 0)
     */
    public static Object makeLowerCaseForPut(Object s) {
        if (s instanceof String) {
            return ((String) s).toLowerCase(Locale.ENGLISH);
        }
        return s;
    }

    /*
     * @ModifyArg(method = "getTabCompletionOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandBase;doesStringStartWith(Ljava/lang/String;Ljava/lang/String;)Z"), index = 0)
     */
    public static String makeLowerCaseForTabComplete(String s) {
        return s != null ? s.toLowerCase(Locale.ENGLISH) : null;
    }

    /*
     * @Mixin(Entity.class)
     *
     * @Redirect(method = "getBrightnessForRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isBlockLoaded(Lnet/minecraft/util/BlockPos;)Z"))
     */
    public static boolean alwaysReturnTrue(World world, BlockPos pos) {
        return true;
    }

    /*
     * @Mixin(Entity.class)
     *
     * @Shadow public boolean onGround;
     *
     * @Inject(method = "spawnRunningParticles", at = @At("HEAD"), cancellable = true)
     */
    public static void checkGroundState(Entity entity) {
        if (!entity.onGround) return;
    }

    /*
     * @Mixin(EntityRenderer.class)
     *
     * @Inject(
     *      method = "renderWorldPass",
     *      slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/util/EnumWorldBlockLayer;TRANSLUCENT:Lnet/minecraft/util/EnumWorldBlockLayer;")),
     *      at = @At(
     *          value = "INVOKE",
     *          target = "Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I",
     *          ordinal = 0
     *      )
     *  )
     */
    public static void enablePolygonOffset() {
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-0.325F, -0.325F);
    }

    /*
     * @Inject(
     *      method = "renderWorldPass",
     *      slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/util/EnumWorldBlockLayer;TRANSLUCENT:Lnet/minecraft/util/EnumWorldBlockLayer;")),
     *      at = @At(
     *          value = "INVOKE",
     *          target = "Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I",
     *          ordinal = 0,
     *          shift = At.Shift.AFTER
     *      )
     *  )
     */
    public static void disablePolygonOffset() {
        GlStateManager.disablePolygonOffset();
    }

    /*
     * @Mixin(EntityXPOrb.class)
     *
     * @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F"))
     */
    public static float lowerHeight(EntityPlayer player) {
        return (float) (player.getEyeHeight() / 2.0D);
    }

    /*
     * @Mixin(FontRenderer.class)
     * @Shadow protected abstract void resetStyles();
     * @Inject(method = "drawString(Ljava/lang/String;FFIZ)I",
     *      at = @At(
     *          value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I",
     *          ordinal = 0, shift = At.Shift.AFTER
     *      )
     *  )
     */
    public static void resetStyle(FontRenderer fr) {
        fr.resetStyles();
    }

    /*
     * @Mixin(GuiContainer.class)
     *
     * @Shadow private int dragSplittingButton;
     * @Shadow private int dragSplittingRemnant;
     *
     * @Inject(method = "updateDragSplitting", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"), cancellable = true)
     */
    public static void fixRemnants(GuiContainer container) {
        Minecraft mc = Minecraft.getMinecraft();
        if (container.dragSplittingButton == 2) {
            container.dragSplittingRemnant = mc.thePlayer.inventory.getItemStack().getMaxStackSize();
            return;
        }
    }

    /*
     * @Mixin(GuiGameOver.class)
     *
     * @Shadow private int enableButtonsTimer;
     *
     * @Inject(method = "initGui", at = @At("HEAD"))
     */
    public static void allowClickable(GuiGameOver guiGameOver) {
        guiGameOver.enableButtonsTimer = 0;
    }

    /*
     * @Mixin(GuiIngame.class)
     *
     * @ModifyConstant(method = "renderScoreboard", constant = @Constant(intValue = 553648127))
     */
    public static int fixTextBlending() {
        return -1;
    }

    /*
     * @Mixin(GuiNewChat.class)
     *
     * @Shadow
     * @Final
     * private Minecraft mc;
     *
     * @Shadow
     * public abstract int getLineCount();
     *
     * @Inject(method = "getChatComponent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiNewChat;scrollPos:I"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
     */
    public static void stopEventsOutsideWindow(int mouseX, int mouseY, ScaledResolution scaledresolution, int i, float f, int j, int k, int l, GuiNewChat gui) {
        Minecraft mc = Minecraft.getMinecraft();
        int line = k / mc.fontRendererObj.FONT_HEIGHT;
        if (line >= gui.getLineCount()) return ;
    }

    /*
     * @Mixin(GuiNewChat.class)
     *
     * @Redirect(method = "deleteChatLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ChatLine;getChatLineID()I"))
     */
    public static int checkIfChatLineIsNull(ChatLine instance) {
        if (instance == null) return -1;
        return instance.getChatLineID();
    }

    /*
     * @Mixin(GuiScreen.class)
     *
     * @Redirect(method = "handleKeyboardInput", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", remap = false))
     */
    public static boolean checkCharacter() {
        return Keyboard.getEventKey() == 0 && Keyboard.getEventCharacter() >= ' ' || Keyboard.getEventKeyState();
    }

    /*
     * @Mixin(targets = "net.minecraft.client.gui.GuiScreenOptionsSounds$Button")
     *
     * @Redirect(method = "mouseDragged(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;saveOptions()V"))
     */
    public static void cancelSaving(GameSettings instance) {
        // no-op
    }

    /*
     * @Inject(method = "mouseReleased(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"))
     */
    public static void save(int mouseX, int mouseY) {
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
}
