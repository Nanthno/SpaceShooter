package src.main.java.graphics;

import src.main.java.Controller;
import src.main.java.GameState;
import src.main.java.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

public class ScreenPanel extends JPanel {

    StatusPanel statusPanel;
    GamePanel gamePanel;
    MenuPanel menuPanel;
    HighScorePanel highScorePanel;

    final static int gameWidth = GraphicsManager.getWidth();
    final static int gameHeight = GraphicsManager.getHeight();
    double gameScale = 1;
    int frameWidth = 0;
    int frameHeight = 0;
    int gameXOrigin = 0;
    int gameYOrigin = 0;

    boolean frameSizeChange = false;

    private enum Button {
        NONE,
        PLAY,
        CREDITS,
        BACK,
        SOUND_SELECTION,
        MENU
    }

    int currentBackgroundX = 0;
    private static final int backgroundScrollRate = 1;
    int backgroundImgWidth;

    int screenXShake = 0;
    int screenYShake = 0;
    int maxScreenShake = 12;

    int[] playerShake = new int[]{0, 0};
    int maxPlayerShake = 10;

    public ScreenPanel() {
        statusPanel = new StatusPanel();
        gamePanel = new GamePanel();
        menuPanel = new MenuPanel();
        highScorePanel = new HighScorePanel();
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
            gameScale = xScale < yScale ? xScale : yScale;

            double drawGameWidth = gameWidth * gameScale;
            double drawGameHeight = gameHeight * gameScale;

            double xScaleDiff = frameWidth - drawGameWidth;
            double yScaleDiff = frameHeight - drawGameHeight;

            gameXOrigin = (int) (xScaleDiff / 2);
            gameYOrigin = (int) (yScaleDiff / 2);
            frameSizeChange = false;
        }

        BufferedImage scaledScreenshot = new BufferedImage((int) (gameWidth * gameScale), (int) (gameHeight * gameScale), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledScreenshot.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, (int) gameScale * gameWidth, (int) gameScale * gameHeight, null);
        g2d.dispose();

        gameGraphics.drawImage(scaledScreenshot, 0, 0, null);//gameXOrigin, gameYOrigin, null);
    }

    private BufferedImage drawMenuScreen() {
        return menuPanel.drawScreenshot();
    }

    private BufferedImage drawPlayingScreen() {
        int[] newScreenShake = updateShake(screenXShake, screenYShake, maxScreenShake);
        screenXShake = newScreenShake[0];
        screenYShake = newScreenShake[1];

        playerShake = updateShake(playerShake, maxPlayerShake);


        BufferedImage status = statusPanel.drawStatus();
        BufferedImage game = gamePanel.drawGameScreenShot(playerShake);
        //BufferedImage mergedImages = ImageUtil.joinImages(status, game);

        BufferedImage mergedImages = new BufferedImage(Globals.screenWidth + status.getWidth(), Globals.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mergedImages.getGraphics();

        g.drawImage(game, screenXShake + status.getWidth(), screenYShake, null);
        g.drawImage(status, 0, 0, null);

        g.dispose();

        return mergedImages;

    }

    private BufferedImage drawHighScoreScreen() {
        return highScorePanel.drawScreenshot();
    }

    private BufferedImage makeBackground() {
        BufferedImage newBackground = new BufferedImage((int) (gameWidth * gameScale), (int) (gameHeight * gameScale), BufferedImage.TYPE_INT_ARGB);
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


    // these are supposed to be used for scaling up the frame size but the scaling thing doesn't work
    public void setFrameHeight(int frameHeight) {
        if (frameHeight != this.frameHeight) {
            frameSizeChange = true;
            this.frameHeight = frameHeight;
        }
    }

    public void setFrameWidth(int frameWidth) {
        if (frameWidth != this.frameWidth) {
            frameSizeChange = true;
            this.frameWidth = frameWidth;
        }
    }
}

