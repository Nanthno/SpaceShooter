// code credit to https://stackoverflow.com/questions/20307754/save-data-from-linkedlist-in-java
// and https://stackoverflow.com/questions/18800717/convert-text-content-to-image

package src.main.java.graphics;


import src.main.java.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class HighScorePanel {

    private static final String scoreFileLocation = Globals.getResourceFile(ResourceFileType.MISC) + "highscores.data";

    private static BufferedImage[] menuButton = ImageUtil.loadAnimation(Globals.getResourceFile(ResourceFileType.IMAGE) + "buttonMenu");

    static String[][] scores;
    static int currentRunPlace = -1;

    static final int textSize = 12;
    static final int scoreYSpacing = 20;
    static final int scoreboardWidth = 200;
    static final int scoreboardXOrigin = Globals.screenWidth / 2 - scoreboardWidth / 2;
    static final int scoreboardYOrigin = 100;
    static final Color textColor = new Color(100, 50, 200);
    static final Color currentRunTextColor = new Color(0, 150, 255);
    static final Font font = new Font("Monospaced Bold", Font.PLAIN, textSize);

    private enum Button {
        NONE,
        MENU
    }

    private static Input input = Controller.getInput();

    private static final int menuButtonOriginX = 64;
    private static final int menuButtonOriginY = 64;
    private static final int menuButtonWidth = menuButton[0].getWidth();
    private static final int menuButtonHeight = menuButton[0].getHeight();

    static BufferedImage drawScreenshot() {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.WIDTH, GraphicsManager.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = screenshot.getGraphics();

        Point mousePoint = Controller.findMousePosition();

        int menuButtonFrame = getButtonFrame(menuButtonOriginX, menuButtonOriginY, menuButtonWidth, menuButtonHeight, mousePoint, Button.MENU);
        g.drawImage(menuButton[menuButtonFrame], menuButtonOriginX, menuButtonOriginY - 24, null);

        BufferedImage scoreImg = drawScores();
        g.drawImage(scoreImg, scoreboardXOrigin, scoreboardYOrigin, null);

        g.dispose();

        return screenshot;
    }

    private static BufferedImage drawScores() {
        int currentYPos = 0;
        int lineSize = textSize + scoreYSpacing;
        BufferedImage img = new BufferedImage(scoreboardWidth,
                scores.length * lineSize,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        for (int i = 0; i < scores.length; i++) {
            Color scoreColor = i == currentRunPlace ? currentRunTextColor : textColor;
            String[] score = scores[i];
            BufferedImage playerNameImg = drawWord((i + 1) + ". " + score[0], scoreColor);
            g.drawImage(playerNameImg, 0, currentYPos, null);

            BufferedImage scoreImg = drawWord(score[1], scoreColor);
            g.drawImage(scoreImg, scoreboardWidth - scoreImg.getWidth(), currentYPos, null);

            currentYPos += lineSize;
        }
        g.dispose();

        return img;
    }

    private static BufferedImage drawWord(String word, Color color) {
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
        g.setColor(color);
        g.drawString(word, 0, fm.getAscent());
        g.dispose();

        return img;
    }

    public static void addScore(int score) {
        currentRunPlace = -1;
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
        return;
    }

    public static String[][] populateEmptyScore(String[][] array) {
        if (array == null)
            array = new String[10][2];

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i][0] == null || array[i][1] == null)
                array[i] = new String[]{"<PLAYER>", "0"};
        }

        return array;
    }

    public static void saveScores() {
        if (scores == null)
            return;

        try {
            FileOutputStream fout = new FileOutputStream(scoreFileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(scores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getButtonFrame(int xPos, int yPos, int width, int height, Point mousePoint, Button button) {
        if (mouseOverlap(mousePoint, xPos, yPos, width, height)) {
            if (input.getIsMouse1Pressed()) {
                if (input.getIsMouse1Released()) {
                    if (button == Button.MENU) {
                        Controller.setGameState(GameState.MENU);
                        saveScores();
                    }

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

        boolean onButton = mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
        return onButton;
    }

}
