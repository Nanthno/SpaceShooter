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

    public ScreenPanel() {
        statusPanel = new StatusPanel();
        gamePanel = new GamePanel();
        menuPanel = new MenuPanel();
        highScorePanel = new HighScorePanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameState state = Controller.getGameState();
        BufferedImage screenshot;
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
}

