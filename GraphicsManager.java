import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GraphicsManager {


    // size of the screen
    static int WIDTH = 1024;
    static int HEIGHT = 640;

    JFrame frame;

    GamePanel gamePanel;
    // panels for showing the state of the ship, gun overheat, etc
    JPanel statusPanel;
    BarPanel healthPanel;

    // images
    BufferedImage background;
    BufferedImage enemy0;
    BufferedImage enemy1;
    BufferedImage playerImg;
    BufferedImage playerBullet;
    BufferedImage[] smallExplosion;
    BufferedImage[] fuelExplosion;
    
    public GraphicsManager() {

	// load the images for the game
	loadImages();
	
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setTitle("Space Shooter");
	frame.setSize(WIDTH, HEIGHT);
	
	gamePanel = new GamePanel();
	frame.add(gamePanel, BorderLayout.CENTER);

	
	healthPanel = new BarPanel(Controller.getPlayerHealth(), Controller.getPlayerHealth(), 50, HEIGHT, new Color(240,0,0), new Color(0,240,0));
	
	statusPanel = new JPanel();
	statusPanel.setLayout(new GridLayout(1, 1));
	statusPanel.add(healthPanel);
	
	frame.add(statusPanel, BorderLayout.WEST);

	// makes the frame visible
	frame.setVisible(true);
    }

    public void drawScreen(int playerHealth) {
        BufferedImage screenshot = drawScreenshot();

	gamePanel.removeAll();
	gamePanel.validate();
	gamePanel.repaint();

	healthPanel.setValue(playerHealth);
	healthPanel.removeAll();
	healthPanel.validate();
	healthPanel.repaint();
    }

    private BufferedImage drawScreenshot() {
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
	    g.drawImage(smallExplosion[e.getStage()], e.getx(), e.gety(), null);
	}
	
	// draw player ship
	PlayerShip ship = Controller.getPlayerShip();
	g.drawImage(playerImg, (int)ship.getx(), (int)ship.gety(), null);

	

	// at end
	g.dispose();

	return screenshot;
	
    }

	
	
    
    // loads the images for the game
    void loadImages() {
	background = loadImage("images/space.png");
	enemy0 = loadImage("images/enemySwarm.png");
	enemy1 = loadImage("images/enemyFuelShip.png");
	playerImg = loadImage("images/player.png");
	playerBullet = loadImage("images/playerBullet.png");

	smallExplosion = new BufferedImage[11];
	for(int i = 0; i < smallExplosion.length; i++) {
	    smallExplosion[i] = loadImage("images/smallExplosion/exp"+i+".png");
	}
	fuelExplosion = new BufferedImage[8];
	for(int i = 0; i < fuelExplosion.length; i++) {
	    fuelExplosion[i] = loadImage("images/fuelExplosion/exp"+i+".png");
	}
	
    }
    
    static BufferedImage loadImage(String f) {

	File file = new File(f);
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

    // clones an image
    // from https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    class BarPanel extends JPanel {
	private BufferedImage bar;

	int maxValue;
	int barValue;
	int displayValue;

	int width;
	int height;

	Color backColor;
	Color fillColor;

	public BarPanel(int maxBar, int barVal, int w, int h, Color b, Color f) {
	    this.maxValue = maxBar;
	    this.barValue = barVal;
	    displayValue = barValue;
	    width = w;
	    height = h;
	    backColor = b;
	    fillColor = f;
	}

	void setValue(int val) {
	    barValue = val;
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    bar = drawBar();
	    g.drawImage(bar, 0, 0, this);
	}

	private BufferedImage drawBar() {
	    if(displayValue > barValue) {
		displayValue--;
	    }
	    
	    bar = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	    Graphics g = bar.getGraphics();
	    g.setColor(backColor);
	    g.fillRect(0, 0, width, height);
	    g.setColor(fillColor);
	    g.fillRect(0, (maxValue-displayValue)*(height/maxValue), width, height);

	    g.dispose();
	    return bar;
	}
    }
    
    class GamePanel extends JPanel {
	private BufferedImage screenshot;

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    screenshot = drawScreenshot();
	    g.drawImage(screenshot, 0, 0, this);

	}
	private BufferedImage drawScreenshot() {
	    BufferedImage screenshot = copyImage(background);

	    // draw enemy ships
	    ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
	    Graphics g = screenshot.getGraphics();
	    for(EnemyShip e : enemyShips) {
		if(e.getType() == 0)
		    g.drawImage(enemy0, e.getx(), e.gety(), null);
		else if(e.getType() == 1)
		    g.drawImage(enemy1, e.getx(), e.gety(), null);
	    }

	    // draw player bullets
	    ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
	    for(PlayerBullet b : pb) {
		g.drawImage(playerBullet, (int)b.getx(), (int)b.gety(), null);
	    }

	    // draw explosions
	    ArrayList<Explosion> exp = Controller.getExplosions();
	    for(int i = 0; i < exp.size(); i++) {
		Explosion e = exp.get(i);
		if(e.expType == 0) {
		    g.drawImage(smallExplosion[e.getStage()], e.getx(), e.gety(), null);
		}
		if(e.expType == 1) {
		    g.drawImage(fuelExplosion[e.getStage()], e.getx(), e.gety(), null);
		}
	    }
	
	    // draw player ship
	    PlayerShip ship = Controller.getPlayerShip();
	    g.drawImage(playerImg, (int)ship.getx(), (int)ship.gety(), null);

	

	    // at end
	    g.dispose();

	    return screenshot;
	 }
	    
    }
}
