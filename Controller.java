import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static final int updatesPerSecond = 60;

    // maximum number of spawns per update
    static int maxSpawn = 1;
    
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
	int spawns = rand.nextInt(maxSpawn+1);
	
	for(int i = 0; i < spawns; i++) {
	    int y = rand.nextInt(GraphicsManager.HEIGHT);
	    int xSpeed = 2;
	    int ySpeed = 0;
	    enemyShips.add(new EnemyShip(y, xSpeed, ySpeed));
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

    static ArrayList<EnemyShip> getEnemyArray() {
	return enemyShips;
    }
}
