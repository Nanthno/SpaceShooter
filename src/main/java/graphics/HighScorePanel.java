// code credit to https://stackoverflow.com/questions/20307754/save-data-from-linkedlist-in-java

package src.main.java.graphics;


import src.main.java.Controller;
import src.main.java.GameState;
import src.main.java.Input;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

public class HighScorePanel {

    private static final String scoreFileLocation = "src/main/resources/misc/highscores.data";

    private static BufferedImage[] menuButton = ImageUtil.loadAnimation("src/main/resources/images/buttonMenu");

    static LinkedList<String[]> scores;

    private enum Button {
        NONE,
        MENU
    }

    private static Button currentClick = Button.NONE;
    private static Input input = Controller.getInput();

    private static final int menuButtonOriginX = 128;
    private static final int menuButtonOriginY = 128;
    private static final int menuButtonWidth = menuButton[0].getWidth();
    private static final int menuButtonHeight = menuButton[0].getHeight();

    static BufferedImage drawScreenshot() {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.WIDTH, GraphicsManager.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = screenshot.getGraphics();

        Point mousePoint = Controller.findMousePosition();

        int menuButtonFrame = getButtonFrame(menuButtonOriginX, menuButtonOriginY, menuButtonWidth, menuButtonHeight, mousePoint, Button.MENU);
        g.drawImage(menuButton[menuButtonFrame], menuButtonOriginX, menuButtonOriginY - 24, null);

        g.dispose();

        return screenshot;
    }

    public static void loadScores() {
        try {
            FileInputStream fin = new FileInputStream(scoreFileLocation);
            ObjectInputStream in = new ObjectInputStream(fin);
            scores = (LinkedList<String[]>) in.readObject();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // if there is an issue reading the file:
        scores = new LinkedList<>();
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
                    if (button == Button.MENU)
                        Controller.setGameState(GameState.MENU);

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
