package src.main.java;

import src.main.java.enemy.EnemyShip;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GraphicsManager {


    // size of the screen
    static int GAME_WIDTH = 1024;
    static int GAME_HEIGHT = 640;
    static int STATUS_WIDTH = 64;
    static int WIDTH = GAME_WIDTH + STATUS_WIDTH;
    static int HEIGHT = 640;

    JFrame frame;

    GamePanel gamePanel;
    // panels for showing the state of the ship, gun overheat, etc
    StatusPanel statusPanel;
    //BarPanel healthPanel;
    //BarPanel heatPanel;

    // images
    BufferedImage background;
    BufferedImage statusBars;
    BufferedImage enemy0;
    BufferedImage enemy1;
    BufferedImage playerImg;
    BufferedImage playerBullet;
    BufferedImage laserBlast;
    BufferedImage[] smallExplosion;
    BufferedImage[] fuelExplosion;

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

    public void drawScreen(int playerHealth, int playerHeat) {
        BufferedImage screenshot = drawScreenshot();

        gamePanel.removeAll();
        gamePanel.validate();
        gamePanel.repaint();

        statusPanel.removeAll();
        statusPanel.validate();
        statusPanel.repaint();

    }

    //UNUSED: USE THE ONE AT THE BOTTOM
    private BufferedImage drawScreenshot() {
        BufferedImage screenshot = copyImage(background);

        // draw enemy ships
        ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
        Graphics g = screenshot.getGraphics();
        for (EnemyShip e : enemyShips) {
            g.drawImage(enemy1, e.getx(), e.gety(), null);
        }

        // draw player bullets
        ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
        for (PlayerBullet b : pb) {
            g.drawImage(playerBullet, (int) b.getx(), (int) b.gety(), null);
        }

        // draw explosions
        ArrayList<Explosion> exp = Controller.getExplosions();
        for (Explosion e : exp) {
            g.drawImage(smallExplosion[e.getStage()], e.getx(), e.gety(), null);
        }


        // draw player ship
        PlayerShip ship = Controller.getPlayerShip();
        //g.drawImage(playerImg, (int)ship.getx(), (int)ship.gety(), null);


        // at end
        g.dispose();

        return screenshot;

    }


    // loads the images for the game
    void loadImages() {
        background = loadImage("images/space.png");
        statusBars = loadImage("images/statusPanel.png");
        enemy0 = loadImage("images/enemySwarm.png");
        enemy1 = loadImage("images/enemyFuelShip.png");
        playerImg = loadImage("images/playerLarge.png");
        playerBullet = loadImage("images/playerBullet.png");
        laserBlast = loadImage("images/LaserBlast.png");

        smallExplosion = new BufferedImage[11];
        for (int i = 0; i < smallExplosion.length; i++) {
            smallExplosion[i] = loadImage("images/smallExplosion/exp" + i + ".png");
        }
        fuelExplosion = new BufferedImage[8];
        for (int i = 0; i < fuelExplosion.length; i++) {
            fuelExplosion[i] = loadImage("images/fuelExplosion/exp" + i + ".png");
        }

    }

    static BufferedImage loadImage(String f) {

        File file = new File(f);
        BufferedImage img;

        try {
            img = ImageIO.read(file);
            return img;
        } catch (IOException e) {
            System.out.println("Failed to load image at " + file);
            e.printStackTrace();
            return null;
        }
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

    class BarPanel extends JPanel {
        private BufferedImage bar;

        int maxValue;
        int barValue;
        int displayValue;

        int width;
        int height;

        Color backColor;
        Color fillColor;

        boolean vertical;

        public BarPanel(int maxBar, int barVal, int w, int h, Color b, Color f, boolean vert) {
            this.maxValue = maxBar;
            this.barValue = barVal;
            displayValue = barValue;
            width = w;
            height = h;
            backColor = b;
            fillColor = f;
            vertical = vert;
        }

        void setValue(int val) {
            barValue = val;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            bar = drawBar();
            g.drawImage(bar, 0, 0, this);
        }

        private BufferedImage drawBar() {
            if (displayValue > barValue) {
                displayValue--;
            } else if (displayValue < barValue) {
                displayValue++;
            }

            bar = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics g = bar.getGraphics();
            g.setColor(backColor);
            g.fillRect(0, 0, width, height);
            g.setColor(fillColor);
            if (vertical) {
                g.fillRect(0, (maxValue - displayValue) * (height / maxValue), width, height);
            } else {
                g.fillRect((maxValue - displayValue) * (width / maxValue), 0, width, height);
            }

            g.dispose();
            return bar;
        }
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

            int health = Controller.getPlayerHealth();
            int maxHealth = Controller.getPlayerMaxHealth();
            int heat = Controller.getPlayerHeat();
            int maxHeat = Controller.getPlayerMaxHeat();
            int charge = Controller.getCharge();
            int maxCharge = Controller.getMaxCharge();


            updateDisplayValues(health, heat);

            BufferedImage panelImg = copyImage(statusBars);
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
	    /*
	    if(heatDisplayValue > heat) {
		heatDisplayValue--;
	    }
	    else if(heatDisplayValue < heat) {
		heatDisplayValue++;
		}*/
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
            BufferedImage screenshot = copyImage(background);

            // draw enemy ships
            ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
            Graphics g = screenshot.getGraphics();
            for (EnemyShip e : enemyShips) {
                if (e.getType() == 0)
                    g.drawImage(enemy0, e.getx(), e.gety(), null);
                else if (e.getType() == 1)
                    g.drawImage(enemy1, e.getx(), e.gety(), null);
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
                if (e.expType == 0) {
                    g.drawImage(smallExplosion[e.getStage()], e.getx(), e.gety(), null);
                }
                if (e.expType == 1) {
                    g.drawImage(fuelExplosion[e.getStage()], e.getx(), e.gety(), null);
                }
            }


            LaserBlast blast = Controller.getLaserBlast();
            if (blast != null)
                g.drawImage(laserBlast, blast.getx(), 0, null);

            // draw player ship
            PlayerShip ship = Controller.getPlayerShip();
            g.drawImage(playerImg, (int) ship.getx(), (int) ship.gety(), null);


            // at end
            g.dispose();

            return screenshot;
        }

    }
}