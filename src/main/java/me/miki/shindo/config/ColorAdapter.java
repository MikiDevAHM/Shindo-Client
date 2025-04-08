package me.miki.shindo.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("red").value(color.getRed());
        out.name("green").value(color.getGreen());
        out.name("blue").value(color.getBlue());
        out.name("alpha").value(color.getAlpha());
        out.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        in.beginObject();
        int red = 0, green = 0, blue = 0, alpha = 255; // Valores padrão
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "red":
                    red = in.nextInt();
                    break;
                case "green":
                    green = in.nextInt();
                    break;
                case "blue":
                    blue = in.nextInt();
                    break;
                case "alpha":
                    alpha = in.nextInt();
                    break;
            }
        }
        in.endObject();
        return new Color(red, green, blue, alpha);
    }
}
