package src.main.java.graphics;

import src.main.java.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {

    private static final BufferedImage[] playButton = ImageUtil.loadAnimation("images/buttonPlay");
    private static final BufferedImage[] creditsButton = ImageUtil.loadAnimation("images/buttonCredits");
    private final BufferedImage[][] buttonOrder = new BufferedImage[][]{playButton, creditsButton};
    private final int buttonYOffset = 32;
    private final int buttonOriginX = 32;
    private final int buttonOriginY = 32;
    private final int buttonWidth;
    private final int buttonHeight;

    private enum MenuStatus {
        MAIN
    }

    private MenuStatus menuStatus = MenuStatus.MAIN;

    protected MenuPanel() {
        int largestWidth = 0;
        int largestHeight = 0;
        for (BufferedImage[] buttonArray : buttonOrder) {
            for (BufferedImage button : buttonArray) {
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

        g.drawImage(GraphicsManager.getBackgroundImage(), 0, 0, null);

        Point mousePoint = Controller.findMousePosition();


        if (menuStatus == MenuStatus.MAIN) {
            BufferedImage specialWeapons = drawSpecialWeapons(mousePoint);
            BufferedImage instructionsBox = drawInstructionsBox();
            BufferedImage buttons = drawButtons(mousePoint);
            g.drawImage(buttons, buttonOriginX, buttonOriginY, null);

        }

        g.dispose();

        return screenshot;
    }

    private BufferedImage drawSpecialWeapons(Point mousePoint) {
        return null;
    }

    private BufferedImage drawInstructionsBox() {
        return null;
    }

    private BufferedImage drawButtons(Point mousePoint) {
        BufferedImage buttons = new BufferedImage(buttonWidth,
                (buttonHeight + buttonYOffset) * buttonOrder.length, BufferedImage.TYPE_INT_ARGB);

        Graphics g = buttons.createGraphics();

        for (int i = 0; i < buttonOrder.length; i++) {
            int yPos = i * (buttonYOffset + buttonHeight);
            if (mouseOverlap(mousePoint, buttonOriginX, yPos + buttonOriginY + buttonYOffset, buttonWidth, buttonHeight)) {
                g.drawImage(buttonOrder[i][1], 0, yPos, null);
            } else
                g.drawImage(buttonOrder[i][0], 0, yPos, null);

        }


        return buttons;
    }

    private static boolean mouseOverlap(Point mousePoint, int x, int y, int width, int height) {
        int mouseX = (int) mousePoint.getX();
        int mouseY = (int) mousePoint.getY();

        boolean onButton = mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
        return onButton;
    }

}