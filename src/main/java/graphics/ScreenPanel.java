package src.main.java.graphics;

import src.main.java.Controller;
import src.main.java.GameState;
import src.main.java.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

class ScreenPanel extends JPanel {

    private final StatusPanel statusPanel;
    private final GamePanel gamePanel;
    private final MenuPanel menuPanel;

    private final static int gameWidth = GraphicsManager.getWidth();
    private final static int gameHeight = GraphicsManager.getHeight();
    private final static int screenHeight = GraphicsManager.HEIGHT;
    private double gameScale = 1;
    private final int frameWidth = 0;
    private final int frameHeight = 0;

    private boolean frameSizeChange = false;

    private int currentBackgroundX = 0;
    private static final int backgroundScrollRate = 1;
    private final int backgroundImgWidth;

    private int screenXShake = 0;
    private int screenYShake = 0;

    private int[] playerShake = new int[]{0, 0};

    public ScreenPanel() {
        statusPanel = new StatusPanel();
        gamePanel = new GamePanel();
        menuPanel = new MenuPanel();
        backgroundImgWidth = GraphicsManager.background.getWidth();
    }

    @Override
    protected void paintComponent(Graphics gameGraphics) {
        super.paintComponent(gameGraphics);

        GameState state = Controller.getGameState();

        BufferedImage backgroundImage = makeBackground();
        BufferedImage screenshot;
        if (state == GameState.MENU)
            screenshot = drawMenuScreen();
        else if (state == GameState.PLAYING)

            // see this? That try-catch below, that... that is awful. However, we are doing it anyway because it only exists to stop a pesky exception from
            // being printed a lot. I'm sorry to anyone who reads this code, including future me, for causing you pain.
            try {
                screenshot = drawPlayingScreen();
            } catch (ConcurrentModificationException e) {
                screenshot = drawPlayingScreen();
            }
        else
            try {
                screenshot = drawHighScoreScreen();
            } catch (NullPointerException e) {
                screenshot = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            } // no clue why this throws a null pointer exception at startup but eh
        Graphics g = backgroundImage.getGraphics();
        g.drawImage(screenshot, 0, 0, null);
        g.dispose();
        if (frameSizeChange) {
            double xScale = frameWidth * 1.0 / gameWidth;
            double yScale = frameHeight * 1.0 / gameHeight;
            gameScale = Math.min(xScale, yScale);

            frameSizeChange = false;
        }

        BufferedImage scaledScreenshot = new BufferedImage((int) (gameWidth * gameScale), (int) (gameHeight * gameScale), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledScreenshot.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, (int) gameScale * gameWidth, (int) gameScale * gameHeight, null);
        g2d.dispose();

        gameGraphics.drawImage(scaledScreenshot, 0, 0, null);

    }

    private BufferedImage drawMenuScreen() {
        return menuPanel.drawScreenshot();
    }

    private BufferedImage drawPlayingScreen() {
        int maxScreenShake = 12;
        int[] newScreenShake = updateShake(screenXShake, screenYShake, maxScreenShake);
        screenXShake = newScreenShake[0];
        screenYShake = newScreenShake[1];

        int maxPlayerShake = 10;
        playerShake = updateShake(playerShake, maxPlayerShake);

        BufferedImage statusBar = statusPanel.drawHorizontalStatus();
        BufferedImage game = gamePanel.drawGameScreenShot(playerShake);

        BufferedImage mergedImages = new BufferedImage(Globals.screenWidth, Globals.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mergedImages.getGraphics();

        g.drawImage(game, screenXShake, screenYShake, null);
        g.drawImage(statusBar, 0, Globals.gameHeight, null);

        g.dispose();

        return mergedImages;

    }

    private BufferedImage drawHighScoreScreen() {
        return HighScorePanel.drawScreenshot();
    }

    private BufferedImage makeBackground() {
        BufferedImage newBackground = new BufferedImage((int) (gameWidth * gameScale), (int) (screenHeight * gameScale), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newBackground.getGraphics();
        g.setColor(new Color(168, 168, 168));
        g.drawRect(0, 0, frameWidth, frameHeight);

        currentBackgroundX -= backgroundScrollRate;
        if (currentBackgroundX <= -1 * backgroundImgWidth) {
            currentBackgroundX += backgroundImgWidth;
        }

        g.drawImage(GraphicsManager.background, currentBackgroundX, 0, null);

        if (backgroundImgWidth + currentBackgroundX <= gameWidth) {
            g.drawImage(GraphicsManager.background, backgroundImgWidth + currentBackgroundX, 0, null);
        }

        return newBackground;
    }

    private int[] updateShake(int[] pos, int maxVal) {
        return updateShake(pos[0], pos[1], maxVal);
    }

    private int[] updateShake(int x, int y, int maxVal) {
        if (x > maxVal)
            x = maxVal;
        else if (x < -1 * maxVal)
            x = -1 * maxVal;

        if (y > maxVal)
            y = maxVal;
        else if (y < -1 * maxVal)
            y = -1 * maxVal;


        if (x > 1)
            x -= 2;
        else if (x < -1)
            x += 2;
        else
            x = 0;

        if (y > 1)
            y -= 2;
        else if (y < -1)
            y += 2;
        else
            y = 0;

        return new int[]{x, y};
    }

    void addScreenShake(int x, int y) {
        screenXShake += x;
        screenYShake += y;
    }

    void addPlayerShake(int x, int y) {
        playerShake = new int[]{playerShake[0] + x, playerShake[1] + y};
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Globals.screenWidth,
                Globals.screenHeight);
    }
}

