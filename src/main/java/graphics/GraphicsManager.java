package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyType;

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
    static BufferedImage[] enemyBasicImages;
    static BufferedImage[] enemyFuelImages;
    static BufferedImage[] enemyAgileImages;
    static BufferedImage[] enemyShielderImages;
    static BufferedImage[] enemyShieldImages;
    static BufferedImage[] playerImages;
    static BufferedImage[] playerBulletImages;
    static BufferedImage[] laserBlastImages;
    static BufferedImage[] smallExplosionImages;
    static BufferedImage[] fuelExplosionImages;
    static BufferedImage[] mediumExplosionImages;
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
        enemyBasicImages = ImageUtil.loadAnimation("images/enemyBasic");
        enemyFuelImages = ImageUtil.loadAnimation("images/enemyFuel");
        enemyAgileImages = ImageUtil.loadAnimation("images/enemyAgile");
        enemyShielderImages = ImageUtil.loadAnimation("images/enemyShielder");
        enemyShieldImages = ImageUtil.loadAnimation("images/enemyShield", 100);
        playerImages = ImageUtil.loadAnimation("images/player");
        playerBulletImages = ImageUtil.loadAnimation("images/playerBullet");
        laserBlastImages = ImageUtil.loadAnimation("images/laserBlast");
        smallExplosionImages = ImageUtil.loadAnimation("images/smallExplosion");
        fuelExplosionImages = ImageUtil.loadAnimation("images/fuelExplosion");
        mediumExplosionImages = ImageUtil.loadAnimation("images/mediumExplosion");

        Globals.addToEnemyShipMaxFrames(EnemyType.BASIC, enemyBasicImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.FUEL, enemyFuelImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.AGILE, enemyAgileImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.SHIELDER, enemyShielderImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.SHIELD, enemyShieldImages.length);

        Globals.setPlayerMaxFrames(playerImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.BULLET, playerBulletImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.LASER_BLAST, laserBlastImages.length);
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
