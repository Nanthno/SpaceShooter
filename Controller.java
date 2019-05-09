import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static final int updatesPerSecond = 60;

    // how many spawn chances there are
    static int maxSpawn = 1;
    // probability that each spawn is successful
    static float enemySpawnRate = 0.25f;
    
    public static void main(String[] args) {

	graphicsManager = new GraphicsManager();

	gameLoop();

    }

    static void update() {
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
	int spawns = (int)(rand.nextInt()*maxSpawn);
	
	for(int i = 0; i < spawns; i++) {
	    int y = rand.nextInt() * GraphicsManager.HEIGHT;
	    int xSpeed = 10;
	    int ySpeed = 0;
	    enemyShips.add(new EnemyShip(y, xSpeed, ySpeed));
	}
	
	graphicsManager.drawScreen();
    }

    public static void gameLoop() {
	int tickCount = 0;
	long time = (new Date()).getTime();
	long startTime = time;
	while(true) {
	    update();
	    tickCount++;

	    while(tickCount*(1000/updatesPerSecond) > time-startTime) {
		time = (new Date()).getTime();
	    }
	
	}
	
    }
}
