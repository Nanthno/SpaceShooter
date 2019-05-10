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
    BufferedImage playerBullet;
    BufferedImage[] explosion;
    
    
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

	// draw player bullets
	ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
	for(PlayerBullet b : pb) {
	    g.drawImage(playerBullet, (int)b.getx(), (int)b.gety(), null);
	}

	// draw explosions
	ArrayList<Explosion> exp = Controller.getExplosions();
	for(Explosion e : exp) {
	    g.drawImage(explosion[e.getStage()], e.getx(), e.gety(), null);
	}
	
	// draw player ship
	PlayerShip ship = Controller.getPlayerShip();
	g.drawImage(playerImg, (int)ship.getx(), (int)ship.gety(), null);

	

	// at end
	g.dispose();
	JLabel screenShotLabel = new JLabel(new ImageIcon(screenshot));

	return screenShotLabel;
    }
    
    // loads the images for the game
    void loadImages() {
	background = loadImage(new File("images/space.png"));
	enemy1 = loadImage(new File("images/enemy1.png"));
	playerImg = loadImage(new File("images/player.png"));
	playerBullet = loadImage(new File("images/playerBullet.png"));

	explosion = new BufferedImage[11];
	for(int i = 0; i < 11; i++) {
	    explosion[i] = loadImage(new File("images/explosion1/exp"+i+".png"));
	}
	
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

    /*// scales image (img) to width w and height h
    private BufferedImage scaleImage(BufferedImage img, int w, int h) {
	return img.getScaledInstance(w, h, Image.SCALE_FAST);
	}*/

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
