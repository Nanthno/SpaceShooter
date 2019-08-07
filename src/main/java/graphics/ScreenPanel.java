package src.main.java.graphics;

import src.main.java.Controller;
import src.main.java.GameState;
import src.main.java.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenPanel extends JPanel {

    StatusPanel statusPanel;
    GamePanel gamePanel;
    MenuPanel menuPanel;
    HighScorePanel highScorePanel;

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

    int[] playerShake = new int[]{0,0};
    int maxPlayerShake = 10;

    public ScreenPanel() {
        statusPanel = new StatusPanel();
        gamePanel = new GamePanel();
        menuPanel = new MenuPanel();
        highScorePanel = new HighScorePanel();
        backgroundImgWidth = GraphicsManager.background.getWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameState state = Controller.getGameState();
        BufferedImage screenshot;
        g.drawImage(makeBackground(), 0, 0, null);
        if (state == GameState.MENU)
            screenshot = drawMenuScreen();
        else if (state == GameState.PLAYING)
            screenshot = drawPlayingScreen();
        else
            screenshot = drawHighScoreScreen();
        g.drawImage(screenshot, 0, 0, null);
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

        BufferedImage mergedImages = new BufferedImage(Globals.screenWidth+status.getWidth(),Globals.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mergedImages.getGraphics();

        g.drawImage(game, screenXShake +status.getWidth(), screenYShake, null);
        g.drawImage(status, 0,0, null);

        g.dispose();

        return mergedImages;

    }

    private BufferedImage drawHighScoreScreen() {
        return highScorePanel.drawScreenshot();
    }

    private BufferedImage makeBackground() {
        BufferedImage newBackground = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newBackground.getGraphics();

        currentBackgroundX -= backgroundScrollRate;
        if (currentBackgroundX <= -1 * backgroundImgWidth) {
            currentBackgroundX += backgroundImgWidth;
        }

        g.drawImage(GraphicsManager.background, currentBackgroundX, 0, null);

        if (backgroundImgWidth + currentBackgroundX <= GraphicsManager.getWidth()) {
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
        playerShake = new int[]{playerShake[0]+x, playerShake[1]+y};
    }

}

