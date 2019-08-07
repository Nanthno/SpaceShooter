package src.main.java.graphics;

import src.main.java.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class MenuPanel extends JPanel {

    String creditsFile = "src/main/resources/misc/credits";
    BufferedImage credits = ImageUtil.stringToImage(FileUtil.readFileToString(creditsFile), new Color(255, 255, 255));
    int creditsX = GraphicsManager.getWidth() / 2 - credits.getWidth() / 2;
    int creditsY = 100;

    private static final BufferedImage[] backButton = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "backButton");
    private static final BufferedImage[] playButton = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "buttonPlay");
    private static final BufferedImage[] creditsButton = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "buttonCredits");
    private static final BufferedImage[] soundModeButtons = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "audioSelectorButtons");
    private Map<Button, BufferedImage[]> buttonMap = Map.of(
            Button.PLAY, playButton,
            Button.CREDITS, creditsButton,
            Button.BACK, backButton
    );

    // for main
    private final Button[] buttonOrder = new Button[]{Button.PLAY, Button.CREDITS};
    private final int buttonYOffset = 32;
    private final int buttonOriginX = 32;
    private final int buttonOriginY = 32;
    private final int buttonWidth;
    private final int buttonHeight;

    // for credits
    private final int backButtonOriginX = 64;
    private final int backButtonOriginY = 64;
    private final int backButtonWidth = backButton[0].getWidth();
    private final int backButtonHeight = backButton[0].getHeight();

    // for sound selection
    private final int soundButtonOriginX = Globals.screenWidth - 250;
    private final int soundButtonOriginY = Globals.screenHeight - 100;
    private final int soundButtonWidth = soundModeButtons[0].getWidth();
    private final int soundButtonHeight = soundModeButtons[0].getHeight();

    Button currentClick = Button.NONE;

    private enum Button {
        NONE,
        PLAY,
        CREDITS,
        BACK,
        SOUND_SELECTION

    }

    private enum MenuStatus {
        MAIN,
        CREDITS
    }

    private Input input;

    private MenuStatus menuStatus = MenuStatus.MAIN;

    protected MenuPanel() {
        input = Controller.getInput();

        int largestWidth = 0;
        int largestHeight = 0;
        for (Button nextButton : buttonOrder) {
            BufferedImage[] buttonImages = buttonMap.get(nextButton);
            for (BufferedImage button : buttonImages) {
                int width = button.getWidth();
                int height = button.getHeight();
                largestWidth = largestWidth > width ? largestWidth : width;
                largestHeight = largestHeight > height ? largestHeight : height;
            }
        }
        buttonHeight = largestHeight;
        buttonWidth = largestWidth;

    }


    protected BufferedImage drawScreenshot() {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics g = screenshot.createGraphics();

        Point mousePoint = Controller.findMousePosition();


        if (menuStatus == MenuStatus.MAIN)
            screenshot = drawMainMenu(screenshot, g, mousePoint);
        else if (menuStatus == MenuStatus.CREDITS)
            screenshot = drawCredits(screenshot, g, mousePoint);


        g.dispose();

        return screenshot;
    }

    private BufferedImage drawCredits(BufferedImage screenshot, Graphics g, Point mousePoint) {
        int buttonValue = getButtonFrame(backButtonOriginX, backButtonOriginY, backButtonWidth, backButtonHeight, mousePoint, Button.BACK);
        BufferedImage backButtonImg = backButton[buttonValue];

        g.drawImage(backButtonImg, backButtonOriginX, backButtonOriginY - 24, null);
        if (buttonValue != 2) {
            currentClick = Button.NONE;
        }

        g.drawImage(credits, creditsX, creditsY, null);

        g.dispose();
        return screenshot;
    }

    private BufferedImage drawMainMenu(BufferedImage screenshot, Graphics g, Point mousePoint) {
        BufferedImage buttons = drawButtons(mousePoint);
        g.drawImage(buttons, buttonOriginX, buttonOriginY, null);

        g.dispose();

        return screenshot;
    }

    private BufferedImage drawButtons(Point mousePoint) {
        BufferedImage buttons = new BufferedImage(Globals.screenWidth,
                Globals.screenHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics g = buttons.createGraphics();

        boolean mouseStillOnCurrentClick = false;

        int buttonNumber = getButtonFrame(soundButtonOriginX, soundButtonOriginY + soundButtonHeight, soundButtonWidth, soundButtonHeight,
                mousePoint, Button.SOUND_SELECTION);

        for (int i = 0; i < buttonOrder.length; i++) {
            Button currentButton = buttonOrder[i];
            BufferedImage[] buttonImages = buttonMap.get(currentButton);
            int yPos = i * (buttonYOffset + buttonHeight);

            if (buttonNumber == 2) {
                mouseStillOnCurrentClick = true;
            }
            buttonNumber = getButtonFrame(buttonOriginX, yPos + buttonOriginY + buttonYOffset, buttonWidth, buttonHeight,
                    mousePoint, currentButton);
            g.drawImage(buttonImages[buttonNumber], 0, yPos, null);

        }

        g.drawImage(soundModeButtons[Controller.getIsSoundExperiential() ? 1 : 0],
                soundButtonOriginX, soundButtonOriginY,
                null);

        if (mouseStillOnCurrentClick == false) {
            currentClick = Button.NONE;
        }
        return buttons;
    }

    private int getButtonFrame(int xPos, int yPos, int width, int height, Point mousePoint, Button button) {
        if (mouseOverlap(mousePoint, xPos, yPos, width, height)) {
            if (input.getIsMouse1Pressed()) {
                if (input.getIsMouse1Released()) {
                    if (button == Button.PLAY) {
                        Controller.setGameState(GameState.PLAYING);
                        currentClick = Button.NONE;
                    } else if (button == Button.CREDITS) {
                        menuStatus = MenuStatus.CREDITS;
                        currentClick = Button.NONE;
                    } else if (button == Button.BACK) {
                        menuStatus = MenuStatus.MAIN;
                        currentClick = Button.NONE;
                    } else if (button == Button.SOUND_SELECTION) {
                        Controller.toggleSoundMode();
                        currentClick = Button.NONE;
                    }
                    input.resetIsMouse1Released();
                }
                return 2;
            }
            return 1;
        }
        return 0;

    }

    /*private int getButtonFrame(int xPos, int yPos, int width, int height, Point mousePoint, Button
            button) {

        if (mouseOverlap(mousePoint, xPos, yPos, width, height)) {
            if (input.getIsMouse1Pressed()) {
                if (button == currentClick || currentClick == Button.NONE) {
                    currentClick = button;
                    return 2;
                } else
                    return 0;
            } else if (button == currentClick) {
                return 1;
            }
            if (input.getIsMouse1Released()) {
                input.resetIsMouse1Released();
                if (button == Button.PLAY) {
                    Controller.setGameState(GameState.PLAYING);
                    currentClick = Button.NONE;
                } else if (button == Button.CREDITS) {
                    menuStatus = MenuStatus.CREDITS;
                    currentClick = Button.NONE;
                } else if (button == Button.BACK) {
                    menuStatus = MenuStatus.MAIN;
                    currentClick = Button.NONE;
                } else if (button == Button.SOUND_SELECTION) {
                    Controller.toggleSoundMode();
                    currentClick = Button.NONE;
                }

            } else {
                return 1;
            }
        }

        return 0;

    }*/

    private static boolean mouseOverlap(Point mousePoint, int x, int y, int width, int height) {
        int mouseX = (int) mousePoint.getX();
        int mouseY = (int) mousePoint.getY();

        boolean onButton = mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
        return onButton;
    }

}