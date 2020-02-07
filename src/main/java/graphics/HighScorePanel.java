// code credit to https://stackoverflow.com/questions/20307754/save-data-from-linkedlist-in-java
// and https://stackoverflow.com/questions/18800717/convert-text-content-to-image

package src.main.java.graphics;


import src.main.java.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class HighScorePanel {

    private static final String scoreFileLocation = Globals.getResourceFile(ResourceFileType.MISC) + "highscores.data";

    private static final BufferedImage[] menuButton = ImageUtil.loadAnimation(Globals.getResourceFile(ResourceFileType.IMAGE) + "buttonMenu");

    private static String[][] scores;
    private static int currentRunPlace = -1;
    private static int currentRunScore = 0;

    private static final int scoreboardWidth = 300;
    private static final int scoreboardHeight = 200;
    private static final int scoreboardXOrigin = Globals.screenWidth / 2 - scoreboardWidth / 2;
    private static final int scoreboardYOrigin = 100;
    private static final Color textColor = new Color(0, 173, 21);
    private static final String fontName = "Monospaced Bold";
    private static final int fontType = Font.PLAIN;


    private static final Input input = Controller.getInput();

    private static final int menuButtonOriginX = 64;
    private static final int menuButtonOriginY = 64;
    private static final int menuButtonWidth = menuButton[0].getWidth();
    private static final int menuButtonHeight = menuButton[0].getHeight();

    private static BufferedImage scoreboardImage = null;

    static BufferedImage drawScreenshot() {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.WIDTH, GraphicsManager.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = screenshot.getGraphics();

        Point mousePoint = Controller.findMousePosition();

        int menuButtonFrame = getButtonFrame(mousePoint);
        g.drawImage(menuButton[menuButtonFrame], menuButtonOriginX, menuButtonOriginY, null);

        if(scoreboardImage == null) {
            scoreboardImage = drawScores();
        }

        BufferedImage scoreImg = scoreboardImage;
        g.drawImage(scoreImg, scoreboardXOrigin, scoreboardYOrigin, null);

        g.dispose();

        return screenshot;
    }

    private static BufferedImage drawScores() {

        System.out.println(currentRunPlace);

        BufferedImage img = new BufferedImage(scoreboardWidth, scoreboardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        if(currentRunPlace != 0) {
            g.drawImage(drawWord("Highscore:", 36), 0, 0, null);

            g.drawImage(drawWord(scores[0][1], 24), 0, 50, null);

            g.drawImage(drawWord("Your Score:", 20), 0, 100, null);

            g.drawImage(drawWord(String.valueOf(currentRunScore), 20), 0, 130, null);
        }
        else {
            g.drawImage(drawWord("New Highscore:", 36), 0, 0, null);

            g.drawImage(drawWord(String.valueOf(currentRunScore), 24), 0, 50, null);

            g.drawImage(drawWord("Previous Highscore:", 20), 0, 100, null);

            g.drawImage(drawWord(String.valueOf(scores[1][1]), 20), 0, 130, null);
        }

        g.dispose();

        return img;
    }

    private static BufferedImage drawWord(String word, int fontSize) {

        Font font = new Font(fontName, fontType, fontSize);

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(word);
        int height = fm.getHeight();
        g.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = img.getGraphics();
        g.setFont(font);
        g.setColor(HighScorePanel.textColor);
        g.drawString(word, 0, fm.getAscent());
        g.dispose();

        return img;
    }

    public static void addScore(int score) {
        currentRunPlace = -1;

        currentRunScore = score;

        String[] scoreNode = new String[]{"<Player>", String.valueOf(score)};

        int placing = -1;
        for (int i = 0; i < 10; i++) {
            String[] compNode = scores[i];
            if (compNode[1] == null || score > Integer.parseInt(compNode[1])) {
                placing = i;
                break;
            }
        }

        if (placing == -1)
            return;

        String[] storedNode = scoreNode;
        String[] moveNode;
        for (int i = placing; i < 10; i++) {
            moveNode = scores[i];
            scores[i] = storedNode;
            storedNode = moveNode;
            if (storedNode == null)
                return;
        }

        saveScores();
        currentRunPlace = placing;
    }

    public static void loadScores() {
        try {
            FileInputStream fin = new FileInputStream(scoreFileLocation);
            ObjectInputStream in = new ObjectInputStream(fin);
            scores = (String[][]) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("The highscore file does not exist. Creating new one...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        scores = populateEmptyScore(scores);

    }

    private static String[][] populateEmptyScore(String[][] array) {
        if (array == null)
            array = new String[10][2];

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i][0] == null || array[i][1] == null)
                array[i] = new String[]{"<PLAYER>", "0"};
        }

        return array;
    }

    private static void saveScores() {
        if (scores == null)
            return;

        try {
            FileOutputStream fOut = new FileOutputStream(scoreFileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(scores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getButtonFrame(Point mousePoint) {
        if (mouseOverlap(mousePoint, HighScorePanel.menuButtonOriginX, HighScorePanel.menuButtonOriginY, HighScorePanel.menuButtonWidth, HighScorePanel.menuButtonHeight)) {
            if (input.getIsMouse1Pressed()) {
                if (input.getIsMouse1Released()) {
                    Controller.setGameState(GameState.MENU);
                    saveScores();

                    input.resetIsMouse1Released();
                }
                return 2;
            }
            return 1;
        }
        return 0;

    }

    private static boolean mouseOverlap(Point mousePoint, int x, int y, int width, int height) {
        int mouseX = (int) mousePoint.getX();
        int mouseY = (int) mousePoint.getY();

        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public static void clearScoreBoardImage() {
        scoreboardImage = null;
    }

}
