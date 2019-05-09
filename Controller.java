import java.util.ArrayList;
import java.util.Date;

class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static final int updatesPerSecond = 60;
    
    public static void main(String[] args) {

	graphicsManager = new GraphicsManager();

	gameLoop();

    }

    static void update() {
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
