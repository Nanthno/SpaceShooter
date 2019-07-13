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

    GamePanel gamePanel;
    // panels for showing the state of the ship, gun overheat, etc
    StatusPanel statusPanel;
    ScreenPanel screenPanel;

    // images
    protected static BufferedImage background;
    protected static BufferedImage statusBars;
    protected static BufferedImage enemyBasicImage;
    protected static BufferedImage enemyFuelImage;
    protected static BufferedImage enemyAgileImage;
    protected static BufferedImage playerImg;
    protected static BufferedImage playerBullet;
    protected static BufferedImage laserBlast;
    protected static BufferedImage[] smallExplosion;
    protected static BufferedImage[] fuelExplosion;
    protected static BufferedImage[] mediumExplosion;

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


}
