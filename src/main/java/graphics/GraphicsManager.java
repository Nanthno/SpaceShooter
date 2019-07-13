package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class GraphicsManager {

    boolean drawDebug = true;


    // size of the screen
    static int GAME_WIDTH = Globals.screenWidth;
    static int GAME_HEIGHT = Globals.screenHeight;
    static int STATUS_WIDTH = 64;
    static int WIDTH = GAME_WIDTH + STATUS_WIDTH;
    static int HEIGHT = Globals.screenHeight;

    JFrame frame;

    GamePanel gamePanel;
    // panels for showing the state of the ship, gun overheat, etc
    StatusPanel statusPanel;
    //BarPanel healthPanel;
    //BarPanel heatPanel;

    // images
    BufferedImage background;
    BufferedImage statusBars;
    BufferedImage enemyBasicImage;
    BufferedImage enemyFuelImage;
    BufferedImage enemyAgileImage;
    BufferedImage playerImg;
    BufferedImage playerBullet;
    BufferedImage laserBlast;
    BufferedImage[] smallExplosion;
    BufferedImage[] fuelExplosion;
    BufferedImage[] mediumExplosion;

    public GraphicsManager() {

        // load the images for the game
        loadImages();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Space Shooter");
        frame.setSize(WIDTH, HEIGHT);

        gamePanel = new GamePanel();
        frame.add(gamePanel, BorderLayout.CENTER);

        statusPanel = new StatusPanel();
        statusPanel.setPreferredSize(new Dimension(STATUS_WIDTH, HEIGHT));
        frame.add(statusPanel, BorderLayout.WEST);

        // makes the frame visible
        frame.setVisible(true);
    }


    public void drawScreen() {

        gamePanel.removeAll();
        gamePanel.validate();
        gamePanel.repaint();

        statusPanel.removeAll();
        statusPanel.validate();
        statusPanel.repaint();

    }

    // loads the images for the game
    void loadImages() {
        background = ImageUtil.loadImage("images/space.png");
        statusBars = ImageUtil.loadImage("images/statusPanel.png");
        enemyBasicImage = ImageUtil.loadImage("images/enemySwarm.png");
        enemyFuelImage = ImageUtil.loadImage("images/enemyFuelShip.png");
        enemyAgileImage = ImageUtil.loadImage("images/enemyAgile.png");
        playerImg = ImageUtil.loadImage("images/playerLarge.png");
        playerBullet = ImageUtil.loadImage("images/playerBullet.png");
        laserBlast = ImageUtil.loadImage("images/LaserBlast.png");
        smallExplosion = ImageUtil.loadAnimation("images/smallExplosion");
        fuelExplosion = ImageUtil.loadAnimation("images/fuelExplosion");
        mediumExplosion = ImageUtil.loadAnimation("images/mediumExplosion");

    }


    public static int getWidth() {
        return WIDTH;
    }

    class StatusPanel extends JPanel {

        int healthDisplayValue = 0;
        int heatDisplayValue = 0;

        int healthWidth = 8;
        int healthHeight = 170;
        Color healthFill = new Color(0, 230, 0);
        Color healthBack = new Color(200, 0, 0);

        int heatWidth = 8;
        int heatHeight = 101;
        Color heatFill = new Color(255, 0, 0);
        Color heatBack = new Color(0, 200, 255);

        int chargeWidth = 8;
        int chargeHeight = 100;
        Color chargeFill = new Color(0, 200, 255);
        Color chargeBack = new Color(255, 0, 0);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage panelImg = drawStatus();
            g.drawImage(panelImg, 0, 0, this);
        }

        private BufferedImage drawStatus() {

            int score = Controller.getScore();
            int health = Controller.getPlayerHealth();
            int maxHealth = Controller.getPlayerMaxHealth();
            int heat = Controller.getPlayerHeat();
            int maxHeat = Controller.getPlayerMaxHeat();
            int charge = Controller.getCharge();
            int maxCharge = Controller.getMaxCharge();


            updateDisplayValues(health, heat);

            BufferedImage panelImg = ImageUtil.copyImage(statusBars);
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

    class GamePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage screenshot = drawScreenshot();
            g.drawImage(screenshot, 0, 0, this);

        }

        private BufferedImage drawScreenshot() {
            BufferedImage screenshot = ImageUtil.copyImage(background);

            // draw enemy ships
            ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
            Graphics g = screenshot.getGraphics();
            for (EnemyShip enemy : enemyShips) {
                if (enemy.getType() == EnemyType.BASIC)
                    g.drawImage(enemyBasicImage, enemy.getx(), enemy.gety(), null);
                else if (enemy.getType() == EnemyType.FUEL)
                    g.drawImage(enemyFuelImage, enemy.getx(), enemy.gety(), null);
                else if (enemy.getType() == EnemyType.AGILE)
                    g.drawImage(enemyAgileImage, enemy.getx(), enemy.gety(), null);
            }

            // draw player bullets
            ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
            for (PlayerBullet b : pb) {
                g.drawImage(playerBullet, (int) b.getx(), (int) b.gety(), null);
            }

            // draw explosions
            ArrayList<Explosion> exp = Controller.getExplosions();
            for (int i = 0; i < exp.size(); i++) {
                Explosion e = exp.get(i);
                if (e.getExpType() == 0) {
                    g.drawImage(smallExplosion[e.getStage()], e.getx(), e.gety(), null);
                }
                if (e.getExpType() == 1) {
                    g.drawImage(fuelExplosion[e.getStage()], e.getx(), e.gety(), null);
                }
                if (e.getExpType() == 2) {
                    g.drawImage(mediumExplosion[e.getStage()], e.getx(), e.gety(), null);
                }
            }


            LaserBlast blast = Controller.getLaserBlast();
            if (blast != null)
                g.drawImage(laserBlast, blast.getx(), 0, null);

            // draw player ship
            PlayerShip ship = Controller.getPlayerShip();
            g.drawImage(playerImg, (int) ship.getx(), (int) ship.gety(), null);

            /*if (drawDebug) {
                BufferedImage debugImage = drawDebugOverlay();
                g.drawImage(debugImage, 0, 0, null);
            }*/

            // at end
            g.dispose();

            return screenshot;
        }

/*
        BufferedImage drawDebugOverlay() {
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            Map<int[], Double> holeMap = Controller.getDensityMap().getHoles();

            int gameScreenXOffset = WIDTH - GAME_WIDTH;

            for (int[] i : holeMap.keySet()) {
                double weight = holeMap.get(i);

                int maxWeight = 1000; // magic number by which weight is scaled into alpha


                Color c = new Color(0, 0, 200); //(int)(weight/maxWeight * 255));

                BufferedImage holeOverlay = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
                Graphics2D overlayG = holeOverlay.createGraphics();
                overlayG.setStroke(new BasicStroke(10));
                overlayG.drawOval(0, 0, 20, 20);
                overlayG.dispose();

                g.drawImage(holeOverlay, i[0] + gameScreenXOffset, i[1], null);

            }
            Map<int[], Double> densityMap = Controller.getDensityMap().getDensity();

            for (int[] i : densityMap.keySet()) {
                double weight = densityMap.get(i);

                int maxWeight = 1000; // magic number by which weight is scaled into alpha


                Color c = new Color(0, 200, 0); //(int)(weight/maxWeight * 255));

                BufferedImage holeOverlay = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
                Graphics2D overlayG = holeOverlay.createGraphics();
                overlayG.setStroke(new BasicStroke(10));
                overlayG.drawOval(0, 0, 20, 20);
                overlayG.dispose();

                g.drawImage(holeOverlay, i[0] + gameScreenXOffset, i[1], null);

            }

            g.dispose();

            return img;
        }*/

    }


}
