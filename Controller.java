import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();

    static final int updatesPerSecond = 60;

    // maximum number of spawns per update
    static int maxSpawn = 1;
    
    public static void main(String[] args) {

	graphicsManager = new GraphicsManager();

	playerBullets.add(new PlayerBullet(400, 400, 5, 0));
	
	gameLoop();

    }

    static void update() {
	// updates player
	player.update();

	// updates playerbullets
	ArrayList<PlayerBullet> newPlayerBullets = new ArrayList<PlayerBullet>();
	for(PlayerBullet b : playerBullets) {
	    b.update();
	    if(b.getx() < 1040) {
		newPlayerBullets.add(b);
	    }
	}
	playerBullets = newPlayerBullets;
	
	// updates enemyShips
	ArrayList<EnemyShip> newEnemyShips = new ArrayList<EnemyShip>();
	for(int i = 0; i < enemyShips.size(); i++) {
	    EnemyShip e = enemyShips.get(i);
	    boolean kill = e.updateShip();
	    if(!kill) {
	        newEnemyShips.add(e);
	    }
	}
	enemyShips = newEnemyShips;

	// spawns new enemyShips
	Random rand = new Random();
	int spawns = rand.nextInt(maxSpawn+1);
	
	for(int i = 0; i < spawns; i++) {
	    int y = rand.nextInt(GraphicsManager.HEIGHT);
	    int xSpeed = 2;
	    int ySpeed = 0;
	    //enemyShips.add(new EnemyShip(y, xSpeed, ySpeed));
	    }

       
	
	graphicsManager.drawScreen();
    }

    public static void gameLoop() {
	long time = (new Date()).getTime();
	long lastTime = time;
	while(true) {
	    update();
	    lastTime = time;
	    while(updatesPerSecond/1000 < time-lastTime) {
		time = (new Date()).getTime();
	    }
	
	}
	
    }

    static void addBullet(PlayerBullet b) {
	playerBullets.add(b);
    }

    static ArrayList<EnemyShip> getEnemyArray() {
	return enemyShips;
    }

    static PlayerShip getPlayerShip() {
	return player;
    }
    static ArrayList<PlayerBullet> getPlayerBullets() {
	return playerBullets;
    }
}
