package src.main.java.graphics;

import src.main.java.*;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsManager {


    // size of the screen
    static int GAME_WIDTH = Globals.screenWidth;
    static int GAME_HEIGHT = Globals.screenHeight;
    static int STATUS_WIDTH = 64;
    static int WIDTH = GAME_WIDTH + STATUS_WIDTH;
    static int HEIGHT = Globals.screenHeight;

    JFrame frame;
    ScreenPanel screenPanel;

    // images
    static BufferedImage background;
    static BufferedImage statusBars;
    static BufferedImage enemyBasicImage;
    static BufferedImage enemyFuelImage;
    static BufferedImage enemyAgileImage;
    static BufferedImage enemyShielderImage;
    static BufferedImage enemyShieldImage;
    static BufferedImage playerImg;
    static BufferedImage playerBullet;
    static BufferedImage laserBlast;
    static BufferedImage[] smallExplosion;
    static BufferedImage[] fuelExplosion;
    static BufferedImage[] mediumExplosion;
    static BufferedImage imageNotFound = makeImageNotFound();

    public GraphicsManager() {

        // load the images for the game
        loadImages();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Space Shooter");
        frame.setSize(WIDTH, HEIGHT);
        frame.addMouseListener(Controller.getMouseListener());

        screenPanel = new ScreenPanel();
        frame.add(screenPanel, BorderLayout.CENTER);

        // makes the frame visible
        frame.setVisible(true);
    }


    public void drawScreen() {

        screenPanel.removeAll();
        screenPanel.validate();
        screenPanel.repaint();


    }

    // loads the images for the game
    void loadImages() {
        background = ImageUtil.loadImage("images/space3.png");
        statusBars = ImageUtil.loadImage("images/statusPanel.png");
        enemyBasicImage = ImageUtil.loadImage("images/enemySwarm.png");
        enemyFuelImage = ImageUtil.loadImage("images/enemyFuelShip.png");
        enemyAgileImage = ImageUtil.loadImage("images/enemyAgile.png");
        enemyShielderImage = ImageUtil.loadImage("images/enemyShielder.png");
        playerImg = ImageUtil.loadImage("images/playerLarge.png");
        playerBullet = ImageUtil.loadImage("images/playerBullet.png");
        laserBlast = ImageUtil.loadImage("images/LaserBlast.png");
        smallExplosion = ImageUtil.loadAnimation("images/smallExplosion");
        fuelExplosion = ImageUtil.loadAnimation("images/fuelExplosion");
        mediumExplosion = ImageUtil.loadAnimation("images/mediumExplosion");


    }

    private static BufferedImage makeImageNotFound() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();
        g.setColor(new Color(255, 0, 200));
        g.fillRect(0, 0, 32, 32);
        g.dispose();
        return img;
    }

    public Point getFramePosition() {
        return frame.getLocationOnScreen();
    }

    public static int getWidth() {
        return WIDTH;
    }

    static int getHeight() {
        return HEIGHT;
    }

    static BufferedImage getBackgroundImage() {
        return background;
    }
}
