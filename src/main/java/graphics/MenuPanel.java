package src.main.java.graphics;

import src.main.java.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MenuPanel implements ActionListener {

    private final String creditsFile = Globals.getResourceFile(ResourceFileType.MISC) + "credits";
    private final BufferedImage credits = drawCreditsImage();

    private static final BufferedImage[] backButtonImages = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "backButton");
    private static final BufferedImage[] playButtonImages = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "buttonPlay");
    private static final BufferedImage[] creditsButtonImages = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "buttonCredits");
    private static final BufferedImage[] soundModeButtonImages = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "audioSelectorButtons");
    private static final BufferedImage[] muteButtonImages = ImageUtil.loadAnimation(GraphicsManager.imageFolderPath + "buttonMute");
    private static final BufferedImage catalystTitle = ImageUtil.loadImage(GraphicsManager.imageFolderPath + "catalystTitle.png");

    private final Map<Button, BufferedImage[]> buttonMap = new HashMap<Button, BufferedImage[]>() {{
        put(Button.PLAY, playButtonImages);
        put(Button.CREDITS, creditsButtonImages);
        put(Button.BACK, backButtonImages);
        put(Button.MUTE, muteButtonImages);

    }};

    // for main
    private final int titleOriginX = 64;
    private final int titleOriginY = 32;

    private final Button[] buttonOrder = new Button[]{Button.PLAY, Button.CREDITS};
    private final int buttonOriginX = 64;
    private final int buttonOriginY = 200;
    private final int buttonWidth;
    private final int buttonHeight;

    private final int backButtonWidth = backButtonImages[0].getWidth();
    private final int backButtonHeight = backButtonImages[0].getHeight();
    private final Color creditsColor = new Color(255, 255, 255);

    private final int soundButtonWidth = soundModeButtonImages[0].getWidth();
    private final int soundButtonHeight = soundModeButtonImages[0].getHeight();

    private final int muteButtonWidth = muteButtonImages[0].getWidth();
    private final int muteButtonHeight = muteButtonImages[0].getHeight();


    private enum Button {
        PLAY,
        CREDITS,
        BACK,
        SOUND_SELECTION,
        MUTE

    }

    private enum MenuStatus {
        MAIN,
        CREDITS
    }

    private final Input input;

    private MenuStatus menuStatus = MenuStatus.MAIN;

    MenuPanel() {
        // setupButtons();

        input = Controller.getInput();

        int largestWidth = 0;
        int largestHeight = 0;
        for (Button nextButton : buttonOrder) {
            BufferedImage[] buttonImages = buttonMap.get(nextButton);
            for (BufferedImage button : buttonImages) {
                int width = button.getWidth();
                int height = button.getHeight();
                largestWidth = Math.max(largestWidth, width);
                largestHeight = Math.max(largestHeight, height);
            }
        }
        buttonHeight = largestHeight;
        buttonWidth = largestWidth;


    }


    BufferedImage drawScreenshot() {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics g = screenshot.createGraphics();

        Point mousePoint = Controller.findMousePosition();

        if (menuStatus == MenuStatus.MAIN)
            screenshot = drawMainMenu(screenshot, g, mousePoint);
        else if (menuStatus == MenuStatus.CREDITS)
            screenshot = drawCredits(screenshot, g, mousePoint);


        g.dispose();

        return screenshot;
    }

    private BufferedImage drawCreditsImage() {
        String creditsString = FileUtil.readFileToString(creditsFile);
        String[] creditsLines = creditsString.split("\\n");
        int creditsLineSplit = 16;
        BufferedImage creditsImage = new BufferedImage(1000, creditsLines.length * creditsLineSplit, BufferedImage.TYPE_INT_ARGB);
        Graphics g = creditsImage.getGraphics();

        for (int i = 0; i < creditsLines.length; i++) {
            if (creditsLines[i].equals(""))
                creditsLines[i] = " ";
            g.drawImage(ImageUtil.stringToImage(creditsLines[i], creditsColor),
                    0, i * creditsLineSplit, null);
        }

        g.dispose();

        return creditsImage;
    }

    private BufferedImage drawCredits(BufferedImage screenshot, Graphics g, Point mousePoint) {
        int backButtonOriginY = 64;// for credits
        int backButtonOriginX = 64;
        int buttonValue = getButtonFrame(backButtonOriginX, backButtonOriginY, backButtonWidth, backButtonHeight, mousePoint, Button.BACK);
        BufferedImage backButtonImg = backButtonImages[buttonValue];

        g.drawImage(backButtonImg, backButtonOriginX, backButtonOriginY, null);
        if (buttonValue != 2) {
        }

        int creditsY = 150;
        int creditsX = 64;
        g.drawImage(credits, creditsX, creditsY, null);

        g.dispose();
        return screenshot;
    }

    private BufferedImage drawMainMenu(BufferedImage screenshot, Graphics g, Point mousePoint) {
        BufferedImage buttons = drawButtons(mousePoint);

        g.drawImage(catalystTitle, titleOriginX, titleOriginY, null);

        g.drawImage(buttons, buttonOriginX, buttonOriginY, null);

        g.dispose();

        return screenshot;
    }

    private BufferedImage drawButtons(Point mousePoint) {
        BufferedImage buttons = new BufferedImage(Globals.screenWidth,
                Globals.gameHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics g = buttons.createGraphics();

        boolean mouseStillOnCurrentClick = false;

        int soundButtonOriginY = Globals.gameHeight - 50;// for sound selection
        int soundButtonOriginX = Globals.screenWidth - 300;
        int buttonNumber = getButtonFrame(soundButtonOriginX + buttonOriginX, soundButtonOriginY + soundButtonHeight, soundButtonWidth + 24, soundButtonHeight,
                mousePoint, Button.SOUND_SELECTION);

        for (int i = 0; i < buttonOrder.length; i++) {
            Button currentButton = buttonOrder[i];
            BufferedImage[] buttonImages = buttonMap.get(currentButton);
            int buttonYOffset = 32;
            int yPos = i * (buttonYOffset + buttonHeight);

            if (buttonNumber == 2) {
                mouseStillOnCurrentClick = true;
            }
            buttonNumber = getButtonFrame(buttonOriginX, yPos + buttonOriginY, buttonWidth, buttonHeight,
                    mousePoint, currentButton);
            g.drawImage(buttonImages[buttonNumber], 0, yPos, null);

        }

        g.drawImage(soundModeButtonImages[Controller.getIsSoundExperiential() ? 1 : 0],
                soundButtonOriginX, soundButtonOriginY-buttonOriginY+50,
                null);


        int muteButtonOriginY = Globals.gameHeight - 181;// for mute button
        int muteButtonOriginX = Globals.screenWidth - 390;
        getButtonFrame(muteButtonOriginX+buttonOriginX, muteButtonOriginY+buttonOriginY,
                muteButtonWidth, muteButtonHeight + 5,
                mousePoint, Button.MUTE);
        g.drawImage(muteButtonImages[Controller.isAudioMuted() ? 1 : 0],
                muteButtonOriginX, muteButtonOriginY,
                null);

        if (!mouseStillOnCurrentClick) {
        }
        return buttons;
    }

    private int getButtonFrame(int xPos, int yPos, int width, int height, Point mousePoint, Button button) {
        if (mouseOverlap(mousePoint, xPos, yPos, width, height)) {
            if (input.getIsMouse1Pressed()) {
                if (input.getIsMouse1Released()) {
                    if (button == Button.PLAY) {
                        Controller.setGameState(GameState.PLAYING);
                    } else if (button == Button.CREDITS) {
                        menuStatus = MenuStatus.CREDITS;
                    } else if (button == Button.BACK) {
                        menuStatus = MenuStatus.MAIN;
                    } else if (button == Button.SOUND_SELECTION) {
                        Controller.toggleSoundMode();
                    } else if (button == Button.MUTE) {
                        Controller.toggleMute();
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

        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
    }
}