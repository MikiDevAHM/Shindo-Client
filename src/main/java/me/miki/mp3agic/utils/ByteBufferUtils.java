package me.miki.mp3agic.utils;

import java.nio.ByteBuffer;

public final class ByteBufferUtils {

    private ByteBufferUtils() {
    }

    public static String extractNullTerminatedString(ByteBuffer bb) {
        int start = bb.position();

        byte[] buffer = new byte[bb.remaining()];

        bb.get(buffer);

        String s = new String(buffer);
        int nullPos = s.indexOf(0);

        s = s.substring(0, nullPos);

        bb.position(start + s.length() + 1);

        return s;
    }
}
