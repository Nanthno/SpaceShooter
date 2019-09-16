package src.main.java.graphics;

import src.main.java.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class StatusPanel extends JPanel {

    int healthDisplayValue = 0;
    int heatDisplayValue = 0;

    static final int healthWidth = 200;
    static final int healthHeight = 10;
    static final Color healthFill = new Color(0, 230, 0);
    static final Color healthBack = new Color(200, 0, 0);

    static final int chargeWidth = 200;
    static final int chargeHeight = 10;
    static final Color[] fillColors = new Color[]{
            new Color(255, 0, 0),
            new Color(255, 128, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 200, 255)};
    static final int[] fillValues = new int[]{0, 50, 100, 150, 200, Integer.MAX_VALUE};
    static final Color chargeFill = new Color(0, 200, 255);
    static final Color chargeBack = new Color(70, 70, 70);

    protected BufferedImage drawHorizontalStatus() {

        int score = Controller.getScore();
        int health = Controller.getHealth();
        int maxHealth = Controller.getMaxHealth();
        int charge = Controller.getCharge();
        int maxCharge = Controller.getMaxCharge();


        updateDisplayValues(health, 0);

        BufferedImage panelImg = ImageUtil.copyImage(GraphicsManager.horizontalStatusBar);
        Graphics g = panelImg.getGraphics();

        // place the health and heat bars on the proper positions on the status panel
        BufferedImage healthBar = drawHorizontalBar(healthWidth, healthHeight, healthFill, healthBack,
                maxHealth, healthDisplayValue);
        g.drawImage(healthBar, 16, 16, null);

        BufferedImage chargeBar = drawChargeBar(charge, maxCharge);
        g.drawImage(chargeBar, 300, 16, null);

        BufferedImage scoreValue = ImageUtil.stringToImage(String.valueOf(score), chargeFill);
        g.drawImage(scoreValue, 48, 40, null);

        return panelImg;
    }

    private BufferedImage drawChargeBar(int charge, int maxFillValue) {
        double ratio = chargeWidth/maxFillValue;
        BufferedImage bar = new BufferedImage(chargeWidth, chargeHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = bar.getGraphics();
        g.setColor(chargeBack);
        g.fillRect(0,0,chargeWidth, chargeHeight);
        for(int i = 0; fillValues[i] <= charge; i++) {
            int value = fillValues[i];
            g.setColor(fillColors[i]);

            int startX = (int)(value*ratio);
            int endX = (int)(charge*ratio);
            g.fillRect(startX, 0, endX - startX,  chargeHeight);
        }

        g.dispose();
        return bar;
    }

    private BufferedImage drawHorizontalBar(int width, int height, Color fillColor, Color backColor,
                                            int maxValue, int displayValue) {

        BufferedImage bar = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = bar.getGraphics();
        g.setColor(backColor);
        g.fillRect(0, 0, width, height);
        g.setColor(fillColor);
        g.fillRect(0, 0, (int) (displayValue * (width * 1.0 / maxValue)), height);

        g.dispose();
        return bar;
    }


    private void updateDisplayValues(int health, int heat) {

        if (healthDisplayValue > health) {
            healthDisplayValue -= 2;
        } else if (healthDisplayValue < health) {
            healthDisplayValue++;
        }

        heatDisplayValue = heat;
    }
}
