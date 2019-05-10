import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GraphicsManager {


    // size of the screen
    static int WIDTH = 1024;
    static int HEIGHT = 640;

    JFrame frame;

    JPanel gamePanel;

    // images
    BufferedImage background;
    BufferedImage enemy1;
    BufferedImage playerImg;
    
    
    public GraphicsManager() {

	// load the images for the game
	loadImages();
	
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setTitle("Space Shooter");
	frame.setSize(WIDTH, HEIGHT);
	
	gamePanel = new JPanel();
	frame.add(gamePanel, BorderLayout.CENTER);

	drawScreen();

	// makes the frame visible
	frame.setVisible(true);
    }

    public void drawScreen() {
	JLabel screenshot = drawScreenshot();

	gamePanel.removeAll();
	gamePanel.add(screenshot);
	gamePanel.validate();
	gamePanel.repaint();
    }

    private JLabel drawScreenshot() {
	BufferedImage screenshot = copyImage(background);

	// draw enemy ships
	ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
	Graphics g = screenshot.getGraphics();
	for(EnemyShip e : enemyShips) {
	    g.drawImage(enemy1, e.getx(), e.gety(), null);
	}
	g.dispose();

	// at end
	JLabel screenShotLabel = new JLabel(new ImageIcon(scaleImage(screenshot, WIDTH, HEIGHT)));

	return screenShotLabel;
    }
    
    // loads the images for the game
    void loadImages() {
	System.out.println("attempting to upload images...");

	background = loadImage(new File("images/space.png"));
	enemy1 = loadImage(new File("images/enemy1.png"));
	playerImg = loadImage(new File("images/player.png"));

	System.out.println("success");
	
    }
    
    static BufferedImage loadImage(File file) {
	BufferedImage img;

	try {
	    img = ImageIO.read(file);
	    return img;
	} catch (IOException e) {
	    System.out.println("Failed to load image at " + file);
	    e.printStackTrace();
	    return null;
	}
    }

    // scales image (img) to width w and height h
    private Image scaleImage(BufferedImage img, int w, int h) {
	    return img.getScaledInstance(w, h, Image.SCALE_FAST);
    }

    // clones an image
    // from https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
	    
}
