package src.main.java.graphics;

import javax.swing.*;

import src.main.java.Controller;
import src.main.java.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenPanel extends JPanel {

    StatusPanel statusPanel;
    GamePanel gamePanel;
    MenuPanel menuPanel;
    HighScorePanel highScorePanel;

    int currentBackgroundX = 0;
    private static final int backgroundScrollRate = 1;
    int backgroundImgWidth;

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
        BufferedImage status = statusPanel.drawStatus();
        BufferedImage game = gamePanel.drawGameScreenShot();
        BufferedImage mergedImages = ImageUtil.joinImages(status, game);
        return mergedImages;

    }

    private BufferedImage drawHighScoreScreen() {
        return null;//highScorePanel.drawScreenshot();
    }

    private BufferedImage makeBackground() {
        BufferedImage newBackground = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = newBackground.getGraphics();

        currentBackgroundX -= backgroundScrollRate;
        if(currentBackgroundX <= -1*backgroundImgWidth) {
            currentBackgroundX += backgroundImgWidth;
        }

        g.drawImage(GraphicsManager.background, currentBackgroundX, 0, null);

        if(backgroundImgWidth + currentBackgroundX <= GraphicsManager.getWidth()) {
            g.drawImage(GraphicsManager.background, backgroundImgWidth + currentBackgroundX, 0, null);
        }

        return newBackground;
    }
}

