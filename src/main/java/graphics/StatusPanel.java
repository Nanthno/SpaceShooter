package src.main.java.graphics;

import src.main.java.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class StatusPanel extends JPanel {

    private int healthDisplayValue = 0;

    private static final int healthWidth = 200;
    private static final int healthHeight = 10;
    private static final Color healthFill = new Color(0, 230, 0);
    private static final Color healthBack = new Color(200, 0, 0);

    private static final int chargeWidth = 200;
    private static final int chargeHeight = 10;
    private static final Color[] fillColors = new Color[]{
            new Color(255, 0, 0),
            new Color(255, 128, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 200, 255)};
    private static final int[] fillValues = new int[]{0, 50, 100, 150, 200, Integer.MAX_VALUE};
    private static final Color chargeFill = new Color(0, 200, 255);
    private static final Color chargeBack = new Color(70, 70, 70);

    BufferedImage drawHorizontalStatus() {

        int score = Controller.getScore();
        int health = Controller.getHealth();
        int maxHealth = Controller.getMaxHealth();
        int charge = Controller.getCharge();
        int maxCharge = Controller.getMaxCharge();


        updateDisplayValues(health);

        BufferedImage panelImg = ImageUtil.copyImage(GraphicsManager.horizontalStatusBar);
        Graphics g = panelImg.getGraphics();

        // place the health and heat bars on the proper positions on the status panel
        BufferedImage healthBar = drawHorizontalBar(
                maxHealth, healthDisplayValue);
        g.drawImage(healthBar, 16, 16, null);

        BufferedImage chargeBar = drawChargeBar(charge, maxCharge);
        g.drawImage(chargeBar, 300, 16, null);

        BufferedImage scoreValue = ImageUtil.stringToImage(String.valueOf(score), chargeFill);
        g.drawImage(scoreValue, 48, 40, null);

        return panelImg;
    }

    private BufferedImage drawChargeBar(int charge, int maxFillValue) {
        BufferedImage bar = new BufferedImage(chargeWidth, chargeHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = bar.getGraphics();
        if (charge == maxFillValue) {
            g.setColor(chargeFill);
            g.fillRect(0, 0, chargeWidth, chargeHeight);
        } else {
            double ratio = chargeWidth / maxFillValue;
            g.setColor(chargeBack);
            g.fillRect(0, 0, chargeWidth, chargeHeight);
            for (int i = 0; fillValues[i] <= charge; i++) {
                int value = fillValues[i];
                g.setColor(fillColors[i]);

                int startX = (int) (value * ratio);
                int endX = (int) (charge * ratio);
                g.fillRect(startX, 0, endX - startX, chargeHeight);
            }
        }

        g.dispose();
        return bar;
    }

    private BufferedImage drawHorizontalBar(int maxValue, int displayValue) {

        BufferedImage bar = new BufferedImage(StatusPanel.healthWidth, StatusPanel.healthHeight, BufferedImage.TYPE_INT_RGB);

        Graphics g = bar.getGraphics();
        g.setColor(StatusPanel.healthBack);
        g.fillRect(0, 0, StatusPanel.healthWidth, StatusPanel.healthHeight);
        g.setColor(StatusPanel.healthFill);
        g.fillRect(0, 0, (int) (displayValue * (StatusPanel.healthWidth * 1.0 / maxValue)), StatusPanel.healthHeight);

        g.dispose();
        return bar;
    }


    private void updateDisplayValues(int health) {

        if (healthDisplayValue > health) {
            healthDisplayValue -= 2;
        } else if (healthDisplayValue < health) {
            healthDisplayValue++;
        }

    }
}
