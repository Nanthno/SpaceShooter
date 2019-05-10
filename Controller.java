import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();

    static final int updatesPerSecond = 60;

    // probability of a spawn occuring on each tick
    static double spawnChance = 0.05;
    
    public static void main(String[] args) {

	graphicsManager = new GraphicsManager();
	
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
	double spawns = rand.nextDouble();
	
        if(spawns < spawnChance) {
	    int y = rand.nextInt(GraphicsManager.HEIGHT);
	    int xSpeed = 2;
	    int ySpeed = 0;
	    enemyShips.add(new EnemyShip(y, xSpeed, ySpeed));
	}

	checkEnemyBulletCollision();
	
	graphicsManager.drawScreen();
    }

    static void checkEnemyBulletCollision() {
	int collisions = 0;

	for(int i = playerBullets.size()-1; i >= 0; i--) {
	    PlayerBullet b = playerBullets.get(i);
	    for(int j = enemyShips.size()-1; j >= 0; j--) {
		EnemyShip e = enemyShips.get(j);
		
		if(b.getx() < e.getx()+16 && b.getx() > e.getx()-16 &&
		   b.gety()-1 < e.gety()+16 && b.gety()+1 > e.gety()-16) {
		    playerBullets.remove(i);
		    enemyShips.remove(j);
		}
	    }
	}
	/*
	int i = 0;
	int j = 0;
	while(i < playerBullets.size()) {
	    PlayerBullet b = playerBullets.get(i);
	    while(j < enemyShips.size()) {
		System.out.println(i + " : " + j + ", " + playerBullets.size() + " : " + enemyShips.size());
		EnemyShip e = enemyShips.get(j);
		if(b.getx()-3 < e.getx()+16 && b.getx()+3 > e.getx()-16 &&
		   b.gety()-1 < e.gety()+16 && b.gety()+1 > e.gety()-16) {
		    playerBullets.remove(i);
		    enemyShips.remove(j);
		    if(playerBullets.size() < i) {
			b = playerBullets.get(i);
		    }
		    else {
			break;
		    }
		}
		else {
		    j++;
		}
	    }
	    i++;
	    }*/
	    
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
