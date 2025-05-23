package me.miki.shindo.helpers;

import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.transferable.ImageTransferable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IOHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void copyStringToClipboard(String s) {
        StringSelection stringSelection = new StringSelection(s);
        getToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public static String getStringFromClipboard() {
        try {
            return getToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void copyImageToClipboard(Image image) {
        ImageTransferable imageSelection = new ImageTransferable(image);
        getToolkit().getSystemClipboard().setContents(imageSelection, null);
    }

    public static Image getImageFromClipboard() {
        try {
            return (Image) getToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.imageFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    private static Toolkit getToolkit() {
        return Toolkit.getDefaultToolkit();
    }

    public static ByteBuffer resourceToByteBuffer(ResourceLocation location) {

        try {
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(mc.getResourceManager().getResource(location).getInputStream());

            ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
            data.flip();

            return data;
        } catch (Exception e) {
            ShindoLogger.error("Failed to load resource", e);
        }

        return null;
    }

    public static ByteBuffer resourceToByteBuffer(File file) {

        try {
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));

            ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
            data.flip();

            return data;
        } catch (Exception e) {
            ShindoLogger.error("Failed to load resource", e);
        }

        return null;
    }
}
