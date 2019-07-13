package src.main.java.graphics;

import src.main.java.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class StatusPanel extends JPanel {

    int healthDisplayValue = 0;
    int heatDisplayValue = 0;

    static final int healthWidth = 8;
    static final int healthHeight = 170;
    static final Color healthFill = new Color(0, 230, 0);
    static final Color healthBack = new Color(200, 0, 0);

    int heatWidth = 8;
    int heatHeight = 101;
    static final Color heatFill = new Color(255, 0, 0);
    static final Color heatBack = new Color(0, 200, 255);

    static final int chargeWidth = 8;
    static final int chargeHeight = 100;
    static final Color chargeFill = new Color(0, 200, 255);
    static final Color chargeBack = new Color(255, 0, 0);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage panelImg = drawStatus();
        g.drawImage(panelImg, 0, 0, this);
    }

    protected BufferedImage drawStatus() {

        int score = Controller.getScore();
        int health = Controller.getPlayerHealth();
        int maxHealth = Controller.getPlayerMaxHealth();
        int heat = Controller.getPlayerHeat();
        int maxHeat = Controller.getPlayerMaxHeat();
        int charge = Controller.getCharge();
        int maxCharge = Controller.getMaxCharge();


        updateDisplayValues(health, heat);

        BufferedImage panelImg = ImageUtil.copyImage(GraphicsManager.statusBars);
        Graphics g = panelImg.getGraphics();

        // place the health and heat bars on the proper positions on the status panel
        BufferedImage healthBar = drawBar(healthWidth, healthHeight, healthFill, healthBack,
                maxHealth, healthDisplayValue);
        g.drawImage(healthBar, 9, 31, null);

        BufferedImage heatBar = drawBar(heatWidth, heatHeight, heatFill, heatBack,
                maxHeat, heatDisplayValue);
        g.drawImage(heatBar, 47, 31, null);

        BufferedImage chargeBar = drawBar(chargeWidth, chargeHeight, chargeFill, chargeBack,
                maxCharge, charge);
        g.drawImage(chargeBar, 9, 250, null);

        BufferedImage scoreValue = ImageUtil.stringToImage(String.valueOf(score), chargeFill);
        g.drawImage(scoreValue, 7, 600, null);

        return panelImg;
    }


    private BufferedImage drawBar(int width, int height, Color fillColor, Color backColor,
                                  int maxValue, int displayValue) {

        BufferedImage bar = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = bar.getGraphics();
        g.setColor(fillColor);
        g.fillRect(0, 0, width, height);
        g.setColor(backColor);
        g.fillRect(0, 0, width, (int) ((maxValue - displayValue) * (height * 1.0 / maxValue)));

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
