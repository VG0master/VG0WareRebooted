import com.google.gson.Gson;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

public class Tanorians {
    private ArrayList<Tanorian> tanorians;

    public Tanorians(Tanorian[] list) {
        this.tanorians = new ArrayList<>(Arrays.asList(list));
    }

    public Tanorian find(BufferedImage image) {
        for (Tanorian t : tanorians) {
            if (t.getWhiteCount() != getWhitePixels(image)) {
                continue;
            }
            //This is where we check the image if we want to.
            return t;
        }
        return null;
    }

    private int getWhitePixels(BufferedImage image) {
        ArrayList<Color> allColors = new ArrayList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3)), true));
            allColors.add(new Color(image.getRGB(x, (int) Math.floor(image.getHeight() /3) * 2), true));
        }
        int c =0;
        for (Color color : allColors) {
            if (color.equals(Color.white)) {
                c++;
            }
        }
        return c;
    }

    public void addTanorian(Tanorian t) throws IOException {
        Gson gson = new Gson();
        tanorians.add(t);
        Writer writer = new FileWriter("data.json");
        gson.toJson(tanorians, writer);
        writer.close();

    }
}
