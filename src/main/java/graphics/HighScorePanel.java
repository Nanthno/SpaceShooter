// code credit to https://stackoverflow.com/questions/20307754/save-data-from-linkedlist-in-java
// and https://stackoverflow.com/questions/18800717/convert-text-content-to-image

package src.main.java.graphics;


import src.main.java.*;
import sun.security.ssl.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class HighScorePanel {

    private static final String scoreFileLocation = Globals.getResourceFile(ResourceFileType.MISC) + "highscores.data";

    private static BufferedImage[] menuButton = ImageUtil.loadAnimation(Globals.getResourceFile(ResourceFileType.IMAGE) + "buttonMenu");

    static String[][] scores;
    static int currentRunPlace = -1;
    static int currentRunScore = 0;

    static final int textSize = 12;
    static final int scoreYSpacing = 20;
    static final int scoreboardWidth = 300;
    static final int scoreboardHeight = 200;
    static final int scoreboardXOrigin = Globals.screenWidth / 2 - scoreboardWidth / 2;
    static final int scoreboardYOrigin = 100;
    static final Color textColor = new Color(0, 173, 21);
    static final String fontName = "Monospaced Bold";
    static final int fontType = Font.PLAIN;

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
        g.drawImage(menuButton[menuButtonFrame], menuButtonOriginX, menuButtonOriginY, null);

        BufferedImage scoreImg = drawScores();
        g.drawImage(scoreImg, scoreboardXOrigin, scoreboardYOrigin, null);

        g.dispose();

        return screenshot;
    }

    private static BufferedImage drawScores() {

        System.out.println(currentRunPlace);

        BufferedImage img = new BufferedImage(scoreboardWidth, scoreboardHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        if(currentRunPlace != 0) {
            g.drawImage(drawWord("Highscore:", textColor, 36), 0, 0, null);

            g.drawImage(drawWord(scores[0][1], textColor, 24), 0, 50, null);

            g.drawImage(drawWord("Your Score:", textColor, 20), 0, 100, null);

            g.drawImage(drawWord(String.valueOf(currentRunScore), textColor, 20), 0, 130, null);
        }
        else {
            g.drawImage(drawWord("New Highscore:", textColor, 36), 0, 0, null);

            g.drawImage(drawWord(String.valueOf(currentRunScore), textColor, 24), 0, 50, null);

            g.drawImage(drawWord("Previous Highscore:", textColor, 20), 0, 100, null);

            g.drawImage(drawWord(String.valueOf(scores[1][1]), textColor, 20), 0, 130, null);
        }

        g.dispose();

        return img;
    }

    private static BufferedImage drawWord(String word, Color color, int fontSize) {

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
        g.setColor(color);
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
            FileOutputStream fOut = new FileOutputStream(scoreFileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
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
