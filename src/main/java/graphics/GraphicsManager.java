package src.main.java.graphics;

import src.main.java.Controller;
import src.main.java.ExplosionType;
import src.main.java.Globals;
import src.main.java.ResourceFileType;
import src.main.java.enemy.EnemyType;
import src.main.java.weapons.WeaponType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class GraphicsManager {


    // size of the screen
    static int GAME_WIDTH = Globals.screenWidth;
    static int GAME_HEIGHT = Globals.screenHeight;
    static int STATUS_WIDTH = 64;
    static int WIDTH = GAME_WIDTH + STATUS_WIDTH;
    static int HEIGHT = Globals.screenHeight;

    static final String imageFolderPath = Globals.getResourceFile(ResourceFileType.IMAGE);

    JFrame frame;
    ScreenPanel screenPanel;

    // images
    static BufferedImage background;
    static BufferedImage statusBars;
    static Map<EnemyType, BufferedImage[]> enemyImages;
    static Map<WeaponType, BufferedImage[]> weaponImages;
    static Map<ExplosionType, BufferedImage[]> explosionImages;

    static BufferedImage[] playerImages;

    static BufferedImage imageNotFound = makeImageNotFound();
    static BufferedImage[] animationNotFound = new BufferedImage[]{imageNotFound};

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

    public static BufferedImage[] getAnimation(Object type) {
        if(type instanceof EnemyType)
            return enemyImages.get(type);

        if(type instanceof WeaponType)
            return weaponImages.get(type);

        if(type instanceof ExplosionType)
            return explosionImages.get(type);

        return animationNotFound;
    }
    public static BufferedImage getFrame(Object type, int frame) {
        BufferedImage[] animation = getAnimation(type);
        if(animation.length <= frame) {
            return imageNotFound;
        }

        return animation[frame];
    }

    // loads the images for the game
    void loadImages() {
        background = ImageUtil.loadImage(Globals.getResourceFile(ResourceFileType.IMAGE) + "spaceLong.png");
        statusBars = ImageUtil.loadImage(Globals.getResourceFile(ResourceFileType.IMAGE) + "statusPanel.png");

        playerImages = ImageUtil.loadAnimation(Globals.getResourceFile(ResourceFileType.IMAGE) + "player");

        enemyImages = loadEnemyImages();
        weaponImages = loadWeaponImages();
        explosionImages = loadExplosionImages();
    }

    private static Map<EnemyType, BufferedImage[]> loadEnemyImages() {
        Map<EnemyType, BufferedImage[]> imageMap = new HashMap<>();

        for(EnemyType type : EnemyType.values()) {
            BufferedImage[] animation = ImageUtil.loadAnimation(imageFolderPath + "enemy_" + type.toString().toLowerCase());
            imageMap.put(type, animation);
            Globals.addToEnemyShipMaxFrames(type, animation.length);
        }

        return imageMap;
    }
    private static Map<WeaponType, BufferedImage[]> loadWeaponImages() {
        Map<WeaponType, BufferedImage[]> imageMap = new HashMap<>();

        for(WeaponType type : WeaponType.values()) {
            BufferedImage[] animation = ImageUtil.loadAnimation(imageFolderPath + "weapon_" + type.toString().toLowerCase());
            imageMap.put(type, animation);
            Globals.addToWeaponMaxFrames(type, animation.length);
        }

        return imageMap;
    }
    private static Map<ExplosionType, BufferedImage[]> loadExplosionImages() {
        Map<ExplosionType, BufferedImage[]> imageMap = new HashMap<>();

        for(ExplosionType type : ExplosionType.values()) {
            BufferedImage[] animation = ImageUtil.loadAnimation(imageFolderPath + "explosion_" + type.toString().toLowerCase());
            imageMap.put(type, animation);
            Globals.addToExplosionMaxFrames(type, animation.length);
        }

        return imageMap;
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

    public static int getHeight() {
        return HEIGHT;
    }

    public void addScreenShake(int x, int y) {
        screenPanel.addScreenShake(x, y);
    }

    public void addScreenShake(int[] pos) {
        addScreenShake(pos[0], pos[1]);
    }

    public void addPlayerShake(int x, int y) {
        screenPanel.addPlayerShake(x, y);
    }
}
