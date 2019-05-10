import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();

    static ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    static final int updatesPerSecond = 30;

    // probability of a spawn occuring on each tick
    static double spawnChance = 0.5;
    
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
	    double xSpeed = rand.nextDouble()*(EnemyShip.maxSpeed-EnemyShip.minSpeed) + EnemyShip.minSpeed;
	    int ySpeed = 0;
	    enemyShips.add(new EnemyShip(y, xSpeed, ySpeed));
	}

	// updates explosions
	for(int i = explosions.size()-1; i >= 0; i--) {
	    boolean kill = explosions.get(i).update();
	    if(kill) {
		explosions.remove(i);
	    }
	}
	    

	checkEnemyBulletCollision();
	checkEnemyExplosionCollision();
	
	graphicsManager.drawScreen();
    }

    static void checkEnemyBulletCollision() {
	for(int j = enemyShips.size()-1; j >= 0; j--) {
	    EnemyShip e = enemyShips.get(j);
		
	    for(int i = playerBullets.size()-1; i >= 0; i--) {
		PlayerBullet b = playerBullets.get(i);
		if(b.getx() < e.getx()+16 && b.getx() > e.getx()-16 &&
		   b.gety()-1 < e.gety()+16 && b.gety()+1 > e.gety()-16) {
		    explosions.add(new Explosion(e.getx(), e.gety()));
		    playerBullets.remove(i);
		    enemyShips.remove(j);
		    
		}
	    }
	}
    }

    static void checkEnemyExplosionCollision() {
	for(int i = explosions.size()-1; i >= 0; i--) {
	    Explosion b = explosions.get(i);
	    for(int j = enemyShips.size()-1; j >= 0; j--) {
		EnemyShip e = enemyShips.get(j);
		if(b.getStage() > 3 &&
		   distance(b.getx(), b.gety(), e.getx(), e.gety()) < 32) {
		    explosions.add(new Explosion(e.getx(), e.gety()));
		    enemyShips.remove(j);
		    
		}
	    }
	}
    }

    static double distance(double x1, double y1, double x2, double y2) {
	double dx = Math.abs(x1-x2);
	double dy = Math.abs(y1-y2);

	return Math.sqrt(dx*dx + dy*dy);
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
    static ArrayList<Explosion> getExplosions() {
	return explosions;
    }
}
