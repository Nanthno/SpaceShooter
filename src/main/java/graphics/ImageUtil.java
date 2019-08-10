package src.main.java.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageUtil {

    static BufferedImage[] loadAnimation(String directory) throws NullPointerException{
        System.out.println("loading animation from " + directory);
        File folder = new File(directory);
        File[] frameFiles = folder.listFiles();
        if(frameFiles == null) {
            throw new NullPointerException("tried to load animation from " + directory + " but does not exist");
        }
        Arrays.sort(frameFiles);
        System.out.println(Arrays.toString(frameFiles));

        BufferedImage[] animation = new BufferedImage[frameFiles.length];

        for (int i = 0; i < frameFiles.length; i++) {
            animation[i] = loadImage(frameFiles[i]);
        }

        return animation;

    }

    static BufferedImage[] loadAnimation(String directory, int alpha) {
        File folder = new File(directory);
        File[] frameFiles = folder.listFiles();

        Arrays.sort(frameFiles);

        BufferedImage[] animation = new BufferedImage[frameFiles.length];

        for (int i = 0; i < frameFiles.length; i++) {
            BufferedImage image = loadImage(frameFiles[i]);
            image = setAlpha(alpha, image);
            animation[i] = image;
        }

        return animation;

    }

    public static BufferedImage setAlpha(int alpha, BufferedImage image) {
        alpha %= 0xff;
        for (int cx=0;cx<image.getWidth();cx++) {
            for (int cy=0;cy<image.getHeight();cy++) {
                int color = image.getRGB(cx, cy);

                int mc = (alpha << 24) | 0x00ffffff;
                int newcolor = color & mc;
                image.setRGB(cx, cy, newcolor);

            }

        }

        return image;
    }

    static BufferedImage loadImage(File file) {

        try {
            BufferedImage img = ImageIO.read(file);
            return img;
        } catch (IOException e) {
            System.out.println("Failed to load image at " + file);
            e.printStackTrace();
            return null;
        }
    }

    static BufferedImage loadImage(String f) {

        File file = new File(f);

        return loadImage(file);
    }

    // clones an image
    // from https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    // code from https://stackoverflow.com/questions/18800717/convert-text-content-to-image
    static BufferedImage stringToImage(String string, Color textColor) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(string);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(textColor);
        g2d.drawString(string, 0, fm.getAscent());
        g2d.dispose();


        return img;

    }
    // code adapted from http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Copy_Join_two_buffered_image_into_one_image_side_by_side.htm
    public static BufferedImage joinImages(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth() + img2.getWidth();
        int height = Math.max(img1.getHeight(), img2.getHeight());
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth(), 0);
        g2.dispose();
        return newImage;
    }
}
