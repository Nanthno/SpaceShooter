import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

class Input {//implements Runnable {

        // KeyEvents for controlling ship1
    private static int upke = KeyEvent.VK_W;
    private static int downke = KeyEvent.VK_S;
    private static int leftke = KeyEvent.VK_A;
    private static int rightke = KeyEvent.VK_D;
    private static int fireke = KeyEvent.VK_SPACE;
    // booleans for storing if a key is pressed
    static volatile boolean up = false;
    static volatile boolean down = false;
    static volatile boolean left = false;
    static volatile boolean right = false;
    static volatile boolean fire = false;

    public Input() {
	IsKeyPressed.makeKeyboardManager();

	    // Code from https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
	    // creates input updater
	KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {

		    @Override
		    public boolean dispatchKeyEvent(KeyEvent ke) {
			synchronized (IsKeyPressed.class) {

			    // switch-case like a cond!
			    switch (ke.getID()) {
			    case KeyEvent.KEY_PRESSED:
				// movement
				if (ke.getKeyCode() == upke) {
				    up = true;
				}
				else if (ke.getKeyCode() == downke) {
				    down = true;
				}
				else if (ke.getKeyCode() == leftke) {
				    left = true;
				}
				else if (ke.getKeyCode() == rightke) {
				    right = true;
				}
				// firing
				else if (ke.getKeyCode() == fireke){
				    fire = true;
				}

				break;
			    

			    case KeyEvent.KEY_RELEASED:
				// movement
				if (ke.getKeyCode() == upke) {
				    up = false;
				}
				else if (ke.getKeyCode() == downke) {
				    down = false;
				}
				else if (ke.getKeyCode() == leftke) {
				    left = false;
				}
				else if (ke.getKeyCode() == rightke) {
				    right = false;
				}
				// firing
				else if (ke.getKeyCode() == fireke){
				    fire = false;
				}

				break;
			    }
			}
			return false;
		    }
		}
		);
	
	
    }
    
}

